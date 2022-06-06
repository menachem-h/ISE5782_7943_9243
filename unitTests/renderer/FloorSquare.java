package renderer;

import geometries.Geometries;
import geometries.Polygon;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

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
        Point p1;
        Point p2;
        Point p3;
        Point p4;
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
