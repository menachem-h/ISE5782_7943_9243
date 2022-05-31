package renderer;

import geometries.Cylinder;
import geometries.Geometries;
import geometries.Polygon;
import primitives.*;

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
    Polygon backrestBottom;
    Cylinder backrestLftPeg;
    Cylinder backrestRtPeg;



    public Chair(Point p, double seatLength, double height, double seatWidth, double backWidth,
                 double legRadius, double barRadius, Vector forward, Vector right, Color color) {

        double cornerScale = seatLength / 2;
        double backrestSize = (height / 2)-seatWidth;
        double heightScale =backrestSize*( (double)2/3);
        double backrestScale = heightScale/3;
        double legHeight = height/2;
        double pegRadius = backWidth/2;
        Vector down = forward.crossProduct(right).normalize();
        Vector up = down.scale(-1);
        Vector downScale = down.scale(seatWidth);
        Point centerDown = p.add(downScale);
        Point frleftUp = p.add(right.scale(-cornerScale).add(forward.scale(cornerScale)));
        Point frRightUp = p.add(right.scale(cornerScale).add(forward.scale(cornerScale)));
        Point bckLeftUp = p.add(right.scale(-cornerScale).add(forward.scale(-cornerScale)));
        Point bckRightUp = p.add(right.scale(cornerScale).add(forward.scale(-cornerScale)));
        Point frleftDwn = frleftUp.add(downScale);
        Point frRightDwn = frRightUp.add(downScale);
        Point bckLeftDwn = bckLeftUp.add(downScale);
        Point bckRightDwn = bckRightUp.add(downScale);
        Point bRestFrLft = bckLeftUp.add(forward.scale(backWidth)).add(up.scale(backrestScale));
        Point bRestFrRt = bckRightUp.add(forward.scale(backWidth)).add(up.scale(backrestScale));
        Point bRestBckLft = bckLeftUp.add(up.scale(backrestScale));
        Point bRestBckRt = bckRightUp.add(up.scale(backrestScale));
        Point bRestFrLftUp = bRestFrLft.add(up.scale(heightScale));
        Point bRestFrRtUp = bRestFrRt.add(up.scale(heightScale));
        Point bRestBckLftUp = bRestBckLft.add(up.scale(heightScale));
        Point bRestBckRtUp = bRestBckRt.add(up.scale(heightScale));
        seatUp = (Polygon) new Polygon(bckLeftUp, frleftUp, frRightUp, bckRightUp).setEmission(color);
        seatDown = (Polygon) new Polygon(bckLeftDwn, frleftDwn, frRightDwn, bckRightDwn).setEmission(color);
        seatSideFr = (Polygon) new Polygon(frleftDwn, frleftUp, frRightUp, frRightDwn).setEmission(color);
        seatSideleft = (Polygon) new Polygon(frleftDwn, frleftUp, bckLeftUp, bckLeftDwn).setEmission(color);
        seatSideBck = (Polygon) new Polygon(frleftUp, frleftDwn, frRightDwn, frRightUp).setEmission(color);
        seatSideRight = (Polygon) new Polygon(bckRightDwn, bckRightUp, frRightUp, frRightDwn).setEmission(color);

        backLeft = (Cylinder) new Cylinder(new Ray(bckLeftDwn.add(forward.scale(legRadius).add(right.scale(legRadius))), down), legRadius, legHeight)
                .setEmission(color);
        backRight = (Cylinder) new Cylinder(new Ray(bckRightDwn.add(forward.scale(legRadius)).add(right.scale(-legRadius)),down ), legRadius, legHeight)
                .setEmission(color);
        frontRight = (Cylinder) new Cylinder(new Ray(frRightDwn.add(forward.scale(-legRadius)).add(right.scale(-legRadius)), down), legRadius, legHeight)
                .setEmission(color);
        frontLeft = (Cylinder) new Cylinder(new Ray(frleftDwn.add(forward.scale(-legRadius)).add(right.scale(legRadius)), down), legRadius, legHeight)
                .setEmission(color);
        double distance = backLeft.getAxisRay().getP0().distance(frontLeft.getAxisRay().getP0())-legRadius*2;
        leftBar = (Cylinder) new Cylinder(new Ray(bckLeftDwn.add(down.scale(heightScale/ 2)).add(forward.scale(legRadius*2).add(right.scale(legRadius))), forward), barRadius, distance)
                .setEmission(color);
        rightBar = (Cylinder) new Cylinder(new Ray(bckRightDwn.add(down.scale(heightScale / 2)).add(forward.scale(legRadius*2).add(right.scale(-legRadius))), forward), barRadius,distance)
                .setEmission(color);
        backrestLft = (Polygon) new Polygon(bRestFrLft, bRestFrLftUp, bRestBckLftUp, bRestBckLft).setEmission(color);
        backrestBck = (Polygon) new Polygon(bRestBckLft, bRestBckLftUp, bRestBckRtUp, bRestBckRt).setEmission(color);
        backrestRt = (Polygon) new Polygon(bRestFrRt, bRestFrRtUp, bRestBckRtUp, bRestBckRt).setEmission(color);
        backrestFr = (Polygon) new Polygon(bRestFrLft, bRestFrRt, bRestFrRtUp, bRestFrLftUp).setEmission(color);
        backrestTop = (Polygon) new Polygon(bRestBckLftUp, bRestFrLftUp, bRestFrRtUp, bRestBckRtUp).setEmission(color);
        backrestBottom = (Polygon) new Polygon(bRestBckLft,bRestFrLft,bRestFrRt,bRestBckRt).setEmission(color);
        backrestLftPeg = (Cylinder) new Cylinder(new Ray(bckLeftUp.add(forward.scale(pegRadius).add(right.scale(pegRadius))), up), pegRadius, backrestScale)
                .setEmission(color);
        backrestRtPeg = (Cylinder) new Cylinder(new Ray(bckRightUp.add(forward.scale(pegRadius).add(right.scale(-pegRadius))), up), pegRadius, backrestScale)
                .setEmission(color);

    }

    public Geometries getGeometries() {
        return new Geometries(seatUp, seatDown, seatSideFr, seatSideleft, seatSideBck, seatSideRight,
                backLeft, backRight, frontLeft, frontRight, leftBar, rightBar
                , backrestLft, backrestBck, backrestRt, backrestFr, backrestTop,backrestLftPeg,backrestRtPeg);
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
