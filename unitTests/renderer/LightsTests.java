package renderer;

import org.junit.jupiter.api.Test;

import lighting.*;
import geometries.*;
import primitives.*;
import renderer.*;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;

import static java.awt.Color.*;

/**
 * Test rendering a basic image
 * 
 * @author Dan
 */
public class LightsTests {
	private Scene scene1;
	private Scene scene2;
	Point camPosition=new Point(0, 0, 1000);
	Vector camVto=new Vector(0, 0, -1);
	Vector camVup=new Vector(0, 1, 0);
	int widthVP1=150;
	int heightVP1=150;
	double distance =1000d;
	int widthVP2=200;
	int heightVP2=200;

	String name ="Test scene";

	AmbientLight ambientLight=new AmbientLight(new Color(WHITE), new Double3(0.15));

	private Camera camera1;

	private Camera camera2 ;

	private Point[] p = { // The Triangles' vertices:
			new Point(-110, -110, -150), // the shared left-bottom
			new Point(95, 100, -150), // the shared right-top
			new Point(110, -110, -150), // the right-bottom
			new Point(-75, 78, 100) }; // the left-top
	private Point trPL = new Point(30, 10, -100); // Triangles test Position of Light
	private Point spPL = new Point(-50, -50, 25); // Sphere test Position of Light
	private Color trCL = new Color(800, 500, 250); // Triangles test Color of Light
	private Color spCL = new Color(800, 500, 0); // Sphere test Color of Light
	private Vector trDL = new Vector(-2, -2, -2); // Triangles test Direction of Light
	private Material material = new Material().setkD(0.5).setkS(0.5).setnShininess(300);
	private Geometry triangle1 = new Triangle(p[0], p[1], p[2]).setMaterial(material);
	private Geometry triangle2 = new Triangle(p[0], p[1], p[3]).setMaterial(material);
	private Geometry sphere = new Sphere(new Point(0, 0, -50), 50d) //
			.setEmission(new Color(BLUE).reduce(2)) //
			.setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(300));
	private List<LightSource> lights= new LinkedList<>();

	/**
	 * Produce a picture of a sphere lighted by a directional light
	 */
	@Test
	public void sphereDirectional() {
		lights.add(new DirectionalLight(spCL, new Vector(1, 1, -0.5)));
		scene1=new Scene.SceneBuilder(name).setGeometries(new Geometries(sphere)).
				setLights(lights).build();

		ImageWriter imageWriter = new ImageWriter("lightSphereDirectional", 500, 500);
		camera1=new Camera.CameraBuilder(camPosition,camVto,camVup).setVPDistance(distance).setVPSize(widthVP1,heightVP1)
				.setImageWriter(imageWriter)
				.setRayTracer(new RayTracerBasic(scene1)).build(); //
		camera1.renderImage();
		camera1.writeToImage();
	}



	/**
	 * Produce a picture of a sphere lighted by a point light
	 */
	@Test
	public void spherePoint() {
		lights.clear();
		lights.add(new PointLight(spCL, spPL).setkL(0.001).setkQ(0.0002));
		scene1=new Scene.SceneBuilder(name).setGeometries(new Geometries(sphere)).
				setLights(lights).build();

		ImageWriter imageWriter = new ImageWriter("lightSpherePoint", 500, 500);
		camera1=new Camera.CameraBuilder(camPosition,camVto,camVup).setVPDistance(distance).setVPSize(widthVP1,heightVP1)
				.setImageWriter(imageWriter)
				.setRayTracer(new RayTracerBasic(scene1)).build(); //
		camera1.renderImage();
		camera1.writeToImage();

	}

	/**
	 * Produce a picture of a sphere lighted by a spotlight
	 */
	@Test
	public void sphereSpot() {
		lights.clear();
		lights.add(new SpotLight(spCL, spPL, new Vector(1, 1, -0.5)).setkL(0.001).setkQ(0.0001));
		scene1=new Scene.SceneBuilder(name).setGeometries(new Geometries(sphere)).
				setLights(lights).build();


		ImageWriter imageWriter = new ImageWriter("lightSphereSpot", 500, 500);
		camera1=new Camera.CameraBuilder(camPosition,camVto,camVup).setVPDistance(distance).setVPSize(widthVP1,heightVP1)
				.setImageWriter(imageWriter)
				.setRayTracer(new RayTracerBasic(scene1)).build(); //
		camera1.renderImage();
		camera1.writeToImage();
	}

	/**
	 * Produce a picture of two triangles lighted by a directional light
	 */
	@Test
	public void trianglesDirectional() {
		lights.clear();
		lights.add(new DirectionalLight(trCL, trDL));
		scene2=new Scene.SceneBuilder(name).setAmbientLight(ambientLight).setGeometries(new Geometries(triangle1, triangle2)).
				setLights(lights).build();


		ImageWriter imageWriter = new ImageWriter("lightTrianglesDirectional", 500, 500);
		camera2=new Camera.CameraBuilder(camPosition,camVto,camVup).setVPDistance(distance).setVPSize(widthVP2,heightVP2)
				.setImageWriter(imageWriter)
				.setRayTracer(new RayTracerBasic(scene2)).build(); //
		camera2.renderImage();
		camera2.writeToImage();

	}

	/**
	 * Produce a picture of two triangles lighted by a point light
	 */
	@Test
	public void trianglesPoint() {
		lights.clear();
		lights.add(new PointLight(trCL, trPL).setkL(0.001).setkQ(0.0002));
		scene2=new Scene.SceneBuilder(name).setAmbientLight(ambientLight).setGeometries(new Geometries(triangle1, triangle2)).
				setLights(lights).build();

		ImageWriter imageWriter = new ImageWriter("lightTrianglesPoint", 500, 500);
		camera2=new Camera.CameraBuilder(camPosition,camVto,camVup).setVPDistance(distance).setVPSize(widthVP2,heightVP2)
				.setImageWriter(imageWriter)
				.setRayTracer(new RayTracerBasic(scene2)).build(); //
		camera2.renderImage();
		camera2.writeToImage();

	}

	/**
	 * Produce a picture of two triangles lighted by a spotlight
	 */
	@Test
	public void trianglesSpot() {
		lights.clear();
		lights.add(new SpotLight(trCL, trPL, trDL).setkL(0.001).setkQ(0.0001));
		scene2=new Scene.SceneBuilder(name).setAmbientLight(ambientLight).setGeometries(new Geometries(triangle1, triangle2)).
				setLights(lights).build();

		ImageWriter imageWriter = new ImageWriter("lightTrianglesSpot", 500, 500);
		camera2=new Camera.CameraBuilder(camPosition,camVto,camVup).setVPDistance(distance).setVPSize(widthVP2,heightVP2)
				.setImageWriter(imageWriter)
				.setRayTracer(new RayTracerBasic(scene2)).build(); //
		camera2.renderImage();
		camera2.writeToImage();

	}


	/**
	 * Produce a picture of a sphere lighted by a narrow spotlight
	 */
	/*
	@Test
	public void sphereSpotSharp() {
		scene1.geometries.add(sphere);
		scene1.lights
				.add(new SpotLight(spCL, spPL, new Vector(1, 1, -0.5)).setNarrowBeam(10).setKl(0.001).setKq(0.00004));

		ImageWriter imageWriter = new ImageWriter("lightSphereSpotSharp", 500, 500);
		camera1.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene1)) //
				.renderImage() //
				.writeToImage(); //
	}

	/**
	 * Produce a picture of a two triangles lighted by a narrow spot light
	 */
	/*
	@Test
	public void trianglesSpotSharp() {
		scene2.geometries.add(triangle1, triangle2);
		scene2.lights.add(new SpotLight(trCL, trPL, trDL).setNarrowBeam(10).setKl(0.001).setKq(0.00004));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesSpotSharp", 500, 500);
		camera2.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene2)) //
				.renderImage() //
				.writeToImage(); //
	}
	*/

}
