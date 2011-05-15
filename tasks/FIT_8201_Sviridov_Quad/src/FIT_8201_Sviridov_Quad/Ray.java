package FIT_8201_Sviridov_Quad;

import FIT_8201_Sviridov_Quad.primitives.Renderable;
import FIT_8201_Sviridov_Quad.primitives.Sphere;
import FIT_8201_Sviridov_Quad.primitives.Triangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author alstein
 */
public class Ray {

    public static final double EPS = 10e-12;
    private final Vertex origin;
    private final Vector direction;

    public IntersectionInfo trace(Collection<Renderable> objects) {
        double closestDistance = Double.MAX_VALUE;
        IntersectionInfo closestIntersection = null;

        for (Renderable obj : objects) {
            Collection<IntersectionInfo> intersections = obj.intersect(this);
            for (IntersectionInfo intersectionInfo : intersections) {
                double distance = intersectionInfo.length();
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestIntersection = intersectionInfo;
                }
            }
        }

        return closestIntersection;
    }

    public Ray(Vertex origin, Vector direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public Ray(Vertex start, Vertex end) {
        this.origin = start;
        this.direction = new Vector(start, end).normalize();
    }

    public Ray(Vector direction) {
        this.origin = new Vertex(0, 0, 0);
        this.direction = direction.normalize();
    }

    public Vector getDirection() {
        return direction;
    }

    public Vertex getOrigin() {
        return origin;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(20);
        sb.append("Ray ");
        sb.append(origin);
        sb.append(" + ");
        sb.append(direction);
        sb.append("t");
        return sb.toString();
    }

    public static void main(String args[]) {
        List<Integer> tests = new ArrayList<Integer>(5);
        tests.add(2);
        if (tests.contains(0)) {
            Ray ray = new Ray(new Vector(1, 1, 1));
            System.out.println(ray);
            ray = new Ray(new Vertex(0, 0, 0), new Vertex(1, 1, 1));
            System.out.println(ray);
            ray = new Ray(new Vertex(0, 0, 0), new Vector(1, 1, 1));
            System.out.println(ray);
        }

        if (tests.contains(1)) {
            Ray ray = new Ray(new Vector(0.1, 0.1, -1));
            System.out.println(ray);
            Sphere sphere = new Sphere(new Vertex(0, 0, -50), 20, null);
            for (IntersectionInfo ii : sphere.intersect(ray)) {
                System.out.println(ii);
            }
        }
        if (tests.contains(2)) {
            Ray ray = new Ray(new Vector(0, 0, -1));
            System.out.println(ray);

            Triangle triangle = new Triangle(
                    new Vertex(0, 10, -10),
                    new Vertex(-10, -10, -10),
                    new Vertex(10, 10, -10), null);

            for (IntersectionInfo ii : triangle.intersect(ray)) {
                System.out.println(ii);
            }
        }
    }
}
