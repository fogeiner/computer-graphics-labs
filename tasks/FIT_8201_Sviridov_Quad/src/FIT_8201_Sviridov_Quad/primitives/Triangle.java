package FIT_8201_Sviridov_Quad.primitives;

import FIT_8201_Sviridov_Quad.Coefficient3D;
import FIT_8201_Sviridov_Quad.ColorModel;
import FIT_8201_Sviridov_Quad.IntersectionInfo;
import FIT_8201_Sviridov_Quad.Light;
import FIT_8201_Sviridov_Quad.Ray;
import FIT_8201_Sviridov_Quad.Vector;
import FIT_8201_Sviridov_Quad.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author alstein
 */
public class Triangle extends RenderableImpl {

    public Triangle(Vertex v1, Vertex v2, Vertex v3, ColorModel colorModel) {
        super(Arrays.asList(
                new Segment[]{new Segment(v1, v2), new Segment(v2, v3), new Segment(v3, v1)}),
                colorModel);
    }

    @Override
    public Renderable clone() {
        List<Segment> segments = getSegments();
        
        Vertex v1 = segments.get(0).getStartVertex(),
                v2 = segments.get(1).getStartVertex(),
                v3 = segments.get(2).getStartVertex();

        return new Triangle(v1, v2, v3, getColorModel());
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
        List<Segment> segments = getSegments();

        Vertex v1 = segments.get(0).getStartVertex(),
                v2 = segments.get(1).getStartVertex(),
                v3 = segments.get(2).getStartVertex();

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
    public Coefficient3D trace(IntersectionInfo intersectionInfo, Collection<Light> lights, Collection<Renderable> objects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(20);

        List<Segment> segments = getSegments();

        Vertex v1 = segments.get(0).getStartVertex(),
                v2 = segments.get(1).getStartVertex(),
                v3 = segments.get(2).getStartVertex();

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
