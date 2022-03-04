package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Tube class represents a three-dimensional tube in  3D Cartesian coordinate
 * system
 */
public class Tube implements Geometry{

    /**
     * ray originating from base of tube
     */
    protected final Ray _axisRay;
    /**
     * radius of tube
     */
    protected final double _radius;

    /**
     * tube constructor based on a radius and a ray from base of tube
     * @param axisRay ray originating from base of tube
     * @param radius radius of tube
     * @throws IllegalArgumentException <p>if radius sent as parameter is not a positive value</p>
     */
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
