package geometries;

import primitives.Point;
import primitives.Vector;

public class Sphere implements Geometry{
    final private Point center;
    final private double radius;


    public Sphere(Point center, double radius) {
        this.center = center;
        if (radius <=0)
            throw new IllegalArgumentException("radius must be positive value");
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Sphere: " +
                "center = " + center +
                ", radius = " + radius;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
