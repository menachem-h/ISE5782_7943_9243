package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    Vector direction= new Vector(1,0,0);
    Ray ray=new Ray(new Point(0,0,0),direction);
    Cylinder _cylinder= new Cylinder(ray,1,4);
    /**
     *
     */
    @Test
    void testGetNormalEP1() {
        assertEquals(new Vector(0,0,1),_cylinder.getNormal(new Point(3,0,1)),
                "");

    }

    @Test
    void testGetNormalEP2() {
        assertEquals(direction.normalize(),_cylinder.getNormal(new Point(0,0.5,0)),
                "gdfsgdfgdf");
    }

    @Test
    void testGetNormalEP3() {
        assertEquals(direction.normalize(),_cylinder.getNormal(new Point(4,0.5,0)),
                "gdfsgdfgdf");
    }

    @Test
    void testGetNormalBVE1(){
        assertEquals(direction.normalize(),_cylinder.getNormal(new Point(4,0,0)),
                "gdfsgdfgdf");
    }

    @Test
    void testGetNormalBVE2(){
        assertEquals(direction.normalize(),_cylinder.getNormal(new Point(0,0,0)),
                "gdfsgdfgdf");
    }

}