package renderer;

import geometries.Cylinder;
import geometries.Geometries;
import geometries.Polygon;
import geometries.Sphere;
import lighting.AmbientLight;
import lighting.LightSource;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;

import static java.awt.Color.WHITE;

public class DepthOfFieldTest {
    /**
     * Produce a picture of two spheres and a cylinder - includes all effects
     * transparency and reflectiveness - image improvement - anti aliasing random beam method
     */
    @Test
    public void depthOfFieldTest(){
        List<LightSource> lights = new LinkedList<>();
        lights.add(new SpotLight(new Color(WHITE),new Point(50,-30,200),new Vector(-1,0,-1)).setkL(0.0004).setkQ(0.0000006));
        lights.add(new SpotLight(new Color(WHITE),new Point(-75,30,200),new Vector(1,0,-0.55)).setkL(0.0004).setkQ(0.0000006));
        Scene scene = new Scene.SceneBuilder("Test Scene")
                .setAmbientLight(new AmbientLight(new Color(229,204,255), new Double3(.15)))
                .setGeometries(new Geometries(
                        new Sphere(new Point(-15,-50,-10),25d).setEmission(new Color(200,50,0))
                                .setMaterial(new Material().setkS(0.25).setkD(0.25).setnShininess(80).setkT(0.5)),
                       new Sphere(new Point(10,150,60),35d).setEmission(new Color(126,113,43))
                               .setMaterial(new Material().setkS(0.25).setkD(0.25).setnShininess(80).setkT(0.5))))



                .setLights(lights)
                .setBackground(new Color(0,102d,102d))
                .build();



        ImageWriter imageWriter = new ImageWriter("DepthOfFieldTest", 600, 600);
        Camera camera = new Camera.CameraBuilder(new Point(0, -1200, 100), new Vector(0, 1, -0.1), new Vector(0, 0.1,1 )) //
                .setVPSize(200, 200)
                .setVPDistance(1000)
                .setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene))
                .setApertureRadius(10).setDof(1200).setUseDOF(true).setN(9).setM(9)
                .build();//
        camera.renderImage(); //
        camera.writeToImage();
    }
}
