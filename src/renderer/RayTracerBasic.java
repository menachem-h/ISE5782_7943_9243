package renderer;

import geometries.Geometries;
import geometries.Geometry;
import geometries.Intersectable;
import lighting.LightSource;
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
     * todo
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * constructor
     *
     * @param scene {@link Scene} for {@link Color} calculations to be executed on
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * calculate a {@link  Color} of a pixel (i,j)
     * @param ray ray constructed from camera through the pixel in view plane
     * @return {@link  Color} of the pixel
     */
    @Override
    public Color traceRay(Ray ray) {

        // find the closest intersection point
        GeoPoint closestPoint = findClosestIntersection(ray);
        // if no intersection point was found , return basic background color of scene
        if (closestPoint == null)
            return scene.getBackground();

        // intersection was found, calculate color of the of pixel.
        return calcColor(closestPoint, ray);
    }

    /**
     * todo
     * @param gp
     * @param ray
     * @return
     */
    private Color calcColor(GeoPoint gp, Ray ray){
        return calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.getAmbientLight().getIntensity());

    }


    /**
     * calculate color of geometric shape at a given point (phong model)
     *
     * @param p   {@link GeoPoint} to calculate color at
     * @param ray {@link  Ray} from camera to the point
     * @return {@link Color} of the shape at the point
     */
    private Color calcColor(GeoPoint p, Ray ray ,int level, Double3 k) {
        Geometry geometry = p.geometry;
        // l = vector from light source to the point
        // n = normal vector to shape at point
        // r = specular vector to vector from light to point
        // v = ray from camera to point
        //Ip  = Ka * Ia + Ie + (Kd * |l.dorProduct(n)|) * Il + (Ks * max(0 ,(-v).dotProduct(r)) ** nShinines) * Il

        // local effects (basic color)
        Color color =
                // Ie
                p.geometry.getEmission()
                // Kd * |l.dorProduct(n)| * +Ks * max(0 ,(-v).dotProduct(r)) ** nShinines * Il
                .add(calcLocalEffects(p, ray));

        // global effects (reflection)
        if (level == 1)
            return color;
        else
            return color.add(calcGlobalEffects(p, ray, level, k));

    }

    /**
     * todo
     * @param gp
     * @param ray
     * @param level
     * @param k
     * @return
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Color color = Color.BLACK;
        Vector normal = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();
        Double3 kR = material.kR;
        Double3 kT = material.kT;
        Vector direction = ray.getDir();

        Double3 kKr = kR.scale(k);
        if (kKr.greaterThan(MIN_CALC_COLOR_K)) {
            Ray reflectedRay = constructReflectedRay(gp.point, normal, direction);
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
            if(reflectedPoint!=null){
                color = color.add(calcColor(reflectedPoint, reflectedRay, level - 1, kKr)
                    .scale(kR));}
        }

        Double3 kKt = kT.scale(k);
        if (kKr.greaterThan(MIN_CALC_COLOR_K)) {
            Ray refractedRay = constructRefractedRay(gp.point,normal, direction);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            if(refractedPoint!=null){
            color = color.add(calcColor(refractedPoint, refractedRay,level-1,kKt))
                .scale(kT);}
        }
        return color;
    }

    /**
     * todo
     * @param p
     * @param N
     * @return
     */
    private Ray constructReflectedRay(Point p,Vector N, Vector l){
        // dot product of N and ray
        double nDotL = alignZero(N.dotProduct(l));
        // r = L - (2 *  l.dorProduct(n)) * n
        Vector r = l.subtract(N.scale(2 * nDotL)).normalize();
        return new Ray(p,N, r);
    }

    private Ray constructRefractedRay(Point p,Vector N, Vector l){
        return new Ray(p,N,l);
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
                if (unshaded(intersection,l,n,lightSource)) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point);
                    // (Kd * |l.dorProduct(n)|) * Il
                    color = color.add(calcDiffusive(kD, nl, lightIntensity),
                            // (Ks * max(0 ,(-v).dotProduct(r)) ** nShinines ) * Il
                            calcSpecular(kS, nl, l, n, v, nShininess, lightIntensity));
                }
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


    /**
     * ToDo
     * @param gp
     * @param l
     * @param n
     * @return
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource light){
        Vector lightScaled = l.scale(-1); // from point to light source
        Ray shadowRay = new Ray(gp.point,n, lightScaled);
        double distance = light.getDistance(shadowRay.getP0());
        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(shadowRay,distance);
        if(intersections== null)
                return true;
        boolean transparency = intersections.get(0).geometry.getMaterial().kT.equals(Double3.ZERO);
        return transparency ? false : true;

    }

    /**
     * find the closest intersection point between ray and geometries in scene
     * @param ray ray constructed from camera to scene
     * @return closest intersection {@link GeoPoint}
     */
    private GeoPoint findClosestIntersection(Ray ray){
        // check if ray constructed through the pixel intersects any of geometries
        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(ray);

        // return closest point if list is not empty
        return intersections == null ? null : ray.findClosestGeoPoint(intersections);

    }

}
