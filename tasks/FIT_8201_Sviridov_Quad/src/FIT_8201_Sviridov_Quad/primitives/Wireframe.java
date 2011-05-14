package FIT_8201_Sviridov_Quad.primitives;

import FIT_8201_Sviridov_Quad.CoordinateSystem;
import FIT_8201_Sviridov_Quad.Rect3D;
import FIT_8201_Sviridov_Quad.SceneObject;
import FIT_8201_Sviridov_Quad.Vector;
import FIT_8201_Sviridov_Quad.Vertex;
import FIT_8201_Sviridov_Quad.transformations.Transformation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for wireframe shapes
 * 
 * @author admin
 */
public class Wireframe implements SceneObject, Cloneable {

    public static final Vertex DEFAULT_ORIGIN = new Vertex(0, 0, 0);
    public static final Color DEFAULT_COLOR = Color.black;
    public static final int DEFAULT_WIDTH = 1;
    private int width = DEFAULT_WIDTH;
    private Color color = DEFAULT_COLOR;
    protected List<Segment> segments;
    protected Vertex origin = DEFAULT_ORIGIN;

    /**
     * Default ctor with given segments given in local WireframeShape model
     *
     * @param segments
     */
    public Wireframe(List<Segment> segments) {
        this.segments = segments;
    }

    public Wireframe(List<Segment> segments, Vertex origin) {
        this.segments = segments;
        this.origin = origin;
        CoordinateSystem cs = new CoordinateSystem(origin);
        Transformation frameToWorld = cs.getFrameToWorldTransformation();
        for (Segment segment : segments) {
            segment.transform(frameToWorld);
        }
    }

    /**
     * Ctor with basis and origin for given wireframe shape
     *
     * @param v1 1st vector
     * @param v2 2nd vector
     * @param v3 3rd vector
     */
    public Wireframe(List<Segment> segments, Vector v1, Vector v2, Vector v3, Vertex origin) {
        this.segments = segments;
        CoordinateSystem cs = new CoordinateSystem(origin, v1, v2, v3);
        Transformation frameToWorld = cs.getFrameToWorldTransformation();
        for (Segment segment : segments) {
            segment.transform(frameToWorld);
        }
    }

    @Override
    public Object clone() {
        List<Segment> segmentsCopy = new ArrayList<Segment>(segments.size());
        for (Segment s : segments) {
            segmentsCopy.add(s.clone());
        }
        Wireframe wireframe = new Wireframe(segmentsCopy);
        wireframe.origin = origin;
        wireframe.color = color;
        wireframe.width = width;
        return wireframe;
    }

    public Vertex getOrigin() {
        return origin;
    }

    /**
     * Returns segments of the shape after Transformation application
     *
     * @return segments
     */
    public List<Segment> getSegments() {
        return segments;
    }

    /**
     * Returns bound 3d rect
     *
     * @return bound 3d rect
     */
    public Rect3D getBoundRect3D() {
        if (segments.isEmpty()) {
            return new Rect3D(0, 0, 0);
        }

        double maxX = Double.NEGATIVE_INFINITY, minX = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, minY = Double.POSITIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;

        for (Segment s : getSegments()) {
            Vertex start = s.getStartVertex(), end = s.getEndVertex();
            double sx = start.getX(), sy = start.getY(), sz = start.getZ(), ex = end.getX(), ey = end.getY(), ez = end.getZ();

            maxX = Math.max(sx, Math.max(ex, maxX));
            maxY = Math.max(sy, Math.max(ey, maxY));
            maxZ = Math.max(sz, Math.max(ez, maxZ));

            minX = Math.min(sx, Math.min(ex, minX));
            minY = Math.min(sy, Math.min(ey, minY));
            minZ = Math.min(sz, Math.min(ez, minZ));
        }

        return new Rect3D(maxX - minX, maxY - minY, maxZ - minZ);
    }

    /**
     * Returns cube
     *
     * @param length
     *            cube edge length
     * @return cube wireframe shape
     */
    public static List<Segment> cube(double length) {
        double p = length / 2;
        double m = -p;

        List<Segment> segs = new ArrayList<Segment>(12);

        Vertex mmm = new Vertex(m, m, m), mmp = new Vertex(m, m, p), mpm = new Vertex(
                m, p, m), mpp = new Vertex(m, p, p), pmm = new Vertex(p, m, m), pmp = new Vertex(
                p, m, p), ppm = new Vertex(p, p, m), ppp = new Vertex(p, p, p);

        segs.add(new Segment(mmm, mmp));
        segs.add(new Segment(mmm, pmm));
        segs.add(new Segment(mmm, mpm));

        segs.add(new Segment(pmm, ppm));
        segs.add(new Segment(pmm, pmp));

        segs.add(new Segment(mmp, mpp));
        segs.add(new Segment(mmp, pmp));

        segs.add(new Segment(mpm, ppm));
        segs.add(new Segment(mpm, mpp));

        segs.add(new Segment(mpp, ppp));
        segs.add(new Segment(ppm, ppp));

        segs.add(new Segment(pmp, ppp));

        return segs;
    }

