package geometries;

import primitives.Point;
import primitives.Vector;

/**
 *  Interface for Geometric shapes in 3D  Space
 */
public interface Geometry  extends  Intersectable{
    /**
     * calculate the normal vector to a geometric shape
     * @param point point to get normal vector at
     * @return Vector -  normal vector to geometry at specified point (vector is normalized)
     */
    Vector getNormal(Point point);
}
