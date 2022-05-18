package renderer;

import primitives.*;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Random;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

;

/**
 * camera in 3D space with view plane
 */
public class Camera {
    //region Fields

    // static data fields
    /**
     * camera's position point in 3D space
     */
    private Point p0;
    /**
     * vector pointing towards view plane (-Z axis)
     */
    private Vector vTo;
    /**
     * vector pointing up ( Y axis)
     */
    private Vector vUp;
    /**
     * vector pointing right ( X axis)
     */
    private Vector vRight;
    /**
     * distance between view plane from camera
     */
    private double distance;
    /**
     * width of view plane "Physical" size
     */
    private int width;
    /**
     * height of view plane "Physical" size
     */
    private int height;
    /**
     * anti aliasing method tu use in image rendering
     */

    //anti-aliasing functionality
    private AntiAliasing antiAliasing;
    /**
     *  first parameter for number of random ray to cast for random beam anti aliasing
     */
    private int n ;
    /**
     *  first parameter for number of random ray to cast for random beam anti aliasing
     */
    private int m ;

    // functionality  fields
    /**
     * image writing to file functionality object
     */
    private ImageWriter imageWriter = null;
    /**
     * calculate color of pixel functionality object
     */
    private RayTracer rayTracer = null;

    //endregion

    /**
     * constructor
     *
     * @param camBuilder internal class based on Builder pattern
     */
    private Camera(CameraBuilder camBuilder) {

        p0 = camBuilder.p0;
        vTo = camBuilder.vTo;
        vUp = camBuilder.vUp;
        vRight = camBuilder.vRight;
        distance = camBuilder.distance;
        width = camBuilder.width;
        height = camBuilder.height;
        antiAliasing = camBuilder.antiAliasing;
        n = camBuilder.n;
        m = camBuilder.m;
        imageWriter = camBuilder.imageWriter;
        rayTracer = camBuilder.rayTracer;
    }

    //region Camera Builder
    /**
     * inner builder class. implements Builder pattern
     * constructs a new {@link Camera} object
     */
    public static class CameraBuilder {

        // static data fields
        /**
         * camera's position point in 3D space
         */
        private Point p0;
        /**
         * vector pointing towards view plane (-Z axis)
         */
        private Vector vTo;
        /**
         * vector pointing up ( Y axis)
         */
        private Vector vUp;
        /**
         * vector pointing right ( X axis)
         */
        private Vector vRight;
        /**
         * distance between view plane from camera
         */
        private double distance;
        /**
         * width of view plane "Physical" size
         */
        private int width;
        /**
         * height of view plane "Physical" size
         */
        private int height;        //

        // functionality  fields
        /**
         * image writing to file functionality object
         */
        private ImageWriter imageWriter;
        /**
         * calculate color of pixel functionality object
         */
        private RayTracer rayTracer;

        // anti-aliasing functionality


        /**
         * anti aliasing method tu use in image rendering
         */
        private AntiAliasing antiAliasing = AntiAliasing.NONE;

        /**
         *  first parameter for number of random ray to cast for random beam anti aliasing
         */
        private int n ;

        /**
         *  first parameter for number of random ray to cast for random beam anti aliasing
         */
        private int m ;



        /**
         * constructor
         *
         * @param p0  position point of camera in #D space
         * @param vTo vector pointing towards view plane (-Z axis)
         * @param vUp vector pointing up ( Y axis)
         */
        public CameraBuilder(Point p0, Vector vTo, Vector vUp) {

            this.p0 = p0;

            //vto and vup must be orthogonal
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("the vectors vUp and vTo are not orthogonal");
            }

            //stores the vector after normalizing
            this.vTo = vTo.normalize();
            this.vUp = vUp.normalize();

            // vector to right (X axis calculated by cross product of vUp and vTo)
            vRight = this.vTo.crossProduct(this.vUp);
        }


        //chaining methods - for builder pattern

