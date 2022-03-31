package renderer;

import geometries.Geometries;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class RayTracerBasic extends RayTracer{


    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        Geometries geometries = super.scene.getGeometries();
        List<Point> intersections =geometries.findIntersections(ray);
        if (intersections==null)
            return scene.getBackground();
        return calcColor(ray.findClosestPoint(intersections));
    }

    private Color calcColor(Point p){
        return scene.getAmbientLight().getIntensity();
    }
}
