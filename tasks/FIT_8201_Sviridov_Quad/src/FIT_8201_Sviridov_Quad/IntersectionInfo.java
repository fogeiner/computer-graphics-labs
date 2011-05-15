package FIT_8201_Sviridov_Quad;

import FIT_8201_Sviridov_Quad.primitives.Renderable;
import java.util.Collection;

/**
 *
 * @author alstein
 */
public class IntersectionInfo {

    private final Vertex intersection;
    private final Vector normal;
    private final Renderable object;

    public IntersectionInfo(Vertex intersection, Vector normal, Renderable object) {
        this.intersection = intersection;
        this.normal = normal.normalize();
        this.object = object;
    }

    public Vertex getIntersection() {
        return intersection;
    }

    public Vector getNormal() {
        return normal;
    }

    public double length() {
        return length(new Vertex(0, 0, 0));
    }

    public double length(Vertex start) {
        return new Vector(intersection, start).length();
    }

    public Coefficient3D trace(Collection<Light> lights, Collection<Renderable> objects) {
        return object.trace(this, lights, objects);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append("Intersection ");
        sb.append(intersection);
        sb.append(' ');
        sb.append(normal);
        sb.append("\r\n");
        sb.append(object);
        return sb.toString();
    }
}
