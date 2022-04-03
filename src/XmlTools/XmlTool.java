package XmlTools;

import geometries.Geometries;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import primitives.Color;
import primitives.Double3;
import primitives.Point;
import scene.Scene;

import javax.swing.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class XmlTool {
    String userName = System.getProperty("user.name");
    private  final String FILENAME = "C:/Users/"+userName+"/IdeaProjects/ISE5782_7943_9243/src/XmlTools/basicRenderTestTwoColors.xml";
    Scene scene;
    Document doc;

    public XmlTool() {

        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            doc = db.parse(new File(FILENAME));

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public Scene createSceneFromXml(String name) {

        // get background color details from xml file
        var _scene = doc.getDocumentElement();
        String color = _scene.getAttribute("background-color");
        List<String> colors = Arrays.stream((color.split("\\s"))).collect(Collectors.toList());
        Color background = new Color(
                Double.valueOf(colors.get(0)),
                Double.valueOf(colors.get(1)),
                Double.valueOf(colors.get(2)));

        // get ambient color details from xml file
        var listAmbient = _scene.getElementsByTagName("ambient-light");
        var ambient = listAmbient.item(0);
        color = ((Element) ambient).getAttribute("color");
        colors = Arrays.stream((color.split("\\s"))).collect(Collectors.toList());
        AmbientLight ambientColor = new AmbientLight(
                new Color(Double.valueOf(colors.get(0)),
                        Double.valueOf(colors.get(1)),
                        Double.valueOf(colors.get(2))), new Double3(1d, 1d, 1d));

        // get geometries details from xml file
        Geometries geomeLst = new Geometries();
        var geometryNode = _scene.getElementsByTagName("geometries");
        var geometries = geometryNode.item(0).getChildNodes();
        List<String> strLst;
        for (int i = 0; i < geometries.getLength(); i++) {
            var node = geometries.item(i);
            if (node.hasAttributes()) {
                String attribute = node.getNodeName();
                var element1 = (Element) node;
                switch (attribute) {
                    case "triangle":
                        String pointStr = element1.getAttribute("p0");
                        strLst = Arrays.stream(pointStr.split("\\s")).collect(Collectors.toList());
                        Point p0 = new Point(
                                Double.valueOf(strLst.get(0)),
                                Double.valueOf(strLst.get(1)),
                                Double.valueOf(strLst.get(2))
                        );
                        pointStr = element1.getAttribute("p1");
                        strLst = Arrays.stream(pointStr.split("\\s")).collect(Collectors.toList());
                        Point p1 = new Point(
                                Double.valueOf(strLst.get(0)),
                                Double.valueOf(strLst.get(1)),
                                Double.valueOf(strLst.get(2))
                        );
                        pointStr = element1.getAttribute("p2");
                        strLst = Arrays.stream(pointStr.split("\\s")).collect(Collectors.toList());
                        Point p2 = new Point(
                                Double.valueOf(strLst.get(0)),
                                Double.valueOf(strLst.get(1)),
                                Double.valueOf(strLst.get(2))
                        );
                        Triangle triangle1 = new Triangle(p0, p1, p2);
                        geomeLst.add(triangle1);
                        break;
                    case "sphere":
                        String center1 = element1.getAttribute("center");
                        strLst = Arrays.stream(center1.split("\\s")).collect(Collectors.toList());
                        Point point1 = new Point(
                                Double.valueOf(strLst.get(0)),
                                Double.valueOf(strLst.get(1)),
                                Double.valueOf(strLst.get(2))
                        );
                        String radius1 = element1.getAttribute("radius");
                        Sphere mySphere1 = new Sphere(point1, Double.valueOf(radius1));
                        geomeLst.add(mySphere1);
                        break;
                    default:
                        break;

                }

            }

        }
        return new Scene.SceneBuilder(name).setAmbientLight(ambientColor).setBackground(background).setGeometries(geomeLst).build();
    }
}
