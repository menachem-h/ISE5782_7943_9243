package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.*;

class VectorTest {

    Vector v1 = new Vector(1, 2, 3);
    Vector v2 = new Vector(-2, -4, -6);
    Vector v3 = new Vector(0, 3, -2);

    /**
     * testing method {@link Vector}
     */
    @Test
    void testZeroVector(){
        assertThrows(IllegalArgumentException.class,
                ()->new Vector(0,0,0),
                "should have throw Exception");
    }

    /**
     * testing method {@link Vector#lengthSquared()}
     */
    @Test
    void testLengthSquared() {
        assertTrue(isZero(new Vector(1,2,3).lengthSquared()-14),
                "Length Squared does not work correctly");
    }

    /**
     * testing method {@link Vector#length()}
     */
    @Test
    void testLength() {
        assertTrue(isZero(new Vector(3,4,0).length()-5),
                "Length does not work correctly" );
    }

    /**
     * testing method {@link Vector#add(Vector)}
     */
    @Test
    void testAddEP() {
        // ============ Equivalence Partitions Tests ==============
            assertEquals(new Vector(5,7,9),
                    new Vector(1,2,3).add(new Vector(4,5,6) ),
                   "add vector does not work correctly" );
    }

    /**
     *
     */
    @Test
    void testAddBVE(){
        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class,
                ()->new Vector(1,2,3).add(new Vector(-1,-2,-3)),
              "v+(-v)" );

    }

    /**
     * testing method {@link Vector#subtract(Vector)}
     */
    @Test
    void testSubtractEP(){
        assertEquals(new Vector(4,5,6).subtract(new Vector(1,2,3)),
                new Vector(3,3,3),
                "subtract vector");
    }

    /**
     * testing method {@link Vector#subtract(Vector)}
     */
    @Test
    void testSubtractBVE(){
        assertThrows(IllegalArgumentException.class,
                ()->new Vector(1,2,3).subtract(new Vector(1,2,3)),
                "v-v must throw exception" );

    }
    /**
     * testing method {@link Vector#scale(double)}
     */
    @Test
    void testScaleEP() {
        // ============ Equivalence Partitions Tests ==============
        assertEquals(new Vector(2,4,6),v1.scale(2),
                "Scale vector does not work correctly");
    }

    @Test
    void testScaleBVA(){
        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class,
                ()->v1.scale(0d),
                "scale 0 throw");
    }

    /**
     * testing method {@link Vector#dotProduct(Vector)}
     */
    @Test
    void testDotProductEP() {
        // ============ Equivalence Partitions Tests ==============

        //assertEquals(-28d,v1.dotProduct(v2),0.0000001,"dotProduct");
        assertTrue(isZero(v1.dotProduct(v2)+28),"dotProduct");
    }

    /**
     *
     */
    @Test
    void testDotProductBVE(){
        // =============== Boundary Values Tests ==================

        assertTrue(isZero(v1.dotProduct(v3)+v3.dotProduct(v1)),
                "dotProduct() for orthogonal vectors is not zero");
    }

    /**
     * testing methode {@link Vector#crossProduct(Vector)}
     */
    @Test
    void testCrossProductEP() {
        // ============ Equivalence Partitions Tests ==============
        Vector vn=v1.crossProduct(v3);
        assertTrue(isZero(v1.length()* v3.length()-vn.length()),
                "crossProduct() wrong result length");
        assertTrue(isZero(vn.dotProduct(v1)),"crossProduct() orthogonal vector");
        assertTrue(isZero(vn.dotProduct(v3)),"crossProduct() orthogonal vector");

    }

    /**
     *  testing methode {@link Vector#crossProduct(Vector)}
     */
    @Test
    void testCrossProductBVE(){
        // =============== Boundary Values Tests ==================
       assertThrows(IllegalArgumentException.class,
               ()->v1.crossProduct(v2),
               "crossProduct() for parallels vector " );
    }

    /**
     * testing method {@link Vector#normalize()}
     */
    @Test
    void testNormalizeEP1() {

        // ============ Equivalence Partitions Tests ==============
        assertTrue(isZero(new Vector(1,2,3).normalize().length()-1),
                "Normalize vector not unit vector");
    }

    /**
     * testing method {@link Vector#normalize()}
     */
    @Test
    void testNormalizeEP2(){
        // ============ Equivalence Partitions Tests ==============
        assertEquals(new Vector(7,24,0).normalize(),
                new Vector(0.28,0.96,0),
                "Normalize vector does not work correctly");
    }

}