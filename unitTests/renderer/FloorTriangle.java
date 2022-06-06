package renderer;

import geometries.Geometries;
import geometries.Triangle;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

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