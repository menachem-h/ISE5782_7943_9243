package geometries;

import java.util.List;

import primitives.*;
import static primitives.Util.*;

/**
 * Two-dimensional polygon in 3D Cartesian coordinate system
 * 
 * @author Dan Zilbershtein
 */
public class Polygon extends Geometry {
	/**
	 * List of polygon's vertices
	 */
	protected List<Point> vertices;
	/**
	 * Associated plane in which the polygon lays
	 */
	protected Plane plane;
	/**
	 * number of vertices of the polygon
	 */
	private int size;

	/**
	 * Polygon constructor based on vertices list. The list must be ordered by edge
	 * path. The polygon must be convex.
	 * 
	 * @param vertices list of vertices according to their order by edge path
	 * @throws IllegalArgumentException in any case of illegal combination of
	 *                                  vertices:
	 *                                  <ul>
	 *                                  <li>Less than 3 vertices</li>
	 *                                  <li>Consequent vertices are in the same
	 *                                  point
	 *                                  <li>The vertices are not in the same
	 *                                  plane</li>
	 *                                  <li>The order of vertices is not according
	 *                                  to edge path</li>
	 *                                  <li>Three consequent vertices lay in the
	 *                                  same line (180&#176; angle between two
	 *                                  consequent edges)
	 *                                  <li>The polygon is concave (not convex)</li>
	 *                                  </ul>
	 */
	public Polygon(Point... vertices) {
			if (vertices.length < 3)
			throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
		this.vertices = List.of(vertices);
		// Generate the plane according to the first three vertices and associate the
		// polygon with this plane.
		// The plane holds the invariant normal (orthogonal unit) vector to the polygon
		plane = new Plane(vertices[0], vertices[1], vertices[2]);
		if (vertices.length == 3)
			return; // no need for more tests for a Triangle

		Vector n = plane.getNormal();

		// Subtracting any subsequent points will throw an IllegalArgumentException
		// because of Zero Vector if they are in the same point
		Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
		Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

		// Cross Product of any subsequent edges will throw an IllegalArgumentException
		// because of Zero Vector if they connect three vertices that lay in the same
		// line.
		// Generate the direction of the polygon according to the angle between last and
		// first edge being less than 180 deg. It is hold by the sign of its dot product
		// with
		// the normal. If all the rest consequent edges will generate the same sign -
		// the
		// polygon is convex ("kamur" in Hebrew).
		boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
		for (var i = 1; i < vertices.length; ++i) {
			// Test that the point is in the same plane as calculated originally
			if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
				throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
			// Test the consequent edges have
			edge1 = edge2;
			edge2 = vertices[i].subtract(vertices[i - 1]);
			if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
				throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
		}
		size = vertices.length;
	}

	@Override
	public Vector getNormal(Point point) {
		return plane.getNormal();
	}


	/**
	 * find intersection between ray and polygon
	 * @param ray ray towards the plane
	 * @return  immutable list of one intersection point as  {@link GeoPoint} object
	 */
	@Override
	protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {

		// find intersection between ray and plane containing the polygon
		List<GeoPoint> points=plane.findGeoIntersections(ray,maxDistance);
		// no intersections with plane , ray does not intersect polygon
		if (points==null)
			return null;

		//check that intersection point is closer to ray origin than
		// max distance parameter
		double distance = points.get(0).point.distance(ray.getP0());
		if(alignZero(distance-maxDistance)>0)
			return null;

		// check that intersection point is within polygon boundary
		// by  creating vectors from ray origin to each pair of adjacent vertices in polygon
		// if the sign of the dot product of the vertices for all pairs is matching. ray intersects polygon
		Point p0 = ray.getP0();
		Vector direction = ray.getDir();

		// get vector from ray origin to first vertices of polygon
		Vector v1 = vertices.get(0).subtract(p0);

		// get vector from ray origin to adjacent vertices of previous vertices
		Vector v2 = vertices.get(1).subtract(p0);

		// get sign of dot product of the vectors
		double sign = direction.dotProduct(v2.crossProduct(v1));

		// if dot product == 0 ray does not intersect polygon
		if (isZero(sign))
			return null;

		// flag setting the sign of the dot product of the first pair of vertices
		boolean checkSign = sign > 0;

		// loop over all adjacent vertices in polygon and check sign of dot-product for constructed
		// vectors
		for (int i = vertices.size() - 1; i > 0; --i) {
			v2 = v1;
			v1 = vertices.get(i).subtract(p0);
			sign = alignZero(direction.dotProduct(v2.crossProduct(v1)));

			// vectors constructed are orthogonal , ray does not intersect polygon
			if (isZero(sign))
				return null;

			//  sign is not matching
			if (checkSign != (sign > 0))
				return null;
		}

		// all signs were matching return the intersection point
		return List.of(new GeoPoint(this,points.get(0).point));
	}


}
