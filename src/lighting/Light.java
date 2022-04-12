package lighting;
import primitives.Color;

/**
 * Light object - abstract class
 */
abstract class Light {
    /**
     * intensity of the light
     */
    private final Color intensity ;

    /**
     * constructor
     * @param intensity {@link Color} of intensity of light
     */
    protected Light(Color intensity){
        this.intensity = intensity;
    }

    /**
     * getter for intensity field
     * @return intensity of light
     */
    public Color getIntensity() {
        return intensity;
    }
}
