package geometries;

import primitives.*;

import java.util.List;

/**
 * Interface for geometric objects that intersect with a ray in 3D space
 */
public abstract class Intersectable {

    public static class GeoPoint {

        public final Geometry geometry;
        public final Point point;

        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }
    }

    /**
     * find all intersection {@link Point}s between ray and an object (geometry)
     *
     * @param ray ray towards the object
     * @return immutable list of intersection points
     */
    public final List<Point> findIntersections(Ray ray) {
        List<GeoPoint> geoList = findGeoIntersections(ray);
        return geoList == null ? null
                : geoList.stream()
                .map(gp -> gp.point)
                .toList();
    }

    //NVI pattern
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        //....
        return findGeoIntersectionsHelper(ray);
    }

    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

}
