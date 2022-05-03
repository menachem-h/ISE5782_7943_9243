package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
        * Testing Polygons
        *
        * @author Dan
        *
        */
class PolygonTest {

    /**
     * Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        try {
            new Polygon(
                    new Point(0, 0, 1),
                    new Point(1, 0, 0),
                    new Point(0, 1, 0),
                    new Point(-1, 1, 1));
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a correct polygon");
        }

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(0, 1, 0),
                        new Point(1, 0, 0),
                        new Point(-1, 1, 1)), //
                "Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0, 2, 2)), //
                "Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)), //
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertix on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0, 0, 1)),
                "Constructed a polygon with vertice on a side");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0, 1, 0)),
                "Constructed a polygon with vertice on a side");

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Polygon pl = new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals(new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point(0, 0, 1)), "Bad normal to trinagle");
    }

    Polygon square = new Polygon(new Point(1, 0, 2), new Point(4, 0, 2), new Point(4, 3, 2),
            new Point(1, 3, 2));

    Plane pl = new Plane(new Point(0, 0, 2), new Point(1, 0, 2), new Point(0, 1, 2));
    Ray ray;

    // ============ Equivalence Partitions Tests ==============
    @Test
    void testFindIntersectionsEP1() {
        // TC01: Inside polygon
        ray = new Ray(new Point(2, 1, 0), new Vector(0, 0, 1));
        var d= square.findIntersections(ray);
        assertEquals(List.of(new Point(2, 1, 2)), square.findIntersections(ray), "wrong intersection point");
    }

    @Test
    void testFindIntersectionsEP2() {
        // TC02: Against edge
        ray = new Ray(new Point(5, 1, 0), new Vector(0, 0, 1));
        assertEquals(List.of(new Point(5, 1, 2)), pl.findIntersections(ray), "wrong intersection point");
        assertNull(square.findIntersections(ray), "no intersections");
    }

    @Test
    void testFindIntersectionsEP3() {
        // TC03: Against vertex
        ray = new Ray(new Point(5, 4, 0), new Vector(0, 0, 1));
        assertEquals(List.of(new Point(5, 4, 2)), pl.findIntersections(ray), "wrong intersection point");
        assertNull(square.findIntersections(ray), "no intersections");

    }


    // =============== Boundary Values Tests ==================
    @Test
    void testFindIntersectionsBVA1() {
        // TC04: In vertex
        ray = new Ray(new Point(4, 3, 0), new Vector(0, 0, 1));
        assertEquals(List.of(new Point(4, 3, 2)), pl.findIntersections(ray), "wrong intersection point");
        assertNull(square.findIntersections(ray), "no intersections");

    }

    @Test
    void testFindIntersectionsBVA2() {
        // TC05: On edge
        ray = new Ray(new Point(4, 1, 0), new Vector(0, 0, 1));
        assertEquals(List.of(new Point(4, 1, 2)), pl.findIntersections(ray), "wrong intersection point");
        assertNull(square.findIntersections(ray), "no intersections");
    }


    @Test
    void testFindIntersectionsBVA3() {
        // TC06: On edge continuation
        ray = new Ray(new Point(5, 0, 0), new Vector(0, 0, 1));
        assertEquals(List.of(new Point(5, 0, 2)), pl.findIntersections(ray), "wrong intersection point");
        assertNull(square.findIntersections(ray), "no intersections");
    }

    //region ***test for intersection points with consideration to maxDistance parameter
    Polygon square1 = new Polygon(
            new Point(-1,0,0),
            new Point(-1,0,1),
            new Point(1,0,1),
            new Point(1,0,0));
    Ray ray1 = new Ray(new Point(0,-3,0.5),new Vector(0,1,0));
    Intersectable.GeoPoint gp1 = new Intersectable.GeoPoint(square1 , new Point(0, 0, 0.5));

    /**
     * Test method for {@link Triangle#findGeoIntersectionsHelper(Ray, double)}
     */
    @Test
    void findGeoIntersectionsEP1() {
        // TC01 -  max distance is smaller than distance to intersection point - no intersections
        assertNull(square1.findGeoIntersectionsHelper(ray1,2),"points are further than maxDistance");

    }

    /**
     * Test method for {@link Triangle#findGeoIntersectionsHelper(Ray, double)}
     */
    @Test
    void findGeoIntersectionsEP2() {
        //TC02 -  max distance is larger than distance to  intersection point - one intersection point
        List<Intersectable.GeoPoint> res = square1.findGeoIntersectionsHelper(ray1,5);
        assertEquals(List.of(gp1),res,"point is in boundary");

    }
    //endregion
}