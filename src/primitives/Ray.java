package primitives;

import java.util.Objects;

public class Ray {

    final Point p0;
    final Vector dir;

    public Ray(Point p0, Vector dir) {

        this.p0 = p0;
        if(dir._xyz.equals(Double3.ZERO))
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

    public Point getP0() {
        return p0;
    }

    public Vector getDir() {
        return dir;
    }

    @Override
    public int hashCode() {
        return Objects.hash(p0, dir);
    }


}
