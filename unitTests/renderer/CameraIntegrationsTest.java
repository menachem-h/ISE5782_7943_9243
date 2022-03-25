package renderer;


import org.junit.jupiter.api.Test;
import primitives.*;
import geometries.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CameraIntegrationsTest {

    private int countCameraGeomtryIntersection(Camera cam,Geometry geomtry){
        List<Point> result=new LinkedList<>();
        Point pc=cam.getP0().add(cam.getvTo().scale(cam.getDistance()));
        return 0;
    }

    @Test
    void CameraSphereTest(){

    }
}
