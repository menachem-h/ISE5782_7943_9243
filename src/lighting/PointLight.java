package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource {


    private double kC;
    private double kL;
    private double kQ;
    private Point position;

    protected PointLight(Color intensity ,Point position, double kC, double kL , double kQ) {
        super(intensity);
        this.position = position;
        this.kC = kC;
        this.kL = kL;
        this.kQ = kQ;

    }

    public PointLight setkC(double kC) {
        this.kC = kC;
        return this;
    }

    public PointLight setkL(double kL) {
        this.kL = kL;
        return this;
    }

    public PointLight setkQ(double kQ) {
        this.kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double distance =p.distance(position);
        double factor=kC+kL*distance+kQ*distance*distance;
        Color color=getIntensity().reduce(factor);
        return color;
    }

    @Override
    public Vector getL(Point p) {
        try{
            Vector v=p.subtract(position).normalize();
            return v;
        }
        catch (Exception ex){
            return null;
        }
    }
}
