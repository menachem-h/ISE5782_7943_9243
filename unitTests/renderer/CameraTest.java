package renderer;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test {@link Camera} class functionalities
 */
class CameraTest {
    static final Point ZERO_POINT = new Point(0, 0, 0);

    String badRay = "Bad ray";
    Camera camera1 = new Camera.CameraBuilder(ZERO_POINT, new Vector(0, 0, -1), new Vector(0, -1, 0))
            .setVPDistance(10).setVPSize(8,8)
            .build();

    Camera camera2 = new Camera.CameraBuilder(ZERO_POINT, new Vector(0, 0, -1), new Vector(0, -1, 0))
            .setVPDistance(10).setVPSize(6,6)
            .build();
    // ============ Equivalence Partitions Tests ==============

    /**
     * Test method for
     * {@link Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testConstructRayEP() {
        // EP01: 4X4 Inside (1,1)

        assertEquals(new Ray(ZERO_POINT, new Vector(1, -1, -10)),
                camera1.constructRay(4, 4, 1, 1), badRay);
    }
    // =============== Boundary Values Tests ==================

    /**
     * Test method for
     * {@link Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testConstructRayBVA1() {
        // BV01: 3X3 Center (1,1)

        assertEquals(new Ray(ZERO_POINT, new Vector(0, 0, -10)),
                camera2.constructRay(3, 3, 1, 1), badRay);
    }

    /**
     * Test method for
     * {@link Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testConstructRayBVA2() {
        // BV02: 3X3 Center of Upper Side (0,1)

        assertEquals(new Ray(ZERO_POINT, new Vector(0, -2, -10)),
                camera2.constructRay(3, 3, 1, 0), badRay);
    }

    /**
     * Test method for
     * {@link Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testConstructRayBVA3() {
        // BV03: 3X3 Center of Left Side (1,0)
        assertEquals(new Ray(ZERO_POINT, new Vector(2, 0, -10)),
                camera2.constructRay(3, 3, 0, 1), badRay);
    }

    /**
     * Test method for
     * {@link Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testConstructRayBVA4() {

        // BV04: 3X3 Corner (0,0)
        assertEquals(new Ray(ZERO_POINT, new Vector(2, -2, -10)),
                camera2.constructRay(3, 3, 0, 0), badRay);
    }

    /**
     * Test method for
     * {@link Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testConstructRayBVA5() {
        // BV05: 4X4 Corner (0,0)
        assertEquals(new Ray(ZERO_POINT, new Vector(3, -3, -10)),
                camera1.constructRay(4, 4, 0, 0), badRay);
    }

    /**
     * Test method for
     * {@link Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testConstructRayBVA6() {
        // BV06: 4X4 Side (0,1)
        assertEquals(new Ray(ZERO_POINT, new Vector(1, -3, -10)),
                camera1.constructRay(4, 4, 1, 0), badRay);

    }
}