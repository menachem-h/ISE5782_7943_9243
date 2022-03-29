package primitives;

import java.util.Objects;

import static primitives.Util.isZero;

/**
 * Ray in 3D space
 */
public class Ray {

    /**
     * fixed starting point of ray
     */
    final private Point p0;

    /**
     * direction of ray
     */
    final private Vector dir;

    /**
     * ray constructor based on a fixed starting point and direction
     *
     * @param p0  starting point
     * @param dir vector for direction
     * @throws IllegalArgumentException <p> if vector (0,0,0) is sent as parameter </p>
     */
    public Ray(Point p0, Vector dir) {

        this.p0 = p0;
        if (dir._xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector (0,0,0) not valid");
        this.dir = dir.normalize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return p0.equals(ray.p0) && dir.equals(ray.dir);
    }

    @Override
    public String toString() {
        return "Ray:" +
                "p0 = " + p0 +
                ", dir = " + dir
                ;
    }

    /**
     * getter for p0 field
     * @return origin point of ray
     */
    public Point getP0() {
        return p0;
    }

    /**
     * getter for dir field
     * @return direction vector of ray
     */
    public Vector getDir() {
        return dir;
    }

    @Override
    public int hashCode() {
        return Objects.hash(p0, dir);
    }

    /**
     * creating a {@link Point} at a specific distance in the ray direction
     * @param t distance
     * @return new {@link Point}
     */
    public Point getPoint(double t) {
        if(isZero(t)){
            throw  new IllegalArgumentException("t should not be ZERO");
        }
        return p0.add(dir.scale(t));
    }
}
