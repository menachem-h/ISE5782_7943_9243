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

public class FloorTest {
    public class FloorTriangle {
        Geometries elements;
        Point start;
        Vector len;
        Vector wid;
        Color color1;
        Color color2;
        double length;
        double width;
        int sumLength;
        int sumWidth;

        FloorTriangle(Point start, Vector len, Vector wid, Color color1, Color color2, double length, double width, int sumLength, int sumWidth) {
            elements = new Geometries();
            double triangleLength = length / sumLength;
            double triangleWidth = width / sumWidth;
            Point p1=Point.ZERO;
            Point p2=Point.ZERO;
            Point p3=Point.ZERO;
            for (int i = 0; i < sumWidth; i++) {

                if (i == 0) {
                    p1 = start;

                }
                else {
                    p1 = start.add(wid.scale(triangleWidth *i));

                }
                for (int j = 0; j < sumLength; j++) {
                    if (j == 0) {
                        p2 = p1.add(len.scale(triangleLength));
                        p3 = p1.add(wid.scale(triangleWidth));
                        elements.add(new Triangle(p1, p2, p3).setEmission(color1));
                        elements.add(new Triangle(p1.add(len.scale(triangleLength)),p1.add(wid.scale(triangleWidth))
                                ,p2.add(wid.scale(triangleWidth))).setEmission(color2));
                    }
                    else {
                        p1 = p1.add(len.scale(triangleLength));
                        p2 = p1.add(len.scale(triangleLength));
                        p3 = p1.add(wid.scale(triangleWidth));
                        elements.add(new Triangle(p1, p2, p3).setEmission(color1));
                        elements.add(new Triangle(p1.add(len.scale(triangleLength)),p1.add(wid.scale(triangleWidth))
                                ,p2.add(wid.scale(triangleWidth))).setEmission(color2));
                    }
                }
            }
        }

        public Geometries getElements() {
            return elements;
        }
    }

    public class FloorSquare {
        Geometries elements;
        Point start;
        Vector len;
        Vector wid;
        Color color1;
        Color color2;
        double length;
        double width;
        int sumLength;
        int sumWidth;

        FloorSquare(Point start, Vector len, Vector wid, Color color1, Color color2, double length, double width, int sumLength, int sumWidth) {
            elements = new Geometries();
            double squareLength = length / sumLength;
            double squareWidth = width / sumWidth;
            Point p1=Point.ZERO;
            Point p2=Point.ZERO;
            Point p3=Point.ZERO;
            Point p4=Point.ZERO;
            for (int i = 0; i < sumWidth; i++) {

                if (i == 0) {
                    p1 = start;

                }
                else {
                    p1 = start.add(wid.scale(squareWidth *i));

                }
                for (int j = 0; j < sumLength; j++) {
                    if (j == 0 ) {
                        p2 = p1.add(len.scale(squareLength));
                        p3 = p2.add(wid.scale(squareWidth));
                        p4 = p1.add(wid.scale(squareWidth));
                    }
                    else {
                        p1 = p1.add(len.scale(squareLength));
                        p2 = p1.add(len.scale(squareLength));
                        p3 = p2.add(wid.scale(squareWidth));
                        p4 = p1.add(wid.scale(squareWidth));
                    }
                    if ((i+j)%2==0){
                        elements.add(new Polygon(p1, p2, p3,p4).setEmission(color1));
                    }
                    else{
                        elements.add(new Polygon(p1, p2, p3,p4).setEmission(color2));
                    }
                }
            }
        }

        public Geometries getElements() {
            return elements;
        }
    }

    @Test
    public void advancedBeamTest() {
        List<LightSource> lights = new LinkedList<>();
        lights.add(new SpotLight(new Color(WHITE), new Point(50, -30, 200), new Vector(-1, 0, -1)).setkL(0.0004).setkQ(0.0000006));
        lights.add(new SpotLight(new Color(WHITE), new Point(-75, 30, 200), new Vector(1, 0, -0.55)).setkL(0.0004).setkQ(0.0000006));
        Scene scene = new Scene.SceneBuilder("Test Scene")
                .setAmbientLight(new AmbientLight(new Color(229, 204, 255), new Double3(.15)))
                .setGeometries(new Geometries(
                        new FloorSquare(new Point(100, 10, -100), new Vector(0, 0, 1), new Vector(-1, 0, 0),
                        new Color(250, 0, 0), new Color(0, 200, 0), 200, 200, 20, 10).getElements()))


                .setLights(lights)
                .setBackground(new Color(0, 102d, 102d))
                .build();


        ImageWriter imageWriter = new ImageWriter("FloorSquareTest", 600, 600);
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