    /**
     * Returns parallelepiped with given parameters
     *
     * @param width
     *            parallelepiped width
     * @param height
     *            parallelepiped height
     * @param depth
     *            parallelepiped depth
     * @return parallelepiped
     */
    public static List<Segment> box(double width, double height,
            double depth) {
        double wp = width / 2, wm = -wp, hp = height / 2, hm = -hp, dp = depth / 2, dm = -dp;

        Vertex mmm = new Vertex(wm, hm, dm), mmp = new Vertex(wm, hm, dp), mpm = new Vertex(
                wm, hp, dm), mpp = new Vertex(wm, hp, dp), pmm = new Vertex(wp,
                hm, dm), pmp = new Vertex(wp, hm, dp), ppm = new Vertex(wp, hp,
                dm), ppp = new Vertex(wp, hp, dp);

        List<Segment> segs = new ArrayList<Segment>(12);

        segs.add(new Segment(mmm, mmp));
        segs.add(new Segment(mmm, pmm));
        segs.add(new Segment(mmm, mpm));

        segs.add(new Segment(pmm, ppm));
        segs.add(new Segment(pmm, pmp));

        segs.add(new Segment(mmp, mpp));
        segs.add(new Segment(mmp, pmp));

        segs.add(new Segment(mpm, ppm));
        segs.add(new Segment(mpm, mpp));

        segs.add(new Segment(mpp, ppp));
        segs.add(new Segment(ppm, ppp));

        segs.add(new Segment(pmp, ppp));

        return segs;
    }

    /**
     * Returns superquadric with given parameters
     *
     * @param size
     *            size
     * @param tSteps
     *            t steps
     * @param sSteps
     *            s steps
     * @param e1
     *            e1
     * @param e2
     *            e2
     * @return superquadric
     */
    public static List<Segment> superquadric(double size, int tSteps,
            int sSteps, double e1, double e2) {

        List<Segment> segs = new ArrayList<Segment>((tSteps + 1) * (sSteps + 1));
        double tStep = Math.PI / tSteps;
        double sStep = 2 * Math.PI / sSteps;

        Vertex vertices[][] = new Vertex[tSteps + 1][sSteps + 1];

        for (int tIndex = 0; tIndex < tSteps + 1; ++tIndex) {
            for (int sIndex = 0; sIndex < sSteps + 1; ++sIndex) {
                double t = -Math.PI / 2 + tIndex * tStep, s = sIndex * sStep, ct = Math.cos(t), cs = Math.cos(s), ss = Math.sin(s), st = Math.sin(t);
                double x = size * Math.pow(Math.abs(ct), e1) * Math.signum(ct)
                        * Math.pow(Math.abs(cs), e2) * Math.signum(cs), y = size
                        * Math.pow(Math.abs(ct), e1)
                        * Math.signum(ct)
                        * Math.pow(Math.abs(ss), e2) * Math.signum(ss), z = size
                        * Math.pow(Math.abs(st), e1) * Math.signum(st);

                Vertex cur = new Vertex(x, y, z);
                vertices[tIndex][sIndex] = cur;
            }
        }

        for (int tIndex = 0; tIndex < tSteps + 1; ++tIndex) {
            for (int sIndex = 0; sIndex < sSteps; ++sIndex) {
                Segment s = new Segment(vertices[tIndex][sIndex],
                        vertices[tIndex][sIndex + 1]);
                segs.add(s);
            }
        }

        for (int sIndex = 0; sIndex < sSteps + 1; ++sIndex) {
            for (int tIndex = 0; tIndex < tSteps; ++tIndex) {
                Segment s = new Segment(vertices[tIndex][sIndex],
                        vertices[tIndex + 1][sIndex]);
                segs.add(s);
            }
        }

        return segs;
    }

    /**
     * Returns color
     *
     * @return color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets color
     *
     * @param color
     *            color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns width
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets width
     *
     * @param width
     *            width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Origin: ");
        sb.append(getOrigin());
        sb.append("\n");
        sb.append("Transformed segments: \n");
        for (Segment s : getSegments()) {
            sb.append(s);
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public void transform(Transformation transformation) {
        for (Segment s : segments) {
            s.transform(transformation);
        }
        origin = transformation.apply(origin);
    }
}