        /**
         * set view plane distance - (chaining method)
         *
         * @param distance distance between camera and view plane
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setVPDistance(double distance) {
            this.distance = distance;
            return this;
        }

        /**
         * set "physical" size of view plane (chaining method)
         *
         * @param width  size of width of view plane
         * @param height size of height of view plane
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setVPSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * set anti aliasing method to be used by camera
         *
         * @param aliasing {@link AntiAliasing} method
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setAntiAliasing(AntiAliasing aliasing) {
            this.antiAliasing = aliasing;
            return this;
        }

        /**
         * setter for first parameter of n*m used for random ray casting loop
         * @param num value of parameter
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setN(int num) {
            this.n = num;
            return this;
        }

        /**
         * setter for second parameter of n*m used for random ray casting loop
         * @param num value of parameter
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setM(int num) {
            this.m = num;
            return this;
        }

        /**
         * set image writing to file functionality of camera
         *
         * @param imageWriter instance of {@link ImageWriter} class ,enables writing a scene to jpeg file
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setImageWriter(ImageWriter imageWriter) {
            this.imageWriter = imageWriter;
            return this;
        }

        /**
         * set image rendering functionality of camera
         *
         * @param rayTracer instance of {@link RayTracer} class - enables calculating color of each pixel
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setRayTracer(RayTracer rayTracer) {
            this.rayTracer = rayTracer;
            return this;
        }

        /**
         * Builder pattern - build function - creates new camera
         * using this instance of {@link CameraBuilder}
         *
         * @return new {@link Camera} object
         */
        public Camera build() {
            Camera cam = new Camera(this);
            return cam;
        }


    }
    //endregion

    //region Getters
    /**
     * getter for _p0 field
     *
     * @return position point of camera
     */
    public Point getP0() {
        return p0;
    }

    /**
     * getter for _vTo field
     *
     * @return vector pointing towards view plane (-Z axis)
     */
    public Vector getvTo() {
        return vTo;
    }

    /**
     * getter for _vUp field
     *
     * @return vector pointing up ( Y axis)
     */
    public Vector getvUp() {
        return vUp;
    }


    /**
     * getter for _vRight field
     *
     * @return vector pointing right ( X axis)
     */
    public Vector getvRight() {
        return vRight;
    }

    /**
     * getter for _distance field
     *
     * @return distance between view plane from camera
     */
    public double getDistance() {
        return distance;
    }

    /**
     * getter for _width field
     *
     * @return width of view plane "Physical" size
     */
    public int getWidth() {
        return width;
    }

    /**
     * getter for _height field
     *
     * @return height of view plane "Physical" size
     */
    public int getHeight() {
        return height;
    }

    /**
     * getter for aliasing field
     *
     * @return anti-aliasing method used by camera
     */
    public AntiAliasing getAntiAliasing() {
        return antiAliasing;
    }

    /**
     * getter for n field
     * @return first parameter for n*m loop for random ray casting
     */
    public int getN() { return n;}

    /**
     * getter for m field
     * @return second parameter of n*m loop for random ray casting
     */
    public int getM() {return m;}
    //endregion

    //region Image Writing functionalities
    /**
     * crate a jpeg file, with scene "captured" by camera
     */
    public void writeToImage() {
        if (imageWriter == null)
            throw new MissingResourceException("image writer is not initialized", ImageWriter.class.getName(), "");
        imageWriter.writeToImage();
    }

    /**
     * print grid lines on  image
     *
     * @param interval interval ("physical" space) between each pair of grid lines
     * @param color    color of grid lines
     */
    public void printGrid(int interval, Color color) {
        if (imageWriter == null)
            throw new MissingResourceException("image writer is not initialized", ImageWriter.class.getName(), "");
        for (int i = 0; i < imageWriter.getNy(); i++) {
            for (int j = 0; j < imageWriter.getNx(); j++) {
                if (i % interval == 0 || j % interval == 0)
                    imageWriter.writePixel(j, i, color);
            }

        }
    }

    /**
     * render image "captured" through view plane
     */
    public void renderImage() {
        // check that image, writing and rendering objects are instantiated
        if (imageWriter == null)
            throw new MissingResourceException("image writer is not initialized", ImageWriter.class.getName(), "");

        if (rayTracer == null)
            throw new MissingResourceException("ray tracer is not initialized", RayTracer.class.getName(), "");

        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();

        // for each pixel (i,j) , construct  ray/rays from camera through pixel,
        // functions use rayTracer object to get correct color, then use imageWriter to write pixel to the file

        //cast ray according to Anti-Aliasing method set to Camera
        switch (getAntiAliasing()) {
            // no method used - cast single ray to center of pixel
            case NONE -> {
                for (int i = 0; i < nY; i++)
                    for (int j = 0; j < nX; j++)
                        castRay(nX, nY, i, j);
                break;
            }
            // bean of random rays cast for each pixel besides the ray towards the center
            case RANDOM -> {
                for (int i = 0; i < nY; i++)
                    for (int j = 0; j < nX; j++)
                        castRayBeamRandom(nX, nY, i, j, n, m);
                break;

            }
            // four rays cast to four corners of pixel besides the ray towards the center
            case CORNERS -> {
                for (int i = 0; i < nY; i++)
                    for (int j = 0; j < nX; j++)
                        castRayBeamCorners(nX, nY, i, j);
                break;


            }
        }
    }

    //endregion

    //region Ray Casting + Anti-Aliasing

    //region No Anti-Aliasing
    /**
     * cast a ray from camera through pixel (i,j) in view plane and get color of pixel
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param j  column index of pixel
     * @param i  row index of pixel
     */
    private void castRay(int Nx, int Ny, int j, int i) {
        // construct ray through pixel
        Ray ray = constructRay(Nx, Ny, j, i);
        // return the color using ray tracer
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j, i, color);
    }


    /**
     * construct ray from a {@link Camera} towards center of a pixel in a view plane
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param j  column index of pixel
     * @param i  row index of pixel
     * @return {@link Ray} from camera to center of pixel (i,j)
     */
    public Ray constructRay(int Nx, int Ny, int j, int i) {
        //view plane center:
        Point Pc = p0.add(vTo.scale(distance));

        // calculate "size" of each pixel -
        // height per pixel = total "physical" height / number of rows
        // width per pixel = total "physical" width / number of columns
        double Ry = (double) height / Ny;
        double Rx = (double) width / Nx;

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
            Pij = Pij.add(vRight.scale(xJ));
        }

        // if result of yI is > 0
        // move result point up/down on Y axis
        // to reach middle point of pixel (i,j)
        if (!isZero(yI)) {
            Pij = Pij.add(vUp.scale(yI));
        }

        //return ray from camera to middle point of pixel(i,j) in view plane
        return new Ray(p0, Pij.subtract(p0));
    }
    //endregion

    //region Random Beam Casting
    /**
     * cast a beam of n*m random beams within a grid of a pixel (i,j)
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param j  column index of pixel
     * @param i  row index of pixel
     * @param n first parameter to set number of random rays to cast
     * @param m second parameter to set number of rays to cast
     */
    private void castRayBeamRandom(int Nx, int Ny, int j, int i, int n, int m) {
        // construct ray through pixel
        Ray ray = constructRay(Nx, Ny, j, i);

        // construct n*m random rays towards the pixel
        var rayBeam = constructRayBeam(Nx, Ny, n, m, ray);


        // calculate color of the pixel using the average from all the rays in beam
        Color color = Color.BLACK;
        for (var r : rayBeam) {
            color = color.add(rayTracer.traceRay(r));
        }
        // reduce final color by total number of rays to get mean value of pixel color
        color = color.reduce(rayBeam.size());

        //write pixel
        imageWriter.writePixel(j, i, color);
    }

    /**
     * given a pixel construct a beam of random rays within the grid of the pixel
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param n first parameter to set number of random rays to cast
     * @param m second parameter to set number of rays to cast
     * @param ray ray towards the center of the pixel
     * @return list with m*n rays cast randomly within the grid of the pixel
     */
    public List<Ray> constructRayBeam(int Nx, int Ny, int n, int m, Ray ray) {
        // get center point of pixel
        Point Pij = ray.getPoint(distance);
        List<Ray> temp = new LinkedList<>();
        // cast m*n random rays
        for (int i = 0; i < n * m; i++)
            temp.add(constructRandomRay(Nx, Ny, Pij));

        // remove from the list rays that were randomly constructed identical to ray to center
        temp.removeIf((item)->{return item.equals(ray);});
        // add to list the ray to the center of the pixel
        temp.add(ray);

        return temp;
    }

    /**
     * given a pixel ,cast a ray to a randomly selected point within the grid of the pixel
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param Pij center point of pixel (i,j)
     * @return {@link Ray} from camera to randomly selected point
     */
    public Ray constructRandomRay(int Nx, int Ny, Point Pij) {

        // calculate "size" of each pixel -
        // height per pixel = total "physical" height / number of rows
        // width per pixel = total "physical" width / number of columns
        double Ry = (double) height / Ny;
        double Rx = (double) width / Nx;

        Random r = new Random();
        // set a random value to scale vector on Y axis
        // value range is from -(height/2) to (height/2)
        double yI = r.nextDouble(Ry) - Ry / 2;
        // set a random value to scale vector on X axis
        // value range is from -(width/2) to (width/2)
        double xJ = r.nextDouble(Rx) - Rx / 2;

        // if result of xJ is > 0
        // move result point from middle of pixel, left/right on  X axis
        if (!isZero(xJ)) {
            Pij = Pij.add(vRight.scale(xJ));
        }

        // if result of yI is > 0
        // move result point from middle of pixel up/down on Y axis
        if (!isZero(yI)) {
            Pij = Pij.add(vUp.scale(yI));
        }

        // return ray cast from camera to randomly selected point within grid of pixel
        // reached by yI and xJ scaling factors
        return new Ray(p0, Pij.subtract(p0));

    }
    //endregion

    //region Corner Ray Casting

    /**
     * cast four rays to the four corners of a pixel(i,j)
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param j  column index of pixel
     * @param i  row index of pixel
     */
    private void castRayBeamCorners(int Nx, int Ny, int j, int i) {
        // construct ray through pixel
        Ray ray = constructRay(Nx, Ny, j, i);
        // construct the four rays to the corners
        var rayBeam = constructRayCorners(Nx, Ny, ray);

        // calculate  color of pixel - add all the rays colors
        // ray towards center color
        Color color = rayTracer.traceRay(ray);
        // corner colors
        for (var r : rayBeam) {
            color = color.add(rayTracer.traceRay(r));
        }
        // reduce the final result by five to get mean value of pixel's color
        color = color.reduce(5);

        //write pixel
        imageWriter.writePixel(j, i, color);
    }

    /**
     * given a pixel , construct four rays to the four corners of the pixel
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param ray ray towards center of the pixel
     * @return list with the four rays
     */
    public List<Ray> constructRayCorners(int Nx, int Ny, Ray ray) {
        //get the point of the center of the pixel
        Point Pij = ray.getPoint(distance);

        // calculate "size" of each pixel -
        // height per pixel = total "physical" height / number of rows
        // width per pixel = total "physical" width / number of columns
        // divide height and width values by two
        double Ry = alignZero((double) height / Ny / 2);
        double Rx = alignZero((double) width / Nx / 2);
        Point p = Pij;

        //construct the four rays by scaling the required camera vectors in correct direction
        // circling around the four corners
        List<Ray> temp = new LinkedList<>();
        // top left
        p = p.add(vUp.scale(Ry)).add(vRight.scale(Rx));
        temp.add(new Ray(p0, p.subtract(p0)));
        // bottom left
        p = p.add(vUp.scale(-2 * Ry));
        temp.add(new Ray(p0, p.subtract(p0)));
        // bottom right
        p = p.add(vRight.scale(-2 * Rx));
        temp.add(new Ray(p0, p.subtract(p0)));
        // top right
        p = p.add(vUp.scale(2 * Ry));
        temp.add(new Ray(p0, p.subtract(p0)));

        return temp;
    }
    //endregion

    //endregion

    /**
     * move a camera to a different position point (angel of camera does not change)
     * @param to    number coordinates to move on Z axis
     * @param up    number coordinates to move on Y axis
     * @param right number coordinates to move on X axis
     */
    public void moveCamera(double to, double up, double right) {
        // all values == 0 , do not move camera
        if (to == 0 && up == 0 && right == 0)
            return;
        // scale  and set camera's position point with values sent as parameters to move to new point
        if (to != 0)
            p0 = p0.add(vTo.scale(to));
        if (up != 0)
            p0 = p0.add(vUp.scale(up));
        if (right != 0)
            p0 = p0.add(vRight.scale(right));

    }
}