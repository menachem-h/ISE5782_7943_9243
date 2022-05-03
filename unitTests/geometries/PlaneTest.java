package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {

    /// three points to configure plane
    Point p1 = new Point(1, 2, 3);
    Point p2 = new Point(4, 7, 11);
    Point p3 = new Point(2, -3, 4);

    // two extra points that are on same line as p1
    Point p4 = new Point(2, 4, 6);
    Point p5 = new Point(4, 8, 12);

    Plane plane = new Plane(p1, p2, p3);
    Plane pl = new Plane(new Point(0, 0, 1), new Vector(1, 1, 1));


    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testConstructorBVA01() {
        /// all points are on same line
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(p1, p5, p4),
                "constructed a plane with parallel vertices");
    }

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testConstructorBVA02() {
        /// two point are equal
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(p1, p1, p4),
                "constructed a plane with points that are equal");

    }

    /**
     * testing method {@link  geometries.Plane#getNormal(Point)}
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // test that normal of plane returned is equal to expected normal vector
        double x = 9 / (7 * Math.sqrt(2));
        double y = 1 / (7 * Math.sqrt(2));
        double z = (-2) * Math.sqrt(2) / 7;

        assertEquals(new Vector(x, y, z),
                plane.getNormal(), "normal vector is not returned correctly");
    }



    //region ======================= Equivalence Partitions Tests =========================================================
    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}
     */
    @Test
    void findIntersectionsEP1() {
        // TC01: Ray into plane
        assertEquals(
                List.of(new Point(0.5, 0.5, 0)),
                pl.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(0, 1, 0))),
                "Bad plane intersection");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}
     */
    @Test
    void findIntersectionsEP2() {
        // TC02: Ray out of plane
        assertNull(pl.findIntersections(new Ray(new Point(4, 0, 0), new Vector(0, 2, 0))),
                "Must not be plane intersection");
    }
    //endregion

    //region ======================= Boundary Values Tests ==================================================================

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}
     */
    @Test
    void findIntersectionsBVA1() {

        // TC11: Ray parallel to plane
        assertNull(pl.findIntersections(new Ray(new Point(2, 2, 2), new Vector(0, -1, 1))),
                "Must not be plane intersection");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}
     */
    @Test
    void findIntersectionsBVA2() {
        // TC12: Ray in plane
        assertNull(pl.findIntersections(new Ray(new Point(-2,2,1), new Vector(0, 1,-1))),
                "Must not be plane intersection");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}
     */
    @Test
    void findIntersectionsBVA3() {
        // TC13: Orthogonal ray into plane
        assertEquals(List.of(new Point(1d / 3, 1d / 3, 1d / 3)),
                pl.findIntersections(new Ray(new Point(3,3,3), new Vector(-1, -1, -1))),
                "Bad plane intersection");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}
     */
    @Test
    void findIntersectionsBVA4() {
        // TC14: Orthogonal ray out of plane
        assertNull(pl.findIntersections(new Ray(new Point(4,4,4), new Vector(1, 1, 1))),
                "Must not be plane intersection");
    }


    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}
     */
    @Test
    void findIntersectionsBVA5() {
        // TC16: Orthogonal ray from plane
        assertNull(pl.findIntersections(new Ray(new Point(-2,2,1), new Vector(1, 1, 1))),
                "Must not be plane intersection");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}
     */
    @Test
    void findIntersectionsBVA6() {
        // TC17: Ray from plane
        assertNull(pl.findIntersections(new Ray(new Point(-2,2,1), new Vector(1, 1, -1))),
                "Must not be plane intersection");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}
     */
    @Test
    void findIntersectionsBVA8() {
        // TC18: Ray from plane's Q point
        assertNull(pl.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 1, 0))), "Must not be plane intersection");
    }
    //endregion

    Plane pl1= new Plane(new Point(4,0,0),new Vector(1,0,0));
    Intersectable.GeoPoint gp= new Intersectable.GeoPoint((Geometry)pl1, new Point(4,0,1));
    Ray ray1= new Ray(new Point(1,0,1),new Vector(1,0,0));
    /**
     * Test method for {@link geometries.Plane#findGeoIntersectionsHelper(Ray, double)}
     */
    @Test
    void findGeoIntersectionsHelperTest1(){
        assertNull(pl1.findGeoIntersectionsHelper(ray1,1d),"wrong zero intersections");
    }

    /**
     * Test method for {@link geometries.Plane#findGeoIntersectionsHelper(Ray, double)}
     */
    @Test
    void findGeoIntersectionsHelperTest2(){
        List<Intersectable.GeoPoint> res=pl1.findGeoIntersectionsHelper(ray1,8d);
        assertEquals(List.of(gp),res,"wrong one intersections");
    }


}