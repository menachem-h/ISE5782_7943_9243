package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    Sphere sphere = new Sphere( new Point (2, 0, 0),2d);
    @Test
    void findIntersectionsEP1() {


        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(0, 0, 4), new Vector(0, 1, 0))),
                "Ray's line out of sphere");
    }

    @Test
    void findIntersectionsEP2() {

        // TC02: Ray starts before and crosses the sphere (2 points)
        Point p1 = new Point(1.203117852267963, 0, 1.834387865917667);
        Point p2 = new Point(3.136504789241472, 0, -1.645708620634649);
        List<Point> result = sphere.findIntersections(new Ray(new Point(0, 0, 4),
                new Vector(5,0,-9)));
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "Ray crosses sphere");
    }

        // TC03: Ray starts inside the sphere (1 point)
        //...
        // TC04: Ray starts after the sphere (0 points)
        //...

        // =============== Boundary Values Tests ==================

        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
        // TC12: Ray starts at sphere and goes outside (0 points)

        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        // TC14: Ray starts at sphere and goes inside (1 points)
        // TC15: Ray starts inside (1 points)
        // TC16: Ray starts at the center (1 points)
        // TC17: Ray starts at sphere and goes outside (0 points)
        // TC18: Ray starts after sphere (0 points)

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        // TC20: Ray starts at the tangent point
        // TC21: Ray starts after the tangent point

        // **** Group: Special cases
        // TC19: Ray's line is outside, ray is orthogonal to ray start to sphere's center line



    }
