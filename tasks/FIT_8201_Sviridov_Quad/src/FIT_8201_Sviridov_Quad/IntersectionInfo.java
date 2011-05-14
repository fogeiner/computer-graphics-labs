package FIT_8201_Sviridov_Quad;

import FIT_8201_Sviridov_Quad.primitives.Renderable;

/**
 *
 * @author alstein
 */
public class IntersectionInfo {

    public final Vertex intersection;
    public final Vector normal;
    public final Renderable object;

    public IntersectionInfo(Vertex intersection, Vector normal, Renderable object) {
        this.intersection = intersection;
        this.normal = normal.normalize();
        this.object = object;
    }

    public Vertex getIntersection(){
        return intersection;
    }

    public Vector getNormal(){
        return normal;
    }

    public double length(){
        return length(new Vertex(0, 0, 0));
    }
    public double length(Vertex start){
        return new Vector(intersection, start).length();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(20);
        sb.append("Intersection ");
        sb.append(intersection);
        return sb.toString();
    }
}
