package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test for {@link Sphere} class functionalities
 */
class SphereTest {

    /**
     * testing methode {@link geometries.Sphere#getNormal(Point)}
     */
    @Test
    void testGetNormal() {
        Sphere sph=new Sphere(new Point(0,0,2),1);
        assertEquals(new Vector(0,0,1),
                sph.getNormal(new Point(0,0,3)),
                "normal vector returned is incorrect");

    }

    // Tests for method findIntersection(Ray)
    Sphere sphere = new Sphere( new Point (2, 0, 0),2d);


    //region  ============ Equivalence Partitions Tests ==============
    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsEP01() {
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(0, 0, 4), new Vector(0, 1, 0))),
                "Ray's line out of sphere");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsEP02() {

        // TC02: Ray starts before and crosses the sphere (2 points)
        Point p1 = new Point(1.203117852267963, 0, 1.834387865917667);
        Point p2 = new Point(3.136504789241472, 0, -1.645708620634649);
        List<Point> result = sphere.findIntersections(new Ray(new Point(0, 0, 4),
                new Vector(5,0,-9)));
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "Ray crosses sphere");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsEP03() {

        // TC03: Ray starts inside sphere and crosses the sphere (1 points)
        Point p1 = new Point(1, 0, 1.732050807568877);
        List<Point> result = sphere.findIntersections(new Ray(new Point(1, 0, -1),
                new Vector(0,0,1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1), result, "Ray crosses sphere");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsEP04() {

        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(5, 8, 9), new Vector(6, 1, 1))),
                "Ray's line out of sphere");
    }
    //endregion


    //region ============================== Boundary Values Tests ========================================================

    //region **** Group: Ray's line crosses the sphere (but not the center)
    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA01() {

        // TC11: Ray starts at sphere and goes inside (1 points)
        Point p1 = new Point(3, 0, 1.732050807568877);
        List<Point> result = sphere.findIntersections(new Ray(new Point(3, 0, -1.732050807568877),
                new Vector(0,0,1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1), result, "Ray crosses sphere");
    }
    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA02() {

        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(3, 0, 1.732050807568877), new Vector(0, 1, 1))),
                "Ray's line out of sphere");
    }
    //endregion

    //region **** Group: Ray's line goes through the center
    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA03() {

        // TC13: Ray starts before the sphere (2 points)
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(4, 0, 0);
        List<Point> result = sphere.findIntersections(new Ray(new Point(-6, 0, 0),
                new Vector(1,0,0)));
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "Ray crosses sphere");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA05() {
        // TC14: Ray starts at sphere and goes inside (1 points)
        Point p1 = new Point(2, 0, 2);
        List<Point> result = sphere.findIntersections(new Ray(new Point(2, 0, -2),
                new Vector(0,0,1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1), result, "Ray crosses sphere");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA06() {
        // TC15: Ray starts inside (1 points)
        Point p1 = new Point(2, 0, -2);
        List<Point> result = sphere.findIntersections(new Ray(new Point(2, 0, 1),
                new Vector(0,0,-1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1), result, "Ray crosses sphere");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA07() {
        // TC16: Ray starts at the center (1 points)
        Point p1 = new Point(0, 0, 0);
        List<Point> result = sphere.findIntersections(new Ray(new Point(2, 0, 0),
                new Vector(-1,0,0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1), result, "Ray crosses sphere");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA08() {
        // TC17: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(4, 0, 0), new Vector(1, 0, 0))),
                "Ray's line out of sphere");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA09() {
        // TC18: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(6, 0, 0), new Vector(1, 0, 0))),
                "Ray's line out of sphere");
    }
    //endregion

    //region  **** Group: Ray's line is tangent to the sphere (all tests 0 points)
    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA10() {
        // TC19: Ray starts before the tangent point(0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(-5, 0, 4), new Vector(1, 0, 0))),
                "Ray's line out of sphere");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA11() {
        // TC20: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(2, 0, -2), new Vector(1, 0, 0))),
                "Ray's line out of sphere");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA12() {
        // TC21: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(4, 0, 3), new Vector(0, 0, 1))),
                "Ray's line out of sphere");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA13() {
        // TC19: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        assertNull(sphere.findIntersections(new Ray(new Point(5, 0, -5), new Vector(0, 0, 1))),
                "Ray's line out of sphere");
    }
    //endregion
    //endregion

    //region *** test including consideration that points are closer to ray origin than maxDistance parameter
    // ray and sphere intersect twice at (0,0,3) and (0,6,3)
    Sphere sphere1 = new Sphere(new Point(0,3,3),3);
    Ray ray = new Ray(new Point(0,-4,3),new Vector(0,1,0));
    Intersectable.GeoPoint gp1 = new Intersectable.GeoPoint(sphere1 , new Point(0, 0, 3));
    Intersectable.GeoPoint gp2 = new Intersectable.GeoPoint(sphere1 , new Point(0, 6, 3));

    /**
     * Test method for {@link geometries.Sphere#findGeoIntersectionsHelper(Ray, double)}
     */
    @Test
    void findGeoIntersectionsEP1() {
        // TC01 -  max distance is smaller than distance to both intersection points - no intersections
        assertNull(sphere1.findGeoIntersectionsHelper(ray,2),"points are further than maxDistance");

    }

    /**
     * Test method for {@link geometries.Sphere#findGeoIntersectionsHelper(Ray, double)}
     */
    @Test
    void findGeoIntersectionsEP2() {
        //TC02 -  max distance is smaller than distance to second intersection point - one intersection point
        List<Intersectable.GeoPoint> res = sphere1.findGeoIntersectionsHelper(ray,5);
        assertEquals(List.of(gp1),res,"one point only is in boundary");

    }

    @Test
    void findGeoIntersectionsEP3() {
        //TC03 -  distance to both points is smaller than maxDistance - two intersection points
        List<Intersectable.GeoPoint> res = sphere1.findGeoIntersectionsHelper(ray,12);
        assertEquals(List.of(gp1,gp2),res,"one point only is in boundary");

    }
    //endregion

    }
