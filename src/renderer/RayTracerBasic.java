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
     * calculate color at a given point
     *
     * @param p {@link  Point} to calculate color at
     * @return {@link Color} value at the point
     */
    private Color calcColor(GeoPoint p, Ray ray) {
        Geometry geometry = p.geometry;
        return scene.getAmbientLight().getIntensity()
                .add((p.geometry.getEmission()))
                .add(calcLocalEffects(p,ray));
    }

    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
        Vector v = ray.getDir();
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return Color.BLACK;
        int nShininess = intersection.geometry.getMaterial().nShininess;
        Double3 kD = intersection.geometry.getMaterial().kD;
        Double3 kS = intersection.geometry.getMaterial().kS;
        Color color = Color.BLACK;
        var lights = scene.getLights();
        for (var lightSource : lights) {
            Vector l = lightSource.getL(intersection.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // sign(nl) == sing(nv)
                Color lightIntensity = lightSource.getIntensity(intersection.point);
                color = color.add(calcDiffusive(kD, nl, lightIntensity),
                        calcSpecular(kS, nl,l, n, v, nShininess, lightIntensity));
            }
        }
        return color;
    }

    private Color calcSpecular(Double3 kS,double nDotL, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        Vector r = l.subtract(n.scale(2 * nDotL)).normalize();
        Vector minusV = v.scale(-1);
        double specular = max(0, minusV.dotProduct(r));
        if (specular != 0)
            specular = pow(specular, nShininess);
        return lightIntensity.scale(kS.scale(specular));
    }

    private Color calcDiffusive(Double3 kD, double nDotL, Color lightIntensity) {
        if(nDotL==0)
            return lightIntensity.scale(0);
        else
            nDotL = abs(nDotL);
            return lightIntensity.scale(kD.scale(nDotL));
    }



}
