package renderer;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;

import static java.awt.Color.*;

public class TableTest {

    public class Table{

        Geometries elements;
        double height;
        double radius;
        Cylinder leg;
        Cylinder surface;
        Cylinder base;
        Point position;
        Color color;

        Material material;

        public Table(int height, double radius, Color color, Point position, Vector dir){

            surface =(Cylinder) new Cylinder(new Ray(position.add(dir.scale(height*19/20)),dir),radius,height/20).setEmission(color);
            Cylinder cy=new Cylinder(new Ray(position.add(dir.scale(height*17/20)),dir),radius,height/20);
            leg=(Cylinder) new Cylinder(new Ray(position.add(dir.scale(height*2/20)),dir),radius/10,height*19/20)
                    .setEmission(color);
            base=(Cylinder) new Cylinder(new Ray(position,dir),radius/3,height*2/20).setEmission(color);
            elements=new Geometries(surface,leg,base,cy);
        }

        public Geometries getElements() {
            return elements;
        }

        public Table setColorSurface(Color color) {
            surface.setEmission(color);
            return this;
        }

        public Table setMaterialSurface(Material material) {
            surface.setMaterial(material);
            return this;
        }

        public Table setColorLeg(Color color) {
            leg.setEmission(color);
            return this;
        }

        public Table setMaterialLeg(Material material) {
            leg.setMaterial(material);
            return this;
        }
    }

    @Test
    public void advancedBeamTest() {
        List<LightSource> lights = new LinkedList<>();
        lights.add(new SpotLight(new Color(WHITE),new Point(50,-30,200),new Vector(-1,0,-1)).setkL(0.0004).setkQ(0.0000006));
        lights.add(new SpotLight(new Color(WHITE),new Point(-75,30,200),new Vector(1,0,-0.55)).setkL(0.0004).setkQ(0.0000006));
        Scene scene = new Scene.SceneBuilder("Test Scene")
                .setAmbientLight(new AmbientLight(new Color(229,204,255), new Double3(.15)))
                .setGeometries(new Geometries(
                        new Sphere(new Point(-15,-10,-10),25d).setEmission(new Color(200,50,0))
                                .setMaterial(new Material().setkS(0.25).setkD(0.25).setnShininess(80).setkT(0.5)),
                        new Polygon(new Point(-250,-90,-70),new Point(0,50,-60),new Point(200,-90,-70))
                                .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)),
                        new Polygon(new Point(-150,90,-65),new Point(-150,90,50),new Point(150,160,50),new Point(150,160,-65))
                                .setEmission(new Color(20, 20, 20)) //
                                .setMaterial(new Material().setkR(0.45)),
                        new Sphere(new Point(-6,2,-2),7).setMaterial(new Material().setkS(0.25).setkD(0.25)).setEmission(new Color(0,255,0)),


                        new Cylinder(new Ray(new Point(75,-8,-88),new Vector(-0.2,-0.3,1)),10d,130d)
                                .setEmission(new Color(102,0,204))
                                .setMaterial(new Material().setkS(0.35).setkD(0.25).setkT(0.2).setkR(0).setnShininess(10)),
                        new Table(60,50,new Color(184,46,179),new Point(0,0,-60),new Vector(0,0,1)).getElements()))

                .setLights(lights)
                .setBackground(new Color(0,102d,102d))
                .build();



        ImageWriter imageWriter = new ImageWriter("TableTest", 600, 600);
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
