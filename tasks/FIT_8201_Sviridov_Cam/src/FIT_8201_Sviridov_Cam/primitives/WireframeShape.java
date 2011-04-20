package FIT_8201_Sviridov_Cam.primitives;

import FIT_8201_Sviridov_Cam.CoordinateSystem;
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

    private static final Color DEFAULT_COLOR = Color.black;
    private List<Segment> segments;
    private Color color = DEFAULT_COLOR;
    private CoordinateSystem coordinateSystem;

    public WireframeShape(List<Segment> segments) {
        this.segments = segments;
        this.coordinateSystem = new CoordinateSystem();
    }

    public List<Segment> getSegments() {
        return Collections.unmodifiableList(segments);
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

        cube.add(new Segment(mmm, mpp));
        cube.add(new Segment(mmm, mmp));
        cube.add(new Segment(mmm, pmm));

        cube.add(new Segment(ppp, mpp));
        cube.add(new Segment(ppp, ppm));
        cube.add(new Segment(ppp, pmm));

        cube.add(new Segment(ppm, mpm));
        cube.add(new Segment(ppm, pmp));

        cube.add(new Segment(mpm, mpp));
        cube.add(new Segment(mpm, mmp));

        cube.add(new Segment(pmp, pmm));
        cube.add(new Segment(pmp, mmp));

        return new WireframeShape(cube);
    }

    public static WireframeShape parallelepiped(
            double maxX, double minX,
            double maxY, double minY,
            double maxZ, double minZ) {

        double width = maxX - minX,
                height = maxY - minY,
                depth = maxZ - minZ;

        double wp = width / 2,
                wm = -width,
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

        parallelepiped.add(new Segment(mmm, mpp));
        parallelepiped.add(new Segment(mmm, mmp));
        parallelepiped.add(new Segment(mmm, pmm));

        parallelepiped.add(new Segment(ppp, mpp));
        parallelepiped.add(new Segment(ppp, ppm));
        parallelepiped.add(new Segment(ppp, pmm));

        parallelepiped.add(new Segment(ppm, mpm));
        parallelepiped.add(new Segment(ppm, pmp));

        parallelepiped.add(new Segment(mpm, mpp));
        parallelepiped.add(new Segment(mpm, mmp));

        parallelepiped.add(new Segment(pmp, pmm));
        parallelepiped.add(new Segment(pmp, mmp));

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
    }

    public void setOrigin(Vertex origin) {
        coordinateSystem.setOrigin(origin);
    }

    public Vertex getOrigin() {
        return coordinateSystem.getOrigin();
    }

    public Transformation getFrameToCanonicalTransformation() {
        return coordinateSystem.getFrameToCanonicalTransformation();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Segment s : segments) {
            sb.append(s);
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String args[]) {
        class Canvas extends JPanel implements ActionListener {

            double v1[] = new double[]{-250, -100, 1},
                    v2[] = new double[]{350, -100, 1},
                    v3[] = new double[]{-250, 250, 1};
            double vertices[][] = new double[][]{v1, v2, v3};
            double phi = 0.01;
            double m[][] = new double[][]{
                new double[]{Math.cos(phi), -Math.sin(phi), 0},
                new double[]{Math.sin(phi), Math.cos(phi), 0},
                new double[]{0, 0, 1}};

            public Canvas() {
                setPreferredSize(new Dimension(500, 500));
                Timer timer = new Timer(1000 / 25, this);
                timer.start();
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                for (double v[] : vertices) {
                    double vprime[] = new double[v.length];
                    for (int i = 0; i < v1.length; ++i) {
                        vprime[i] = m[i][0] * v[0]
                                + m[i][1] * v[1]
                                + m[i][2] * v[2];
                    }
                    for (int i = 0; i < v.length; ++i) {
                        v[i] = vprime[i];
                    }
                }
                repaint();
            }

            @Override
            protected void paintComponent(Graphics g1) {
                super.paintComponent(g1);
                Graphics2D g = (Graphics2D) g1;
                g.translate(getWidth() / 2, getHeight() / 2);
                g.scale(1, -1);
                int ix[] = new int[]{(int) (v1[0] + 0.5), (int) (v2[0] + 0.5), (int) (v3[0] + 0.5)};
                int iy[] = new int[]{(int) (v1[1] + 0.5), (int) (v2[1] + 0.5), (int) (v3[1] + 0.5)};
                g.drawPolygon(ix, iy, Math.min(ix.length, iy.length));
            }
        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Canvas());
        frame.pack();
        frame.setVisible(true);

    }
}
