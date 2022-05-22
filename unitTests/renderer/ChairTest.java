package renderer;

import geometries.Cylinder;
import geometries.Geometries;
import geometries.Polygon;
import lighting.AmbientLight;
import lighting.LightSource;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;

import static java.awt.Color.WHITE;

public class ChairTest {

    public class Chair {

        Geometries elements;


        public Chair(Point p, double seatLength, double height, double seatWidth, double backWidth,Vector forward,Vector right,  Color color) {
            Cylinder frontLeft;
            Cylinder frontRight;
            Cylinder backLeft;
            Cylinder backRight;
            Cylinder leftBar;
            Cylinder rightBar;
            Polygon seatUp;
            Polygon seatDown;
            Polygon seatSideFr;
            Polygon seatSideleft;
            Polygon seatSideBck;
            Polygon seatSideRight;
            Polygon backFr;
            Polygon backBck;
            Polygon backLft;
            Polygon backRt;

            Vector zAxis = new Vector(0, 0, 1);
            double cornerScale = seatLength / 2;
            double heightScale = height / 2;
            Vector down = forward.crossProduct(right).normalize();
            Vector up = down.scale(-1);
            Point centerDown=p.add(down.scale(-seatWidth));
            Point frleftUp = p.add(right.scale(-cornerScale).add(forward.scale(cornerScale)));
            Point frRightUp = p.add(right.scale(cornerScale).add(forward.scale(cornerScale)));
            Point bckLeftUp = p.add(right.scale(-cornerScale).add(forward.scale(-cornerScale)));
            Point bckRightUp = p.add(right.scale(cornerScale).add(forward.scale(-cornerScale)));
            Point frleftDwn = frleftUp.add(down.scale(-seatWidth));
            Point frRightDwn = frRightUp.add(down.scale(-seatWidth));
            Point bckLeftDwn = bckLeftUp.add(down.scale(-seatWidth));
            Point bckRightDwn = bckRightUp.add(down.scale(-seatWidth));
            seatUp = (Polygon) new Polygon(bckLeftUp, frleftUp, frRightUp, bckRightUp).setEmission(color);
            seatDown = (Polygon) new Polygon(bckLeftDwn, frleftDwn, frRightDwn, bckRightDwn).setEmission(color);
            seatSideFr = (Polygon) new Polygon(frleftDwn, frleftUp, frRightUp, frRightDwn).setEmission(color);
            seatSideleft = (Polygon) new Polygon(frleftDwn, frleftUp, bckLeftUp, bckLeftDwn).setEmission(color);
            seatSideBck = (Polygon) new Polygon(frleftUp, frleftDwn, frRightDwn, frRightUp).setEmission(color);
            seatSideRight = (Polygon) new Polygon(bckRightDwn, bckRightUp, frRightUp, frRightDwn).setEmission(color);
            Vector tmp = centerDown.subtract(bckLeftDwn).normalize();
            backLeft = (Cylinder) new Cylinder(new Ray(bckLeftDwn.add(down.scale(height/2)).add(tmp.scale(3)),up),3,height/2).setEmission(color);
            tmp = centerDown.subtract(bckRightDwn).normalize();
            backRight = (Cylinder) new Cylinder(new Ray(bckRightDwn.add(down.scale(height/2)).add(tmp.scale(3)),up),3,height/2).setEmission(color);
            tmp = centerDown.subtract(frRightDwn).normalize();
            frontRight = (Cylinder) new Cylinder(new Ray(frRightDwn.add(down.scale(height/2)).add(tmp.scale(3)),up),3,height/2).setEmission(color);
            tmp = centerDown.subtract(frleftDwn).normalize();
            frontLeft = (Cylinder) new Cylinder(new Ray(frleftDwn.add(down.scale(height/2)).add(tmp.scale(3)),up),3,height/2).setEmission(color);
            leftBar = (Cylinder) new Cylinder(new Ray(bckLeftDwn.add(down.scale(height/2/2)),forward),3,seatLength).setEmission(color) ;
            rightBar = (Cylinder) new Cylinder(new Ray(bckRightDwn.add(down.scale(height/2/2)),forward),3,seatLength).setEmission(color) ;
            elements = new Geometries(seatUp, seatDown, seatSideFr, seatSideleft, seatSideBck, seatSideRight,backLeft,backRight,frontLeft,frontRight,leftBar,rightBar);

        }

        public Geometries getElements() {
            return elements;
        }


    }

    /**
     * Produce a simple picture of a sphere - basic test
     *  image improvement - anti aliasing random beam method
     */
    @Test
    public void TestSeat() {


    }

    @Test
    public void seatTest(){
        List<LightSource> lights = new LinkedList<>();
        lights.add(new SpotLight(new Color(WHITE),new Point(50,-30,200),new Vector(-1,0,-1)).setkL(0.0004).setkQ(0.0000006));
        lights.add(new SpotLight(new Color(WHITE),new Point(-75,30,200),new Vector(1,0,-0.55)).setkL(0.0004).setkQ(0.0000006));
        Scene scene = new Scene.SceneBuilder("Test Scene")
                .setAmbientLight(new AmbientLight(new Color(229,204,255), new Double3(.15)))
                .setGeometries(new Geometries(
                        new Chair(new Point(0,0,5),45,100,6,100,
                                new Vector(-1,1,0),new Vector(1,1,0), new Color(164,116,73)).getElements()
                        , new Polygon(new Point(-250,-90,-70),new Point(0,50,-60),new Point(200,-90,-70))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60))))


                .setLights(lights)
                .setBackground(new Color(0,102d,102d))
                .build();



        ImageWriter imageWriter = new ImageWriter("TestSeat2", 600, 600);
        Camera camera = new Camera.CameraBuilder(new Point(0, -1200, 100), new Vector(0, 1, -0.1), new Vector(0, 0.1,1 )) //
                .setVPSize(200, 200)
                .setVPDistance(1000)
                .setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene))
                .setAntiAliasing(AntiAliasing.NONE)
                .build();//
        camera.renderImage(); //
        camera.writeToImage();
    }
}


