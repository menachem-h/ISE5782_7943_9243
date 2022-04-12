package renderer;

import geometries.Geometries;
import geometries.Geometry;
import geometries.Intersectable;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

import static java.lang.Math.*;
import static primitives.Util.alignZero;

/**
 * class implements rayTracer abstract class
 */
public class RayTracerBasic extends RayTracer {


    /**
     * constructor
     *
     * @param scene {@link Scene} for {@link Color} calculations to be executed on
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * claculate a {@link  Color} of a pixel (i,j)
     *
     * @param ray ray constructed from camera through the pixel in view plane
     * @return {@link  Color} of the pixel
     */
    @Override
    public Color traceRay(Ray ray) {

        // get geometries of scene
        Geometries geometries = super.scene.getGeometries();

        // check if ray constructed through the pixel intersects any of geometries
        List<GeoPoint> intersections = geometries.findGeoIntersections(ray);

        // if no intersections were found , return basic background color of scene
        if (intersections == null)
            return scene.getBackground();

        // intersection was found, calculate color of the nearest intersection point.
        // the nearest intersection point is first point ray will "reach" and it hides
        // intersection points from camera "view"
        GeoPoint geoPoint = ray.findClosestGeoPoint(intersections);
        return calcColor(geoPoint, ray);
    }

    /**
     * calculate color of geometric shape at a given point (phong model)
     *
     * @param p   {@link GeoPoint} to calculate color at
     * @param ray {@link  Ray} from camera to the point
     * @return {@link Color} of the shape at the point
     */
    private Color calcColor(GeoPoint p, Ray ray) {
        Geometry geometry = p.geometry;
        // l = vector from light source to the point
        // n = normal vector to shape at point
        // r = specular vector to vector from light to point
        // v = ray from camera to point
        //Ip  = Ka * Ia + Ie + (Kd * |l.dorProduct(n)|) * Il + (Ks * max(0 ,(-v).dotProduct(r)) ** nShinines) * Il

        // Ka * Ia
        return scene.getAmbientLight().getIntensity()
                // Ie
                .add((p.geometry.getEmission()))
                // Kd * |l.dorProduct(n)| * +Ks * max(0 ,(-v).dotProduct(r)) ** nShinines * Il
                .add(calcLocalEffects(p, ray));
    }

    /**
     * calculate Kd * |l.dorProduct(n)| * +Ks * max(0 ,(-v).dotProduct(r)) ** nShinines * Il
     * from phong model (specular and diffusive light) for each light in scene
     * adds the color from all light to returned result
     *
     * @param intersection {@link GeoPoint} to calculate color at
     * @param ray          {@link  Ray} from camera to the point
     * @return {@link Color} of the shape at the point
     */
    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
        // (Kd * |l.dorProduct(n)| ) * Il + (Ks * max(0 ,(-v).dotProduct(r)) ** nShinines) * Il
        // l = vector from light source to the point
        // n = normal vector to shape at point
        // r = specular vector to vector from light to point
        // v = ray from camera to point


        // v
        Vector v = ray.getDir();
        // n
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return Color.BLACK;
        //  nShininess
        int nShininess = intersection.geometry.getMaterial().nShininess;
        // Kd
        Double3 kD = intersection.geometry.getMaterial().kD;
        // Ks
        Double3 kS = intersection.geometry.getMaterial().kS;
        Color color = Color.BLACK;
        // loop through all light sources in scene
        var lights = scene.getLights();
        for (var lightSource : lights) {
            // l
            Vector l = lightSource.getL(intersection.point);
            // l.dorProduct(n)
            double nl = alignZero(n.dotProduct(l));
            // check that light direction is towards shape and not behind
            if (nl * nv > 0) { // sign(nl) == sing(nv)
                Color lightIntensity = lightSource.getIntensity(intersection.point);
                // (Kd * |l.dorProduct(n)|) * Il
                color = color.add(calcDiffusive(kD, nl, lightIntensity),
                        // (Ks * max(0 ,(-v).dotProduct(r)) ** nShinines ) * Il
                        calcSpecular(kS, nl, l, n, v, nShininess, lightIntensity));
            }
        }
        return color;
    }

    /**
     * calculate  (Ks * max(0 ,(-v).dotProduct(r)) ** nShinines )* Il
     * from phong model
     *
     * @param kS             attenuation factor of specular light
     * @param nDotL          l.dorProduct(n)
     * @param l              vector from light source to the point
     * @param n              normal vector to shape at point
     * @param v              ray from camera to point
     * @param nShininess     shininess factor of shape
     * @param lightIntensity Il
     * @return Il scaled by  factor
     */
    private Color calcSpecular(Double3 kS, double nDotL, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        // calculating (Ks * max(0 ,(-v).dotProduct(r)) ** nShinines )* Il

        // r = L - (2 *  l.dorProduct(n)) * n
        Vector r = l.subtract(n.scale(2 * nDotL)).normalize();
        // -v
        Vector minusV = v.scale(-1);
        // max(0 ,(-v).dotProduct(r))
        double specular = max(0, minusV.dotProduct(r));
        //** nShinines
        if (specular != 0)
            specular = pow(specular, nShininess);
        // Il scaled by factor
        return lightIntensity.scale(kS.scale(specular));
    }

    /**
     * calculate  (Kd * |l.dorProduct(n)|) * Il
     * from phong model
     *
     * @param kD             attenuation factor of diffusive light
     * @param nDotL          l.dorProduct(n)
     * @param lightIntensity Il
     * @return Il scaled by factor
     */
    private Color calcDiffusive(Double3 kD, double nDotL, Color lightIntensity) {
        // Kd * |l.dorProduct(n)|
        if (nDotL == 0)
            return lightIntensity.scale(0);
        else
            nDotL = abs(nDotL);
        // Il scaled by factor
        return lightIntensity.scale(kD.scale(nDotL));
    }


}
