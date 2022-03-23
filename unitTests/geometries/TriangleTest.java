package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    Triangle tri = new Triangle(
            new Point(1,0,0),
            new Point(3,0,0),
            new Point(2,0,1));
    /**
     * Test method for {@link geometries.Triangle#getNormal(Point)}
     */
    @Test
    void testGetNormal()
    {
        assertEquals(new Vector(0,-1,0), tri.getNormal(new Point(1, 0, 0.5)), "Bad normal to triangle");
    }

    //region ==================== Equivalence Partitions Tests =====================================================
    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsEP1() {
        // TC01 ray crosses triangle
        List<Point> result = tri.findIntersections(new Ray(new Point(2,-1,0.5),new Vector(0,1,0)));
        assertEquals(1,result.size(),"ray crosses triangle once");
        assertEquals(result,List.of(new Point(2,0,0.5)),"ray crosses triangle");
    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsEP2() {
        //TC02 -  ray does not cross triangle - against vertex
        List<Point> result = tri.findIntersections(new Ray(new Point(2,-1,1.5),new Vector(0,1,0)));
        assertNull(result,"ray does not cross triangle");
    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsEP3() {
        // TC03 -  ray does not cross triangle - against edge
        List<Point> result = tri.findIntersections(new Ray(new Point(3,-1,0.5),new Vector(0,1,0)));
        assertNull(result,"ray does not cross triangle");
    }
    //endregion

    //region =============== Boundary Values Tests =========================================================
    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA1() {
        // TC04 ray intersects triangle at a vertex - 0 points
        List<Point> result = tri.findIntersections(new Ray(new Point(1,-1,0),new Vector(0,1,0)));
        assertNull(result,"ray does not cross triangle");
    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA2() {
        // TC05 ray intersects triangle at one of the triangle edges - 0 points
        List<Point> result = tri.findIntersections(new Ray(new Point(2.5,-1,0.5),new Vector(0,1,0)));
        assertNull(result,"ray does not cross triangle");
    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}
     */
    @Test
    void testFindIntersectionsBVA3() {
        // TC03 -  ray does not cross triangle  run on "continuation" of one of edges
        List<Point> result = tri.findIntersections(new Ray(new Point(1,-1,2),new Vector(0,1,0)));
        assertNull(result,"ray does not cross triangle");
    }
    //endregion
}