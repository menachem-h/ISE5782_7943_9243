package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Sphere in 3D Cartesian coordinate system
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

    /**
     * getter for center field
     * @return center point of sphere
     */
    public Point getCenter() {
        return center;
    }

    /**
     * getter for radius field
     * @return radius of sphere
     */
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

    /**
     * find intersection {@link Point}s between ray and sphere
     * @param ray ray towards the object
     * @return list of intersection {@link Point}s
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        Point P0 = ray.getP0();
        Vector v = ray.getDir();

        // ray starts at center point of sphere
        // return point on surface in direction of ray
        if (P0.equals(center)) {
            return List.of(center.add(v.scale(radius)));
        }

        // vector from ray origin to center point
        Vector U = center.subtract(P0);

        // tm = U's projection on ray's vector
        double tm = alignZero(v.dotProduct(U));
        // d between u and ray (at center point)
        double d = alignZero(Math.sqrt(U.lengthSquared() - tm * tm));

        //distance from center to ray is larger than the radius
        // no intersections : the ray direction is above the sphere
        if (d >= radius) {
            return null;
        }

        // th = distance from ray intersection point to point
        // reached by scaling ray vector by tm
        double th = alignZero(Math.sqrt(radius * radius - d * d));
        // t1 = size of distance from first intersection point to
        //  point reached by ray scaled by tm
        double t1 = alignZero(tm - th);
        // t2 = size of distance from point reached by ray scaled by tm
        // to second intersection point
        double t2 = alignZero(tm + th);

        // ray constructed outside sphere
        // two intersection points
        if (t1 > 0 && t2 > 0) {
            Point P1 =ray.getPoint(t1);
            Point P2 =ray.getPoint(t2);
            return List.of(P1, P2);
        }
        // ray constructed inside sphere and intersect in back direction
        if (t1 > 0) {
            Point P1 =ray.getPoint(t1);
            return List.of(P1);
        }
        // ray constructed inside sphere and intersect in forward direction
        if (t2 > 0) {
            Point P2 =ray.getPoint(t2);
            return List.of(P2);
        }
        // no intersection points found - assurance return
        // code should not be reaching this point
        return null;
    }
}
