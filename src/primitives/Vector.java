package primitives;
import static primitives.Util.*;
/**
 * Vector class represents two-dimensional vector  in 3D Cartesian coordinate
 * system
 */
public class Vector extends Point{

    /**
     * constructor to initialize vector using a Double3 object
     * @param xyz double3 object containing <p> (x,y,z) values of vector</p>
     * @throws IllegalArgumentException <p>if  vector (0,0,0) is trying to be constructed</p>
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if(_xyz.equals(Double3.ZERO))
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
        return "Vector " + _xyz ;
    }




    /**
     * calculate x² + y² + z² expression from the 3D vector length formula:
     *<p> |V| = sqrt(x² + y² + z²) </p>
     * @return expression value in double format
     */
    public double lengthSquared() {

        double u1= _xyz._d1;
        double u2= _xyz._d2;
        double u3= _xyz._d3;

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
    public Vector add(Vector vector){ return new Vector(_xyz.add(vector._xyz));}


    /**
     * subtract between two vectors
     * @param vector second vector
     * @return vector from second vector to this vector
     */
    public Vector subtract(Vector vector){
            return new Vector(_xyz.subtract(vector._xyz));
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

        return new Vector(_xyz.scale(scalar));
    }


    /**
     * calculate dot product of two vectors.
     *  a · b   =   a1b1 + a2b2 + a3b3
     * @param vector second vector
     * @return result of dot product on the vectors in double format
     */
    public double dotProduct(Vector vector) {

        double v1= vector._xyz._d1;
        double v2= vector._xyz._d2;
        double v3= vector._xyz._d3;

        double u1= _xyz._d1;
        double u2= _xyz._d2;
        double u3= _xyz._d3;

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

        double v1= vector._xyz._d1;
        double v2= vector._xyz._d2;
        double v3= vector._xyz._d3;

        double u1= _xyz._d1;
        double u2= _xyz._d2;
        double u3= _xyz._d3;

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
         return new Vector(_xyz.reduce(length));
    }
}
