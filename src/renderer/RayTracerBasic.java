package renderer;

import geometries.Geometries;
import geometries.Intersectable;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import  geometries.Intersectable.GeoPoint;

import java.util.List;

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
        Color result =scene.getAmbientLight().getIntensity();
        result = result.add(p.geometry.getEmission());
        return result;
    }
}
