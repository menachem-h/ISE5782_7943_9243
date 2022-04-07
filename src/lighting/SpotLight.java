package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight  {

    private final Vector direction;

    protected SpotLight(Color intensity, Point position,
                        double kC, double kL, double kQ, Vector direction) {
        super(intensity, position, kC, kL, kQ);
        this.direction = direction;
    }
}
