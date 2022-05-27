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
        Cylinder surfaceTop;
        Cylinder surfaceBase;
        Cylinder base;
        Cylinder cy;
        Cylinder cy1;
        Point position;
        Color color;

        Material material;

        public Table(int height, double radius, Color color, Point position, Vector dirHeight,Vector dirSurface){
           double radiusMini=radius/15;
           Vector dirMini=dirSurface.scale(radius-radiusMini);
            Point tpSurfaceTop =position.add(dirHeight.scale(height*19/20));
            Point tpSurfaceBase=position.add(dirHeight.scale(height*15/20));
            surfaceTop =(Cylinder) new Cylinder(new Ray(tpSurfaceTop,dirHeight),radius,height/20).setEmission(color);
            surfaceBase=(Cylinder) new Cylinder(new Ray(tpSurfaceBase,dirHeight),radius,height/20).setEmission(new Color(GRAY))
                    .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60));

            leg=(Cylinder) new Cylinder(new Ray(position.add(dirHeight.scale(height*2/20)),dirHeight),radius/7,height*13/20)
                    .setEmission(color);
            base=(Cylinder) new Cylinder(new Ray(position,dirHeight),radius/3,height*2/20).setEmission(color);
            elements=new Geometries(surfaceTop,leg,base,surfaceBase);
            for (int i = 0; i < 16; i++) {
                double angle = 360 / 16;
                cy1 = (Cylinder) new Cylinder(new Ray(tpSurfaceBase.add(dirMini.vectorRotate(dirHeight, i * angle)), dirHeight), radiusMini, height * 4 / 20)
                        .setEmission(new Color(255, 90, 0));
                elements.add(cy1);
            }
        }

        public Geometries getElements() {
            return elements;
        }

        public Table setColorSurfaceTop(Color color) {
            surfaceTop.setEmission(color);
            return this;
        }

        public Table setMaterialSurfaceTop(Material material) {
            surfaceTop.setMaterial(material);
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
        lights.add(new SpotLight(new Color(WHITE),new Point(100,0,50),new Vector(-1,0,0.25)).setkL(0.0004).setkQ(0.0000006));

        Scene scene = new Scene.SceneBuilder("Test Scene")
                .setAmbientLight(new AmbientLight(new Color(229,204,255), new Double3(.15)))
                .setGeometries(new Geometries(
                        new Polygon(new Point(-250,-90,-70),new Point(0,50,-60),new Point(200,-90,-70))
                                .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)),
                        new Polygon(new Point(-150,90,-65),new Point(-150,90,50),new Point(150,160,50),new Point(150,160,-65))
                                .setEmission(new Color(20, 20, 20)) //
                                .setMaterial(new Material().setkR(0.45)),
                        new Table(60,50,new Color(184,46,179),new Point(0,0,-60),new Vector(0,0,1),new Vector(1,0,0)).getElements()))

                .setLights(lights)
                .setBackground(new Color(0,102d,102d))
                .build();



        ImageWriter imageWriter = new ImageWriter("TableTest3", 600, 600);
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
