package FIT_8201_Sviridov_Quad.primitives;

import FIT_8201_Sviridov_Quad.ColorModel;
import FIT_8201_Sviridov_Quad.IntersectionInfo;
import FIT_8201_Sviridov_Quad.Ray;
import FIT_8201_Sviridov_Quad.Vector;
import FIT_8201_Sviridov_Quad.Vertex;
import FIT_8201_Sviridov_Quad.transformations.PlaneProjectionTransformation;
import FIT_8201_Sviridov_Quad.transformations.Transformation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author alstein
 */
public class Triangle extends RenderableImpl {

    private Vertex initialV1, initialV2, initialV3;
    private static Transformation planeProjectionTransformation;

    static {
        planeProjectionTransformation = PlaneProjectionTransformation.makePlaneProjectionTransformation(new Vector(1, 1, 1));
    }

    public Triangle(Vertex v1, Vertex v2, Vertex v3, ColorModel colorModel) {
        super(Arrays.asList(
                new Segment[]{new Segment(v1, v2), new Segment(v2, v3), new Segment(v3, v1)}),
                colorModel);
        initialV1 = v1;
        initialV2 = v2;
        initialV3 = v3;
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

        Vertex v1 = segments.get(0).getStartVertex(),
                v2 = segments.get(1).getStartVertex(),
                v3 = segments.get(2).getStartVertex();

        Vector Rd = ray.getDirection();
        Vertex R0 = ray.getOrigin();

        double v1x = v1.getX(), v1y = v1.getY(), v1z = v1.getZ(),
                v2x = v2.getX(), v2y = v2.getY(), v2z = v2.getZ(),
                v3x = v3.getX(), v3y = v3.getY(), v3z = v3.getZ();

        Vector u1 = new Vector(v2x - v1x, v2y - v1y, v2z - v1z),
                u2 = new Vector(v3x - v2x, v3y - v2y, v3z - v2z);

        Vector n = Vector.cross(u1, u2).normalize();

        if (Rd.dot(n) > 0.0) {
            n = new Vector(-n.getX(), -n.getY(), -n.getZ());
        }

        double A = n.getX(), B = n.getY(), C = n.getZ(),
                D = -(v1x * A + v1y * B + v1z * C);






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

            Vertex w1 = planeProjectionTransformation.apply(v1),
                    w2 = planeProjectionTransformation.apply(v2),
                    w3 = planeProjectionTransformation.apply(v3),
                    q = planeProjectionTransformation.apply(p);

            double triangleArea = area(w1, w2, w3);

            double alpha = area(q, w2, w3) / triangleArea,
                    beta = area(w1, q, w3) / triangleArea,
                    gamma = area(w1, w2, q) / triangleArea;

            if (alpha < 0 || alpha > 1 || beta < 0 || beta > 1
                    || gamma < 0 || gamma > 1
                    || (alpha + beta + gamma - 1.0) > Ray.EPS) {
                return intersections;
            }
        }


        IntersectionInfo intersectionInfo = new IntersectionInfo(p, n, this);
        intersections.add(intersectionInfo);

        return intersections;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(20);

        Vertex v1 = segments.get(0).getStartVertex(),
                v2 = segments.get(1).getStartVertex(),
                v3 = segments.get(2).getStartVertex();

        sb.append("TRG\r\n");
        for (Vertex v : new Vertex[]{initialV1, initialV2, initialV3}) {
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
