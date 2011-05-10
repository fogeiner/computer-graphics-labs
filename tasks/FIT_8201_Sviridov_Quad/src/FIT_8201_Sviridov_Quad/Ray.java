package FIT_8201_Sviridov_Quad;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author alstein
 */
public class Ray {

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
            Sphere sphere = new Sphere(new Vertex(0, 0, -50), 20);
            for (IntersectionInfo ii : sphere.intersect(ray)) {
                System.out.println(ii);
            }

        }
    }
}

interface Renderable {

    public Collection<IntersectionInfo> intersect(Ray ray);

    public Color trace(IntersectionInfo intersectionInfo, Collection<LightSource> lights, Collection<Renderable> objects);
}

class Sphere implements Renderable {

    private Vertex origin;
    private double radius;

    public Sphere(Vertex origin, double radius) {
        this.origin = origin;
        this.radius = radius;
    }

    @Override
    public Collection<IntersectionInfo> intersect(Ray ray) {
        Vector rayDirection = ray.getDirection();
        Vertex rayOrigin = ray.getOrigin();
        double x0 = rayOrigin.getX(),
                y0 = rayOrigin.getY(),
                z0 = rayOrigin.getZ();

        List<IntersectionInfo> intersections = new ArrayList<IntersectionInfo>(2);

        double r = this.radius;

        double xd = rayDirection.getX(),
                yd = rayDirection.getY(),
                zd = rayDirection.getZ();

        double xc = origin.getX(),
                yc = origin.getY(),
                zc = origin.getZ();

        double A = xd * xd + yd * yd + zd * zd,
                B = 2 * (xd * x0 - xd * xc + yd * y0 - yd * yc + zd * z0 - zd * zc),
                C = x0 * x0 - 2 * x0 * xc + xc * xc + y0 * y0 - 2 * y0 * yc + yc * yc + z0 * z0 - 2 * z0 * zc + zc * zc;

        double E = C - r * r;

        double discriminantSquared = B * B - 4 * E;

        if (discriminantSquared < 0) {
            return intersections;
        }

        double discriminant = Math.sqrt(discriminantSquared);

        for (double t : new double[]{(-B - discriminant) / 2, (-B + discriminant) / 2}) {
            if (t > 0) {
                double xi = x0 + xd * t,
                        yi = y0 + yd * t,
                        zi = z0 + zd * t;
                Vertex intersection = new Vertex(xi, yi, zi);
                Vector normal = new Vector((xi - xc) / r, (yi - yc) / r, (zi - zc) / r);
                intersections.add(new IntersectionInfo(intersection, normal, this));
            }
        }

        return intersections;
    }

    @Override
    public Color trace(IntersectionInfo intersectionInfo, Collection<LightSource> lights, Collection<Renderable> objects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

class Triangle {

    private Vertex v1, v2, v3;
}
