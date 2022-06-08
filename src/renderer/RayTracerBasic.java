package renderer;

import geometries.Geometry;
import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

import static java.lang.Math.*;
import static primitives.Util.alignZero;

/**
 * class implements rayTracer abstract class
 */
public class RayTracerBasic extends RayTracer {


    /**
     * recursion tree level for color calculation
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    /**
     * stopping condition for recursive color calculation
     */
    private static final double MIN_CALC_COLOR_K = 0.001;

    /**
     * default coefficient for color calculations
     */
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * determine if to use soft shadow functionality
     */
    private boolean softShadow = false;

    /**
     * constructor
     *
     * @param scene {@link Scene} for {@link Color} calculations to be executed on
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * getter for soft shadow field
     * @return true  if soft shadow functionality is used, otherwise, false
     */
    public boolean isSoftShadow() {
        return softShadow;
    }

    /**
     * setter for soft shadow field
     * @param softShadow boolean value to determine if to use soft shadow functionality
     * @return this instance of the ray tracer
     */
    public RayTracerBasic setSoftShadow(boolean softShadow) {
        this.softShadow = softShadow;
        return this;
    }

    /**
     * calculate a {@link  Color} of a pixel (i,j)
     *
     * @param ray ray constructed from camera through the pixel in view plane
     * @return {@link  Color} of the pixel
     */
    @Override
    public Color traceRay(Ray ray) {

        // find the closest intersection point
        GeoPoint closestPoint = findClosestIntersection(ray);
        // if no intersection point was found , return basic background color of scene
        if (closestPoint == null)
            return scene.getBackground();

        // intersection was found, calculate color of the of pixel.
        return calcColor(closestPoint, ray);
    }

    /**
     * calculate the color of a pixel
     *
     * @param gp  the {@link GeoPoint} viewed through the pixel to calculate color of
     * @param ray ray of camera through pixel in view plane where the point is located
     * @return color of the pixel
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        return calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.getAmbientLight().getIntensity());

    }


    /**
     * calculate color of geometric shape at a given point (phong model)
     *
     * @param p   {@link GeoPoint} to calculate color at
     * @param ray {@link  Ray} from camera to the point
     * @return {@link Color} of the shape at the point
     */
    private Color calcColor(GeoPoint p, Ray ray, int level, Double3 k) {
        Geometry geometry = p.geometry;
        // l = vector from light source to the point
        // n = normal vector to shape at point
        // r = specular vector to vector from light to point
        // v = ray from camera to point
        //Ip  = Ka * Ia + Ie + (Kd * |l.dorProduct(n)|) * Il + (Ks * max(0 ,(-v).dotProduct(r)) ** nShinines) * Il

        // local effects (basic color)
        Color color =
                // Ie
                geometry.getEmission()
                        // Kd * |l.dorProduct(n)| * +Ks * max(0 ,(-v).dotProduct(r)) ** nShinines * Il
                        .add(calcLocalEffects(p, ray, k));

        // global effects (reflection and transparency)
        if (level == 1)
            return color;
        else
            return color.add(calcGlobalEffects(p, ray, level, k));

    }

    /**
     * calculate the transparency and reflectiveness of a point
     *
     * @param gp    {@link GeoPoint} to calculate effects at
     * @param ray   light direction ray
     * @param level level of recursion
     * @param k     coefficient of transparency and reflectiveness
     * @return {@link Color} of point with transparency and reflectiveness
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        //initial color
        Color color = Color.BLACK;
        // normal vector to geometry at the point
        Vector normal = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();
        Double3 kR = material.kR;
        Double3 kT = material.kT;
        Vector direction = ray.getDir();

        // if material's kR multiplied by coefficient is larger than end condition of recursion
        // calculate reflectiveness effect.
        Double3 kKr = kR.scale(k);
        if (kKr.greaterThan(MIN_CALC_COLOR_K)) {
            //construct a ray reflected to light ray
            Ray reflectedRay = constructReflectedRay(gp.point, normal, direction);
            // if reflective ray intersects a geometry calculate the new intersection color and add to total
            // color calculation
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
            if (reflectedPoint != null) {
                color = color.add(calcColor(reflectedPoint, reflectedRay, level - 1, kKr)
                        .scale(kR));
            }
        }

        // if material's kT multiplied by coefficient is larger than end condition of recursion
        // calculate transparency effect.
        Double3 kKt = kT.scale(k);
        if (kKt.greaterThan(MIN_CALC_COLOR_K)) {
            //construct a ray refracted to light ray
            Ray refractedRay = constructRefractedRay(gp.point, normal, direction);
            // if refractive ray intersects a geometry calculate the new intersection color and add to total
            // color calculation
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            if (refractedPoint != null) {
                color = color.add(calcColor(refractedPoint, refractedRay, level - 1, kKt))
                        .scale(kT);
            }
        }
        //return the color calculated with transparency and reflectiveness effects
        return color;
    }

    /**
     * given a ray , construct a new ray which is reflective to original ray
     *
     * @param p {@link Point} that original ray intersects
     * @param N normal {@link Vector} to geometry at the point
     * @param l light direction {@link Vector} (the original ray)
     * @return reflective {@link Ray}
     */
    private Ray constructReflectedRay(Point p, Vector N, Vector l) {
        // dot product of N and ray
        double nDotL = alignZero(N.dotProduct(l));
        // reflected ray = L - (2 *  l.dorProduct(n)) * n
        Vector r = l.subtract(N.scale(2 * nDotL)).normalize();
        return new Ray(p, N, r);
    }

