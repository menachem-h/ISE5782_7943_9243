package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static java.lang.Math.max;

public class SpotLight extends PointLight  {

    private final Vector direction;

    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
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
