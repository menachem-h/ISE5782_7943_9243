package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.*;

class PointTest {
    Point p1 = new Point(1, 2, 3);

    /**
     * testing method {@link primitives.Point#distanceSquared(Point)}
     */

    @Test
    void testDistanceSquared() {
        assertTrue(isZero(p1.distanceSquared(new Point(3,5,6))-22),
                "Distance Squared does not work correctly");

    }

    /**
     * testing method {@link Point#distance(Point)}
     */

    @Test
    void testDistance() {
        assertTrue(isZero(new Point(5,5,3).distance(p1)-5) ,
                "Distance does not work correctly");
    }

    /**
     * testing method {@link Point#add(Vector)}
     */
    @Test
    void testAdd() {
        assertEquals(p1.add(new Vector(-1,-2,-3)),
                new Point(0,0,0),"Point + Vector does not work correctly");
    }

    /**
     * testing method {@link  primitives.Point#subtract(Point)}
     */
    @Test
    void testSubtractEP() {
        assertEquals(new Point(2,3,4).subtract(new Point(1,2,3)),
                new Vector(1,1,1),
                "Point - Point does not work correctly");
    }

    /**
     * testing method {@link primitives.Point#subtract(Point)}
     */
    @Test
    void testSubtractBVE() {
        assertThrows(IllegalArgumentException.class,
                ()->new Point(1,2,3).subtract(new Point(1,2,3)),
                "Point- same Point must throw exception" );
    }
}
