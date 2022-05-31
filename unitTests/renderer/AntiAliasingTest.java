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

import static java.awt.Color.*;

/**
 * test picture improvements using various Anti-Aliasing methods
 */
public class AntiAliasingTest {



    /**
     * Produce a simple picture of a sphere - basic test
     *  image improvement - anti aliasing random beam method
     */
    @Test
    public void antiAliasingBasicTest() {
        Scene scene = new Scene.SceneBuilder("Test Scene")
                .setAmbientLight(new AmbientLight(new Color(black), new Double3(1)))
                .setGeometries(new Geometries( //
                        new Sphere(new Point(0, 0, -50), 250d).setEmission(new Color(yellow))))

                .build();
        ImageWriter imageWriter = new ImageWriter("advancedBeamTestRandom", 400,400);

        Camera camera1 = new Camera.CameraBuilder(new Point(0,0,500), new Vector(0, 0, -1), new Vector(0, -1, 0))
                .setVPDistance(75).setVPSize(200,200)
                .setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene))
                .setAntiAliasing(AntiAliasing.RANDOM).setN(33).setM(33)
                .build();

        camera1.renderImage();
        camera1.writeToImage();

    }
    /**
     * Produce a picture of two spheres and a cylinder - includes all effects - advanced test
     *  image improvement - anti aliasing random beam method
     */
    @Test
    public void antiAliasingRandomTest(){
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
                                .setMaterial(new Material().setkS(0.35).setkD(0.25).setkT(0.2).setkR(0).setnShininess(10))))

                .setLights(lights)
                .setBackground(new Color(0,102d,102d))
                .build();



        ImageWriter imageWriter = new ImageWriter("AntiAliasRANDOM_9x9", 600, 600);
        Camera camera = new Camera.CameraBuilder(new Point(0, -1200, 100), new Vector(0, 1, -0.1), new Vector(0, 0.1,1 )) //
                .setVPSize(200, 200)
                .setVPDistance(1000)
                .setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene))
                .setAntiAliasing(AntiAliasing.RANDOM).setN(9).setM(9)
                .build();//
        camera.renderImage(); //
        camera.writeToImage();
    }

    /**
     * Produce a picture of two spheres and a cylinder - includes all effects
     * transparency and reflectiveness - image improvement - anti aliasing random beam method
     */
    @Test
    public void antiAliasingCornersTest(){
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
                                .setMaterial(new Material().setkS(0.35).setkD(0.25).setkT(0.2).setkR(0).setnShininess(10))))

                .setLights(lights)
                .setBackground(new Color(0,102d,102d))
                .build();



        ImageWriter imageWriter = new ImageWriter("AntiAliasingReflectionCORNERS", 600, 600);
        Camera camera = new Camera.CameraBuilder(new Point(0, -1200, 100), new Vector(0, 1, -0.1), new Vector(0, 0.1,1 )) //
                .setVPSize(200, 200)
                .setVPDistance(1000)
                .setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene))
                .setAntiAliasing(AntiAliasing.CORNERS)
                .build();//
        camera.renderImage(); //
        camera.writeToImage();
    }

    /**
     * Produce a picture of two spheres and a cylinder - includes all effects
     * transparency and reflectiveness - image improvement - anti aliasing random beam method
     */
    @Test
    public void antiAliasingAdaptiveTest(){
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
                                .setMaterial(new Material().setkS(0.35).setkD(0.25).setkT(0.2).setkR(0).setnShininess(10))))

                .setLights(lights)
                .setBackground(new Color(0,102d,102d))
                .build();



        ImageWriter imageWriter = new ImageWriter("AntiAliasingAdaptive16", 1400, 1400);
        Camera camera = new Camera.CameraBuilder(new Point(0, -1200, 100), new Vector(0, 1, -0.1), new Vector(0, 0.1,1 )) //
                .setVPSize(200, 200)
                .setVPDistance(1000)
                .setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene))
                .setAntiAliasing(AntiAliasing.ADAPTIVE).setRecurseDepth(16)
                .build();//
        camera.renderImage(); //
        camera.writeToImage();
    }

    /**
     * Produce a picture of two spheres and a cylinder - includes all effects
     * transparency and reflectiveness - image improvement - anti aliasing random beam method
     */
    @Test
    public void antiAliasingCornersTestSoft(){
        List<LightSource> lights = new LinkedList<>();
        lights.add(new SpotLight(new Color(WHITE),new Point(50,-30,200),new Vector(-1,0,-1)).setkL(0.0004).setkQ(0.0000006).setRadius(15d));
        lights.add(new SpotLight(new Color(WHITE),new Point(-75,30,200),new Vector(1,0,-0.55)).setkL(0.0004).setkQ(0.0000006).setRadius(15d));
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
                                .setMaterial(new Material().setkS(0.35).setkD(0.25).setkT(0.2).setkR(0).setnShininess(10))))

                .setLights(lights)
                .setBackground(new Color(0,102d,102d))
                .build();



        ImageWriter imageWriter = new ImageWriter("AntiAliasingReflectionRandom_soft_9*9", 600, 600);
        Camera camera = new Camera.CameraBuilder(new Point(0, -1200, 100), new Vector(0, 1, -0.1), new Vector(0, 0.1,1 )) //
                .setVPSize(200, 200)
                .setVPDistance(1000)
                .setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene).setSoftShadow(true))
                .setAntiAliasing(AntiAliasing.RANDOM).setM(9).setN(9)
                .build();//
        camera.renderImage(); //
        camera.writeToImage();
    }

}
