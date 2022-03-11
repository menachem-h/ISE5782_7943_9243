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
    Point p4 = new Point(2,4,6);
    Point p5 = new Point(4,8,12);

    Plane plane = new Plane(p1, p2, p3);
    Plane pl = new Plane(new Point(0, 0, 1), new Vector(1, 1, 1));


    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testConstructorBVA01()
    {
        /// all points are on same line
        assertThrows(IllegalArgumentException.class,
                ()->new Plane(p1,p5,p4),
                "constructed a plane with parallel vertices");
    }

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testConstructorBVA02()
    {
        /// two point are equal
        assertThrows(IllegalArgumentException.class,
                ()->new Plane(p1,p1,p4),
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


    @Test
    void findIntersections() {
        findIntersections1();
        findIntersections2();
        findIntersections3();

    }
    @Test
    void findIntersections1() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray into plane
        assertEquals(
                List.of(new Point(0, 1 , 0)),
                pl.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(0, 1, 0))),
                "Bad plane intersection");
    }
    @Test
    void findIntersections2() {
        // TC02: Ray out of plane
        assertNull(pl.findIntersections(new Ray(new Point(2, 0, 0), new Vector(1, 0, 0))),
                "Must not be plane intersection");


    }
    @Test
    void findIntersections3() {
        // =============== Boundary Values Tests ==================
        // TC11: Ray parallel to plane
        assertNull(pl.findIntersections(new Ray(new Point(1, 1, 1), new Vector(0, 1, -1))),
                "Must not be plane intersection");

        // TC12: Ray in plane
        assertNull(pl.findIntersections(new Ray(new Point(0, 0.5, .5), new Vector(0, 1, -1))),
                "Must not be plane intersection");


        // TC13: Orthogonal ray into plane
        assertEquals(List.of(new Point(1d / 3, 1d / 3, 1d / 3)),
                pl.findIntersections(new Ray(new Point(1, 1, 1), new Vector(-1, -1, -1))),
                "Bad plane intersection");

        // TC14: Orthogonal ray out of plane
        assertNull(pl.findIntersections(new Ray(new Point(1, 1, 1), new Vector(1, 1, 1))),
                "Must not be plane intersection");

        // TC15: Orthogonal ray out of plane
        assertNull(pl.findIntersections(new Ray(new Point(1, 1, 1), new Vector(1, 1, 1))),
                "Must not be plane intersection");

        // TC16: Orthogonal ray from plane
        assertNull(pl.findIntersections(new Ray(new Point(0, 0.5, 0.5), new Vector(1, 1, 1))),
                "Must not be plane intersection");

        // TC17: Ray from plane
        assertNull(pl.findIntersections(new Ray(new Point(0, 0.5, 0.5), new Vector(1, 1, 0))),
                "Must not be plane intersection");

        // TC18: Ray from plane's Q point
        assertThrows(
                IllegalArgumentException.class,
                ()-> pl.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 1, 0))),
                "Must not be plane intersection");
    }
}