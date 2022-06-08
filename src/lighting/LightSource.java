package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import java.util.List;

/**
 * interface for objects representing a light source
 */
public interface LightSource {
    /**
     * get color of intensity of light at a given point in a 3D space
     * @param p {@link Point} to get intensity at
     * @return {@link Color} of intensity of light
     */
    public Color getIntensity(Point p);

    /**
     * get a direction vector from light source to a given point in 3D space
     * @param p {@link Point} to get intensity at
     * @return {@link  Vector} from light source to the point
     */
    public Vector getL(Point p);

    /**
     * get a beam of rays from a point on a geometry towards a light,
     * all the rays are constructed within the soft shadow radius boundary
     *
     * @param p point on the geometry
     * @return {@link List}of rys from the geometry to the soft shadow radius
     * @author Yona Shmerla
     */
    public List<Vector> getListL(Point p);
    /**
     * get the distance between a light source to a given point
     * @param p {@link Point} to calculate distance to
     * @return distance
     */
    public double getDistance(Point p);
}
