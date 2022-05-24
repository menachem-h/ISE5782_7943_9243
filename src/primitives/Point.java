package primitives;

import java.util.Objects;

/**
 * Point in 3D space - Cartesian coordinate representation
 */
public class Point{

    /**
     * origin point of 3D space
     */
    public static final Point ZERO = new Point(0d,0d,0d);
    /**
     * (x,y,z) coordinates of point
     */
    final protected Double3 xyz;

    /**
     * Point constructor to initialize a point based on  a double3 class object
     * @param xyz Double3 object containing value of (x,y,z) coordinates
     */
    protected Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * get X axis coordinate of a point
     * @return X axis coordinate - (double)
     */
    public double getX() {
        return xyz.d1;
    }

    /**
     * get Y axis coordinate of a point
     * @return Y axis coordinate - (double)
     */
    public double getY() {
        return xyz.d2;
    }

    /**
     * get Z axis coordinate of a point
     * @return Z axis coordinate - (double)
     */
    public double getZ() { return xyz.d3; }


    /**
     * Point constructor to initialize point based on three coordinates
     * @param x  x coordinate value
     * @param y  y coordinate value
     * @param z  z coordinate value
     */
    public Point(double x, double y, double z) {
        xyz =new Double3(x,y,z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(xyz, point.xyz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xyz);
    }

    @Override
    public String toString() {
        return "Point " + xyz;
    }

    /**
     * calculate (x_2-x_1)²+(y_2-y_1)² expression
     * from  the d= sqrt((x_2-x_1)²+(y_2-y_1)²) formula of distance between two points.
     * @param point second point
     * @return expressions value in double format
     */
    public double distanceSquared(Point point) {

        double u1= xyz.d1 - point.xyz.d1;
        double u2= xyz.d2 - point.xyz.d2;
        double u3= xyz.d3 - point.xyz.d3;

        return u1*u1 + u2*u2 + u3*u3;
    }

    /**
     * calculate distance between two points.
     * implements the formula : d=sqrt((x_2-x_1)²+(y_2-y_1)²)
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
        return new Point(xyz.add(vector.xyz));
    }

    /**
     * subtract  point B  from  point A, (point B  sent as parameter is subtracted from point A calling the method)
     * @param point  point B
     * @return  new vector created by subtraction of the points
     * @throws IllegalArgumentException
     * <p>if a vector is subtracted from itself which returns the illegal (0,0,0) vector</p>
     */
    public Vector subtract(Point point) {

        Double3 result= xyz.subtract(point.xyz);
        if(result.equals(Double3.ZERO))
            throw new IllegalArgumentException(("resulting of subtract: vector (0,0,0) not allowed"));
        return new Vector(result);
    }
}
