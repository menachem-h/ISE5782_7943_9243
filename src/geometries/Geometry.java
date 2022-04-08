package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 *  Interface for Geometric shapes in 3D  Space
 */
public abstract class Geometry  extends  Intersectable{
    protected  Color emission = Color.BLACK;
    private Material material=new Material();

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

    public Material getMaterial() {
        return material;
    }

    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
