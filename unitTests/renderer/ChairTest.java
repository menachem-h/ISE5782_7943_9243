package renderer;

import geometries.Cylinder;
import geometries.Geometries;
import geometries.Geometry;
import geometries.Polygon;
import lighting.AmbientLight;
import lighting.LightSource;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;

import static java.awt.Color.WHITE;
import static java.awt.Color.yellow;

/**
 * todo
 */
public class ChairTest {

    /**
     * todo
     */
    public class Chair {

        Cylinder frontLeft;
        Cylinder frontRight;
        Cylinder backLeft;
        Cylinder backRight;
        Cylinder leftBar;
        Cylinder rightBar;
        Polygon seatUp;
        Polygon seatDown;
        Polygon seatSideFr;
        Polygon seatSideleft;
        Polygon seatSideBck;
        Polygon seatSideRight;
        Polygon backrestFr;
        Polygon backrestBck;
        Polygon backrestLft;
        Polygon backrestRt;
        Polygon backrestTop;


        public Chair(Point p, double seatLength, double height, double seatWidth, double backWidth,
                     double legRadius, double barRadius,Vector forward, Vector right, Color color) {

            double cornerScale = seatLength / 2;
            double heightScale = height / 2;
            Vector down = forward.crossProduct(right).normalize();
            Vector up = down.scale(-1);
            Vector downScale = down.scale(-seatWidth);
            Point centerDown = p.add(downScale);
            Point frleftUp = p.add(right.scale(-cornerScale).add(forward.scale(cornerScale)));
            Point frRightUp = p.add(right.scale(cornerScale).add(forward.scale(cornerScale)));
            Point bckLeftUp = p.add(right.scale(-cornerScale).add(forward.scale(-cornerScale)));
            Point bckRightUp = p.add(right.scale(cornerScale).add(forward.scale(-cornerScale)));
            Point frleftDwn = frleftUp.add(downScale);
            Point frRightDwn = frRightUp.add(downScale);
            Point bckLeftDwn = bckLeftUp.add(downScale);
            Point bckRightDwn = bckRightUp.add(downScale);
            Point bRestFrLft = bckLeftUp.add(forward.scale(backWidth));
            Point bRestFrRt = bckRightUp.add(forward.scale(backWidth));
            Point bRestFrLftUp = bRestFrLft.add(up.scale(heightScale));
            Point bRestFrRtUp = bRestFrRt.add(up.scale(heightScale));
            Point bRestBckLftUp = bckLeftUp.add(up.scale(heightScale));
            Point bRestBckRtUp = bckRightUp.add(up.scale(heightScale));
            seatUp = (Polygon) new Polygon(bckLeftUp, frleftUp, frRightUp, bckRightUp).setEmission(color);
            seatDown = (Polygon) new Polygon(bckLeftDwn, frleftDwn, frRightDwn, bckRightDwn).setEmission(color);
            seatSideFr = (Polygon) new Polygon(frleftDwn, frleftUp, frRightUp, frRightDwn).setEmission(color);
            seatSideleft = (Polygon) new Polygon(frleftDwn, frleftUp, bckLeftUp, bckLeftDwn).setEmission(color);
            seatSideBck = (Polygon) new Polygon(frleftUp, frleftDwn, frRightDwn, frRightUp).setEmission(color);
            seatSideRight = (Polygon) new Polygon(bckRightDwn, bckRightUp, frRightUp, frRightDwn).setEmission(color);
            Vector tmp = centerDown.subtract(bckLeftDwn).normalize();
            backLeft = (Cylinder) new Cylinder(new Ray(bckLeftDwn.add(down.scale(heightScale)).add(tmp.scale(legRadius)), up), legRadius, heightScale-seatWidth)
                    .setEmission(color);
            tmp = centerDown.subtract(bckRightDwn).normalize();
            backRight = (Cylinder) new Cylinder(new Ray(bckRightDwn.add(down.scale(heightScale)).add(tmp.scale(legRadius)), up), legRadius, heightScale-seatWidth)
                    .setEmission(color);
            tmp = centerDown.subtract(frRightDwn).normalize();
            frontRight = (Cylinder) new Cylinder(new Ray(frRightDwn.add(down.scale(heightScale)).add(tmp.scale(legRadius)), up), legRadius, heightScale-seatWidth)
                    .setEmission(color);
            tmp = centerDown.subtract(frleftDwn).normalize();
            frontLeft = (Cylinder) new Cylinder(new Ray(frleftDwn.add(down.scale(heightScale)).add(tmp.scale(legRadius)), up), legRadius, heightScale-seatWidth)
                    .setEmission(color);
            double distance = backLeft.getAxisRay().getP0().distance(frontLeft.getAxisRay().getP0())-legRadius*2;
            leftBar = (Cylinder) new Cylinder(new Ray(bckLeftDwn.add(down.scale(heightScale/ 2)).add(forward.scale(legRadius)), forward), barRadius, distance)
                    .setEmission(color);
            rightBar = (Cylinder) new Cylinder(new Ray(bckRightDwn.add(down.scale(heightScale / 2)).add(forward.scale(legRadius)), forward), barRadius,distance)
                    .setEmission(color);
            backrestLft = (Polygon) new Polygon(bRestFrLft, bRestFrLftUp, bRestBckLftUp, bckLeftUp).setEmission(color);
            backrestBck = (Polygon) new Polygon(bckLeftUp, bRestBckLftUp, bRestBckRtUp, bckRightUp).setEmission(color);
            backrestRt = (Polygon) new Polygon(bRestFrRt, bRestFrRtUp, bRestBckRtUp, bckRightUp).setEmission(color);
            backrestFr = (Polygon) new Polygon(bRestFrLft, bRestFrRt, bRestFrRtUp, bRestFrLftUp).setEmission(color);
            backrestTop = (Polygon) new Polygon(bRestBckLftUp, bRestFrLftUp, bRestFrRtUp, bRestBckRtUp).setEmission(color);


        }

