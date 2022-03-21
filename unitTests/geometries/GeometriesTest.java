package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTest {

    Plane plane=new Plane(new Point(5,2,2),new Vector(1,0,0));
    Sphere sphere =new Sphere(new Point(2,0,0),1d);
    Triangle triangle = new Triangle(new Point(4,-2,-1),new Point(4,2,-1),new Point(4,0,1));

    Geometries geometries=new Geometries(sphere,triangle,plane);
    Geometries emptyGeometries= new Geometries();

    @Test
    void testFindIntersectionsEP(){
        Ray ray = new Ray(new Point(3.5,0,0),new Vector(1,0,0));
        assertEquals(2,geometries.findIntersections(ray).size(),
                "TC-04 not all shape intersect");
        assertEquals(List.of(new Point(4,0,0),new Point(5,0,0)),
                geometries.findIntersections(ray),
                "TC-04 not all shape intersect");
    }

    @Test
    void testFindIntersectionsBVA1() {
        // TC01 - ray intersects all of the geometries
        Ray ray = new Ray(new Point(0.5,0,0),new Vector(1,0,0));
        assertEquals(4, geometries.findIntersections(ray).size()
        ,"TC-01 all shapes intersect");

        assertEquals(List.of(new Point(1,0,0),new Point(3,0,0),new Point(4,0,0),
                new Point(5,0,0)),geometries.findIntersections(ray),
                "TC-01 all shapes intersect");
    }

    @Test
    void testFindIntersectionsBVA2() {
        Ray ray = new Ray(new Point(0.5,0,0),new Vector(0,1,0));
        assertNull(geometries.findIntersections(ray),"TC-02 no intersection shapes");
    }

    @Test
    void testFindIntersectionsBVA3() {
        Ray ray = new Ray(new Point(4.5,0,0),new Vector(1,0,0));
        assertEquals(1,geometries.findIntersections(ray).size(),
                "TC-03 one shape intersect");
        assertEquals(List.of(new Point(5,0,0)),geometries.findIntersections(ray),
                "TC-03");
    }

    @Test
    void testFindIntersectionsBVA4() {
        Ray ray = new Ray(new Point(4.5,0,0),new Vector(1,0,0));
        assertNull(emptyGeometries.findIntersections(ray),
                "TC-04 empty");

    }


}