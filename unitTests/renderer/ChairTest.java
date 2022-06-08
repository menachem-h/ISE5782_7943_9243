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
 * test for Chair class
 */
public class ChairTest {


    @Test
    public void chairTest() {
        List<LightSource> lights = new LinkedList<>();
        lights.add(new SpotLight(new Color(WHITE), new Point(50, -30, 200), new Vector(-1, 0, -1)).setkL(0.0004).setkQ(0.0000006));
        lights.add(new SpotLight(new Color(WHITE), new Point(-75, 30, 200), new Vector(1, 0, -0.55)).setkL(0.0004).setkQ(0.0000006));
        Scene scene = new Scene.SceneBuilder("Test Scene")
                .setAmbientLight(new AmbientLight(new Color(229, 204, 255), new Double3(.15)))
                .setGeometries(new Geometries(
                        new Chair(new Point(-50, -50, -500), 45d, 100d, 6d, 5d, 3d, 0.75d,
                                new Vector(0, 0, -1), new Vector(1, 0, 0), new Color(164, 116, 73),true)
                                .getGeometries()
                        , new Chair(new Point(50, -50, -500), 45d, 100d, 6d, 5d, 3d, 0.75d,
                        new Vector(-1, 0, 0), new Vector(0, 0, -1), new Color(164, 116, 73),false)
                        .getGeometries()

                ))


                .setLights(lights)
                .setBackground(new Color(0, 102d, 102d))
                .build();


        ImageWriter imageWriter = new ImageWriter("TestSeat9Flipped", 600, 600);
        Camera camera = new Camera.CameraBuilder(new Point(0, 0, 1000), new Vector(0, 0, -0.5), new Vector(0, 1, 0)) //
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


