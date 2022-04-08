package renderer;

import geometries.Geometries;
import geometries.Geometry;
import geometries.Intersectable;
import primitives.*;
import scene.Scene;
import  geometries.Intersectable.GeoPoint;

import java.util.List;

import static java.lang.Math.*;

/**
 * class implements rayTracer abstract class
 */
public class RayTracerBasic extends RayTracer{


    /**
     * constructor
     * @param scene {@link Scene} for {@link Color} calculations to be executed on
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * claculate a {@link  Color} of a pixel (i,j)
     * @param ray ray constructed from camera through the pixel in view plane
     * @return {@link  Color} of the pixel
     */
    @Override
    public Color traceRay(Ray ray) {

        // get geometries of scene
        Geometries geometries = super.scene.getGeometries();

        // check if ray constructed through the pixel intersects any of geometries
        List<GeoPoint> intersections =geometries.findGeoIntersections(ray);

        // if no intersections were found , return basic background color of scene
        if (intersections==null)
            return scene.getBackground();

        // intersection was found, calculate color of the nearest intersection point.
        // the nearest intersection point is first point ray will "reach" and it hides
        // intersection points from camera "view"
        GeoPoint geoPoint = ray.findClosestGeoPoint(intersections);
        return calcColor(geoPoint,ray);
    }

    /**
     * calculate color at a given point
     * @param p {@link  Point} to calculate color at
     * @return {@link Color} value at the point
     */
    private Color calcColor(GeoPoint p, Ray  ray){
        Geometry geometry=p.geometry;
        Color Ia =scene.getAmbientLight().getIntensity();
        Color Ie = geometry.getEmission();
        var lights =super.scene.getLights();
        Point pt=p.point;
        Vector N=geometry.getNormal(pt);
        Vector minusV= ray.getDir().scale(-1);
        int shininess =geometry.getMaterial().nShininess;
        Double3 kD=geometry.getMaterial().kD;
        Double3 kS=geometry.getMaterial().kS;

        Color Il=Color.BLACK;

        for (var light:lights){
            Vector l=light.getL(pt);
            double lDotN=abs(l.dotProduct(N));
            Vector r=l.subtract(N.scale(2*lDotN)).normalize();

            double diffuse= max(0,minusV.dotProduct(r));
            if (diffuse !=0)
                diffuse=pow(diffuse,shininess);

            Double3 factor= kD.scale(lDotN).add( kS.scale(diffuse));
            Il=Il.add(light.getIntensity(pt).scale(factor));
        }

        return Ia.add(Ie).add(Il);
    }
}
