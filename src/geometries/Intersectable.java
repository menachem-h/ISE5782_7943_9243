package geometries;

import primitives.*;

import java.util.List;

/**
 *  interface for all objects that can intersect with a ray
 */
public interface Intersectable {
    /**
     * find all intersection {@link Point}s to the specific object
     * @param ray ray towards the object
     * @return immutable list of intersection points
     */
    public List<Point> findIntersections(Ray ray);
}
