package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    Vector direction= new Vector(1,0,0);
    Ray ray=new Ray(new Point(0,0,0),direction);
    Cylinder _cylinder= new Cylinder(ray,1,4);

    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormalEP1() {
        assertEquals(new Vector(0,0,1),_cylinder.getNormal(new Point(3,0,1)),
                "returned normal vector is incorrect");

    }

    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormalEP2() {
        assertEquals(direction.normalize(),_cylinder.getNormal(new Point(0,0.5,0)),
                "returned normal vector is incorrect");
    }

    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormalEP3() {
        assertEquals(direction.normalize(),_cylinder.getNormal(new Point(4,0.5,0)),
                "returned normal vector is incorrect");
    }

    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormalBVE1(){
        assertEquals(direction.normalize(),_cylinder.getNormal(new Point(4,0,0)),
                "returned normal vector is incorrect");
    }

    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormalBVE2(){
        assertEquals(direction.normalize(),_cylinder.getNormal(new Point(0,0,0)),
                "returned normal vector is incorrect");
    }


    // ============ Equivalence Partitions Tests ==============



    Cylinder cylinder = new Cylinder(new Ray(new Point(2,0,0), new Vector(0,0,1)), 1d,2d);
    List<Point> result=null;
    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestEP1() {
      //TC01 ray is outside and parallel to the cylinder's ray

        result = cylinder.findIntersections(new Ray(new Point(5, 0, 0), new Vector(0, 0, 1)));
        assertNull(result, "Wrong number of points");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestEP2() {
        //TC02 ray starts inside and parallel to the cylinder's ray

        result = cylinder.findIntersections(new Ray(new Point(2.5, 0, 1), new Vector(0, 0, 1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(2.5, 0, 2)), result, "Bad intersection point");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestEP3() {
        //TC03 ray starts outside and parallel to the cylinder's ray and crosses the cylinder

        result = cylinder.findIntersections(new Ray(new Point(2.5, 0, -1), new Vector(0, 0, 1)));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(2.5, 0, 0), new Point(2.5, 0, 2)), result, "Bad intersection point");
    }
    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestEP4() {
        //TC04 ray starts from outside and crosses the cylinder

        result = cylinder.findIntersections(new Ray(new Point(-2, 0, 0.5), new Vector(1, 0, 0)));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 0.5), new Point(3, 0, 0.5)), result, "Bad intersection points");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestEP5() {
        //TC05 ray starts from inside and crosses the cylinder

        result = cylinder.findIntersections(new Ray(new Point(1.5, 0, 0.5), new Vector(1, 0, 0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(3, 0, 0.5)), result, "Bad intersection points");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestEP6() {
        //TC06 ray starts from outside the cylinder and doesn't cross the cylinder

        result = cylinder.findIntersections(new Ray(new Point(5, 0, 0), new Vector(1, 0, 0)));
        assertNull(result, "Wrong number of points");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestEP7() {
        //TC07 ray starts from outside and crosses base and surface

        result = cylinder.findIntersections(new Ray(new Point(1, 0, -1), new Vector(1, 0, 1)));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(2, 0, 0), new Point(3, 0, 1)), result, "Bad intersection points");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestEP8() {
        //TC08 ray starts from outside and crosses surface and base

        result = cylinder.findIntersections(new Ray(new Point(4, 0, 2), new Vector(-1, 0, -1)));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(2, 0, 0), new Point(3, 0, 1)), result, "Bad intersection points");

    }
        // =============== Boundary Values Tests ==================

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA1() {
        //TC09 ray is on the surface of the cylinder (not bases)

        result = cylinder.findIntersections(new Ray(new Point(3, 0, 0), new Vector(0, 0, 1)));
        assertNull(result, "Wrong number of points");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA2() {
        //TC10 ray is on the base of the cylinder and crosses 2 times

        result = cylinder.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, 0, 0)));
        assertNull(result, "Wrong number of points");
    }
    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA3() {
        //TC11 ray is in center of the cylinder

        result = cylinder.findIntersections(new Ray(new Point(2, 0, 0), new Vector(0, 0, 1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(2, 0, 2)), result, "Bad intersection points");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA4() {
        //TC12 ray is perpendicular to cylinder's ray and starts from outside the tube

        result = cylinder.findIntersections(new Ray(new Point(-2, 0, 0.5), new Vector(1, 0, 0)));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 0.5), new Point(3, 0, 0.5)), result, "Bad intersection points");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA5() {
        //TC13 ray is perpendicular to cylinder's ray and starts from inside cylinder (not center)

        result = cylinder.findIntersections(new Ray(new Point(1.5, 0, 0.5), new Vector(1, 0, 0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(3, 0, 0.5)), result, "Bad intersection points");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA6() {
        //TC14 ray is perpendicular to cylinder's ray and starts from the center of cylinder

        result = cylinder.findIntersections(new Ray(new Point(2, 0, 0.5), new Vector(1, 0, 0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(3, 0, 0.5)), result, "Bad intersection points");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA7() {
        //TC15 ray is perpendicular to cylinder's ray and starts from the surface of cylinder to inside

        result = cylinder.findIntersections(new Ray(new Point(1, 0, 0.5), new Vector(1, 0, 0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(3, 0, 0.5)), result, "Bad intersection points");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA8() {
        //TC16 ray is perpendicular to cylinder's ray and starts from the surface of cylinder to outside

        result = cylinder.findIntersections(new Ray(new Point(3, 0, 0), new Vector(1, 0, 0)));
        assertNull(result, "Wrong number of points");
    }
    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA9() {

        //TC17 ray starts from the surface to outside

        result = cylinder.findIntersections(new Ray(new Point(3,0,0), new Vector(1,1,1)));
        assertNull(result, "Wrong number of points");

    }
    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA10() {
        //TC18 ray starts from the surface to inside

        result = cylinder.findIntersections(new Ray(new Point(3,0,0.5), new Vector(-1,0,0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1,0,0.5)), result, "Bad intersection point");

    }
    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA11() {
        //TC19 ray starts from the center

        result = cylinder.findIntersections(new Ray(new Point(2,0,0), new Vector(1,0,1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(3,0,1)), result, "Bad intersection point");

    }
    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA12() {
        //TC20 prolongation of ray crosses cylinder

        result = cylinder.findIntersections(new Ray(new Point(3,0,0), new Vector(1,0,0)));
        assertNull(result, "Wrong number of points");

    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA13() {
        //TC21 ray is on the surface starts before cylinder

        result = cylinder.findIntersections(new Ray(new Point(3,0,-1), new Vector(0,0,1)));
        assertNull(result, "Wrong number of points");

    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA14() {

        //TC22 ray is on the surface starts at bottom's base

        result = cylinder.findIntersections(new Ray(new Point(3,0,0), new Vector(0,0,1)));
        assertNull(result, "Wrong number of points");

    }
    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA15() {
        //TC23 ray is on the surface starts on the surface

        result = cylinder.findIntersections(new Ray(new Point(3,0,1), new Vector(0,0,1)));
        assertNull(result, "Wrong number of points");
    }
    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTestBVA16() {
        //TC24 ray is on the surface starts at top's base

        result = cylinder.findIntersections(new Ray(new Point(3,0,2), new Vector(0,0,1)));
        assertNull(result, "Wrong number of points");
    }

}