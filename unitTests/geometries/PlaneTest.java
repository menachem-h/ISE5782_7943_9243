package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

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
        /// first and second point are equal
        assertThrows(IllegalArgumentException.class,
                ()->new Plane(p1,p1,p4),
                "constructed a plane with points that are equal");

        /// first and third point are equal
        assertThrows(IllegalArgumentException.class,
                ()->new Plane(p1,p4,p1),
                "constructed a plane with points that are equal");

        /// first and third point are equal
        assertThrows(IllegalArgumentException.class,
                ()->new Plane(p4,p1,p1),
                "constructed a plane with points that are equal");

        /// all points are equal
        assertThrows(IllegalArgumentException.class,
                ()->new Plane(p1,p1,p1),
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




}