package renderer;


import org.junit.jupiter.api.Test;
import primitives.*;
import geometries.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * integration test class, checks camera ray intersection with geometric shapes in 3D space
 */
public class CameraIntegrationsTest {

    static final Point ZERO_POINT = new Point(0, 0, 0);

    private int countCameraGeometryIntersection(Camera cam, int Nx, int Ny, Intersectable geometry) {
        List<Point> result = new LinkedList<>();
        int count = 0;
        for (int i = 0; i < Nx; i++) {

            for (int j = 0; j < Ny; j++) {
                result = geometry.findIntersections(cam.constructRay(Nx, Ny, j, i));
                count += (result== null) ?0: result.size();
            }

        }
        return count;
    }

    Camera camera1 = new Camera.CameraBuilder(ZERO_POINT, new Vector(0, 0, -1), new Vector(0, -1, 0))
            .setVPDistance(1d).setVPSize(3,3)
            .build();
    Camera camera2 = new Camera.CameraBuilder(new Point(0,0,0.5), new Vector(0, 0, -1), new Vector(0, -1, 0))
            .setVPDistance(1d).setVPSize(3,3)
            .build();
    String message  = " incorrect number of intersections";
    /**
     * Test method for {@link Camera#constructRay(int, int, int, int)}.
     *  testing number of intersections , not actual points
     */
    @Test
    void CameraSphereTest1() {
        // sphere size covers all view plane
        // sphere is intersected at each pixel  - 2 per pixel - total 18 points
        Sphere sp = new Sphere(new Point(0,0,-2.5),2.5);
        assertEquals(18,countCameraGeometryIntersection(camera2,3,3,sp), message);
    }

    /**
     * Test method for {@link Camera#constructRay(int, int, int, int)}.
     *  testing number of intersections , not actual points
     */
    @Test
    void CameraSphereTest2() {
        // sphere size covers middle pixel and reaches all adjacent pixels middle point
        // sphere is intersected in middle point only - adjacent pixels points are tangent
        // to sphere and do not return as intersection - 2 points
        Sphere sp = new Sphere(new Point(0,0,-3),1);
        assertEquals(2,countCameraGeometryIntersection(camera1,3,3,sp),message);
    }

    /**
     * Test method for {@link Camera#constructRay(int, int, int, int)}.
     *  testing number of intersections , not actual points
     */
    @Test
    void CameraSphereTest3() {
        // sphere size covers all middle of view plane, ray does not intersect at corner pixels
        // 10 point ( 2 per pixel)
        Sphere sp = new Sphere(new Point(0,0,-2),2);
        assertEquals(10,countCameraGeometryIntersection(camera2,3,3,sp), message);
    }

    /**
     * Test method for {@link Camera#constructRay(int, int, int, int)}.
     *  testing number of intersections , not actual points
     */
    @Test
    void CameraSphereTest4() {
        // sphere size covers all view plane, but camera is inside sphere
        // so each pixel only intersects once
        // 9 point ( 1 per pixel)
        Sphere sp = new Sphere(new Point(0,0,1),5);
        assertEquals(9,countCameraGeometryIntersection(camera1,3,3,sp), message);
    }

    /**
     * Test method for {@link Camera#constructRay(int, int, int, int)}.
     *  testing number of intersections , not actual points
     */
    @Test
    void CameraSphereTest5() {
        // sphere is in opposite direction to camera
        // 0 point
        Sphere sp = new Sphere(new Point(0,0,10),5);
        assertEquals(0,countCameraGeometryIntersection(camera1,3,3,sp), message);
    }

    /**
     * Test method for {@link Camera#constructRay(int, int, int, int)}.
     *  testing number of intersections , not actual points
     */
    @Test
    void CameraPlaneTest1() {
        // plane is in opposite direction to camera
        // 0 point
        Plane pl = new Plane(new Point(0,0,10), new Vector(0,0,1));
        assertEquals(0,countCameraGeometryIntersection(camera1,3,3,pl), message);
    }

    /**
     * Test method for {@link Camera#constructRay(int, int, int, int)}.
     *  testing number of intersections , not actual points
     */
    @Test
    void CameraPlaneTest2() {
        // plane is in direction of camera - covers all view plane
        // 9 points ( 1 per pixel)
        Plane pl = new Plane(new Point(0,0,-10), new Vector(0,0,1));
        assertEquals(9,countCameraGeometryIntersection(camera1,3,3,pl), message);
    }

    /**
     * Test method for {@link Camera#constructRay(int, int, int, int)}.
     *  testing number of intersections , not actual points
     */
    @Test
    void CameraPlaneTest3() {
        // plane is in diagonal to camera  - covers two rows out of three of view plane
        // 6 points ( 1 per pixel)
        Plane pl = new Plane(new Point(0,2,-3), new Vector(0,-1,-1));
        assertEquals(6,countCameraGeometryIntersection(camera1,3,3,pl), message);
    }

    /**
     * Test method for {@link Camera#constructRay(int, int, int, int)}.
     *  testing number of intersections , not actual points
     */
    @Test
    void CameraPlaneTest4() {
        // plane not in straight direction of camera but ray through all pixels intersect plane
        // 9 points ( 1 per pixel)
        Plane pl = new Plane(new Point(0,0,-1), new Vector(1.5,3,5));
        assertEquals(9,countCameraGeometryIntersection(camera1,3,3,pl), message);
    }

    /**
     * Test method for {@link Camera#constructRay(int, int, int, int)}.
     *  testing number of intersections , not actual points
     */
    @Test
    void CameraTriangle1() {
        // triangle is intersected in middle pixel only
        // 9 points ( 1 per pixel)
        Triangle tri = new Triangle(new Point(0,1,-2),new Point(1,-1,-2),new Point(-1,-1,-2));
        assertEquals(1,countCameraGeometryIntersection(camera1,3,3,tri), message);
    }

    /**
     * Test method for {@link Camera#constructRay(int, int, int, int)}.
     *  testing number of intersections , not actual points
     */
    @Test
    void CameraTriangle2() {
        // triangle is intersected in middle pixel only
        // 9 points ( 1 per pixel)
        Triangle tri = new Triangle(new Point(0,20,-2),new Point(1,-1,-2),new Point(-1,-1,-2));
        assertEquals(2,countCameraGeometryIntersection(camera1,3,3,tri), message);
    }
}
