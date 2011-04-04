package FIT_8201_Sviridov_Vect.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class Vector {

    static public final double DEFAULT_DX = 0.3;
    static public final double DEFAULT_DY = 0.15;
    static public final double DEFAULT_ARROW_THRESHOLD = 7;
    private double lengthThreshold = DEFAULT_ARROW_THRESHOLD;
    private Point2D start;
    private Point2D end;
    private double dx = DEFAULT_DX;
    private double dy = DEFAULT_DY;
    private Stroke stroke;
    private GeneralPath openVector;
    private GeneralPath closedVector;
    private Color color;
    private boolean filled;

    private void computePath() {
        if (start == null || end == null) {
            return;
        }

        openVector = new GeneralPath();
        closedVector = new GeneralPath();

        Point2D v = new Point2D.Double(end.getX() - start.getX(), end.getY() - start.getY());
        Point2D n = new Point2D.Double(-v.getY(), v.getX());
        double length = Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY());

        Point2D p = new Point2D.Double(
                end.getX() - dx * v.getX(), end.getY() - dx * v.getY());

        Point2D p1 = new Point2D.Double(
                p.getX() + dy * n.getX(), p.getY() + dy * n.getY());
        Point2D p2 = new Point2D.Double(
                p.getX() - dy * n.getX(), p.getY() - dy * n.getY());

        Path2D line = new Path2D.Double(new Line2D.Double(
                start.getX(), start.getY(),
                end.getX(), end.getY()));

        Path2D s1 = new Path2D.Double(new Line2D.Double(
                p1.getX(), p1.getY(),
                end.getX(), end.getY()));

        Path2D s2 = new Path2D.Double(new Line2D.Double(
                end.getX(), end.getY(),
                p2.getX(), p2.getY()));

        Path2D s3 = new Path2D.Double(new Line2D.Double(
                p2.getX(), p2.getY(),
                p1.getX(), p1.getY()));


        openVector.append(line, true);

        closedVector.append(line, true);
        if (length > lengthThreshold) {
            openVector.append(s1, false);
            openVector.append(s2, true);


            closedVector.append(s2, true);
            closedVector.append(s3, true);
            closedVector.append(s1, true);
        }
    }

    public Vector(Point2D start, Point2D end) {
        this.start = start;
        this.end = end;

        computePath();
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public double getArrowThreshold() {
        return lengthThreshold;
    }

    public void setArrowThreshold(double arrow_threshold) {
        this.lengthThreshold = arrow_threshold;
        computePath();
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
        computePath();
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
        computePath();
    }

    public Point2D getEnd() {
        return end;
    }

    public void setEnd(Point2D end) {
        this.end = end;
        computePath();
    }

    public Point2D getStart() {
        return start;
    }

    public void setStart(Point2D start) {
        this.start = start;
        computePath();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw(Graphics2D g) {
        if (stroke == null) {
            stroke = new BasicStroke(0.5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0.0f);

        }

        Stroke old_stroke = g.getStroke();
        g.setColor(color);

        g.setStroke(stroke);

        if (filled) {
            g.fill(closedVector);
            g.draw(closedVector);
        } else {
            g.draw(openVector);
        }

        g.setStroke(old_stroke);
    }

    public static void main(String args[]) {
        class Canvas extends JPanel {

            private List<Vector> _vectors = new LinkedList<Vector>();

            public Canvas() {
                setBackground(Color.white);

                addMouseListener(new MouseAdapter() {

                    private Point _start;

                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        _start = e.getPoint();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        super.mouseReleased(e);
                        Vector v = new Vector(_start, e.getPoint());
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            v.setFilled(false);
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            v.setFilled(true);
                        } else {
                            return;
                        }

                        addVector(v);
                        repaint();
                    }
                });
            }

            private void addVector(Vector v) {
                _vectors.add(v);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                for (Vector v : _vectors) {
                    v.draw(g2);
                }
            }
        }

        JFrame frame = new JFrame();
        frame.setSize(new Dimension(600, 400));
        frame.setLayout(new BorderLayout());
        frame.add(new Canvas(), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