        public Geometries getGeometries() {
            return new Geometries(seatUp, seatDown, seatSideFr, seatSideleft, seatSideBck, seatSideRight,
                    backLeft, backRight, frontLeft, frontRight, leftBar, rightBar
                    , backrestLft, backrestBck, backrestRt, backrestFr, backrestTop);
        }

        //region setters for chair seat
        public Chair setSeatEmission(Color color) {
            seatUp = (Polygon) seatUp.setEmission(color);
            seatDown = (Polygon) seatDown.setEmission(color);
            seatSideFr = (Polygon) seatSideFr.setEmission(color);
            seatSideleft = (Polygon) seatSideleft.setEmission(color);
            seatSideBck = (Polygon) seatSideBck.setEmission(color);
            seatSideRight = (Polygon) seatSideRight.setEmission(color);
            return this;

        }

        public Chair setSeatMaterial(Material mt) {
            seatUp = (Polygon) seatUp.setMaterial(mt);
            seatDown = (Polygon) seatDown.setMaterial(mt);
            seatSideFr = (Polygon) seatSideFr.setMaterial(mt);
            seatSideleft = (Polygon) seatSideleft.setMaterial(mt);
            seatSideBck = (Polygon) seatSideBck.setMaterial(mt);
            seatSideRight = (Polygon) seatSideRight.setMaterial(mt);
            return this;
        }

        public Chair setSeatKs(double ks) {
            Material mt = seatUp.getMaterial();
            mt = mt.setkS(ks);
            return setSeatMaterial(mt);
        }

        public Chair setSeatKd(double kd) {
            Material mt = seatUp.getMaterial();
            mt = mt.setkD(kd);
            return setSeatMaterial(mt);
        }

        public Chair setSeatKr(double kr) {
            Material mt = seatUp.getMaterial();
            mt = mt.setkR(kr);
            return setLegsMaterial(mt);
        }

        public Chair setSeatKt(double kt) {
            Material mt = seatUp.getMaterial();
            mt = mt.setkT(kt);
            return setSeatMaterial(mt);
        }

        public Chair setSeatShinines(int nshinines) {
            Material mt = frontLeft.getMaterial();
            mt = mt.setnShininess(nshinines);
            return setSeatMaterial(mt);
        }
        //endregion

        //region setters for chair backrest
        public Chair setBackRestEmission(Color color) {
            backrestFr = (Polygon) backrestFr.setEmission(color);
            backrestBck = (Polygon) backrestBck.setEmission(color);
            backrestLft = (Polygon) backrestLft.setEmission(color);
            backrestRt = (Polygon) backrestRt.setEmission(color);
            backrestTop = (Polygon) backrestTop.setEmission(color);
            return this;

        }

        public Chair setBackRestMaterial(Material mt) {
            backrestFr = (Polygon) backrestFr.setMaterial(mt);
            backrestBck = (Polygon) backrestBck.setMaterial(mt);
            backrestLft = (Polygon) backrestLft.setMaterial(mt);
            backrestRt = (Polygon) backrestRt.setMaterial(mt);
            backrestTop = (Polygon) backrestTop.setMaterial(mt);
            return this;
        }

        public Chair setBackRestKs(double ks) {
            Material mt = backrestFr.getMaterial();
            mt = mt.setkS(ks);
            return setBackRestMaterial(mt);
        }

        public Chair setBackRestKd(double kd) {
            Material mt = backrestFr.getMaterial();
            mt = mt.setkD(kd);
            return setBackRestMaterial(mt);
        }

        public Chair setBackRestKr(double kr) {
            Material mt = backrestFr.getMaterial();
            mt = mt.setkR(kr);
            return setBackRestMaterial(mt);
        }

        public Chair setBackRestKt(double kt) {
            Material mt = backrestFr.getMaterial();
            mt = mt.setkT(kt);
            return setBackRestMaterial(mt);
        }

        public Chair setBackRestShinines(int nshinines) {
            Material mt = backrestFr.getMaterial();
            mt = mt.setnShininess(nshinines);
            return setBackRestMaterial(mt);
        }
        //endregion

        //region setters for chair legs

        public Chair setLegsEmission(Color color) {
            frontLeft = (Cylinder) frontLeft.setEmission(color);
            frontRight = (Cylinder) frontRight.setEmission(color);
            backLeft = (Cylinder) backLeft.setEmission(color);
            backRight = (Cylinder) backRight.setEmission(color);
            return this;
        }

