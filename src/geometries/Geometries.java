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

    public Geometries() {
        _intersectables = new LinkedList<Intersectable>();
    }

    public Geometries(Intersectable... intersectables) {
        _intersectables = new LinkedList<Intersectable>();
         Collections.addAll(_intersectables, intersectables);
    }

    public void  add( Intersectable... intersectables){
        Collections.addAll(_intersectables, intersectables);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> result = null;
        //TODO comment here
        for (var item: _intersectables ) {

            List<Point> itemList = item.findIntersections(ray);

            if(itemList != null) {
                if(result==null){
                    result= new LinkedList<>();
                }
                result.addAll(itemList);
            }
        }
        return result;
    }
}
