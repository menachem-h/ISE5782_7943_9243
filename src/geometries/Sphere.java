package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Sphere class represents a three-dimensional sphere in 3D Cartesian coordinate
 * system
 */
public class Sphere implements Geometry{

    /**
     * center point of sphere
     */
    final private Point center;

    /**
     * radius of sphere
     */
    final private double radius;

    /**
     * sphere constructor based on a center point and radius
     * @param center center point of sphere
     * @param radius radius of sphere
     * @throws IllegalArgumentException <p>if radius sent as parameter is not a positive value</p>
     */
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

    /**
     * given a point on the sphere's circumference - calculate normal vector at the point
     * @param point point on sphere  to get normal vector at
     * @return normal vector (normalized) at given point
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }
}
