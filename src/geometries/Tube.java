package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.*;

/**
 * Tube class represents a three-dimensional tube in  3D Cartesian coordinate
 * system
 */
public class Tube implements Geometry {

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
     *
     * @param axisRay ray originating from base of tube
     * @param radius  radius of tube
     * @throws IllegalArgumentException <p>if radius sent as parameter is not a positive value</p>
     */
    public Tube(Ray axisRay, double radius) {
        _axisRay = axisRay;
        if (radius <= 0)
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
        Vector direction = _axisRay.getDir();
        Point P0 = _axisRay.getP0();

        double t = (direction.dotProduct(point.subtract(P0)));
        Point O = P0.add(direction.scale(t));

        //given point is on axis ray
        if (point.equals(O))
            throw new IllegalArgumentException("point cannot be on the axis ray");

        if (isZero(t))
            return point.subtract(P0).normalize();
        else
            return point.subtract(O).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {

        Vector v = ray.getDir();
        Vector vt = _axisRay.getDir();
        Point pa = _axisRay.getP0();
        Point p0 = ray.getP0();

        if (v.equals(vt))
            return null;


        double vvt, a, b, c;
        vvt = v.dotProduct(vt);

        Vector vMinusVt =null;
        Vector delta = null;

        if (isZero(vvt))
            a = v.lengthSquared();
        else {
            vMinusVt = v.subtract(vt.scale(vvt));
            a = vMinusVt.lengthSquared();
        }

        if(p0.equals(pa))
        {
            b = 0;
            c = -(_radius * _radius);
        }
        else {

            delta = p0.subtract(pa);

            try {
                Vector deltaMinusVt = delta.subtract(vt.scale(delta.dotProduct(vt)));
                if(isZero(vvt))
                    b= 2*v.dotProduct(deltaMinusVt);
                else
                    b = 2 * (vMinusVt.dotProduct(deltaMinusVt));
                c = deltaMinusVt.lengthSquared() - (_radius * _radius);
            }
            catch (IllegalArgumentException ex) {
                if(isZero(vvt))
                    b = 0;
                else
                    b= b = 2 * (vMinusVt.dotProduct(delta));
                c = delta.lengthSquared()-(_radius * _radius);
            }
        }


        double discriminant = alignZero(b * b - 4 * a * c);
        if (discriminant <= 0)
            return null;
        else {
            double t1 = (-b + Math.sqrt(discriminant)) / (2 * a);
            double t2 = (-b - Math.sqrt(discriminant)) / (2 * a);

            if (t1 > 0 && t2 > 0)
                return List.of(ray.getPoint(t2), ray.getPoint(t1));
            if (t1 > 0)
                return List.of(ray.getPoint(t1));
            if (t2 > 0)
                return List.of(ray.getPoint(t2));
        }

        return null;
    }
}
