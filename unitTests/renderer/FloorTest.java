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

import static java.awt.Color.WHITE;

/**
 * test for floor class , both square and triangle
 */
public class FloorTest {




    @Test
    public void floorTest() {
        List<LightSource> lights = new LinkedList<>();
        lights.add(new SpotLight(new Color(WHITE), new Point(50, -30, 200), new Vector(-1, 0, -1)).setkL(0.0004).setkQ(0.0000006));
        lights.add(new SpotLight(new Color(WHITE), new Point(-75, 30, 200), new Vector(1, 0, -0.55)).setkL(0.0004).setkQ(0.0000006));
        Scene scene = new Scene.SceneBuilder("Test Scene")
                .setAmbientLight(new AmbientLight(new Color(229, 204, 255), new Double3(.15)))
                .setGeometries(new Geometries(
                        new FloorSquare(new Point(100, 10, -100), new Vector(0, 0, 1), new Vector(-1, 0, 0),
                        new Color(176,131,78), new Color(129,102,70), 100, 100, 10, 10,new Material()).getElements()))
                .setLights(lights)
                .setBackground(new Color(0, 102d, 102d))
                .build();


        ImageWriter imageWriter = new ImageWriter("FloorSquareTest6", 600, 600);
        Camera camera = new Camera.CameraBuilder(new Point(0, -1200, 100), new Vector(0, 1, -0.1), new Vector(0, 0.1, 1)) //
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
