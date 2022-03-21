package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

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

    /**
     * implementation {@link Geometry#getNormal(Point)}
     *
     * @param point point to calculate normal from/to
     * @return normal
     */
    @Override
    public Vector getNormal(Point point){
        Vector direction = _axisRay.getDir();
        Point P0 = _axisRay.getP0();

        //given point is on base of cylinder
        if(point.equals(P0)||isZero(point.subtract(P0).dotProduct(direction)))
            return direction.normalize().scale(-1);


        // given point is on top base of the cylinder
        if (isZero(point.subtract(P0.add(direction.scale(height))).dotProduct(direction))||
                point.equals(P0.add(direction.scale(height))))
            return direction.normalize();

        // given point is on the circumference of cylinder
        return super.getNormal(point);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {

        Point basePoint=_axisRay.getP0();
        Point topPoint =_axisRay.getPoint(height);
        Vector vC=_axisRay.getDir();

        List<Point> result= new LinkedList<>();
        Plane basePlane= new Plane(basePoint,vC);
        List<Point> intersectionsBase=basePlane.findIntersections(ray);

        if(intersectionsBase!=null){
            for (Point p:intersectionsBase) {
                if(p.equals(basePoint))
                   result.add(basePoint);
                else if(p.subtract(basePoint).dotProduct(p.subtract(basePoint))<_radius*_radius)
                    result.add(p);
            }
        }

        Plane topPlane= new Plane(topPoint,vC);
        List<Point> intersectionsTop=basePlane.findIntersections(ray);
        if(intersectionsTop!=null){
            for (Point p:intersectionsTop) {
                if(p.equals(topPoint))
                    result.add(topPoint);
                else if(p.subtract(topPoint).dotProduct(p.subtract(topPoint))<_radius*_radius)
                    result.add(p);
            }
        }

        List<Point> intersectionsTube=super.findIntersections(ray);


        if(intersectionsTube!=null){
            for (Point p:intersectionsTube){
                if(vC.dotProduct(p.subtract(basePoint))>0 && vC.dotProduct(p.subtract(topPoint))<0)
                    result.add(0,p);
            }
        }

        if(result.size()<2){





        }

        return super.findIntersections(ray);
    }
}