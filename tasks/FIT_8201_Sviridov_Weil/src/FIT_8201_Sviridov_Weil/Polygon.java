package FIT_8201_Sviridov_Weil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Class incapsulating polygon with such properties as: color, thickness and points
 * Based on <code>Point2D.Double</code>
 * @author alstein
 */
public class Polygon {

    public static double INF_POINT_COORD = 10E6;
    private List<Point2D> _points = new ArrayList<Point2D>();
    Stroke _stroke = null;
    private Color _color;
    private int _thickness;

    /**
     * Determines if the given point (x,y) lies inside of the Polygon
     * using even-odd method
     * @param x x coordinate
     * @param y y coordinate
     * @return true if point lies inside, false otherwise
     */
    public boolean isInside(int x, int y) {
        return isInside(new Point2D.Double(x, y));
    }

    /**
     * Determines if the given point <code>p</code> lies inside of the Polygon
     * using even-odd method
     * @param p point to check
     * @return true if point lies inside, false otherwise
     */
    public boolean isInside(Point2D p) {
        int size = _points.size();
        if (size < 2) {
            return false;
        }

        int intersections_number = 0;
        // selecting direction
        Point2D dp = new Point2D.Double(INF_POINT_COORD, INF_POINT_COORD);

        for (int i = 1; i < size + 1; ++i) {
            Point2D p1 = _points.get(i - 1);
            Point2D p2;

            if (i == size) {
                p2 = _points.get(0);
            } else {
                p2 = _points.get(i);
            }
            Point2D intersection_point = EuclideanGeometry.getIntersection(p, dp, p1, p2);

            if (intersection_point != null) {
                intersections_number++;
            }
        }

        if (intersections_number % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Constructor with out parameters
     * color and thickness field are set to default values black and 1
     */
    public Polygon() {
        _color = Color.black;
        _thickness = 1;
    }

    /**
     * Constructor with given color and thickness
     * @param color inital color
     * @param thickness initial thickness
     */
    public Polygon(Color color, int thickness) {
        _color = new Color(color.getRGB());
        _thickness = thickness;
    }

    /**
     * Clears all points of the polygon
     */
    public void clear() {
        _points.clear();
    }

    /**
     * Replaces points with <code>p</code> Polygon's points
     */
    public void setPoints(Polygon p) {
        this._points = p._points;
    }

    /**
     * Adds another point to the <code>Polygon</code>
     * @param x x coordinate
     * @param y y coordinate
     */
    public void addPoint(int x, int y) {
        addPoint(new Point2D.Double(x, y));
    }

    /**
     * Adds another point to the <code>Polygon</code>
     * @param p new point to be added
     */
    public void addPoint(Point2D p) {
        _points.add(p);
    }

    /**
     * Draws Polygon on the given <code>Graphics2D</code> object
     * @param g2 Graphics2D object to draw on
     */
    public void draw(Graphics2D g2) {
        if (_stroke == null) {
            _stroke = new BasicStroke(_thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        }

        g2.setColor(_color);
        g2.setStroke(_stroke);

        int size = _points.size();

        for (int i = 1; i < size + 1; ++i) {
            int x1, x2, y1, y2;

            Point2D p1 = _points.get(i - 1);
            Point2D p2;

            if (i == size) {
                p2 = _points.get(0);
            } else {
                p2 = _points.get(i);
            }

            x1 = (int) (p1.getX() + 0.5);
            y1 = (int) (p1.getY() + 0.5);
            x2 = (int) (p2.getX() + 0.5);
            y2 = (int) (p2.getY() + 0.5);

            g2.drawLine(x1, y1, x2, y2);
        }

    }

    /**
     * Returns current thickness of Polygon
     * @return current thickness of Polygon
     */
    public int getThickness() {
        return _thickness;


    }

    /**
     * Sets thickness of Polygon
     * @param thickness new thickness of Polygon
     */
    public void setThickness(int thickness) {
        _thickness = thickness;


    }

    /**
     * Returns current color of Polygon
     * @return current color of Polygon
     */
    public Color getColor() {
        return _color;


    }

    /**
     * Sets color of Polygon
     * @param color new color of Polygon
     */
    public void setColor(Color color) {
        _color = new Color(color.getRGB());


    }

    /**
     * Returns polygon object in standard representation: number of points,
     * coordinates of points
     * @return String with standard polyline representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_points.size());
        sb.append("\r\n");

        for (Point2D p : _points) {
            int x = (int) (p.getX() + 0.5);
            int y = (int) (p.getY() + 0.5);

            sb.append(x);
            sb.append(" ");
            sb.append(y);
            sb.append("\r\n");
        }

        return sb.toString();
    }
}
