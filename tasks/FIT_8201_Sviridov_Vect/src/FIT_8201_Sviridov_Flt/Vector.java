package FIT_8201_Sviridov_Vect;

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
    static public final double DEFAULT_ARROW_THRESHOLD = 5;
    
    private double _arrow_threshold = DEFAULT_ARROW_THRESHOLD;
    private Point _start;
    private Point _end;
    private double _dx = DEFAULT_DX;
    private double _dy = DEFAULT_DY;
    private Stroke _stroke;
    private Path2D _path;
    private Color _color;

    private void computePath() {
        Point2D vector = new Point2D.Double(
                _end.getX() - _start.getX(),
                _end.getY() - _start.getY());

        double length = Math.sqrt(
                vector.getX() * vector.getX()
                + vector.getY() * vector.getY());

        Point2D n = new Point2D.Double(
                -vector.getY(), vector.getX());
        Point2D p = new Point2D.Double(
                _end.getX() - _dx * vector.getX(), _end.getY() - _dx * vector.getY());

        Point p1 = new Point(
                (int) (p.getX() + _dy * n.getX() + 0.5), (int) (p.getY() + _dy * n.getY() + 0.5));
        Point p2 = new Point(
                (int) (p.getX() - _dy * n.getX() + 0.5), (int) (p.getY() - _dy * n.getY() + 0.5));

        _path = new Path2D.Double();
        _path.moveTo(_start.getX(), _start.getY());
        _path.lineTo(_end.getX(), _end.getY());
        if (length > _arrow_threshold) {
            _path.moveTo(p1.getX(), p1.getY());
            _path.lineTo(_end.getX(), _end.getY());
            _path.lineTo(p2.getX(), p2.getY());
        }
    }

    public Vector(Point start, Point end) {
        _start = start;
        _end = end;

        computePath();
    }

    public double getArrowThreshold() {
        return _arrow_threshold;
    }

    public void setArrowThreshold(double arrow_threshold) {
        this._arrow_threshold = arrow_threshold;
        computePath();
    }

    public double getDx() {
        return _dx;
    }

    public void setDx(double dx) {
        this._dx = dx;
        computePath();
    }

    public double getDy() {
        return _dy;
    }

    public void setDy(double dy) {
        this._dy = dy;
        computePath();
    }

    public Point getEnd() {
        return _end;
    }

    public void setEnd(Point end) {
        this._end = end;
        computePath();
    }

    public Point getStart() {
        return _start;
    }

    public void setStart(Point start) {
        this._start = start;
        computePath();
    }

    public Color getColor() {
        return _color;
    }

    public void setColor(Color color) {
        this._color = color;
    }

    public void draw(Graphics2D g) {
        if (_stroke == null) {
            _stroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 5.0f);
        }
        Stroke old_stroke = g.getStroke();
        g.setStroke(_stroke);
        g.setColor(_color);

        g.draw(_path);

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
                        addVector(new Vector(_start, e.getPoint()));
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
