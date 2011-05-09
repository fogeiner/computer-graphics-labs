package FIT_8201_Sviridov_Quad;

import java.util.Collection;

/**
 *
 * @author alstein
 */
public class Ray {

    private final Vertex origin;
    private final Vector direction;

    public Renderable trace(Collection<Renderable> objects){
        double closestDistance = Double.MAX_VALUE;
        Renderable closestObject = null;

        for(Renderable obj: objects){
            Collection<Vertex> intersections = obj.intersect(this);
            for(Vertex v: intersections){
                double distance = new Vector(v).length();
                if (distance < closestDistance){
                    closestDistance = distance;
                    closestObject = obj;
                }
            }
        }
        return closestObject;
    }

    public Ray(Vertex origin, Vector direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public Vector getDirection() {
        return direction;
    }

    public Vertex getOrigin() {
        return origin;
    }
}
