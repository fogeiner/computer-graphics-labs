package FIT_8201_Sviridov_Quad;

import FIT_8201_Sviridov_Quad.primitives.Wireframe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author alstein
 */
public class Ray {

    public static final double EPS = 10e-8;
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
        if (tests.contains(2)) {
            Ray ray = new Ray(new Vector(0, 0, -1));
            System.out.println(ray);

            Triangle triangle = new Triangle(
                    new Vertex(0, 10, -10),
                    new Vertex(-10, -10, -10),
                    new Vertex(10, 10, -10));

            for (IntersectionInfo ii : triangle.intersect(ray)) {
                System.out.println(ii);
            }
        }


    }
}

interface Renderable {

    public Collection<IntersectionInfo> intersect(Ray ray);

    public Coefficient3D trace(IntersectionInfo intersectionInfo, Collection<LightSource> lights, Collection<Renderable> objects);
}

abstract class RenderableImpl implements Renderable {

    private ColorModel colorModel;
    private Wireframe wireframe;

    public ColorModel getColorModel() {
        return colorModel;
    }

    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;
    }
}

class Sphere extends RenderableImpl {

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
                B = 2 * (xd * x0 - xd * xc
                + yd * y0 - yd * yc
                + zd * z0 - zd * zc),
                C = x0 * x0 - 2 * x0 * xc
                + xc * xc + y0 * y0
                - 2 * y0 * yc + yc * yc
                + z0 * z0 - 2 * z0 * zc
                + zc * zc;

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
    public Coefficient3D trace(IntersectionInfo intersectionInfo,
            Collection<LightSource> lights, Collection<Renderable> objects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append("SPH\r\n");
        sb.append(origin);
        sb.append("\r\n");
        sb.append(radius);
        sb.append("\r\n");
        sb.append(getColorModel());
        sb.append("\r\n");
        sb.append("\r\n");
        return sb.toString();
    }
}

class Triangle extends RenderableImpl {

    private Vertex v1, v2, v3;

    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public static double area(Vertex v1, Vertex v2, Vertex v3) {
        double v1x = v1.getX(),
                v1y = v1.getY(),
                v1z = v1.getZ(),
                v2x = v2.getX(),
                v2y = v2.getY(),
                v2z = v2.getZ(),
                v3x = v3.getX(),
                v3y = v3.getY(),
                v3z = v3.getZ();

        double area = 0.5 * ((v2x - v1x) * (v3y - v1y) - (v3x - v1x) * (v2y - v1y));

        return area;
    }

    @Override
    public Collection<IntersectionInfo> intersect(Ray ray) {
        List<IntersectionInfo> intersections = new ArrayList<IntersectionInfo>(1);

        double v1x = v1.getX(), v1y = v1.getY(), v1z = v1.getZ(),
                v2x = v2.getX(), v2y = v2.getY(), v2z = v2.getZ(),
                v3x = v3.getX(), v3y = v3.getY(), v3z = v3.getZ();

        Vector u1 = new Vector(v2x - v1x, v2y - v1y, v2z - v1z),
                u2 = new Vector(v3x - v2x, v3y - v2y, v3z - v2z);

        Vector n = Vector.cross(u1, u2).normalize();

        double A = n.getX(), B = n.getY(), C = n.getZ(),
                D = -(v1x * A + v1y * B + v1z * C);

        Vector Rd = ray.getDirection();
        Vertex R0 = ray.getOrigin();

        double nRd = Vector.dot(Rd, n);
        if (Math.abs(nRd) < Ray.EPS) {
            return intersections;
        }

        double nR0 = Vector.dot(n, new Vector(R0));

        double t = -(nR0 + D) / nRd;
        if (t < 0) {
            return intersections;
        }

        Vertex p = new Vertex(
                R0.getX() + t * Rd.getX(),
                R0.getY() + t * Rd.getY(),
                R0.getZ() + t * Rd.getZ());

        // inside triangle?
        {
            int maxCoordinate = -1;
            double max = Double.NEGATIVE_INFINITY;

            if (p.getX() > max) {
                maxCoordinate = 0;
                max = p.getX();
            }

            if (p.getY() > max) {
                maxCoordinate = 1;
                max = p.getY();
            }

            if (p.getZ() > max) {
                maxCoordinate = 2;
            }

            Vertex w1, w2, w3, q;
            switch (maxCoordinate) {
                case 0:
                    w1 = new Vertex(0, v1.getY(), v1.getZ());
                    w2 = new Vertex(0, v2.getY(), v2.getZ());
                    w3 = new Vertex(0, v3.getY(), v3.getZ());
                    q = new Vertex(0, p.getY(), p.getZ());
                    break;
                case 1:
                    w1 = new Vertex(v1.getX(), 0, v1.getZ());
                    w2 = new Vertex(v2.getX(), 0, v2.getZ());
                    w3 = new Vertex(v3.getX(), 0, v3.getZ());
                    q = new Vertex(p.getX(), 0, p.getZ());
                    break;
                case 2:
                    w1 = new Vertex(v1.getX(), v1.getY(), 0);
                    w2 = new Vertex(v2.getX(), v2.getY(), 0);
                    w3 = new Vertex(v3.getX(), v3.getY(), 0);
                    q = new Vertex(p.getX(), p.getY(), 0);
                    break;
                default:
                    throw new IllegalStateException("For a given set of numbers maximum exists always");
            }

            double triangleArea = area(w1, w2, w3);

            double alpha = area(q, w2, w3) / triangleArea,
                    beta = area(w1, q, w3) / triangleArea,
                    gamma = area(w1, w2, q) / triangleArea;

            if (alpha < 0 || alpha > 1 || beta < 0 || beta > 1
                    || gamma < 0 || gamma > 1
                    || (alpha + beta + gamma - 1) > Ray.EPS) {
                return intersections;
            }
        }


        IntersectionInfo intersectionInfo = new IntersectionInfo(p, n, this);
        intersections.add(intersectionInfo);

        return intersections;
    }

    @Override
    public Coefficient3D trace(IntersectionInfo intersectionInfo, Collection<LightSource> lights, Collection<Renderable> objects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(20);

        sb.append("TRG\r\n");
        for (Vertex v : new Vertex[]{v1, v2, v3}) {
            sb.append(v.getX());
            sb.append(' ');
            sb.append(v.getY());
            sb.append(' ');
            sb.append(v.getZ());
            sb.append("\r\n");
        }
        sb.append(getColorModel());
        sb.append("\r\n");

        return sb.toString();
    }
}
