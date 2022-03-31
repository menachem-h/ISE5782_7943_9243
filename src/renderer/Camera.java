package renderer;

import primitives.*;

import static primitives.Util.isZero;

/**
 * camera in 3D space with view plane
 */
public class Camera {


    private Point _p0;         // camera's position point in 3D space
    private Vector _vTo;       // vector pointing towards view plane (-Z axis)
    private Vector _vUp;       // vector pointing up ( Y axis)
    private Vector _vRight;    // vector pointing right ( X axis)

    private double _distance;   // distance between view plane from camera

    private int _width;         // width of view plane "Physical" size
    private int _height;        // height of view plane "Physical" size
    private ImageWriter imageWriter = null;

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

    /**
     * getter for _p0 field
     * @return position point of camera
     */
    public Point getP0() {
        return _p0;
    }

    /**
     * getter for _vTo field
     * @return vector pointing towards view plane (-Z axis)
     */
    public Vector getvTo() {
        return _vTo;
    }

    /**
     * getter for _vUp field
     * @return vector pointing up ( Y axis)
     */
    public Vector getvUp() {
        return _vUp;
    }


    /**
     * getter for _vRight field
     * @return vector pointing right ( X axis)
     */
    public Vector getvRight() {
        return _vRight;
    }

    /**
     * getter for _distance field
     * @return distance between view plane from camera
     */
    public double getDistance() {
        return _distance;
    }

    /**
     * getter for _width field
     * @return width of view plane "Physical" size
     */
    public int getWidth() {
        return _width;
    }

    /**
     * getter for _height field
     * @return height of view plane "Physical" size
     */
    public int getHeight() {
        return _height;
    }

    public void writeToImage() {
        imageWriter.writeToImage();
    }

    public void printGrid(int i, Color color) {
    }

    public void renderImage() {
    }

    /**
     * inner builder class. implements Builder pattern
     * constructs a new {@link Camera} object
     */
    public static class CameraBuilder {

        // fields identical to Camera class
        private Point _p0;         // camera's position point in 3D space
        private Vector _vTo;       // vector pointing towards view plane (-Z axis)
        private Vector _vUp;       // vector pointing up ( Y axis)
        private Vector _vRight;    // vector pointing right ( X axis)

        private double _distance;   // distance between view plane from camera

        private int _width;         // width of view plane "Physical" size
        private int _height;        // height of view plane "Physical" size

        /**
         * constructor
         * @param p0  position point of camera in #D space
         * @param vTo vector pointing towards view plane (-Z axis)
         * @param vUp vector pointing up ( Y axis)
         */
        public CameraBuilder(Point p0, Vector vTo, Vector vUp) {

            _p0 = p0;

            //vto and vup must be orthogonal
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("the vectors vUp and vTo are not orthogonal");
            }

            //stores the vector after normalizing
            _vTo = vTo.normalize();
            _vUp = vUp.normalize();

            // vector to right (X axis calculated by cross product of vUp and vTo)
            _vRight = _vTo.crossProduct(_vUp);
        }


        //chaining methods

        /**
         * set view plane distance - (chaining method)
         *
         * @param distance distance between camera and view plane
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setVPDistance(double distance) {
            _distance = distance;
            return this;
        }

        /**
         * set "physical" size of view plane (chaining method)
         * @param width size of width of view plane
         * @param height size of height of view plane
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setVPSize(int width, int height) {
            _width = width;
            _height = height;
            return this;
        }

        /**
         * Builder pattern - build function - creates new camera
         * using this instance of {@link CameraBuilder}
         * @return Camera object
         */
        public Camera build() {
            Camera cam = new Camera(this);
            return cam;
        }

        public CameraBuilder setImageWriter(){
            imageWriter  =
        }
    }


    /**
     * construct ray from a {@link Camera} towards a pixel in a view plane
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param j  column index of pixel
     * @param i  row index of pixel
     * @return  {@link Ray}
     */
    public Ray constructRay(int Nx, int Ny, int j, int i) {
        //view plane center:
        Point Pc = _p0.add(_vTo.scale(_distance));

        // calculate "size" of each pixel -
        // height per pixel = total "physical" height / number of rows
        // width per pixel = total "physical" width / number of columns
        double Ry = (double) _height / Ny;
        double Rx = (double) _width / Nx;

        // set result point to middle of view plane
        Point Pij = Pc;

        // calculate necessary "size" needed to move from
        // center of view plane to reach the middle point of pixel (i,j)
        double yI = -(i - (Ny - 1) / 2d) * Ry;
        double xJ = (j - (Nx - 1) / 2d) * Rx;


        // if result of xJ is > 0
        // move result point left/right on  X axis
        // to reach middle point of pixel (i,j) (on X axis direction)
        if (!isZero(xJ)) {
            Pij = Pij.add(_vRight.scale(xJ));
        }

        // if result of yI is > 0
        // move result point up/down on Y axis
        // to reach middle point of pixel (i,j)
        if (!isZero(yI)) {
            Pij = Pij.add(_vUp.scale(yI));
        }

        //return ray from camera to midlle point of pixel(i,j) in view plane
        return new Ray(_p0, Pij.subtract(_p0));
    }
}