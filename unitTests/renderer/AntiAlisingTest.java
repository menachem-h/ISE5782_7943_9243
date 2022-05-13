package renderer;

import geometries.Geometries;
import geometries.Sphere;
import lighting.AmbientLight;
import lighting.PointLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.*;

public class AntiAlisingTest {



    @Test
    public void advancedBeamTest() {
        Scene scene = new Scene.SceneBuilder("Test Scene")
                .setAmbientLight(new AmbientLight(new Color(black), new Double3(1)))
                .setGeometries(new Geometries( //
                        new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(yellow))))

                .build();
        ImageWriter imageWriter = new ImageWriter("advancedBeamTest1", 120,120);

        Camera camera1 = new Camera.CameraBuilder(new Point(0,0,500), new Vector(0, 0, -1), new Vector(0, -1, 0))
                .setVPDistance(75).setVPSize(20,20)
                .setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene)).build();

        camera1.renderImage();
        camera1.writeToImage();

    }

}