    /**
     * given a ray, construct from it a new refracted ray
     *
     * @param p {@link Point} that original ray intersects
     * @param N normal {@link Vector} to geometry at the point
     * @param l light direction {@link Vector} (the original ray)
     * @return refracted {@link Ray}
     */
    private Ray constructRefractedRay(Point p, Vector N, Vector l) {
        return new Ray(p, N, l);
    }


    /**
     * calculate Kd * |l.dorProduct(n)| * +Ks * max(0 ,(-v).dotProduct(r)) ** nShinines * Il
     * from phong model (specular and diffusive light) for each light in scene
     * adds the color from all light to returned result
     *
     * @param intersection {@link GeoPoint} to calculate color at
     * @param ray          {@link  Ray} from camera to the point
     * @return {@link Color} of the shape at the point
     */
    private Color calcLocalEffects(GeoPoint intersection, Ray ray, Double3 k) {
        // (Kd * |l.dorProduct(n)| ) * Il + (Ks * max(0 ,(-v).dotProduct(r)) ** nShinines) * Il
        // l = vector from light source to the point
        // n = normal vector to shape at point
        // r = specular vector to vector from light to point
        // v = ray from camera to point


        // v
        Vector v = ray.getDir();
        // n
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return Color.BLACK;
        //  nShininess
        int nShininess = intersection.geometry.getMaterial().nShininess;
        // Kd
        Double3 kD = intersection.geometry.getMaterial().kD;
        // Ks
        Double3 kS = intersection.geometry.getMaterial().kS;
        Color color = Color.BLACK;
        // loop through all light sources in scene

        var lights = scene.getLights();
        if (softShadow) {
            for (var lightSource : lights) {
                Color colorBeam = Color.BLACK;
                var vectors = lightSource.getListL(intersection.point);
                for (var l:vectors) {

                    // l.dorProduct(n)
                    double nl = alignZero(n.dotProduct(l));
                    // check that light direction is towards shape and not behind
                    if (nl * nv > 0) { // sign(nl) == sing(nv)

                        Double3 ktr = transparency(intersection, lightSource, l, n);
                        if (ktr.scale(k).greaterThan(MIN_CALC_COLOR_K)) {
                            Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                            // (Kd * |l.dorProduct(n)|) * Il
                            colorBeam = colorBeam.add(calcDiffusive(kD, nl, lightIntensity),
                                    // (Ks * max(0 ,(-v).dotProduct(r)) ** nShinines ) * Il
                                    calcSpecular(kS, nl, l, n, v, nShininess, lightIntensity));
                        }
                    }
                }
                color=color.add(colorBeam.reduce(vectors.size()));
            }
        }
        else {
            for (var lightSource : lights) {
                // l
                Vector l = lightSource.getL(intersection.point);
                // l.dorProduct(n)
                double nl = alignZero(n.dotProduct(l));
                // check that light direction is towards shape and not behind
                if (nl * nv > 0) { // sign(nl) == sing(nv)

                    Double3 ktr = transparency(intersection, lightSource, l, n);
                    if (ktr.scale(k).greaterThan(MIN_CALC_COLOR_K)) {
                        Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                        // (Kd * |l.dorProduct(n)|) * Il
                        color = color.add(calcDiffusive(kD, nl, lightIntensity),
                                // (Ks * max(0 ,(-v).dotProduct(r)) ** nShinines ) * Il
                                calcSpecular(kS, nl, l, n, v, nShininess, lightIntensity));
                    }
                }
            }
        }
        return color;
    }

