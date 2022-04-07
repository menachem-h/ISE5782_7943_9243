package geometries;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 *  Interface for Geometric shapes in 3D  Space
 */
public abstract class Geometry  extends  Intersectable{
    protected  Color emission = Color.BLACK;

    public Color getEmission() {
        return emission;
    }

    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * calculate the normal vector to a geometric shape
     * @param point point to get normal vector at
     * @return Vector -  normal vector to geometry at specified point (vector is normalized)
     */
    public abstract Vector getNormal(Point point);
}
