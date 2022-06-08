package lighting;

import geometries.Sphere;
import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;

import java.awt.image.DataBufferUShort;
import java.util.LinkedList;
import java.util.List;

/**
 * light source object
 */
public class PointLight extends Light implements LightSource {

    /**
     * attenuation coefficient
     */
    private Double3 kC = Double3.ONE;
    /**
     * attenuation coefficient depending on distance
     */
    private Double3 kL = Double3.ZERO;
    /**
     * attenuation coefficient depending on distance²
     */
    private Double3 kQ = Double3.ZERO;

    /**
     * position {@link Point} of light source in 3D space
     */
    private Point position;

    /**
     * size of radius around the light to create soft shadows
     */
    private Double radius;

    /**
     * constructor
     *
     * @param intensity {@link Color} of intensity of light
     * @param position  position {@link Point} of the light object
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * constructor
     *
     * @param intensity {@link Color} of intensity of light
     * @param position  position {@link Point} of the light object
     * @param radius    size of radius around the light for soft shadow calculations
     */
    public PointLight(Color intensity, Point position, Double radius) {
        super(intensity);
        this.position = position;
        this.radius = radius;
    }

    /**
     * setter for radius field
     *
     * @param radius size of the radius
     * @return this instance of object
     */
    public PointLight setRadius(Double radius) {
        this.radius = radius;
        return this;
    }

    /**
     * setter for kC field (Builder pattern style)
     *
     * @param kC attenuation coefficient
     * @return this instance of object
     */
    public PointLight setkC(double kC) {
        this.kC = new Double3(kC);
        return this;
    }

    /**
     * setter for kL field (Builder pattern style)
     *
     * @param kL coefficient for attenuation depending on distance
     * @return this instance of object
     */
    public PointLight setkL(double kL) {
        this.kL = new Double3(kL);
        return this;
    }

    /**
     * setter for kQ field (Builder pattern style)
     *
     * @param kQ coefficient for attenuation depending on distance²
     * @return this instance of object
     */
    public PointLight setkQ(double kQ) {
        this.kQ = new Double3(kQ);
        return this;
    }

    /**
     * get color of intensity of the point light at a given point
     *
     * @param p {@link Point} to get intensity at
     * @return {@link Color} of intensity of light
     */
    @Override
    public Color getIntensity(Point p) {
        // intensity for point light holds
        //Ip = I0 / (kC + kL*distance + kQ*distance²)
        // calculate distance from light to point
        double distance = p.distance(position);
        // calculate denominator
        Double3 factor = kC.add(kL.scale(distance)).add(kQ.scale(distance * distance));
        // scale color by 1/denominator
        Color color = getIntensity().reduce(factor);
        return color;
    }

    /**
     * return the direction from the point light to a given point
     *
     * @param p {@link Point} to get intensity at
     * @return {@link Vector} from point light to the point
     */
    @Override
    public Vector getL(Point p) {
        try {
            Vector v = p.subtract(position).normalize();
            return v;
        }
        // point light is at given point
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * get a beam of rays from a point on a geometry towards the light,
     * all the rays are within the radius boundary
     *
     * @param p point on the geometry
     * @return {@link List}of rys from the geometry to the soft shadow radius
     * @author Yona Shmerla
     */
    public List<Vector> getListL(Point p) {

        double distance = p.subtract(position).length();
        Sphere sphere = new Sphere(position, distance / 10);

        List<Vector> vectors = new LinkedList();
        for (double i = -radius; i < radius; i += radius / 10) {
            for (double j = -radius; j < radius; j += radius / 10) {
                if (i != 0 && j != 0) {
                    Point point = position.add(new Vector(i, 0.1d, j));
                    if (point.equals(position)) {
                        vectors.add(p.subtract(point).normalize());
                    } else {
                        try {
                            if (point.subtract(position).dotProduct(point.subtract(position)) <= radius * radius) {
                                vectors.add(p.subtract(point).normalize());
                            }
                        } catch (Exception e) {
                            vectors.add(p.subtract(point).normalize());
                        }

                    }
                }

            }
        }
        vectors.add(getL(p));
        return vectors;
    }

    /**
     * get the distance between a point light to a given point
     *
     * @param p {@link Point} to calculate distance to
     * @return distance
     */
    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }
}