        public Chair setLegsMaterial(Material mt) {
            frontLeft = (Cylinder) frontLeft.setMaterial(mt);
            frontRight = (Cylinder) frontRight.setMaterial(mt);
            backLeft = (Cylinder) backLeft.setMaterial(mt);
            backRight = (Cylinder) backRight.setMaterial(mt);
            return this;
        }

        public Chair setLegsKs(double ks) {
            Material mt = frontLeft.getMaterial();
            mt = mt.setkS(ks);
            return setLegsMaterial(mt);
        }

        public Chair setLegsKd(double kd) {
            Material mt = frontLeft.getMaterial();
            mt = mt.setkD(kd);
            return setLegsMaterial(mt);
        }

        public Chair setLegsKr(double kr) {
            Material mt = frontLeft.getMaterial();
            mt = mt.setkR(kr);
            return setLegsMaterial(mt);
        }

        public Chair setLegsKt(double kt) {
            Material mt = frontLeft.getMaterial();
            mt = mt.setkT(kt);
            return setLegsMaterial(mt);
        }

        public Chair setLegsShinines(int nshinines) {
            Material mt = frontLeft.getMaterial();
            mt = mt.setnShininess(nshinines);
            return setLegsMaterial(mt);
        }
        //endregion

        //region setters for chair leg bars
        public Chair setBarsEmission(Color color) {
            leftBar = (Cylinder) leftBar.setEmission(color);
            rightBar = (Cylinder) rightBar.setEmission(color);
            return this;
        }

        public Chair setBarsMaterial(Material mt) {
            leftBar = (Cylinder) leftBar.setMaterial(mt);
            rightBar = (Cylinder) rightBar.setMaterial(mt);
            return this;
        }

        public Chair setBarsKs(double ks) {
            Material mt = leftBar.getMaterial();
            mt = mt.setkS(ks);
            return setBarsMaterial(mt);
        }

        public Chair setBarsKd(double kd) {
            Material mt = leftBar.getMaterial();
            mt = mt.setkD(kd);
            return setBarsMaterial(mt);
        }

        public Chair setBarsKr(double kr) {
            Material mt = leftBar.getMaterial();
            mt = mt.setkR(kr);
            return setBarsMaterial(mt);
        }

        public Chair setBarsKt(double kt) {
            Material mt = leftBar.getMaterial();
            mt = mt.setkT(kt);
            return setBarsMaterial(mt);
        }

        public Chair setBarsShinines(int nshinines) {
            Material mt = leftBar.getMaterial();
            mt = mt.setnShininess(nshinines);
            return setBarsMaterial(mt);
        }
        //endregion


    }

    /**
     * Produce a simple picture of a sphere - basic test
     * image improvement - anti aliasing random beam method
     */
    @Test
    public void TestSeat() {


    }

    @Test
    public void seatTest() {
        List<LightSource> lights = new LinkedList<>();
        lights.add(new SpotLight(new Color(WHITE), new Point(50, -30, 200), new Vector(-1, 0, -1)).setkL(0.0004).setkQ(0.0000006));
        lights.add(new SpotLight(new Color(WHITE), new Point(-75, 30, 200), new Vector(1, 0, -0.55)).setkL(0.0004).setkQ(0.0000006));
        Scene scene = new Scene.SceneBuilder("Test Scene")
                .setAmbientLight(new AmbientLight(new Color(229, 204, 255), new Double3(.15)))
                .setGeometries(new Geometries(
                        new Chair(new Point(-50, 0, -20), 45d, 100d, 6d, 5d,3d,0.75d,
                                new Vector(1, 0, 0), new Vector(0, -1, 0), new Color(164, 116, 73))
                                .setLegsEmission(new Color(0,235,35))
                                .setSeatEmission(new Color(132,143,202))
                                .setBackRestEmission(new Color(255,76,33))
                                .getGeometries()
                        , new Chair(new Point(50, 0, -20), 45d, 100d, 6d, 5d,3d,0.75d,
                        new Vector(-1, 0, 0), new Vector(0, 1, 0), new Color(164, 116, 73))
                        .setLegsEmission(new Color(0,235,35))
                        .setSeatEmission(new Color(132,143,202))
                        .setBackRestEmission(new Color(255,76,33))
                        .getGeometries()
                        , new Polygon(new Point(-250, -90, -70), new Point(0, 50, -60), new Point(200, -90, -70))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60))))


                .setLights(lights)
                .setBackground(new Color(0, 102d, 102d))
                .build();


        ImageWriter imageWriter = new ImageWriter("TestSeat7_multiColor", 600, 600);
        Camera camera = new Camera.CameraBuilder(new Point(0, -1200, 100), new Vector(0, 1, -0.1), new Vector(0, 0.1, 1)) //
                .setVPSize(200, 200)
                .setVPDistance(1000)
                .setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene))
                .setAntiAliasing(AntiAliasing.RANDOM).setN(9).setM(9)
                .build();//
        camera.renderImage(); //
        camera.writeToImage();
    }
}


