package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

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

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point P0 = ray.getP0();
        Vector v = ray.getDir();

        // check at home
        if (P0.equals(center)) {
            return List.of(center.add(v.scale(radius)));
        }

        Vector U = center.subtract(P0);

        double tm = alignZero(v.dotProduct(U));
        double d = alignZero(Math.sqrt(U.lengthSquared() - tm * tm));

        // no intersections : the ray direction is above the sphere
        if (d >= radius) {
            return null;
        }

        double th = alignZero(Math.sqrt(radius * radius - d * d));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        if (t1 > 0 && t2 > 0) {
            Point P1 =ray.getPoint(t1);
            Point P2 =ray.getPoint(t2);
            return List.of(P1, P2);
        }
        if (t1 > 0) {
            Point P1 =ray.getPoint(t1);
            return List.of(P1);
        }
        if (t2 > 0) {
            Point P2 =ray.getPoint(t2);
            return List.of(P2);
        }
        return null;
    }
}
