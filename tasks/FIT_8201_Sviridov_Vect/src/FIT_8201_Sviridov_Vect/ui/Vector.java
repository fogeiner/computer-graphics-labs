package FIT_8201_Sviridov_Vect.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 *
 * @author admin
 */
public class Vector {

    static public final double DEFAULT_DX = 0.3;
    static public final double DEFAULT_DY = 0.2;
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
        double length = Math.hypot(v.getX(), v.getY());

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
            stroke = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f);

        }

        Stroke oldStroke = g.getStroke();
        Color oldColor = g.getColor();
        g.setColor(color);
        g.setStroke(stroke);

        if (filled) {
            g.fill(closedVector);
            g.draw(closedVector);
        } else {
            g.draw(openVector);
        }

        g.setColor(oldColor);
        g.setStroke(oldStroke);
    }

}
