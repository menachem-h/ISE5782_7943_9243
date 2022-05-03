package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// test ray intersection with objects - with consideration ti maxDistance parameter
// distance from ray origin to intersection point must be < maxDistance
class IntersectTest {

    // ray and sphere intersect twice at (0,0,3) and (0,6,3)
    Intersectable sphere = new Sphere(new Point(0,3,3),3);
    Ray ray = new Ray(new Point(0,-4,3),new Vector(0,1,0));
    Intersectable.GeoPoint gp1 = new Intersectable.GeoPoint((Geometry)sphere , new Point(0, 0, 3));
    Intersectable.GeoPoint gp2 = new Intersectable.GeoPoint((Geometry)sphere , new Point(0, 6, 3));

    @Test
    void findGeoIntersectionsEP1() {
        // TC01 -  max distance is smaller than distance to both intersection points - no intersections
        assertNull(sphere.findGeoIntersections(ray,2),"points are further than maxDistance");

    }

    @Test
    void findGeoIntersectionsEP2() {
        //TC02 -  max distance is smaller than distance to second intersection point - one intersection point
        List<Intersectable.GeoPoint> res = sphere.findGeoIntersections(ray,5);
        assertEquals(List.of(gp1),res,"one point only is in boundary");

    }

    @Test
    void findGeoIntersectionsEP3() {
        //TC03 -  distance to both points is smaller than maxDistance - two intersection points
        List<Intersectable.GeoPoint> res = sphere.findGeoIntersections(ray,12);
        assertEquals(List.of(gp1,gp2),res,"one point only is in boundary");

    }


}