    /**
     * calculate  (Ks * max(0 ,(-v).dotProduct(r)) ** nShinines )* Il
     * from phong model
     *
     * @param kS             attenuation factor of specular light
     * @param nDotL          l.dorProduct(n)
     * @param l              vector from light source to the point
     * @param n              normal vector to shape at point
     * @param v              ray from camera to point
     * @param nShininess     shininess factor of shape
     * @param lightIntensity Il
     * @return Il scaled by  factor
     */
    private Color calcSpecular(Double3 kS, double nDotL, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        // calculating (Ks * max(0 ,(-v).dotProduct(r)) ** nShinines )* Il

        // r = L - (2 *  l.dorProduct(n)) * n
        Vector r = l.subtract(n.scale(2 * nDotL)).normalize();
        // -v
        Vector minusV = v.scale(-1);
        // max(0 ,(-v).dotProduct(r))
        double specular = max(0, minusV.dotProduct(r));
        //** nShinines
        if (specular != 0)
            specular = pow(specular, nShininess);
        // Il scaled by factor
        return lightIntensity.scale(kS.scale(specular));
    }

    /**
     * calculate  (Kd * |l.dorProduct(n)|) * Il
     * from phong model
     *
     * @param kD             attenuation factor of diffusive light
     * @param nDotL          l.dorProduct(n)
     * @param lightIntensity Il
     * @return Il scaled by factor
     */
    private Color calcDiffusive(Double3 kD, double nDotL, Color lightIntensity) {
        // Kd * |l.dorProduct(n)|
        if (nDotL == 0)
            return lightIntensity.scale(0);
        else
            nDotL = abs(nDotL);
        // Il scaled by factor
        return lightIntensity.scale(kD.scale(nDotL));
    }


    /**
     * check if a point is shaded from a light source by a different geometry
     *
     * @param gp    {@link GeoPoint} to be checked
     * @param l     normal {@link Vector} to geometry at the point
     * @param n     light direction {@link Vector} (the original ray)
     * @param light {@link LightSource} lighting towards the geometry
     * @return true if unshaded ,else  false.
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource light) {
        // create a vector by scaling  light direction vector to opposite direction
        // now originating from point towards light
        Vector lightScaled = l.scale(-1);
        // construct a new ray using the scaled vector from the point towards ray
        // slightly removed from original point by epsilon (in Ray class)
        Ray shadowRay = new Ray(gp.point, n, lightScaled);
        // get distance from the light to the point
        double lightDistance = light.getDistance(shadowRay.getP0());
        // check if new ray intersect a geometry between point and the light source
        // further objects behind the light are avoided by distance parameter
        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(shadowRay, lightDistance);
        // no intersections were found - point is not shaded
        if (intersections == null)
            return true;
        //iterate through intersection points: if they are closer to point than the light
        // and the material of the geometry of intersection is not transparent
        // point is shaded return false
        for (var geoPoint : intersections) {
            if (alignZero(geoPoint.point.distance(gp.point) - lightDistance) <= 0
                    && geoPoint.geometry.getMaterial().kT.equals(Double3.ZERO)) {
                return false;
            }
        }
        // all geometries intersected are transparent - point is not shaded - return true
        return true;

    }

    /**
     * find the closest intersection point between ray and geometries in scene
     *
     * @param ray ray constructed from camera to scene
     * @return closest intersection {@link GeoPoint}
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        // check if ray constructed through the pixel intersects any of geometries
        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(ray);

        // return closest point if list is not empty
        return intersections == null ? null : ray.findClosestGeoPoint(intersections);

    }

    /**
     * calculate transparency of a point (shade)
     *
     * @param gp    {@link GeoPoint} to calculate transparency for
     * @param light {@link LightSource} lighting towards the geometry
     * @param l     normal {@link Vector} to geometry at the point
     * @param n     light direction {@link Vector} (the original ray)
     * @return {@link Double3} value of transparency at point
     */
    private Double3 transparency(GeoPoint gp, LightSource light, Vector l, Vector n) {
        // create a vector by scaling  light direction vector to opposite direction
        // now originating from point towards light
        Vector lightScaled = l.scale(-1);
        // construct a new ray using the scaled vector from the point towards ray
        // slightly removed from original point by epsilon (in Ray class)
        Ray shadowRay = new Ray(gp.point, n, lightScaled);
        // get distance from the light to the point
        double lightDistance = light.getDistance(shadowRay.getP0());
        // check if new ray intersect a geometry between point and the light source
        // further objects behind the light are avoided by distance parameter
        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(shadowRay, lightDistance);
        // point is not shaded - return transparency level of 1
        if (intersections == null)
            return Double3.ONE;

        // point is shaded - iterate through intersection points and add the shade effect from geometry
        //to transparency level at point
        Double3 ktr = Double3.ONE;
        for (var geoPoint : intersections) {
            ktr = ktr.scale(geoPoint.geometry.getMaterial().kT);
            if (ktr.lowerThan(MIN_CALC_COLOR_K))
                return Double3.ZERO;
        }
        // return the transparency
        return ktr;
    }

}
