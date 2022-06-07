package renderer;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.*;

import java.util.LinkedList;
import java.util.List;

import static java.awt.Color.*;

public class ProjectTest {


    @Test
    public void imageTest() {

        List<LightSource> lights = new LinkedList<>();
        lights.add(new SpotLight(new Color(255, 197, 143), new Point(145, 300, 150), new Vector(-1, -2, 0)).setkL(0.0004).setkQ(0.0000006));
        lights.add(new SpotLight(new Color(255, 197, 143), new Point(-8, 230, 625), new Vector(0, -1, -1)).setkL(0.0004).setkQ(0.0000006));


        Plane rightWall = (Plane) new Plane(new Point(200, 0, 0), new Vector(-1, 0, 0)).setEmission(new Color(GRAY));
        Plane leftWall = (Plane) new Plane(new Point(-200, 0, 0), new Vector(1, 0, 0)).setEmission(new Color(GRAY));
        Plane solidFloor = (Plane)new Plane(new Point(0,-50,0), new Vector(0,1,0)).setEmission(new Color(132,133,98));
        Polygon backWallTop = (Polygon) new Polygon(new Point(-250, 250, 200), new Point(-250, 450, 200), new Point(250, 450, 200), new Point(250, 250, 200))
                .setEmission(new Color(222, 184, 135));
        Polygon backWallBottom = (Polygon) new Polygon(new Point(-250, -50, 200), new Point(-250, 50, 200), new Point(250, 50, 200), new Point(250, -50, 200))
                .setEmission(new Color(222, 184, 135));
        Polygon backWallLeft = (Polygon) new Polygon(new Point(-250, 50, 200), new Point(-250, 250, 200), new Point(-150, 250, 200), new Point(-150, 50, 200))
                .setEmission(new Color(222, 184, 135));
        Polygon backWallRight = (Polygon) new Polygon(new Point(150, 50, 200), new Point(150, 250, 200), new Point(250, 250, 200), new Point(250, 50, 200))
                .setEmission(new Color(222, 184, 135));


        FloorSquare floor = new FloorSquare(new Point(-250, -50, 200), new Vector(0, 0, 1), new Vector(1, 0, 0), new Color(218, 160, 109), new Color(111, 78, 55), 1200, 500, 36, 30);

        Polygon window = (Polygon) new Polygon(new Point(-150, 50, 200), new Point(-150, 250, 200), new Point(150, 250, 200), new Point(150, 50, 200))
                .setMaterial(new Material().setkT(0.78).setkD(0.22));
        Plane sky = (Plane) new Plane(new Point(-5000, -500, 190), new Vector(0, 0, -1))
                .setEmission(new Color(201, 226, 255)).setMaterial(new Material().setnShininess(10).setkS(0.2));
        Chair leftChair = new Chair(new Point(-95, 0, 800), 45d, 100d, 6d, 5d, 3d, 0.75d,
                new Vector(1, 0, 0), new Vector(0, 0, 1), new Color(186,164,138))
                .setSeatMaterial(new Material().setkD(0.8))
                .setSeatCoverEmission(new Color(108,122,134)).setBrCoverEmission(new Color(108,122,134));

        Chair rightChair = new Chair(new Point(78, 0, 800), 45d, 100d, 6d, 5d, 3d, 0.75d,
                new Vector(-1, 0, 0), new Vector(0, 0, -1),  new Color(186,164,138))
                .setSeatMaterial(new Material().setkD(0.8))
                .setSeatCoverEmission(new Color(108,122,134)).setBrCoverEmission(new Color(108,122,134));
        Chair backChair = new Chair(new Point(-8, 0, 700), 45d, 100d, 6d, 5d, 3d, 0.75d,
                new Vector(0, 0, 1), new Vector(-1, 0, 0),  new Color(186,164,138))
                .setSeatMaterial(new Material().setkD(0.8))
                .setSeatCoverEmission(new Color(108,122,134)).setBrCoverEmission(new Color(108,122,134));
        Chair frontChair = new Chair(new Point(-8, 0, 880), 45d, 100d, 6d, 5d, 3d, 0.75d,
                new Vector(0, 0, -1), new Vector(1, 0, 0), new Color(186,164,138))
                .setSeatMaterial(new Material().setkD(0.8))
                .setSeatCoverEmission(new Color(108,122,134)).setBrCoverEmission(new Color(108,122,134));
        Table table = new Table(75, 95d, new Color(160, 82, 45), new Point(-8, -50, 800), new Vector(0, 1, 0), new Vector(0, 0, -1))
                .setMaterialSurfaceTop(new Material().setkS(0.4).setkD(0.1).setkR(0.15));
        Sphere centerLight = (Sphere) new Sphere(new Point(-8, 230, 625), 45).setEmission(new Color(GRAY)).setMaterial(new Material().setkT(1).setnShininess(50));


        Scene scene = new Scene.SceneBuilder("Test Scene")
                .setAmbientLight(new AmbientLight(new Color(229, 204, 255), new Double3(.15)))
                .setGeometries(new Geometries(rightWall,
                        leftWall,
                        backWallTop,
                        backWallBottom,
                        backWallLeft,
                        backWallRight,
                        //solidFloor,
                        floor.elements,
                        window,
                        sky,
                        leftChair.getGeometries(),
                        rightChair.getGeometries(),
                        frontChair.getGeometries(),
                        backChair.getGeometries(),
                        table.getElements(),
                        centerLight ))
                .setLights(lights)
                .setBackground(new Color(0, 102d, 102d))
                .build();


        // interesting fact when camera is set to position(0,0,1000) an exception is thrown
        ImageWriter imageWriter = new ImageWriter("ProjectTest", 600, 600);
        Camera camera = new Camera.CameraBuilder(new Point(0, 120, 1300), new Vector(0, -0.15, -1), new Vector(0, (double)20/3, -1)) //
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