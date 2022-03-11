package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

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

    @Test
    void findIntersections() {
    }
}