/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Quad.primitives;

import FIT_8201_Sviridov_Quad.Coefficient3D;
import FIT_8201_Sviridov_Quad.ColorModel;
import FIT_8201_Sviridov_Quad.IntersectionInfo;
import FIT_8201_Sviridov_Quad.Light;
import FIT_8201_Sviridov_Quad.Ray;
import FIT_8201_Sviridov_Quad.Vector;
import FIT_8201_Sviridov_Quad.Vertex;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author alstein
 */
public class Sphere extends RenderableImpl {

    private Vertex origin;
    private double radius;

    public Sphere(Vertex origin, double radius, ColorModel colorModel) {
        super(Wireframe.superquadric(radius, 20, 20, 1, 1),
                origin, colorModel);
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
            Collection<Light> lights, Collection<Renderable> objects) {
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
