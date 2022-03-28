package renderer;

import primitives.*;

import static primitives.Util.isZero;

/**
 * camera in 3D space  with view plane
 */
public class Camera {


    private Point _p0;         // camera's position point in 3D space
    private Vector _vTo;       // vector pointing towards view plane (-Z axis)
    private Vector _vUp;       // vector pointing up ( Y axis)
    private Vector _vRight;    // vector pointing right ( X axis)

    private double _distance;   // distance between view plane from camera

    private int _width;         // width of view plane "Physical" size
    private int _height;        // height of view plane "Physical" size

    /**
     * constructor
     *
     * @param camBuilder internal class based on Builder pattern
     */
    private Camera(CameraBuilder camBuilder) {

        _p0 = camBuilder._p0;
        _vTo = camBuilder._vTo;
        _vUp = camBuilder._vUp;
        _vRight = camBuilder._vRight;
        _distance = camBuilder._distance;
        _width = camBuilder._width;
        _height = camBuilder._height;

    }

    public Point getP0() {
        return _p0;
    }

    public Vector getvTo() {
        return _vTo;
    }

    public Vector getvUp() {
        return _vUp;
    }

    public Vector getvRight() {
        return _vRight;
    }

    public double getDistance() {
        return _distance;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }

    public static class CameraBuilder {
        private Point _p0;
        private Vector _vTo;
        private Vector _vUp;
        private Vector _vRight;

        private double _distance;

        private int _width;
        private int _height;

        public CameraBuilder(Point p0, Vector vTo, Vector vUp) {

            _p0 = p0;

            //vto and vup mus be orthogonal
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("the vectors vUp and vTo are not orthogonal");
            }

            //normalizing the vectors
            _vTo = vTo.normalize();
            _vUp = vUp.normalize();

            _vRight = _vTo.crossProduct(_vUp);
        }
        //chaining methods

        /**
         * set view plane distance
         *
         * @param distance distance between camera and view plane
         * @return the Camera instance
         */
        public CameraBuilder setVPDistance(double distance) {
            _distance = distance;
            return this;
        }

        public CameraBuilder setVPSize(int width, int height) {
            _width = width;
            _height = height;
            return this;
        }

        public Camera build() {
            Camera cam = new Camera(this);
            return cam;
        }
    }


    public Ray constructRay(int Nx, int Ny, int j, int i) {
        //Image center  :
        Point Pc = _p0.add(_vTo.scale(_distance));

        //Ratio (pixel width & height)
        double Ry = (double) _height / Ny;
        double Rx = (double) _width / Nx;

        Point Pij = Pc;

        double yI = -(i - (Ny - 1) / 2d) * Ry;
        double xJ = (j - (Nx - 1) / 2d) * Rx;

        // move to middle of pixel i,j

        if (!isZero(xJ)) {
            Pij = Pij.add(_vRight.scale(xJ));
        }

        if (!isZero(yI)) {
            Pij = Pij.add(_vUp.scale(yI));
        }

        //return ray from camera to viewplane coordinates (i, j)
        return new Ray(_p0, Pij.subtract(_p0));
    }
}