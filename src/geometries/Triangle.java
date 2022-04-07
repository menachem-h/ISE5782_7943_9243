package geometries;

import primitives.*;
import static primitives.Util.*;
import java.util.List;

/**
 * Two-dimensional Triangle in a 3D Cartesian coordinate system
 */
public class Triangle extends Polygon {

    /**
     * Triangle constructor based on three points
     *
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    /**
     * find intersection between ray and  2D triangle
     * @param ray ray towards object
     * @return list containing one intersection point
     */

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        // check if ray intersects plane containing the triangle
        List<GeoPoint> result = plane.findGeoIntersections(ray);
        // no intersections
        if (result == null)
            return null;

        // check if intersection points are in Triangle
        Vector v = ray.getDir();
        Point p0 = ray.getP0();

        // create three vectors between ray origin and
        //each of triangle vertices
        Point p1 = vertices.get(0);
        Point p2 = vertices.get(1);
        Point p3 = vertices.get(2);
        Vector v1 = p1.subtract(p0);
        Vector v2 = p2.subtract(p0);
        Vector v3 = p3.subtract(p0);

        // n1,n2 ,n3 = value of dot product between ray vector
        // and the result vector of cross product between pairs
        // of vectors from ray origin and triangle vertices
        // if n1 or n2 pr m3 == 0 - intersection on border -> no intersection

        double n1 = v.dotProduct(v1.crossProduct(v2));
        if (isZero(n1))
            return null;

        double n2 = v.dotProduct(v2.crossProduct(v3));
        if (isZero(n2))
            return null;

        double n3 = v.dotProduct(v3.crossProduct(v1));
        if (isZero(n3))
            return null;

        // if sign of all three values ,n1 ,n2 ,n3 is not equal
        // intersection point is not on triangle
        if (!((n1 < 0 && n2 < 0 && n3 < 0 )||( n1 > 0 && n2 > 0 && n3 > 0)))
            return null;

        // ray intersects triangle
        return List.of(new GeoPoint(this, result.get(0).point));
    }

    @Override
    public String toString() {
        return "Triangle: " +
                "vertices = " + vertices +
                ", plane = " + plane;
    }
}
