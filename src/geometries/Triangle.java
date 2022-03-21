package geometries;

import primitives.*;
import static primitives.Util.*;
import java.util.List;

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

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> result = plane.findIntersections(ray);
        if (result == null)
            return null;

        Vector v = ray.getDir();
        Point p0 = ray.getP0();
        Point p1 = vertices.get(0);
        Point p2 = vertices.get(1);
        Point p3 = vertices.get(2);
        Vector v1 = p1.subtract(p0);
        Vector v2 = p2.subtract(p0);
        Vector v3 = p3.subtract(p0);

        double n1 = v.dotProduct(v1.crossProduct(v2));
        if (isZero(n1))
            return null;

        double n2 = v.dotProduct(v2.crossProduct(v3));
        if (isZero(n2))
            return null;

        double n3 = v.dotProduct(v3.crossProduct(v1));
        if (isZero(n3))
            return null;

        if (!((n1 < 0 && n2 < 0 && n3 < 0 )||( n1 > 0 && n2 > 0 && n3 > 0)))
            return null;

        return List.of(result.get(0));
    }

    @Override
    public String toString() {
        return "Triangle: " +
                "vertices = " + vertices +
                ", plane = " + plane;
    }
}
