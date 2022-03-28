package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Composite class for all geometries object implementing {@link Intersectable}
 */
public class Geometries  implements Intersectable{

    private List<Intersectable>  _intersectables;

    /**
     * constructor
     */
    public Geometries() {
        _intersectables = new LinkedList<Intersectable>();
    }

    /**
     * constructor with parameter
     * @param intersectables collection of {@link  Geometry} implemented objects to add to geometry composite
     */
    public Geometries(Intersectable... intersectables) {
        _intersectables = new LinkedList<Intersectable>();
         Collections.addAll(_intersectables, intersectables);
    }

    /**
     * add list of {@link  Geometries} geometry composite
     * @param intersectables collection of geometries passed as
     */
    public void  add( Intersectable... intersectables){
        Collections.addAll(_intersectables, intersectables);
    }

    /**
     * find intersection between ray and all geometries in the geometry composite
     * @param ray ray towards the composite of geometries
     * @return list of intersection points
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> result = null;   // intersection points
        //for each geometry in intersect-able collection check intersection points
        for (var item: _intersectables ) {

            // get intersection point for a specific geometry in composite
            List<Point> itemList = item.findIntersections(ray);

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
