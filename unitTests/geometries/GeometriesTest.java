package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test for {@link Geometries} class functionalities
 */
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


    // test for intersection points with consideration to maxDistance parameter

    // ray and sphere intersect twice at (0,0,3) and (0,6,3)
    Sphere sphere1 = new Sphere(new Point(0,3,3),3);
    Triangle triangle1 = new Triangle(new Point(-10,8,0),new Point(10,8,0),new Point(0,8,10));
    Plane plane1 = new Plane(new Point(0,12,0),new Vector(0,1,0));
    Ray ray = new Ray(new Point(0,-4,3),new Vector(0,1,0));
    Intersectable.GeoPoint gp1 = new Intersectable.GeoPoint(sphere1 , new Point(0, 0, 3));
    Intersectable.GeoPoint gp2 = new Intersectable.GeoPoint(sphere1 , new Point(0, 6, 3));
    Intersectable.GeoPoint gp3 = new Intersectable.GeoPoint(triangle1 , new Point(0, 8, 3));
    Intersectable.GeoPoint gp4 = new Intersectable.GeoPoint(plane1 , new Point(0, 12, 3));
    Geometries geometries1=new Geometries(sphere1,triangle1,plane1);

    /**
     * Test method for {@link Geometries#findGeoIntersectionsHelper(Ray, double)}
     */
    @Test
    void findGeoIntersectionsEP1() {
        // TC01 -  max distance is larger than distance to all intersection points - 4 intersections
        List<Intersectable.GeoPoint> res = geometries1.findGeoIntersectionsHelper(ray,20);
        assertEquals(List.of(gp1,gp2,gp3,gp4),res,"one point only is in boundary");

    }

    /**
     * Test method for {@link Geometries#findGeoIntersectionsHelper(Ray, double)}
     */
    @Test
    void findGeoIntersectionsEP2() {
        //TC02 -  max distance is smaller than distance to all intersection points - no intersection
        List<Intersectable.GeoPoint> res = geometries1.findGeoIntersectionsHelper(ray,2);
        assertNull(res, "points are beyond distance");

    }

    /**
     * Test method for {@link Geometries#findGeoIntersectionsHelper(Ray, double)}
     */
    @Test
    void findGeoIntersectionsEP3() {
        //TC03 -  distance to points of sphere and triangle are close enough but plane not - 3 intersection points
        List<Intersectable.GeoPoint> res = geometries1.findGeoIntersectionsHelper(ray,13.5);
        assertEquals(List.of(gp1,gp2,gp3),res,"one point only is in boundary");

    }


}