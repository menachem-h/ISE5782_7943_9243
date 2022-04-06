package renderer;

import geometries.Geometries;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

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
        List<Point> intersections =geometries.findIntersections(ray);

        // if no intersections were found , return basic background color of scene
        if (intersections==null)
            return scene.getBackground();

        // intersection was found, calculate color of the nearest intersection point.
        // the nearest intersection point is first point ray will "reach" and it hides
        // intersection points from camera "view"
        return calcColor(ray.findClosestPoint(intersections));
    }

    /**
     * calculate color at a given point
     * @param p {@link  Point} to calculate color at
     * @return {@link Color} value at the point
     */
    private Color calcColor(Point p){
        return scene.getAmbientLight().getIntensity();
    }
}
