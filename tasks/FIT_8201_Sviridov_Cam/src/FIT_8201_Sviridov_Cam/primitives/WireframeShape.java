package FIT_8201_Sviridov_Cam.primitives;

import FIT_8201_Sviridov_Cam.CoordinateSystem;
import FIT_8201_Sviridov_Cam.Rect3D;
import FIT_8201_Sviridov_Cam.Transformation;
import FIT_8201_Sviridov_Cam.Vector;
import FIT_8201_Sviridov_Cam.Vertex;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author admin
 */
public class WireframeShape {

    private static final Color DEFAULT_COLOR = Color.white;
    private static final int DEFAULT_WIDTH = 1;
    private List<Segment> segments;
    private Color color = DEFAULT_COLOR;
    private int width = DEFAULT_WIDTH;
    private CoordinateSystem coordinateSystem;
    private Transformation transformation;

    /**
     * Default ctor with given segments given in local WireframeShape model
     * @param segments
     */
    public WireframeShape(List<Segment> segments) {
        this.segments = segments;
        this.coordinateSystem = new CoordinateSystem();
        transformation = this.coordinateSystem.getFrameToCanonicalTransformation();
    }

    /**
     * Returns segments of the shape after Transformation application
     * @return
     */
    public List<Segment> getTransformedSegments() {
        List<Segment> result = new ArrayList<Segment>(this.segments.size());
        for (Segment s : segments) {
            Segment newSegment = new Segment(transformation.apply(s.getStartPoint()), transformation.apply(s.getEndPoint()));
            result.add(newSegment);
        }
        return Collections.unmodifiableList(result);
    }

    public Rect3D getBoundRect3D() {
        if (segments.isEmpty()) {
            return new Rect3D(0, 0, 0);
        }

        double maxX = Double.NEGATIVE_INFINITY, minX = Double.POSITIVE_INFINITY,
                maxY = Double.NEGATIVE_INFINITY, minY = Double.POSITIVE_INFINITY,
                maxZ = Double.NEGATIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;

        for (Segment s : getTransformedSegments()) {
            Vertex start = s.getStartPoint(),
                    end = s.getEndPoint();
            double sx = start.getX(),
                    sy = start.getY(),
                    sz = start.getZ(),
                    ex = end.getX(),
                    ey = end.getY(),
                    ez = end.getZ();

            maxX = Math.max(sx, Math.max(ex, maxX));
            maxY = Math.max(sy, Math.max(ey, maxY));
            maxZ = Math.max(sz, Math.max(ez, maxZ));

            minX = Math.min(sx, Math.min(ex, minX));
            minY = Math.min(sy, Math.min(ey, minY));
            minZ = Math.min(sz, Math.min(ez, minZ));
        }

        return new Rect3D(maxX - minX, maxY - minY, maxZ - minZ);
    }

    public static WireframeShape cube(double length) {
        double p = length / 2;
        double m = -p;

        List<Segment> cube = new ArrayList<Segment>(12);

        Vertex mmm = new Vertex(m, m, m),
                mmp = new Vertex(m, m, p),
                mpm = new Vertex(m, p, m),
                mpp = new Vertex(m, p, p),
                pmm = new Vertex(p, m, m),
                pmp = new Vertex(p, m, p),
                ppm = new Vertex(p, p, m),
                ppp = new Vertex(p, p, p);

        cube.add(new Segment(mmm, mmp));
        cube.add(new Segment(mmm, pmm));
        cube.add(new Segment(mmm, mpm));


        cube.add(new Segment(pmm, ppm));
        cube.add(new Segment(pmm, pmp));

        cube.add(new Segment(mmp, mpp));
        cube.add(new Segment(mmp, pmp));

        cube.add(new Segment(mpm, ppm));
        cube.add(new Segment(mpm, mpp));

        cube.add(new Segment(mpp, ppp));
        cube.add(new Segment(ppm, ppp));
        
        cube.add(new Segment(pmp, ppp));

        return new WireframeShape(cube);
    }

    public static WireframeShape parallelepiped(
            double width, double height, double depth) {
        double wp = width / 2,
                wm = -wp,
                hp = height / 2,
                hm = -hp,
                dp = depth / 2,
                dm = -dp;

        Vertex mmm = new Vertex(wm, hm, dm),
                mmp = new Vertex(wm, hm, dp),
                mpm = new Vertex(wm, hp, dm),
                mpp = new Vertex(wm, hp, dp),
                pmm = new Vertex(wp, hm, dm),
                pmp = new Vertex(wp, hm, dp),
                ppm = new Vertex(wp, hp, dm),
                ppp = new Vertex(wp, hp, dp);

        List<Segment> parallelepiped = new ArrayList<Segment>(12);

        parallelepiped.add(new Segment(mmm, mmp));
        parallelepiped.add(new Segment(mmm, pmm));
        parallelepiped.add(new Segment(mmm, mpm));


        parallelepiped.add(new Segment(pmm, ppm));
        parallelepiped.add(new Segment(pmm, pmp));

        parallelepiped.add(new Segment(mmp, mpp));
        parallelepiped.add(new Segment(mmp, pmp));

        parallelepiped.add(new Segment(mpm, ppm));
        parallelepiped.add(new Segment(mpm, mpp));

        parallelepiped.add(new Segment(mpp, ppp));
        parallelepiped.add(new Segment(ppm, ppp));

        parallelepiped.add(new Segment(pmp, ppp));

        return new WireframeShape(parallelepiped);
    }

    public static WireframeShape segment(
            double x, double y, double z) {
        List<Segment> segment = new ArrayList<Segment>(1);
        segment.add(new Segment(new Vertex(0, 0, 0),
                new Vertex(x, y, z)));
        return new WireframeShape(segment);
    }

    public void setBasis(Vector v1, Vector v2, Vector v3) {
        coordinateSystem.setBasis(v1, v2, v3);
        transformation = coordinateSystem.getFrameToCanonicalTransformation();
    }

    public void setOrigin(Vertex origin) {
        coordinateSystem.setOrigin(origin);
        transformation = coordinateSystem.getFrameToCanonicalTransformation();
    }

    public Vertex getTransformedOrigin() {
        return transformation.apply(coordinateSystem.getOrigin());
    }


    public Vertex getOrigin() {
        return coordinateSystem.getOrigin();
    }

    private Transformation getFrameToCanonicalTransformation() {
        return coordinateSystem.getFrameToCanonicalTransformation();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void transform(Transformation transformation) {
        this.transformation = Transformation.compose(transformation, this.transformation);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Segment s : getTransformedSegments()) {
            sb.append(s);
            sb.append('\n');
        }
        return sb.toString();
    }
}
