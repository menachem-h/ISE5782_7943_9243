package renderer;

/**
 * methods available for Anti-Aliasing improvements of camera rendering
 */
public enum AntiAliasing
    {
        /**
         * no Anti-Aliasing method used
         */
        NONE,
        /**
         * improve Anti-Aliasing - for each ray cast to a pixel
         * cast other random rays in a grid in the pixel to get the mean value for the final color value
         */
        RANDOM,
        /**
         * improve Anti-Aliasing - for each ray cast to a pixel
         * cast four other rays to the corners of the pixel to get the mean value for the final color value
         */
        CORNERS
    }
