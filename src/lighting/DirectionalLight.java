package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * light source object with direction to the light (no attenuation)
 */
public class DirectionalLight extends Light implements LightSource {

    /**
     * direction of the light
     */
    private Vector direction;

    /**
     * constructor
     * @param intensity {@link Color} of intensity of the light
     * @param direction direction {@link Vector} of beam
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }


    /**
     * get intensity of the light at a given point in 3D space
     * @param p {@link Point} to get intensity at
     * @return {@link Color} of intensity of the light at the point
     */
    @Override
    public Color getIntensity(Point p) {
        return getIntensity();
    }

    /**
     * get the direction from the light to a {@link Point} in 3D space
     * @param p {@link Point} point to get vector to
     * @return {@link Vector} from light to the point
     */
    @Override
    public Vector getL(Point p) {
        return direction;
    }


}