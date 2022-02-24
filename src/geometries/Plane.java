package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane implements Geometry {
    final Point _q0;

    public Plane(Point p1, Point p2, Point p3) {
        _q0=p1;

        Vector U= p2.subtract(p1);
        Vector V = p3.subtract(p1);


        Vector N = U.crossProduct(V);
         _normal =N.normalize();
    }

    public Point getQ0() {
        return _q0;
    }

    public Vector getNormal() {
        return _normal;
    }

    final Vector _normal;

    public Plane(Point q0, Vector vector) {
        _q0 = q0;
        _normal = vector.normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
