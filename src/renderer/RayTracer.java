package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

public abstract class RayTracer {

    protected final Scene _scene;

    public RayTracer(Scene scene) {
        _scene = scene;
    }

    abstract Color traceRay(Ray ray);

}
