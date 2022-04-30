package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * light source object
 */
public class PointLight extends Light implements LightSource {

    /**
     * attenuation coefficient
     */
    private double kC=1;
    /**
     * attenuation coefficient depending on distance
     */
    private double kL=0;
    /**
     * attenuation coefficient depending on distance²
     */
    private double kQ=0;

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
        this.kC = kC;
        return this;
    }

    /**
     * setter for kL field (Builder pattern style)
     * @param kL coefficient for attenuation depending on distance
     * @return this instance of object
     */
    public PointLight setkL(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * setter for kQ field (Builder pattern style)
     * @param kQ coefficient for attenuation depending on distance²
     * @return this instance of object
     */
    public PointLight setkQ(double kQ) {
        this.kQ = kQ;
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
        double factor=kC+ kL *distance+kQ*distance*distance;
        // scale color by 1/denominator
        Color color=getIntensity().scale(1/factor);
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
     * todo
     * @param p
     * @return
     */
    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }
}
