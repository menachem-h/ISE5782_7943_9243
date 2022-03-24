package renderer;

import primitives.*;

import static primitives.Util.isZero;


public class Camera {

    private  Point _p0;
    private  Vector _vTo;
    private  Vector _vUp;
    private  Vector _vRight;

    private double _distance;

    private int _width;
    private int _height;

    public Camera(Point p0, Vector vTo, Vector vUp) {
        _p0 = p0;

        //vto and vup mus be orthogonal
        if(!isZero(vTo.dotProduct(vUp))){
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
    public Camera setVPDistance(double distance) {
        _distance = distance;
        return this;
    }

    public Camera setVPSize(int width , int height) {
        _width =width;
        _height = height;
        return this;
    }

    public Ray constructRay(int Nx, int Ny, int j, int i) {
        //Image center  :
        Point Pc = _p0.add(_vTo.scale(_distance));

        //Ratio (pixel width & height)
        double Ry = (double)  _height / Ny;
        double Rx = (double)  _width / Nx;

        Point Pij = Pc;

        double yI = -(i - (Ny -1)/2d)* Ry;
        double xJ = (j - (Nx -1)/2d)* Rx;

        // move to middle of pixel i,j

        if(! isZero(xJ)){
            Pij= Pij.add(_vRight.scale(xJ));
        }

        if(! isZero(yI)){
            Pij= Pij.add(_vUp.scale(yI));
        }

        //return ray from camera to viewplane coordinates (i, j)
        return  new Ray(_p0, Pij.subtract(_p0));
    }
}
