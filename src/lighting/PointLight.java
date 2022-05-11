package lighting;

import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;

import java.awt.image.DataBufferUShort;

/**
 * light source object
 */
public class PointLight extends Light implements LightSource {

    /**
     * attenuation coefficient
     */
    private Double3 kC=Double3.ONE;
    /**
     * attenuation coefficient depending on distance
     */
    private Double3 kL=Double3.ZERO;
    /**
     * attenuation coefficient depending on distance²
     */
    private Double3 kQ=Double3.ZERO;

    /**
     * position {@link Point} of light source in 3D space
     */
    private Point position;

    /**
     * constructor
     * @param intensity {@link Color} of intensity of light
     * @param position position {@link Point} of the light object
     */
    public PointLight(Color intensity ,Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * setter for kC field (Builder pattern style)
     * @param kC attenuation coefficient
     * @return this instance of object
     */
    public PointLight setkC(double kC) {
        this.kC = new Double3(kC);
        return this;
    }

    /**
     * setter for kL field (Builder pattern style)
     * @param kL coefficient for attenuation depending on distance
     * @return this instance of object
     */
    public PointLight setkL(double kL) {
        this.kL =new Double3(kL);
        return this;
    }

    /**
     * setter for kQ field (Builder pattern style)
     * @param kQ coefficient for attenuation depending on distance²
     * @return this instance of object
     */
    public PointLight setkQ(double kQ) {
        this.kQ =new Double3(kQ);
        return this;
    }

    /**
     * get color of intensity of the point light at a given point
     * @param p {@link Point} to get intensity at
     * @return {@link Color} of intensity of light
     */
    @Override
    public Color getIntensity(Point p) {
        // intensity for point light holds
        //Ip = I0 / (kC + kL*distance + kQ*distance²)
        // calculate distance from light to point
        double distance =p.distance(position);
        // calculate denominator
        Double3 factor=kC.add(kL.scale(distance)).add(kQ.scale(distance*distance));
        // scale color by 1/denominator
        Color color=getIntensity().reduce(factor);
        return color;
    }

    /**
     * return the direction from the point light to a given point
     * @param p {@link Point} to get intensity at
     * @return {@link Vector} from point light to the point
     */
    @Override
    public Vector getL(Point p) {
        try{
            Vector v=p.subtract(position).normalize();
            return v;
        }
        // point light is at given point
        catch (Exception ex){
            return null;
        }
    }

    /**
     * get the distance between a point light to a given point
     * @param p {@link Point} to calculate distance to
     * @return distance
     */
    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }
}
