package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * abstract class responsible for calculating colors of a scene
 */
public abstract class RayTracer {

    /**
     * {@link Scene} for {@link Color} calculations to be executed on
     */
    protected final Scene scene;

    /**
     * constructor
     * @param scene {@link Scene}
     */
    public RayTracer(Scene scene) {
        this.scene = scene;
    }

    /**
     * abstract method - calculate color of a pixel in  image
     * @param ray ray constructed from camera through the pixel in view plane
     * @return {@link Color} of pixel
     */
    public abstract Color traceRay(Ray ray);

}
