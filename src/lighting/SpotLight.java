package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static java.lang.Math.max;

public class SpotLight extends PointLight  {

    private final Vector direction;

    protected SpotLight(Color intensity, Point position,
                        double kC, double kL, double kQ, Vector direction) {
        super(intensity, position, kC, kL, kQ);
        this.direction = direction;
    }

    @Override
    public Color getIntensity(Point p) {
        Color color =super.getIntensity(p);
        //vector from light origin point to point p on geometry
        Vector v=super.getL(p);
        double factor=max(0,direction.dotProduct(v));
        return color.scale(factor);
    }
}
