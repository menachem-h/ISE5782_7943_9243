package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

public class Cylinder extends Tube{

    /**
     * height of cylinder
     */
    final private double height;

    /**
     * cylinder constructor based on a radius , ray (direction), and a height
     * @param axisRay ray originating from base of cylinder
     * @param radius radius of cylinder
     * @param height height of cylinder
     * @throws IllegalArgumentException <p>if height sent as parameter is not a positive value</p>
     */
    public Cylinder(Ray axisRay, double radius, double height) {
        super(axisRay, radius);
        if(height<= 0)
            throw new IllegalArgumentException("height must be positive value");
        this.height=height;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Cylinder: " +
                "height = " + height +
                ", axisRay = " + _axisRay +
                ", radius=" + _radius ;
    }

    @Override
    public Vector getNormal(Point point){
        Vector direction = _axisRay.getDir();
        Point P0 = _axisRay.getP0();

        //given point is on direction vector - equal to p0 or same spot on top base of cylinder
        if(point.equals(P0)||point.equals(P0.add(direction.scale(height))))
            return direction.normalize();

        // given point is on the base of the cylinder
        if(isZero(point.subtract(P0).dotProduct(direction)))
            return direction.normalize();

        // given point is on top base of the cylinder
        if (isZero(point.subtract(P0.add(direction.scale(height))).dotProduct(direction)))
            return direction.normalize();

        // given point is on the circumference of cylinder
        return super.getNormal(point);
    }
}
