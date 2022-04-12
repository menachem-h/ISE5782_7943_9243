package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 *  Interface for Geometric shapes in 3D  Space
 */
public abstract class Geometry  extends  Intersectable{

    /**
     * {@link Color} of the shape
     */
    protected  Color emission = Color.BLACK;

    /**
     * {@link Material} type of the shape
     */
    private Material material=new Material();

    /**
     * getter for emission field
     * @return {@link  Color} of the shape
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * setter for emission field (builder pattern style)
     * @param emission {@link Color} object to set shape's color to
     * @return this instance of object
     */
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

    /**
     * getter for material field
     * @return geometry's {@link Material} type
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * setter for material field (builder pattern style)
     * @param material {@link Material} object to set geometry's material to
     * @return this instance of object
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
