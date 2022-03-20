package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    Vector direction= new Vector(1,0,0);
    Ray ray1 =new Ray(new Point(0,0,0),direction);

    /**
     * Test method for {@link geometries.Tube#getNormal(Point)}
     */
    @Test
    void testGetNormalEP() {
        Tube tube = new Tube(ray1,1 );
        assertEquals(new Vector(0,0,1),tube.getNormal(new Point(5,0,1)),
                "normal vector returned is incorrect");
    }

    /**
     * Test method for {@link geometries.Tube#getNormal(Point)}
     */
    @Test
    void testGetNormalBVE(){
        Tube tube=new Tube(ray1,1);
        assertThrows(IllegalArgumentException.class,
                ()->tube.getNormal(new Point(2,0,0)),
                "GetNormal() does not work for point on axis ray");

    }

    Tube tube1 = new Tube(new Ray(new Point(1, 0, 0), new Vector(0, 1, 0)), 1d);
    Vector vAxis = new Vector(0, 0, 1);
    Tube tube2 = new Tube(new Ray(new Point(1, 1, 1), vAxis), 1d);
    Ray ray;

    @Test
    void testFindIntersectionEP1() {



        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray's line is outside the tube (0 points)
        ray1 = new Ray(new Point(1, 1, 2), new Vector(1, 1, 0));
        assertNull(tube1.findIntersections(ray1), "ERROR - TC01: Must not be intersections");
    }

    @Test
    void testFindIntersectionEP2() {
        // TC02: Ray's crosses the tube (2 points)
        ray = new Ray(new Point(0, 0, 0), new Vector(12, 6, 6));
        List<Point> result = tube2.findIntersections(ray);
        assertNotNull(result, "ERROR - TC02: must be intersections");
        assertEquals(2, result.size(), "ERROR - TC02: must be 2 intersections");
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(
                List.of(new Point (0.4,0.2,0.2),
                        new Point (2,1,1)), result,
                "ERROR - TC02: Bad intersections");
    }

    @Test
    void testFindIntersectionEP3() {
        // TC03: Ray's starts within tube and crosses the tube (1 point)
        ray = new Ray(new Point(1, 0.5, 0.5), new Vector(12, 6, 6));
        List<Point> result = tube2.findIntersections(ray);
        assertNotNull( result,"ERROR - TC03: must be intersections");
        assertEquals( 1, result.size(), "ERROR - TC03: must be 1 intersections");
        assertEquals(List.of(new Point(2, 1, 1)), result, "ERROR - TC03: Bad intersections");
    }


    // =============== Boundary Values Tests ==================

    // **** Group: Ray's line is parallel to the axis (0 points)

    @Test
    void testFindIntersectionBVA1() {
        // TC11: Ray is inside the tube (0 points)
        ray = new Ray(new Point(0.5, 0.5, 0.5), vAxis);
        assertNull(tube2.findIntersections(ray), "must not be intersections" );

    }
    @Test
    void testFindIntersectionBVA2() {
        // TC12: Ray is outside the tube
        ray = new Ray(new Point(0.5, -5, 0.5), vAxis);
        assertNull( tube2.findIntersections(ray),"must not be intersections");
    }

    @Test
    void testFindIntersectionBVA3(){
        // TC13: Ray is at the tube surface
        ray = new Ray(new Point(2, 1, 0), vAxis);
        assertNull( tube2.findIntersections(ray), "must not be intersections");
    }
    @Test
    void testFindIntersectionBVA4(){
        // TC14: Ray is inside the tube and starts against axis head
        ray = new Ray(new Point(0.5, 0.5, 1), vAxis);
        assertNull( tube2.findIntersections(ray), "must not be intersections");
    }

    @Test
    void testFindIntersectionBVA30() {
        // TC30: Ray starts before and crosses the axis (2 points)
        ray = new Ray(new Point(1, -2, 2), new Vector(0,3 , 0));
        List<Point> result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals( 2, result.size(), "must be 2 intersections");
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(1, 0, 2), new Point(1, 2, 2)), result, "Bad intersections");
    }


    @Test
    void testFindIntersectionBVA31() {
        // TC31: Ray starts at the surface and goes inside and crosses the axis
        ray = new Ray(new Point(1, 0, 4), new Vector(0, 6, 0));
        List<Point> result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals( 1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point(1, 2, 4)), result, "Bad intersections");
    }

    @Test
    void testFindIntersectionBVA32() {
        // TC32: Ray starts inside and the line crosses the axis (1 point)
        ray = new Ray(new Point(1, 0.3, 4), new Vector(0, 6, 0));
        List<Point> result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals( 1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point(1, 2, 4)), result, "Bad intersections");

    }

    @Test
    void testFindIntersectionBVA33() {
        // TC33: Ray starts at the surface and goes outside and the line crosses the axis (0 points)
        ray = new Ray(new Point(0, 1, 3), new Vector(-1, 0, 0));
        List<Point> result = tube2.findIntersections(ray);
        assertNull(result,"Bad intersections");
    }

    @Test
    void testFindIntersectionBVA34() {
        // TC34: Ray starts after and crosses the axis (0 points)
        ray = new Ray(new Point(4,1,4), new Vector(0, 3, 0));
        List<Point> result = tube2.findIntersections(ray);
        assertNull(result,"Bad intersections");
    }

    @Test
    void testFindIntersectionBVA35() {
        // TC35: Ray start at the axis
        ray = new Ray(new Point(1, 1, 3), new Vector(0, 1, 0));
        List<Point> result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals( 1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point(1, 2, 3)), result, "Bad intersections");

    }

    // **** Group: Ray is orthogonal to axis and begins against the axis head

    @Test
    void testFindIntersectionBVA41() {
        // TC41: Ray starts outside and the line is outside (
        ray = new Ray(new Point(0, 2, 1), new Vector(1, 1, 0));
        List<Point> res=tube2.findIntersections(ray);
        assertNull(res, "must not be intersections");
    }
