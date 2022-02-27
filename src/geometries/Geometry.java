package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Geometry Interface represnts Geometric shapes in 3D Cartesian coordinate system
 */
public interface Geometry {
    Vector getNormal(Point point);
}
