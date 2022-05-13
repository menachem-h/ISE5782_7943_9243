package renderer;

import primitives.*;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Random;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * camera in 3D space with view plane
 */
public class Camera {
    
    // static data fields
    /**
     * camera's position point in 3D space
     */
    private Point p0;
    /**
     *  vector pointing towards view plane (-Z axis)
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
    private ImageWriter imageWriter = null;
    /**
     * calculate color of pixel functionality object
     */
    private RayTracer rayTracer = null;

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
        imageWriter = camBuilder.imageWriter;
        rayTracer = camBuilder.rayTracer;

    }

    /**
     * getter for _p0 field
     * @return position point of camera
     */
    public Point getP0() {
        return p0;
    }

    /**
     * getter for _vTo field
     * @return vector pointing towards view plane (-Z axis)
     */
    public Vector getvTo() {
        return vTo;
    }

    /**
     * getter for _vUp field
     * @return vector pointing up ( Y axis)
     */
    public Vector getvUp() {
        return vUp;
    }


    /**
     * getter for _vRight field
     * @return vector pointing right ( X axis)
     */
    public Vector getvRight() {
        return vRight;
    }

    /**
     * getter for _distance field
     * @return distance between view plane from camera
     */
    public double getDistance() {
        return distance;
    }

    /**
     * getter for _width field
     * @return width of view plane "Physical" size
     */
    public int getWidth() {
        return width;
    }

    /**
     * getter for _height field
     * @return height of view plane "Physical" size
     */
    public int getHeight() {
        return height;
    }

    /**
     * crate a jpeg file, with scene "captured" by camera
     */
    public void writeToImage() {
        if (imageWriter==null)
            throw new MissingResourceException("image writer is not initialized",ImageWriter.class.getName(),"");
        imageWriter.writeToImage();
    }

    /**
     * print grid lines on  image
     * @param interval interval ("physical" space) between each pair of grid lines
     * @param color color of grid lines
     */
    public void printGrid(int interval, Color color) {
        if (imageWriter==null)
            throw new MissingResourceException("image writer is not initialized",ImageWriter.class.getName(),"");
        for (int i = 0; i < imageWriter.getNy(); i++) {
            for (int j = 0; j < imageWriter.getNx(); j++) {
                if(i%interval ==0 || j%interval==0)
                    imageWriter.writePixel(j,i,color);
            }

        }
    }

    /**
     * render image "captured" through view plane
     */
    public void renderImage() {
        // check that image, writing and rendering objects are instantiated
        if (imageWriter==null)
            throw new MissingResourceException("image writer is not initialized",ImageWriter.class.getName(),"");

        if(rayTracer==null)
            throw new MissingResourceException("ray tracer is not initialized",RayTracer.class.getName(),"");

        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        // for each pixel (i,j) , construct  a ray from camera through pixel,
        // then use rayTracer object to get correct color, finally use imageWriter to write pixel
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                castRayBeam( nX, nY,i, j,3,3);
              //castRay(nX,nY,i,j);
            }

        }
    }

    /**
     * cast a ray from camera through pixel (i,j) in view plane and get color of pixel
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param j  column index of pixel
     * @param i  row index of pixel
     */
    private void castRay(int Nx, int Ny, int j, int i){
        // construct ray through pixel
        Ray ray= constructRay(Nx,Ny,j,i);
        // return the color using ray tracer
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j,i,color);
    }

    private void castRayBeam(int Nx, int Ny, int j, int i,int n ,int m){
        // construct ray through pixel
        Ray ray= constructRay(Nx,Ny,j,i);
        var rayBeam = constructRayBeam(Nx,Ny,n,m,ray);
        // return the color using ray tracer
        Color color = rayTracer.traceRay(ray);
        for (var r : rayBeam) {
            color = color.add(rayTracer.traceRay(r));
        }
        //color = color.scale(1/(m*n)+1);
        imageWriter.writePixel(j,i,color);
    }

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
         *  vector pointing towards view plane (-Z axis)
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

        /**
         * constructor
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
         * @param width size of width of view plane
         * @param height size of height of view plane
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setVPSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * set image writing to file functionality of camera
         * @param imageWriter instance of {@link ImageWriter} class ,enables writing a scene to jpeg file
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setImageWriter(ImageWriter imageWriter){
            this.imageWriter = imageWriter;
            return this;
        }

        /**
         * set image rendering functionality of camera
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
         * @return new {@link Camera} object
         */
        public Camera build() {
            Camera cam = new Camera(this);
            return cam;
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

    /**
     * todo
     * @param Nx
     * @param Ny
     * @param Pij
     * @return
     */
    public Ray constructRandomRay(int Nx, int Ny, Point Pij){

        // calculate "size" of each pixel -
        // height per pixel = total "physical" height / number of rows
        // width per pixel = total "physical" width / number of columns
        double Ry = (double) height / Ny;
        double Rx = (double) width / Nx;

        Random r = new Random();
        // calculate necessary "size" needed to move from
        // center of view plane to reach the middle point of pixel (i,j)
        double yI = r.nextDouble(Ry)-Ry/2;
        double xJ = r.nextDouble(Rx)-Rx/2;

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

        return new Ray(p0,Pij.subtract(p0));

    }

    /**
     * todo
     * @param m
     * @param n
     * @param Nx
     * @param Ny
     * @param ray
     * @return
     */
    public List<Ray> constructRayBeam(int m, int n,int Nx,int Ny,Ray ray){
        Point Pij = ray.getPoint(distance);
        List<Ray> temp = new LinkedList<>();
        for (int i = 0; i < n*m; i++)
            temp.add(constructRandomRay(Nx,Ny,Pij));
        return temp;
    }

    /**
     * move a camera to a different position point (angel of camera does not change)
     * @param to number coordinates to move on Z axis
     * @param up number coordinates to move on Y axis
     * @param right number coordinates to move on X axis
     */
    public void moveCamera(double to, double up, double right){
        // all values == 0 , do not move camera
        if(to==0&&up==0&&right==0)
            return ;
        // scale  and set camera's position point with values sent as parameters to move to new point
        if(to!=0)
            p0 = p0.add(vTo.scale(to));
        if(up!=0)
            p0 = p0.add(vUp.scale(up));
        if(right!=0)
            p0 = p0.add(vRight.scale(right));

    }
}