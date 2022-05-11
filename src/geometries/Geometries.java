package geometries;

import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Collection of geometry objects implementing {@link Intersectable}
 * implements Composite pattern
 */
public class Geometries  extends Intersectable{

    /**
     * list of geometries that implement {@link Intersectable} interface
     */
    private List<Intersectable> intersectables;

    /**
     * constructor
     */
    public Geometries() {
        intersectables = new LinkedList<Intersectable>();
    }

    /**
     * constructor with parameter
     * @param intersectables collection of {@link  Intersectable} implemented objects, to add to geometry composite
     */
    public Geometries(Intersectable... intersectables) {
        this.intersectables = new LinkedList<Intersectable>();
         Collections.addAll(this.intersectables, intersectables);
    }

    /**
     * add collection of {@link  Geometries} geometry composite
     * @param intersectables collection of geometries passed as parameters
     */
    public void  add( Intersectable... intersectables){
        Collections.addAll(this.intersectables, intersectables);
    }

    /**
     * find intersection between ray and all geometries in the geometry collection
     * @param ray ray towards the composite of geometries
     * @return  immutable list of intersection points as  {@link GeoPoint} objects
     */
       @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxDistance) {
        List<GeoPoint> result = null;   // intersection points

        //for each geometry in intersect-able collection check intersection points
        for (var item: intersectables) {

            // get intersection point for each specific item, (item can be either geometry/nested composite of geometries)
            List<GeoPoint> itemList = item.findGeoIntersections(ray,maxDistance);

            // points were found , add to composite's total intersection points list
            if(itemList != null) {
                if(result==null){
                    result= new LinkedList<>();
                }
                result.addAll(itemList);
            }
        }
        // return list of points - null if no intersection points were found
        return result;

    }
}
