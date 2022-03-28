package geometries;

import java.util.List;

import primitives.*;
import static primitives.Util.*;

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
     *
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     * @throws IllegalArgumentException in one of any case of illegal combination of
     *                                  points:
     *                                  <ul>
     *                                  <li>two or more points are the same point</li>
     *                                  <li> all three points are on same line </li>
     *                                  </ul>
     */
    public Plane(Point p1, Point p2, Point p3) {

        // check that all three points are different
        if (p1.equals(p2) || p1.equals(p3) || p2.equals(p3))
            throw new IllegalArgumentException("points must be different");

        _q0 = p1;

        // generate two vectors from the three points
        Vector U = p2.subtract(p1);
        Vector V = p3.subtract(p1);
        Vector N;

        // attempt to get cross product vector of the above vectors
        // if exception is thrown all three points are
        // on same line and cannot represent a plane
        try {
            N = U.crossProduct(V);
        } catch (Exception e) {
            throw new IllegalArgumentException("The three points are on same line, can not represent a Plane");
        }
        // set plane's normal vector to normalized result of cross product vector
        _normal = N.normalize();
    }

    /**
     * plane constructor based on a point and normal vector
     *
     * @param q0     point on plane
     * @param vector normal vector orthogonal to plane
     */
    public Plane(Point q0, Vector vector) {
        _q0 = q0;
        _normal = vector.normalize();
    }


    public Point getQ0() {
        return _q0;
    }

    /**
     * getter for normal field
     * @return normal vector to the plane
     */
    public Vector getNormal() {
        return _normal;
    }

    @Override
    public String toString() {
        return "Plane: " +
                "_q0=" + _q0 +
                ", _normal=" + _normal;
    }

    /**
     * given a point on the plane calculate normal vector to plane at the point
     * @param point point on plane to get normal vector at
     * @return normal vector (normalized)
     */
    @Override
    public Vector getNormal(Point point) {
        return _normal;
    }

    /**
     * find intersection between ray and plane
     * @param ray ray towards the plane
     * @return list with one intersection point
     * @throws IllegalArgumentException if ray is constructed at
     * plane's representing point
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
       Point P0 = ray.getP0();
       Vector v = ray.getDir();
       Vector n = _normal;

        // ray cannot start at plane's origin point
       if(_q0.equals(P0))
           return null;

       // ray points -> P = p0 + t*v_ (v_ = direction vector)
        // points on plane  if normal vector dot product with vector from
        // origin point to proposed point == 0
        // glossary:  (n,v) = dot product between vectors n,v
        // isolating t ,( scaling factor for ray's direction vector ) ->
        // t = (normal vector, vector from origin to point)/ (normal vector, ray vector)
        // if t is positive ray intersects plane
        double nv = n.dotProduct(v);

        // ray direction cannot be parallel to plane orientation
        if (isZero(nv)){
            return null;
        }

        // vector from origin to point
        Vector Q_P0 = _q0.subtract(P0);

        double nQMinusP0 = alignZero(n.dotProduct(Q_P0));

        //t should not be equal to 0
        if( isZero(nQMinusP0)){
            return null;
        }
        // scaling factor for ray , if value is positive
        // ray intersects plane
        double t = alignZero(nQMinusP0 / nv);
        if (t > 0){
            //return immutable List
            return List.of(ray.getPoint(t));
        }
        // no intersection point  - ray and plane in opposite  direction
        return null;
    }
}
