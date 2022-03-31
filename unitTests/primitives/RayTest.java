package primitives;

import geometries.Geometries;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    Plane plane=new Plane(new Point(5,2,2),new Vector(1,0,0));
    Sphere sphere =new Sphere(new Point(2,0,0),1d);
    Triangle triangle = new Triangle(new Point(4,-2,-1),new Point(4,2,-1),new Point(4,0,1));

    Geometries geometries=new Geometries(triangle,sphere,plane);
    Geometries emptyGeometries= new Geometries();
    Ray ray= new Ray(new Point(0.5,0,0),new Vector(1,0,0));

    @Test
    void findClosestPointEP() {
        //TC01 the closest point is in middle of list
        assertEquals(new Point(1,0,0),ray.findClosestPoint(geometries.findIntersections(ray))
                ,"wrong intersection point");
    }

    @Test
    void findClosestPointBVA1() {
        //TC02 empty list
        assertNull(ray.findClosestPoint(emptyGeometries.findIntersections(ray))
                ,"no intersection point");
    }

    @Test
    void findClosestPointBVA2() {
        //TC03 the closest point is first in list
        geometries=new Geometries(sphere,triangle,plane);
        assertEquals(new Point(1,0,0),ray.findClosestPoint(geometries.findIntersections(ray))
                ,"wrong intersection point");
    }

    @Test
    void findClosestPointBVA3() {
        //TC04 the closest point is last in list
        geometries=new Geometries(plane,triangle);
        assertEquals(new Point(4,0,0),ray.findClosestPoint(geometries.findIntersections(ray))
                ,"wrong intersection point");
    }
}