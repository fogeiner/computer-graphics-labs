package FIT_8201_Sviridov_Vect.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;

/**
 *
 * @author admin
 */
public class Vector {

    static public final double DEFAULT_DX = 0.4;
    static public final double DEFAULT_DY = 0.2;
    static public final double DEFAULT_ARROW_THRESHOLD = 7;
    private double lengthThreshold = DEFAULT_ARROW_THRESHOLD;
    private Point2D start;
    private Point2D end;
    private double dx = DEFAULT_DX;
    private double dy = DEFAULT_DY;
    private Stroke stroke;
    private Color color;
    private boolean filled;
    int lineX[] = new int[2];
    int lineY[] = new int[2];
    int arrowX[] = new int[3];
    int arrowY[] = new int[3];
    double length;

    private void computePath() {
        if (start == null || end == null) {
            return;
        }
        double ex = end.getX(), ey = end.getY(),
                sx = start.getX(), sy = start.getY(),
                vx = ex - sx, vy = ey - sy,
                nx = -vy, ny = vx;

        length = Math.hypot(vx, vy);

        double px = ex - dx * vx, py = ey - dx * vy,
                p1x = px + dy * nx, p1y = py + dy * ny,
                p2x = px - dy * nx, p2y = py - dy * ny;

        lineX[0] = (int) (sx + 0.5);
        lineY[0] = (int) (sy + 0.5);

        lineX[1] = (int) (ex + 0.5);
        lineY[1] = (int) (ey + 0.5);

        arrowX[0] = (int) (p1x + 0.5);
        arrowY[0] = (int) (p1y + 0.5);

        arrowX[1] = lineX[1];
        arrowY[1] = lineY[1];

        arrowX[2] = (int) (p2x + 0.5);
        arrowY[2] = (int) (p2y + 0.5);
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

        g.drawPolygon(lineX, lineY, 2);
        if (length > lengthThreshold) {
            if (filled) {
                g.fillPolygon(arrowX, arrowY, 3);
            } else {
                g.drawPolyline(arrowX, arrowY, 3);
            }
        }

        g.setColor(oldColor);
        g.setStroke(oldStroke);
    }
}
