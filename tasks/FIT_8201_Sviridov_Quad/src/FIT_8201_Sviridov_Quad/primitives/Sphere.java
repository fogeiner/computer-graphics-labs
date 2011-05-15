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

    private Vertex initialOrigin;
    private double radius;

    public Sphere(Vertex origin, double radius, ColorModel colorModel) {
        super(Wireframe.superquadric(radius, 20, 20, 1, 1),
                origin, colorModel);
        this.origin = origin;
        this.initialOrigin = origin;
        this.radius = radius;
    }

    @Override
    public Renderable clone() {
        Sphere sphere = new Sphere(origin, radius, getColorModel());
        List<Segment> segments = getSegments();
        List<Segment> segmentsCopy = new ArrayList<Segment>(segments.size());
        for (Segment s : segments) {
            segmentsCopy.add(s.clone());
        }

        sphere.origin = this.origin;
        sphere.segments = segmentsCopy;
        sphere.radius = this.radius;
        return sphere;
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
    public Coefficient3D trace(IntersectionInfo intersectionInfo, Collection<Renderable> objects, Collection<Light> lights, Coefficient3D ambient) {

        Vector n = intersectionInfo.getNormal();
        Vertex p = intersectionInfo.getIntersection();
        Vector e = new Vector(p, new Vertex(0, 0, 0)).normalize();

        ColorModel cm = getColorModel();

        Coefficient3D ambientCoefficient = cm.getAmbientCoefficient(),
                diffuseCoefficient = cm.getDiffuseCoefficient(),
                specularCoefficient = cm.getSpecularCoefficient();

        double R = ambient.getR() * ambientCoefficient.getR(),
                G = ambient.getG() * ambientCoefficient.getG(),
                B = ambient.getB() * ambientCoefficient.getB();

        lightCycle:
        for (Light light : lights) {

            Vector l = new Vector(p, light.getOrigin());
            double distanceToLight = l.length();
            l = l.normalize();
            double nl = n.dot(l);

            // is light visible?
            // 1. it's on the right side
            if (nl < 0) {
                continue;
            }
            // 2. it's not hidden by some other object
            // check all the objects (except current one!) if they intersect
            // ray from p to l and if so if there's at least one object
            // that's closer to point than light source -- it's hidden
            Ray ray = new Ray(origin, l);
            Collection<IntersectionInfo> intersections = new ArrayList<IntersectionInfo>();
            for(Renderable renderable: objects){
                if(renderable == this) continue;
                intersections.addAll(renderable.intersect(ray));
            }
            
            for(IntersectionInfo ii: intersections){
                double distance = new Vector(p, ii.getIntersection()).length();
                if(distance < distanceToLight) continue lightCycle;
            }

            Coefficient3D I = light.getColor();


            double fatt = 1.0 / (1 + distanceToLight);

            Vector r = new Vector(
                    2 * nl * n.getX() - l.getX(),
                    2 * nl * n.getY() - l.getY(),
                    2 * nl * n.getZ() - l.getZ()).normalize();
            Vector h = new Vector(
                    l.getX() + e.getX(),
                    l.getY() + e.getY(),
                    l.getZ() + e.getZ()).normalize();

            double nhpow = Math.pow(n.dot(h), cm.getPower());

            R += fatt * I.getR() * (diffuseCoefficient.getR() * nl +
                    specularCoefficient.getR() * nhpow);
            G += fatt * I.getG() * (diffuseCoefficient.getG() * nl +
                    specularCoefficient.getG() * nhpow);
            B += fatt * I.getB() * (diffuseCoefficient.getB() * nl +
                    specularCoefficient.getB() * nhpow);
        }
        return new Coefficient3D(R, G, B);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append("SPH\r\n");
        sb.append(initialOrigin);
        sb.append("\r\n");
        sb.append(radius);
        sb.append("\r\n");
        sb.append(getColorModel());
        sb.append("\r\n");
        return sb.toString();
    }
}