/**












        // TC15: Ray is outside the tube and starts against axis head
        ray = new Ray(new Point3D(0.5, -0.5, 1), vAxis);
        assertNull( tube2.findIntersections(ray), "must not be intersections");

        // TC16: Ray is at the tube surface and starts against axis head
        ray = new Ray(new Point3D(2, 1, 1), vAxis);
        assertNull( tube2.findIntersections(ray), "must not be intersections");

        // TC17: Ray is inside the tube and starts at axis head
        ray = new Ray(new Point3D(1, 1, 1), vAxis);
        assertNull( tube2.findIntersections(ray), "must not be intersections");

        // **** Group: Ray is orthogonal but does not begin against the axis head

        // TC21: Ray starts outside and the line is outside (0 points)
        ray = new Ray(new Point3D(0, 2, 2), new Vector(1, 1, 0));
        assertNull( tube2.findIntersections(ray), "must not be intersections");

        // TC22: The line is tangent and the ray starts before the tube (0 points)
        ray = new Ray(new Point3D(0, 2, 2), new Vector(1, 0, 0));
        assertNull( tube2.findIntersections(ray), "must not be intersections");

        // TC23: The line is tangent and the ray starts at the tube (0 points)
        ray = new Ray(new Point3D(1, 2, 2), new Vector(1, 0, 0));
        assertNull( tube2.findIntersections(ray), "must not be intersections");

        // TC24: The line is tangent and the ray starts after the tube (0 points)
        ray = new Ray(new Point3D(2, 2, 2), new Vector(1, 0, 0));
        assertNull( tube2.findIntersections(ray), "must not be intersections");

        // TC25: Ray starts before (2 points)
        ray = new Ray(new Point3D(0, 0, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals(2, result.size(), "must be 2 intersections");
        if (result.get(0).getYDouble() > result.get(1).getYDouble())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point3D(0.4, 0.2, 2), new Point3D(2, 1, 2)), result, "Bad intersections");

        // TC26: Ray starts at the surface and goes inside (1 point)
        ray = new Ray(new Point3D(0.4, 0.2, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals(1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(2, 1, 2)), result, "Bad intersections");

        // TC27: Ray starts inside (1 point)
        ray = new Ray(new Point3D(1, 0.5, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals( 1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(2, 1, 2)), result, "Bad intersections");

        // TC28: Ray starts at the surface and goes outside (0 points)
        ray = new Ray(new Point3D(2, 1, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");

        // TC29: Ray starts after
        ray = new Ray(new Point3D(4, 2, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");
















        // TC42: The line is tangent and the ray starts before the tube
        ray = new Ray(new Point3D(0, 2, 1), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), "must not be intersections");

        // TC43: The line is tangent and the ray starts at the tube
        ray = new Ray(new Point3D(1, 2, 1), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), "must not be intersections");

        // TC44: The line is tangent and the ray starts after the tube
        ray = new Ray(new Point3D(2, 2, 2), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), "must not be intersections");

        // TC45: Ray starts before
        ray = new Ray(new Point3D(0, 0, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals(2, result.size(), "must be 2 intersections");
        if (result.get(0).getYDouble() > result.get(1).getYDouble())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point3D(0.4, 0.2, 1), new Point3D(2, 1, 1)), result, "Bad intersections");

        // TC46: Ray starts at the surface and goes inside
        ray = new Ray(new Point3D(0.4, 0.2, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals( 1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(2, 1, 1)), result, "Bad intersections");

        // TC47: Ray starts inside
        ray = new Ray(new Point3D(1, 0.5, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals( 1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(2, 1, 1)), result, "Bad intersections");

        // TC48: Ray starts at the surface and goes outside
        ray = new Ray(new Point3D(2, 1, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");

        // TC49: Ray starts after
        ray = new Ray(new Point3D(4, 2, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");

        // TC50: Ray starts before and goes through the axis head
        ray = new Ray(new Point3D(1, -1, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result,"must be intersections");
        assertEquals(2, result.size(), "must be 2 intersections");
        if (result.get(0).getYDouble() > result.get(1).getYDouble())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point3D(1, 0, 1), new Point3D(1, 2, 1)), result, "Bad intersections");

        // TC51: Ray starts at the surface and goes inside and goes through the axis head
        ray = new Ray(new Point3D(1, 0, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull( result, "must be intersections");
        assertEquals( 1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(1, 2, 1)), result, "Bad intersections");

        // TC52: Ray starts inside and the line goes through the axis head
        ray = new Ray(new Point3D(1, 0.5, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull( result, "must be intersections");
        assertEquals( 1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(1, 2, 1)), result, "Bad intersections");

        // TC53: Ray starts at the surface and the line goes outside and goes through the axis head
        ray = new Ray(new Point3D(1, 2, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");

        // TC54: Ray starts after and the line goes through the axis head
        ray = new Ray(new Point3D(1, 3, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");

        // TC55: Ray start at the axis head
        ray = new Ray(new Point3D(1, 1, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals( 1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(1, 2, 1)), result, "Bad intersections");

        // **** Group: Ray's line is neither parallel nor orthogonal to the axis and begins against axis head

        Point3D p0 = new Point3D(0, 2, 1);

        // TC61: Ray's line is outside the tube
        ray = new Ray(p0, new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");

        // TC62: Ray's line crosses the tube and begins before
        ray = new Ray(p0, new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals(2, result.size(), "must be 2 intersections");
        if (result.get(0).getYDouble() > result.get(1).getYDouble())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point3D(2, 1, 2), new Point3D(0.4, 1.8, 1.2)), result, "Bad intersections");

        // TC63: Ray's line crosses the tube and begins at surface and goes inside
        ray = new Ray(new Point3D(0.4, 1.8, 1), new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull( result, "must be intersections");
        assertEquals( 1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(2, 1, 1.8)), result, "Bad intersections");

        // TC64: Ray's line crosses the tube and begins inside
        ray = new Ray(new Point3D(1, 1.5, 1), new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals(1, result.size(),"must be 1 intersections");
        assertEquals(List.of(new Point3D(2, 1, 1.5)), result, "Bad intersections");

        // TC65: Ray's line crosses the tube and begins at the axis head
        ray = new Ray(new Point3D(1, 1, 1), new Vector(0, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals(1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(1, 2, 2)), result, "Bad intersections");

        // TC66: Ray's line crosses the tube and begins at surface and goes outside
        ray = new Ray(new Point3D(2, 1, 1), new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");

        // TC67: Ray's line is tangent and begins before
        ray = new Ray(p0, new Vector(0, 2, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");

        // TC68: Ray's line is tangent and begins at the tube surface
        ray = new Ray(new Point3D(1, 2, 1), new Vector(1, 0, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");

        // TC69: Ray's line is tangent and begins after
        ray = new Ray(new Point3D(2, 2, 1), new Vector(1, 0, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");

        // **** Group: Ray's line is neither parallel nor orthogonal to the axis and does not begin against axis head

        double sqrt2 = Math.sqrt(2);
        double denomSqrt2 = 1 / sqrt2;
        double value1 = 1 - denomSqrt2;
        double value2 = 1 + denomSqrt2;



        // TC72: Ray's crosses the tube and the axis head
        ray = new Ray(new Point3D(0, 0, 0), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals(2, result.size(), "must be 2 intersections");
        if (result.get(0).getYDouble() > result.get(1).getYDouble())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point3D(value1, value1, value1), new Point3D(value2, value2, value2)),
                result, "Bad intersections");

        // TC73: Ray's begins at the surface and goes inside
        // TC74: Ray's begins at the surface and goes inside crossing the axis
        ray = new Ray(new Point3D(value1, value1, 2 + value1), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals(1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(value2, value2, 2 + value2)), result, "Bad intersections");

        // TC75: Ray's begins at the surface and goes inside crossing the axis head
        ray = new Ray(new Point3D(value1, value1, value1), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result,"must be intersections");
        assertEquals(1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(value2, value2, value2)), result, "Bad intersections");

        // TC76: Ray's begins inside and the line crosses the axis
        ray = new Ray(new Point3D(0.5, 0.5, 2.5), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals(1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(value2, value2, 2 + value2)), result, "Bad intersections");

        // TC77: Ray's begins inside and the line crosses the axis head
        ray = new Ray(new Point3D(0.5, 0.5, 0.5), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals(1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(value2, value2, value2)), result, "Bad intersections");

        // TC78: Ray's begins at the axis
        ray = new Ray(new Point3D(1, 1, 3), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals(1, result.size(), "must be 1 intersections");
        assertEquals(List.of(new Point3D(value2, value2, 2 + value2)), result, "Bad intersections");

        // TC79: Ray's begins at the surface and goes outside
        ray = new Ray(new Point3D(2, 1, 2), new Vector(2, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");

        // TC80: Ray's begins at the surface and goes outside and the line crosses the axis
        ray = new Ray(new Point3D(value2, value2, 2 + value2), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");

        // TC81: Ray's begins at the surface and goes outside and the line crosses the axis head
        ray = new Ray(new Point3D(value2, value2, value2), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, "Bad intersections");
    }
**/

// **** Group: Ray's line is neither parallel nor orthogonal to the axis and does not begin against axis head

    double sqrt2 = Math.sqrt(2);
    double denomSqrt2 = 1 / sqrt2;
    double value1 = 1 - denomSqrt2;
    double value2 = 1 + denomSqrt2;

    @Test
    void testFindIntersectionBVA71() {
        // TC71: Ray's crosses the tube and the axis
        ray = new Ray(new Point(0, 0, 2), new Vector(1, 1, 1));
        List<Point> result = tube2.findIntersections(ray);
        assertNotNull(result, "must be intersections");
        assertEquals(2, result.size(), "must be 2 intersections");
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(value1, value1, 2 + value1), new Point(value2, value2, 2 + value2)),
                result, "Bad intersections");
    }

    @Test
    void testFindIntersectionBVA72() {

    }
}