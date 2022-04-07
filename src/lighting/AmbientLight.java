package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Ambient Light as color for all object in the scene
 */
public class AmbientLight extends Light {



    /**
     * Primary Constructor
     * @param Ia light intensity
     * @param Ka Attenuation coefficient of color
     */
    public AmbientLight(Color Ia, Double3 Ka) {
        super(Ia.scale(Ka));
    }

    /**
     * default constructor
     */
    public AmbientLight() {
        super( Color.BLACK);
    }


}
