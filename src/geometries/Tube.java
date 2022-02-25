package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Tube implements Geometry{

    final protected Ray _axisRay;
    final protected double _radius;

    public Tube(Ray axisRay, double radius) {
        _axisRay = axisRay;
        if (radius <=0)
            throw new IllegalArgumentException("radius must be positive value");
        _radius = radius;
    }

    public Ray getAxisRay() {
        return _axisRay;
    }

    public double getRadius() {
        return _radius;
    }

    @Override
    public String toString() {
        return "Tube: " +
                "axisRay = " + _axisRay +
                ", radius = " + _radius;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
