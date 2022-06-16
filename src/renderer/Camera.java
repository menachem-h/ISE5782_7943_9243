package renderer;

import primitives.*;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.stream.IntStream;

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


    //anti-aliasing functionality
    /**
     * anti aliasing method to use in image rendering
     */
    private AntiAliasing antiAliasing;
    /**
     * first parameter for number of random ray to cast for random beam anti aliasing
     */
    private int n;
    /**
     * first parameter for number of random ray to cast for random beam anti aliasing
     */
    private int m;

    /**
     * depth of recursion for adaptive anti-aliasing
     */
    private int recurseDepth;

    // functionality  fields
    /**
     * image writing to file functionality object
     */
    private ImageWriter imageWriter = null;
    /**
     * calculate color of pixel functionality object
     */
    private RayTracer rayTracer = null;

    // depth of field functionality
    /**
     * distance of focal popint from camera to create dof effect
     */
    private double dof;

    /**
     * camera's aperture size
     */
    private double apertureRadius;

    /**
     * determine if DOF functionality is used in image rendering
     */
    boolean useDOF;
    //endregion

    //region threads functionality
    /**
     * number of threads to use in image rendering
     */
    private int threadsCount ;
    /**
     * Spare threads if trying to use all the cores
     */
    private static final int SPARE_THREADS = 2;
    /**
     *  print interval in seconds
     */
    private double printInterval ;
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
        recurseDepth = camBuilder.recurseDepth;
        imageWriter = camBuilder.imageWriter;
        rayTracer = camBuilder.rayTracer;
        dof = camBuilder.dof;
        apertureRadius = camBuilder.apertureRadius;
        useDOF = camBuilder.useDOF;
        threadsCount = camBuilder.threadsCount;
        printInterval = camBuilder.printInterval;
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
         * first parameter for number of random ray to cast for random beam anti aliasing
         */
        private int n;

        /**
         * first parameter for number of random ray to cast for random beam anti aliasing
         */
        private int m;

        /**
         * depth of recursion for adaptive anti-aliasing
         */
        private int recurseDepth = 2;


        // depth of field functionality
        /**
         * distance of focal popint from camera to create dof effect
         */
        private double dof;

        /**
         * camera's aperture size
         */
        private double apertureRadius;

        /**
         * determine if DOF functionality is used in image rendering
         */
        private boolean useDOF = false;

        /**
         * number of threads to use in image rendering
         */
        private int threadsCount =0 ;
        /**
         * Spare threads if trying to use all the cores
         */
        private static final int SPARE_THREADS = 2;
        /**
         *  print interval in seconds
         */
        private double printInterval =0l ;






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
         *
         * @param num value of parameter
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setN(int num) {
            this.n = num;
            return this;
        }

        /**
         * setter for second parameter of n*m used for random ray casting loop
         *
         * @param num value of parameter
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setM(int num) {
            this.m = num;
            return this;
        }

        /**
         * setter for recursive depth field
         *
         * @param recurseDepth depth of recursion
         * @return value of depth of recursion wanted in adaptive anti-aliasing
         */
        public CameraBuilder setRecurseDepth(int recurseDepth) {
            this.recurseDepth = recurseDepth;
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
         * set for dof field
         *
         * @param dof depth of field focal point distance from camera
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setDof(double dof) {
            this.dof = dof;
            return this;
        }

        /**
         * set for apertureRadius field
         *
         * @param apertureRadius radius of aperture of camera
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setApertureRadius(double apertureRadius) {
            this.apertureRadius = apertureRadius;
            return this;
        }

        /**
         * setter for use DOF field
         *
         * @param useDOF true if DOF is used , otherwise, false.
         * @return this {@link CameraBuilder} instance
         */
        public CameraBuilder setUseDOF(boolean useDOF) {
            this.useDOF = useDOF;
            return this;
        }

        /**
         * Set multi-threading <br>
         * - if the parameter is 0 - number of cores less 2 is taken
         *
         * @param threads number of threads
         * @return the Render object itself
         */
        public CameraBuilder setMultithreading(int threads) {
            if (threads < -2)
                throw new IllegalArgumentException("Multithreading parameter must be 0 or higher");
            if (threads >= -1)
                this.threadsCount = threads;
            else {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                this.threadsCount = cores <= 2 ? 1 : cores;
            }
            return this;
        }

        /**
         * Set debug printing interval. If it's zero - there won't be printing at all
         *
         * @param interval printing interval in seconds
         * @return the Render object itself
         */
        public CameraBuilder setDebugPrint(double interval) {
            printInterval = interval;
            return this;
        }

        /**
         * Builder pattern - build function - creates new camera
         * using this instance of {@link CameraBuilder}
         *
         * @return new {@link Camera} object
         */
        public Camera build() {
           return new Camera(this);
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
     *
     * @return first parameter for n*m loop for random ray casting
     */
    public int getN() {
        return n;
    }

    /**
     * getter for m field
     *
     * @return second parameter of n*m loop for random ray casting
     */
    public int getM() {
        return m;
    }

    /**
     * getter for recurseDepth field
     *
     * @return depth of recursion for adaptive anti-aliasing
     */
    public int getRecurseDepth() {
        return recurseDepth;
    }

    /**
     * getter for useDOF field
     *
     * @return whether DOF functionality is used
     */
    public boolean isUseDOF() {
        return useDOF;
    }

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

        //initialize thread progress reporter
        Pixel.initialize(nY, nX, printInterval);

        // for each pixel (i,j) , construct  ray/rays from camera through pixel,
        // functions use rayTracer object to get correct color, then use imageWriter to write pixel to the file

        if (isUseDOF()) {
           renderImageDOF(nX,nY);
        }
        //cast ray according to Anti-Aliasing method set to Camera
        else {
            switch (getAntiAliasing()) {
                // no method used - cast single ray to center of pixel
                case NONE -> {
                    renderImageAntiAliasingNone(nX,nY);
                }
                // bean of random rays cast for each pixel besides the ray towards the center
                case RANDOM -> {
                    renderImageAntiAliasingRandom(nX,nY);
                }
                // four rays cast to four corners of pixel besides the ray towards the center
                case CORNERS -> {
                    renderImageAntiAliasingCorners(nX,nY);
                }
                case ADAPTIVE -> {
                    renderImageAntiAliasingAdaptive(nX,nY);
                }
            }
        }
    }

    /**
     * render image using DOF improvement with support of multi threading
     * @param nX number of rows in the view Plane
     * @param nY number of columns in the view plane
     */
    private void renderImageDOF(int nX,int nY)
    {
        if (threadsCount == 0) {
            for (int i = 0; i < nY; ++i)
                for (int j = 0; j < nX; ++j) {
                    castRayDOF(nX, nY, i, j, n, m);
                    Pixel.pixelDone();
                    Pixel.printPixel();
                }
        } else if (threadsCount == -1) {
            IntStream.range(0, nY).parallel().forEach(i -> IntStream.range(0, nX).parallel().forEach(j -> {
                castRayDOF(nX, nY, i, j, n, m);
                Pixel.pixelDone();
                Pixel.printPixel();
            }));
        } else {
            while (threadsCount-- > 0) {
                new Thread(() -> {
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone())
                        castRayDOF(nX, nY, pixel.col, pixel.row, n, m);
                }).start();
            }
            Pixel.waitToFinish();
        }
    }
    /**
     * render image using No improvement with support of multi threading
     * (the fastest runtime - image has the lowest resolution)
     * @param nX number of rows in the view Plane
     * @param nY number of columns in the view plane
     */
    private void renderImageAntiAliasingNone(int nX, int nY)
    {
        if (threadsCount == 0) {
            for (int i = 0; i < nY; ++i)
                for (int j = 0; j < nX; ++j) {
                    castRay(nX, nY, j, i);
                    Pixel.pixelDone();
                    Pixel.printPixel();
                }
        } else if (threadsCount == -1) {
            IntStream.range(0, nY).parallel().forEach(i -> IntStream.range(0, nX).parallel().forEach(j -> {
                castRay(nX, nY, j, i);
                Pixel.pixelDone();
                Pixel.printPixel();
            }));
        } else {
            while (threadsCount-- > 0) {
                new Thread(() -> {
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone())
                        castRay(nX, nY, pixel.col, pixel.row);
                }).start();
            }
            Pixel.waitToFinish();
        }
    }

    /**
     * render image using Random method Anti aliasing improvement with support of multi threading
     * (the slowest runtime - image has high resolution)
     * @param nX number of rows in the view Plane
     * @param nY number of columns in the view plane
     */
    private void renderImageAntiAliasingRandom(int nX,int nY)
    {
        if (threadsCount == 0) {
            for (int i = 0; i < nY; ++i)
                for (int j = 0; j < nX; ++j) {
                    castRayBeamRandom(nX, nY, i, j, n, m);
                    Pixel.pixelDone();
                    Pixel.printPixel();
                }
        } else if (threadsCount == -1) {
            IntStream.range(0, nY).parallel().forEach(i -> IntStream.range(0, nX).parallel().forEach(j -> {
                castRayBeamRandom(nX, nY, i, j, n, m);
                Pixel.pixelDone();
                Pixel.printPixel();
            }));
        } else {
            while (threadsCount-- > 0) {
                new Thread(() -> {
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone())
                        castRayBeamRandom(nX, nY, pixel.col, pixel.row, n, m);
                }).start();
            }
            Pixel.waitToFinish();
        }

    }

    /**
     * render image using Corner method Anti aliasing improvement with support of multi threading
     * (medium runtime - image has medium resolution)
     * @param nX number of rows in the view Plane
     * @param nY number of columns in the view plane
     */
    private void renderImageAntiAliasingCorners(int nX, int nY)
    {
        if (threadsCount == 0) {
            for (int i = 0; i < nY; ++i)
                for (int j = 0; j < nX; ++j) {
                    castRayBeamCorners(nX, nY, i, j);
                    Pixel.pixelDone();
                    Pixel.printPixel();
                }
        } else if (threadsCount == -1) {
            IntStream.range(0, nY).parallel().forEach(i -> IntStream.range(0, nX).parallel().forEach(j -> {
                castRayBeamCorners(nX, nY, i, j);
                Pixel.pixelDone();
                Pixel.printPixel();
            }));
        } else {
            while (threadsCount-- > 0) {
                new Thread(() -> {
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone())
                        castRayBeamCorners(nX, nY,pixel.col, pixel.row);
                }).start();
            }
            Pixel.waitToFinish();
        }
    }

    /**
     * render image using Adaptive Anti-Aliasing method improvement with support of multi threading
     * ( most optimal runtime for high resolution - image has high resolution)
     * @param nX number of rows in the view Plane
     * @param nY number of columns in the view plane
     */
    private void renderImageAntiAliasingAdaptive(int nX, int nY)
    {
        int depth = getRecurseDepth();
        if (threadsCount == 0) {
            for (int i = 0; i < nY; ++i)
                for (int j = 0; j < nX; ++j) {
                    castRayAdaptive(nX, nY, i, j, 2, depth);
                    Pixel.pixelDone();
                    Pixel.printPixel();
                }
        } else if (threadsCount == -1) {
            IntStream.range(0, nY).parallel().forEach(i -> IntStream.range(0, nX).parallel().forEach(j -> {
                castRayAdaptive(nX, nY, i, j, 2, depth);
                Pixel.pixelDone();
                Pixel.printPixel();
            }));
        } else {
            while (threadsCount-- > 0) {
                new Thread(() -> {
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone())
                        castRayAdaptive(nX, nY, pixel.col, pixel.row, 2, depth);
                }).start();
            }
            Pixel.waitToFinish();
        }
    }

    //endregion

    //region default ray casting

    /**
     * cast a ray from camera through pixel (i,j) in view plane and get color of pixel
     *
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
     *
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

    //region ray casting with Anti-Aliasing using grid

    /**
     * cast a beam of n*m random beams within a grid of a pixel (i,j)
     *
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param j  column index of pixel
     * @param i  row index of pixel
     * @param n  first parameter to set number of random rays to cast
     * @param m  second parameter to set number of rays to cast
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
     *
     * @param Nx  number of rows in view plane
     * @param Ny  number of columns in view plane
     * @param n   first parameter to set number of random rays to cast
     * @param m   second parameter to set number of rays to cast
     * @param ray ray towards the center of the pixel
     * @return list with m*n rays cast randomly within the grid of the pixel
     */
    public List<Ray> constructRayBeam(int Nx, int Ny, int n, int m, Ray ray) {
        // get center point of pixel
        Point Pij = ray.getPoint(distance);
        List<Ray> temp = new LinkedList<>();

        // create a grid of n rows * m columns in each pixel
        // construct a ray from camera to every cell in grid
        // each ray is constructed randomly precisely within the grid borders
        for (int i = -n / 2; i < n / 2; i++)
            for (int j = -m / 2; j < m / 2; j++)
                temp.add(constructRandomRay(Nx, Ny, Pij, i, j, n, m));

        // remove from the list if a  ray was randomly constructed identical to ray to center
        temp.removeIf((item) -> {
            return item.equals(ray);
        });
        // add to list the ray to the center of the pixel
        temp.add(ray);

        return temp;
    }

    /**
     * given a pixel ,cast a ray to a randomly selected point within a cell in a sub-grid made on a pixel
     *
     * @param Nx         number of rows in view plane
     * @param Ny         number of columns in view plane
     * @param Pij        center point of pixel (i,j)
     * @param gridRow    row index of the cell in  the sub-grid of pixel
     * @param gridColumn column index of the cell in  the sub-grid of pixel
     * @param n          number of rows in the grid
     * @param m          number of columns in the grid
     * @return {@link Ray} from camera to randomly selected point
     */
    public Ray constructRandomRay(int Nx, int Ny, Point Pij, int gridRow, int gridColumn, int n, int m) {

        // calculate "size" of each pixel -
        // height per pixel = total "physical" height / number of rows
        // width per pixel = total "physical" width / number of columns
        double Ry = (double) height / Ny;
        double Rx = (double) width / Nx;

        //calculate height and width of a cell from the sub-grid
        double gridHeight = (double) Ry / n;
        double gridWidth = (double) Rx / m;

        Random r = new Random();
        // set a random value to scale vector on Y axis
        // value range is from -(gridHeight/2) to (gridHeight/2)
        double yI = r.nextDouble(gridHeight) - gridHeight / 2;
        // set a random value to scale vector on X axis
        // value range is from -(gridWidth/2) to (gridWidth/2)
        double xJ = r.nextDouble(gridWidth) - gridWidth / 2;

        // if result of xJ is > 0
        // move result point from middle of pixel to column index in sub-grid
        // then add the random value to move left/right on X axis within the cell
        if (!isZero(xJ)) {
            Pij = Pij.add(vRight.scale(gridWidth * gridColumn + xJ));
        }

        // if result of yI is > 0
        // move result point from middle of pixel to row index in sub-grid
        // then add the random value to move up/down on Y axis within the cell
        if (!isZero(yI)) {
            Pij = Pij.add(vUp.scale(gridHeight * gridRow + yI));
        }

        // return ray cast from camera to randomly selected point within grid of pixel
        // reached by yI and xJ scaling factors
        return new Ray(p0, Pij.subtract(p0));

    }
    //endregion

    //region  ray casting with Anti-Aliasing using Adaptive Ray Casting

    /**
     * cast ray through pixel in the view plane, using adaptive Anti-Aliasing
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param j  column index of pixel
     * @param i  row index of pixel
     * @param size size of division of pixel
     * @param depth depth of division to end recursive call
     */
    private void castRayAdaptive(int Nx, int Ny, int j, int i, int size, int depth) {

        // construct ray through pixel
        Ray ray = constructRay(Nx, Ny, j, i);
        Point center = ray.getPoint(distance);
        //construct four rays to the corners of the pixel
        //function returns list with rays sorted from top left corner to bottom left, clockwise.
        var rayBeam = constructRayCorners(Nx, Ny, ray, size);

        // calculate  color of pixel , if the corner color matches the center , add the color,
        // else recursively  calculate the subpixel which corner color does not match
        // ray towards center color
        Color color = rayTracer.traceRay(ray);
        int k = 0; //index of ray in the list
        for (var r : rayBeam) {
            Color cornerColor = rayTracer.traceRay(r);
            if (color.equals(cornerColor))
                color = color.add(cornerColor);
            // corner color does not match
            else {
                // get the center point of the subpixel
                Point subPixelCenter = getSubPixelCenter(k, Nx, Ny, size * 2, center);
                //call recursive function to calculate color of subpixel
                color = color.add(constructRayAdaptiveRec(Nx, Ny, subPixelCenter, r, size * 2, depth));
            }
            k++;
        }
        //get avg color of pixel between all the samples
        color = color.reduce(rayBeam.size() + 1);

        //write pixel
        imageWriter.writePixel(j, i, color);
    }

    /**
     * recursive function to calculate color of a subpixel
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param center center point of subpixel
     * @param rayToCorner ray constructed from camera to one of corners of the pixel (depending on location of subpixel)
     * @param size size of division of pixel at current level of recursion
     * @param depth depth of division to end recursive call
     * @return color of the subpixel
     */
    private Color constructRayAdaptiveRec(int Nx, int Ny, Point center, Ray rayToCorner, int size, int depth) {

        //if division size is smaller or equal to recursion depth limit , continue to calculate subpixel color
        if (size <= depth) {
            //ray from camera to center of subpixel
            Vector camToSubPixel = center.subtract(p0);
            Ray ray = new Ray(p0, camToSubPixel);
            Color color = rayTracer.traceRay(ray);
            //construct four rays to the four corners of the subpixel
            var cornersBeam = constructRayCorners(Nx, Ny, ray, size);

            //if color of the corners matches , ad rhe color, otherwise ,recursively calculate
            //the color of the mismatching corner subpixel

            int k = 0;
            for (var r : cornersBeam) {
                Color cornerColor = rayTracer.traceRay(r);
                if (color.equals(cornerColor))
                    color = color.add(cornerColor);
                else {
                    // get the center point of the sub pixel
                    Point subPixelCenter = getSubPixelCenter(k, Nx, Ny, size * 2, center);
                    // recursively calculate color , doubling division of pixel by two for every level of recursive call
                    color = color.add(constructRayAdaptiveRec(Nx, Ny, subPixelCenter, r, size * 2, depth));
                }
                k++;
            }
            //return the color of the subpixel
            color = color.reduce(cornersBeam.size() + 1);
            return color;

        // recursion depth is reached , return the color of the corner of the subpixel
        } else {
            return rayTracer.traceRay(rayToCorner);
        }
    }

    /**
     * given a pixel and an index get the center point of  subpixel
     * indexes run from top left corner to bottom left, clockwise
     * @param k index of subpixel
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param size size of division of pixel
     * @param center center point of current pixel/subpixel
     * @return center point of the subpixel
     */
    private Point getSubPixelCenter(int k, int Nx, int Ny, int size, Point center) {

        //calculate scaling factors with division size parameter
        double Rx = alignZero(((double) width / Nx) / size);
        double Ry = alignZero(((double) height / Ny) / size);
        Point p = center;

        switch (k) {
            case 0: // top left corner subpixel
                return p.add(vUp.scale(Ry)).add(vRight.scale(Rx));
            case 1: //top right corner subpixel
                return p.add(vUp.scale(-Ry)).add(vRight.scale(Rx));
            case 2: // bottom right corner subpixel
                return p.add(vUp.scale(-Ry)).add(vRight.scale(-Rx));
            case 3: // bottom left corner subpixel
                return p.add(vUp.scale(Ry)).add(vRight.scale(-Rx));
        }
        return null;
    }

    /**
     * cast four rays to the four corners of a pixel(i,j)
     *
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param j  column index of pixel
     * @param i  row index of pixel
     */
    private void castRayBeamCorners(int Nx, int Ny, int j, int i) {
        // construct ray through pixel
        Ray ray = constructRay(Nx, Ny, j, i);
        Point center = ray.getPoint(distance);
        // construct the four rays to the corners
        var rayBeam = constructRayCorners(Nx, Ny, ray, 2);

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
     * given a pixel , construct four rays to the four corners of the pixel , or a subpixel
     *
     * @param Nx  number of rows in view plane
     * @param Ny  number of columns in view plane
     * @param ray ray towards center of the pixel or subpixel
     * @param size size of division of pixel
     * @return list with the four rays
     */
    public List<Ray> constructRayCorners(int Nx, int Ny, Ray ray, int size) {
        //get the point of the center of the pixel
        Point Pij = ray.getPoint(distance);

        // calculate "size" of each pixel -
        // height per pixel = total "physical" height / number of rows
        // width per pixel = total "physical" width / number of columns
        // divide height and width values by size of pixel, size is set to two for original pixel
        // moving from center point to the corners by dividing pixel size to two,
        //for subpixel , the size of division is larger hence the movement from the center is smaller
        // allowing to cast the rays to the corners of the specific subpixel
        double Ry = alignZero((double) height / Ny / size);
        double Rx = alignZero((double) width / Nx / size);
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

    //region ray casting with DOF
    /**
     * cast ray from camera through pixels on view plane using DOF
     * @param Nx number of rows in view plane
     * @param Ny number of columns in view plane
     * @param j  column index of pixel
     * @param i  row index of pixel
     * @param n  first parameter to set number of  rays to cast from aperture
     * @param m  second parameter to set number of rays to cast from aperture
     */
    private void castRayDOF(int Nx, int Ny, int j, int i, int n, int m) {
        // construct ray through pixel
        Ray ray = constructRay(Nx, Ny, j, i);

        // construct n*m random rays towards the pixel
        var apertureBeam = constructGridRaysFromAperture(n, m, ray);
        apertureBeam.add(ray);

        // calculate color of the pixel using the average from all the rays in beam
        Color color = Color.BLACK;
        for (var r : apertureBeam) {
            color = color.add(rayTracer.traceRay(r));
        }
        // reduce final color by total number of rays to get mean value of pixel color
        color = color.reduce(apertureBeam.size());

        //write pixel
        imageWriter.writePixel(j, i, color);
    }

    /**
     * construct n*m rays from a camera through a grid within an aperture around a pixel
     * @param n  first parameter to set number of  rays to cast from aperture
     * @param m  second parameter to set number of rays to cast from aperture
     * @param ray ray from camera towards center of pixel where the aperture is used around
     * @return List of n*m rays constructed from camera through the aperture
     */
    public List<Ray> constructGridRaysFromAperture(int n, int m, Ray ray) {
        List<Ray> result = new LinkedList<>();

        //reach top left corner if aperture
        Point topCorner = p0.add(vRight.scale(-apertureRadius)).add(vUp.scale(apertureRadius));
        //get the focal point
        Point focal = ray.getPoint(dof);
        //get the point of center of the pixel (which is also the center of the aperture)
        Point pixelCenter = ray.getPoint(distance);

        // length of a cell in the subgrid
        double sizeRow = alignZero((apertureRadius * 2) / n);
        // width of a cell in the subgrid
        double sizeCol = alignZero((apertureRadius * 2) / m);


        // loop through entire grid and construct a ray through every cell in subgrid
        // end after constructing n*m rays
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Ray tmp = constructRayFromAperture(i, j, sizeRow, sizeCol, focal, topCorner);
                if (!pixelCenter.equals(tmp.getP0())) {
                    result.add(tmp);
                }
            }
        }
        return result;
    }

    /**
     * construct a ray from  camera through a cell in the subgrid within an aperture around a pixel
     *
     * @param i row index of call in subgrid
     * @param j column index of cell in subgrid
     * @param sizeRow subgrid row length
     * @param sizeCol subgrid column length
     * @param focal focal point
     * @param topCorner top left corner of aperture
     * @return {@link Ray} constructed from camera through aperture
     */
    private Ray constructRayFromAperture(int i, int j, double sizeRow, double sizeCol, Point focal, Point topCorner) {

        Point p = topCorner;
        Vector v;

        // move point from top corner to required cell in subgrid
        if (i != 0) {
            p = p.add(vUp.scale(-sizeRow * i));
        }
        if (j != 0) {
            p = p.add(vRight.scale(sizeCol * j));
        }

        // create vector from camera to the point in the cell
        v = focal.subtract(p);

        //construct ray from camera to the point
        return new Ray(p, v);

    }
    //endregion

    //region move and rotate camera
    /**
     * move a camera to a different position point (angel of camera does not change)
     *
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


    /**
     * Camera constructor by photographer location and target view point<br/>
     * the camera is always horizontal although may be directed a bit up or
     * down.<br/>
     * However, if the camera is directed upwards or downwards, it's top will be with
     * the Z axis
     * @param location camera location
     * @param target   point the camera is directed to
     * @param angle    clockwise rotation angle by vTo axis - in degrees
     * @author Dan Zilbershtein
     */
    public Camera(Point location, Point target, double angle) {
        vTo = target.subtract(location).normalize();
        try {
            vRight = vTo.crossProduct(Vector.Y_AXIS).normalize();
            vUp = vRight.crossProduct(vTo).normalize();
        } catch (IllegalArgumentException e) {
            // vTo is co-lined with Y axis
            vUp = Vector.Z_AXIS;
            vRight = vTo.crossProduct(vUp).normalize();
        }

        if (isZero(angle))
            return;

        double radians = Math.toRadians(angle);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        if (isZero(cos))
            vUp = vRight.scale(sin).normalize();
        else if (isZero(sin))
            vUp = vUp.scale(cos).normalize();
        else
            vUp = vUp.scale(cos).add(vRight.scale(sin)).normalize();
        vRight = vTo.crossProduct(vRight).normalize();
    }

    /**
     * This function set new camera position with rotation
     *
     * @param angle - the angle of rotation (degree)
     * @return the camera after set the new position
     * @author Dan Zilbershtein
     */

     public Camera rotateCamera(double angle) {
     if (angle == 0)
     return this;
     vUp = vUp.vectorRotate(vTo, angle);
     vRight = vTo.crossProduct(vUp).normalize();
     return this;
     }

    /**
     * move camera to new position using origin point and target view point to set direction of camera
     * allows also to rotate the camera
     * @param newPosition new origin position point of camera
     * @param target new target viewing point for direction of camera
     * @param angle angle to rotate camera by
     * @return the camera after it is set o its new position and rotated
     * @author Dan Zilbershtein
     */

     public Camera cameraPosition(Point newPosition, Point target, double angle) {
     p0 = newPosition;
     vTo = target.subtract(newPosition).normalize();
     try {
     vUp = vTo.crossProduct(vRight).normalize();
     vRight = vTo.crossProduct(Vector.Y_AXIS).normalize();

     } catch (IllegalArgumentException e) {
     vUp = Vector.Z_AXIS;
     vRight = Vector.X_AXIS;
     }
     rotateCamera(angle);
     return this;
     }
     //endregion


}