package geometries;

import primitives.Double3;
import primitives.Point;
import primitives.Vector;

/**
 * Plane class represents a two-dimensional plane in 3D Cartesian coordinate
 * system
 */
public class Plane implements Geometry {

    /**
     * point on plane
     */
    final private Point _q0;

    /**
     * normal vector of plane ( vector orthogonal to plan)
     */
    final private Vector _normal;

    /**
     * plane constructor based on three points
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     * @throws IllegalArgumentException in one of any case of illegal combination of
     * points:
     * <ul>
     * <li>two or more points are the same point</li>
     * <li> all three points are on same line </li>
     * </ul>
     */
    public Plane(Point p1, Point p2, Point p3) {

        if(p1.equals(p2) || p1.equals(p3) || p2.equals(p3))
            throw new IllegalArgumentException("points must be different");

        _q0=p1;

        Vector U= p2.subtract(p1);
        Vector V = p3.subtract(p1);
        Vector N;
        try {
            N = U.crossProduct(V);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("The three points are on same line, can not represent a Plane");
        }
         _normal =N.normalize();
    }

    /**
     * plane constructor based on a point and normal vector
     * @param q0 point on plane
     * @param vector normal vector orthogonal to plane
     */
    public Plane(Point q0, Vector vector) {
        _q0 = q0;
        _normal = vector.normalize();
    }

    public Point getQ0() {
        return _q0;
    }

    public Vector getNormal() {
        return _normal;
    }

    @Override
    public String toString() {
        return "Plane: " +
                "_q0=" + _q0 +
                ", _normal=" + _normal;
    }

    @Override
    public Vector getNormal(Point point) {
        return _normal;
    }
}
