package renderer;

import geometries.Cylinder;
import geometries.Geometries;
import primitives.*;

import static java.awt.Color.GRAY;

public class Table {
    Geometries elements;
    double height;
    double radius;
    Cylinder leg;
    Cylinder surfaceTop;
    Cylinder surfaceBase;
    Cylinder base;
    Cylinder cy;
    Cylinder cy1;
    Point position;
    Color color;

    Material material;

    public Table(int height, double radius, Color color, Point position, Vector dirHeight, Vector dirSurface){
        double radiusMini=radius/15;
        Vector dirMini=dirSurface.scale(radius-radiusMini);
        Point tpSurfaceTop =position.add(dirHeight.scale(height*19/20));
        Point tpSurfaceBase=position.add(dirHeight.scale(height*15/20));
        surfaceTop =(Cylinder) new Cylinder(new Ray(tpSurfaceTop,dirHeight),radius,height/20).setEmission(color);
        surfaceBase=(Cylinder) new Cylinder(new Ray(tpSurfaceBase,dirHeight),radius,height/20).setEmission(color)
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60));

        leg=(Cylinder) new Cylinder(new Ray(position.add(dirHeight.scale(height*2/20)),dirHeight),radius/7,height*13/20)
                .setEmission(color);
        base=(Cylinder) new Cylinder(new Ray(position,dirHeight),radius/3,height*2/20).setEmission(color);
        elements=new Geometries(surfaceTop,leg,base,surfaceBase);
        for (int i = 0; i < 4; i++) {
            double angle = 360 / 4;
            cy1 = (Cylinder) new Cylinder(new Ray(tpSurfaceBase.add(dirMini.vectorRotate(dirHeight, i * angle)), dirHeight), radiusMini, height * 4 / 20)
                    .setEmission(color);
            elements.add(cy1);
        }
    }

    public Geometries getElements() {
        return elements;
    }

    public Table setColorSurfaceTop(Color color) {
        surfaceTop.setEmission(color);
        return this;
    }

    public Table setMaterialSurfaceTop(Material material) {
        surfaceTop.setMaterial(material);
        return this;
    }

    public Table setColorLeg(Color color) {
        leg.setEmission(color);
        return this;
    }

    public Table setMaterialLeg(Material material) {
        leg.setMaterial(material);
        return this;
    }
}
