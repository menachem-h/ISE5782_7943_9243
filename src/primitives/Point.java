package primitives;

import java.util.Objects;

/**
 * Point class represents a point in 3D Cartesian coordinate
 * system
 */
public class Point{

    /**
     * (x,y,z) coordinates of point
     */
    final protected Double3 _xyz;

    /**
     * Point constructor to initialize a point based on  a double3 class object
     * @param xyz Double3 object containing value of (x,y,z) coordinates
     */
    protected Point(Double3 xyz) {
        _xyz = xyz;
    }

    public double getX() {
        return _xyz._d1;
    }
    public double getY() {
        return _xyz._d2;
    }
    public double getZ() {
        return _xyz._d3;
    }


    /**
     * Point constructor to initialize point based on three coordinates
     * @param x  x coordinate value
     * @param y  y coordinate value
     * @param z  z coordinate value
     */
    public Point(double x, double y, double z) {
        _xyz=new Double3(x,y,z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(_xyz, point._xyz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_xyz);
    }

    @Override
    public String toString() {
        return "Point " + _xyz ;
    }

    /**
     * calculate (x_2-x_1)²+(y_2-y_1)² expression
     * from  the d=√((x_2-x_1)²+(y_2-y_1)²) formula of distance between two points.
     * @param point second point
     * @return expressions value in double format
     */
    public double distanceSquared(Point point) {

        double u1= _xyz._d1 - point._xyz._d1;
        double u2= _xyz._d2 - point._xyz._d2;
        double u3= _xyz._d3 - point._xyz._d3;

        return u1*u1 + u2*u2 + u3*u3;
    }

    /**
     * calculate distance between two points.
     * implements the formula : d=√((x_2-x_1)²+(y_2-y_1)²)
     * @param point second point to calculate distance from
     * @return distance between points in double format
     */
    public double distance(Point point){ return Math.sqrt(distanceSquared(point));
    }

    /**
     * add a vector to a point
     * @param vector vector to be added to point
     * @return new point received by adding vector to original point
     */
    public Point add(Vector vector) {
        return new Point(_xyz.add(vector._xyz));
    }

    /**
     * subtract  point B  from  point A, (point B  sent as parameter is subtracted from point A calling the method)
     * @param point  point B
     * @return  new vector created by subtraction of the points
     * @throws IllegalArgumentException
     * <p>if a vector is subtracted from itself which returns the illegal (0,0,0) vector</p>
     */
    public Vector subtract(Point point) {

        Double3 result=_xyz.subtract(point._xyz);
        if(result.equals(Double3.ZERO))
            throw new IllegalArgumentException(("resulting of subtract: vector (0,0,0) not allowed"));
        return new Vector(result);
    }
}
