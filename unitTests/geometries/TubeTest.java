package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    Vector direction= new Vector(1,0,0);
    Ray ray=new Ray(new Point(0,0,0),direction);

    /**
     * Test method for {@link geometries.Tube#getNormal(Point)}
     */
    @Test
    void testGetNormalEP() {
        Tube tube = new Tube(ray,1 );
        assertEquals(new Vector(0,0,1),tube.getNormal(new Point(5,0,1)),
                "normal vector returned is incorrect");
    }

    /**
     * Test method for {@link geometries.Tube#getNormal(Point)}
     */
    @Test
    void testGetNormalBVE(){
        Tube tube=new Tube(ray,1);
        assertThrows(IllegalArgumentException.class,
                ()->tube.getNormal(new Point(2,0,0)),
                "GetNormal() does not work for point on axis ray");

    }
}