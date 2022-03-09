package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Geometry Interface represnts Geometric shapes in 3D Cartesian coordinate system
 */
public interface Geometry {
    /**
     * calculate the normal vector at a given point of a geometry
     * @param point point to get normal vector at
     * @return Vector -  normal vector to geometry at specified point (normalized)
     */
    Vector getNormal(Point point);
}
