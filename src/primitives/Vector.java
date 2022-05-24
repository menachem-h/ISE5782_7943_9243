package primitives;
import static primitives.Util.*;
/**
 * Vector in 3D space
 */
public class Vector extends Point{

    /**
     * todo
     */
    public static final Vector axisX = new Vector(1,0,0);
    /**
     * todo
     */
    public static final Vector axisY = new Vector(0,1,0);
    /**
     * todo
     */
    public static final Vector axisZ = new Vector(0,0,1);


    /**
     * constructor to initialize vector using a Double3 object
     * @param xyz double3 object containing <p> (x,y,z) values of vector</p>
     * @throws IllegalArgumentException <p>if  vector (0,0,0) is trying to be constructed</p>
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if(this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector (0,0,0) not valid");
    }

    /**
     * constructor to initialize vector with its x,y,z coordinates values
     * @param x x coordinate value
     * @param y y coordinate value
     * @param z z coordinate value
     * @throws IllegalArgumentException <p>if  vector (0,0,0) is trying to be constructed</p>
     */
    public Vector(double x, double y, double z) {
        this(new Double3(x,y,z));
    }

    @Override
    public String toString() {
        return "Vector " + xyz;
    }




    /**
     * calculate x² + y² + z² expression from the 3D vector length formula:
     *<p> |V| = sqrt(x² + y² + z²) </p>
     * @return expression value in double format
     */
    public double lengthSquared() {

        double u1= xyz.d1;
        double u2= xyz.d2;
        double u3= xyz.d3;

        return u1*u1 + u2*u2 + u3*u3;
    }

    /**
     * calculate length of a vector implementing: <p> ||V|| = sqrt(x² + y² + z²) formula</p>
     * @return length of vector in double format
     */
    public double length(){
        return Math.sqrt(lengthSquared());
    }

    /**
     * add two vectors
     * @param vector second vector
     * @return new vector received from adding both vectors operation
     */
    public Vector add(Vector vector){ return new Vector(xyz.add(vector.xyz));}


    /**
     * subtract between two vectors
     * @param vector second vector
     * @return vector from second vector to this vector
     */
    public Vector subtract(Vector vector){
            return new Vector(xyz.subtract(vector.xyz));
    }

    /**
     * scale a vector length with a scalar value.
     * aV = (ax,ay,az)
     * @param scalar scalar value to scale vector with
     * @return new vector with value (ax,ay,az)
     */
    public Vector scale(double scalar){
        if (isZero(scalar))
            throw new IllegalArgumentException("scaling factor == 0");

        return new Vector(this.xyz.scale(scalar));
    }


    /**
     * calculate dot product of two vectors.
     *  a · b   =   a1b1 + a2b2 + a3b3
     * @param vector second vector
     * @return result of dot product on the vectors in double format
     */
    public double dotProduct(Vector vector) {

        double v1= vector.xyz.d1;
        double v2= vector.xyz.d2;
        double v3= vector.xyz.d3;

        double u1= xyz.d1;
        double u2= xyz.d2;
        double u3= xyz.d3;

        return v1*u1 + v2*u2 + v3*u3;
    }

    /**
     * calculate cross product between two vectors a,b
     * @param vector second vector -> b
     * @return  vector  that is perpendicular (orthogonal) to both a and b,
     * with a direction given by the right-hand rule
     */
    public Vector crossProduct(Vector vector) {

        //  | i   j   k  |
        //  | a1  a2  a3 |
        //  | b1  b2  b3 |
        // cross product vector =
        // new vector(a2*b3-b2*a3 , -(a1*b3-b1*a3) , a1*b2-b1*b2)

        double v1= vector.xyz.d1;
        double v2= vector.xyz.d2;
        double v3= vector.xyz.d3;

        double u1= xyz.d1;
        double u2= xyz.d2;
        double u3= xyz.d3;

        return new Vector(u2*v3-v2*u3,-(u1*v3-v1*u3),u1*v2-v1*u2);

    }

    /**
     * normalize a given vector u to a unit vector with same direction
     * @return new vector with value of (x/|u|, y/|u| , z/|u|)
     */
    public Vector normalize() {
         double length=length();
         if(isZero(length))
             throw new IllegalArgumentException("length cannot be zero");
         return new Vector(xyz.reduce(length));
    }

    /**
     * todo
     * @param axis
     * @param theta
     * @return
     */
    public Vector vectorRotate(Vector axis, double theta) {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        double u = axis.getX();
        double v = axis.getY();
        double w = axis.getZ();

        double v1 = u * x + v * y + w * z;

        double thetaRad=Math.toRadians(theta);
        double xPrime = u * v1 * (1d - Math.cos(thetaRad))
                + x * Math.cos(thetaRad)
                + (-w * y + v * z) * Math.sin(thetaRad);

        double yPrime = v * v1 * (1d - Math.cos(thetaRad))
                + y * Math.cos(thetaRad)
                + (w * x - u * z) * Math.sin(thetaRad);

        double zPrime = w * v1 * (1d - Math.cos(thetaRad))
                + z * Math.cos(thetaRad)
                + (-v * x + u * y) * Math.sin(thetaRad);

        return new Vector(xPrime, yPrime, zPrime);
    }
}
