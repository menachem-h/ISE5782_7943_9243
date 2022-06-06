package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.LightSource;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;
import java.util.LinkedList;
import java.util.List;

import static java.awt.Color.*;

/**
 * todo
 */
public class ChairTest {






    @Test
    public void seatTest() {
        List<LightSource> lights = new LinkedList<>();
        lights.add(new SpotLight(new Color(WHITE), new Point(50, -30, 200), new Vector(-1, 0, -1)).setkL(0.0004).setkQ(0.0000006));
        lights.add(new SpotLight(new Color(WHITE), new Point(-75, 30, 200), new Vector(1, 0, -0.55)).setkL(0.0004).setkQ(0.0000006));
        Scene scene = new Scene.SceneBuilder("Test Scene")
                .setAmbientLight(new AmbientLight(new Color(229, 204, 255), new Double3(.15)))
                .setGeometries(new Geometries(
                        new Chair(new Point(-50, -50, -500), 45d, 100d, 6d, 5d,3d,0.75d,
                                new Vector(0, 0, -1), new Vector(1, 0, 0), new Color(164, 116, 73))
                                .getGeometries()
                         ,new Chair(new Point(50, -50, -500), 45d, 100d, 6d, 5d,3d,0.75d,
                        new Vector(-1, 0, 0), new Vector(0, 0, -1), new Color(164, 116, 73))
                        .getGeometries()

                ))


                .setLights(lights)
                .setBackground(new Color(0, 102d, 102d))
                .build();


        ImageWriter imageWriter = new ImageWriter("TestSeat9withGapAdaptive", 600, 600);
        Camera camera = new Camera.CameraBuilder(new Point(0, 0, 1000), new Vector(0, 0, -0.5), new Vector(0, 1, 0)) //
                .setVPSize(200, 200)
                .setVPDistance(1000)
                .setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene))
                .setAntiAliasing(AntiAliasing.ADAPTIVE).setRecurseDepth(16)
                .build();//

        camera.renderImage(); //
        camera.writeToImage();
    }

    @Test
    public void imageTest() {
        List<LightSource> lights = new LinkedList<>();
        lights.add(new SpotLight(new Color(255, 197, 143), new Point(145, 300, 150), new Vector(-1, -2, 0)).setkL(0.0004).setkQ(0.0000006));
        lights.add(new SpotLight(new Color(255, 197, 143), new Point(-8, 280, 600), new Vector(0.65, -1, 0)).setkL(0.0004).setkQ(0.0000006));
        //lights.add(new SpotLight(new Color(WHITE), new Point(-75, 30, 200), new Vector(1, 0, -0.55)).setkL(0.0004).setkQ(0.0000006));
        Scene scene = new Scene.SceneBuilder("Test Scene")
                .setAmbientLight(new AmbientLight(new Color(229, 204, 255), new Double3(.15)))
                .setGeometries(new Geometries(
                        new Plane(new Point(250,0,0),new Vector(-1,0,0)).setEmission(new Color(GRAY)),
                        new Plane(new Point(-250,0,0),new Vector(1,0,0)).setEmission(new Color(GRAY)),
                       new Plane(new Point(0,0,90),new Vector(0,0,-1)).setEmission(new Color(222,184,135)),
                      new Plane(new Point(0,-50,0),new Vector(0,1,0)).setEmission(new Color(165,42,42)),
                        new Chair(new Point(-95, 0, 600), 45d, 100d, 6d, 5d,3d,0.75d,
                                new Vector(1, 0, 0), new Vector(0, 0, 1), new Color(164, 116, 73))
                                .getGeometries(),
                        new Chair(new Point(78, 0, 600), 45d, 100d, 6d, 5d,3d,0.75d,
                                new Vector(-1, 0, 0), new Vector(0, 0, -1), new Color(164, 116, 73))
                                .getGeometries(),
                        new Chair(new Point(-8, 0, 500), 45d, 100d, 6d, 5d,3d,0.75d,
                                new Vector(0, 0, 1), new Vector(-1, 0, 0), new Color(164, 116, 73))
                                .getGeometries(),
                        new Chair(new Point(-8, 0, 680), 45d, 100d, 6d, 5d,3d,0.75d,
                                new Vector(0, 0, -1), new Vector(1, 0, 0), new Color(164, 116, 73))
                                .getGeometries(),
                        new Table(90,95d,new Color(160,82,45),new Point(-8,-50,600),new Vector(0,1,0),new Vector(0,0,-1)).getElements(),
                        new Sphere(new Point(-8,280,600),45).setEmission(new Color(GRAY)).setMaterial(new Material().setkT(1).setnShininess(50))
                       // new FloorSquare(new Point(-250,-50,90),new Vector(0,0,1),new Vector(1,0,0),new Color(218, 160, 109),new Color(111, 78, 55),1200,500,360,240).elements

                ))


                .setLights(lights)
                .setBackground(new Color(0, 102d, 102d))
                .build();



        // interesting fact when camera is set to position(0,0,1000) an exception is thrown
        ImageWriter imageWriter = new ImageWriter("RoomTestNew", 600, 600);
        Camera camera = new Camera.CameraBuilder(new Point(0, 100, 1500), new Vector(0, -0.1, -1), new Vector(0, 10, -1)) //
                .setVPSize(600, 600)
                .setVPDistance(1000)
                .setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene))
                .setAntiAliasing(AntiAliasing.NONE)
                .setMultithreading(3)
                .setDebugPrint(0.1)
                .build();//

        camera.renderImage(); //
        camera.writeToImage();
    }


}


