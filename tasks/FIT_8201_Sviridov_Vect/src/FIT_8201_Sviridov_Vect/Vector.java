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
    static public final double DEFAULT_ARROW_THRESHOLD = 7;
    private double _length_threshold = DEFAULT_ARROW_THRESHOLD;
    private Point _start;
    private Point _end;
    private double _dx = DEFAULT_DX;
    private double _dy = DEFAULT_DY;
    private Stroke _stroke;
    private Path2D _open_arrow;
    private Path2D _closed_arrow;
    private Path2D _line;
    private Color _color;
    private boolean _filled;

    private void computePath() {

        Point vector = new Point(_end.x - _start.x, _end.y - _start.y);
        Point n = new Point(-vector.y, vector.x);
        double length = Math.sqrt(vector.x * vector.x + vector.y * vector.y);

        Point2D p = new Point2D.Double(
                _end.x - _dx * vector.x, _end.y - _dx * vector.y);

        Point p1 = new Point(
                (int) (p.getX() + _dy * n.x + 0.5), (int) (p.getY() + _dy * n.y + 0.5));
        Point p2 = new Point(
                (int) (p.getX() - _dy * n.x + 0.5), (int) (p.getY() - _dy * n.y + 0.5));

        _open_arrow = new Path2D.Double();
        _closed_arrow = new Path2D.Double();
        _line = new Path2D.Double();


        _line.moveTo(_start.x, _start.y);
        _line.lineTo(_end.x, _end.y);


        if (length > _length_threshold) {
            _open_arrow.moveTo(p1.getX(), p1.getY());
            _open_arrow.lineTo(_end.x, _end.y);
            _open_arrow.lineTo(p2.getX(), p2.getY());

            _closed_arrow.moveTo(p1.getX(), p1.getY());
            _closed_arrow.lineTo(_end.x, _end.y);
            _closed_arrow.lineTo(p2.getX(), p2.getY());
            _closed_arrow.closePath();
        }
    }

    public Vector(Point start, Point end) {
        _start = start;
        _end = end;

        computePath();
    }

    public boolean isFilled() {
        return _filled;
    }

    public void setFilled(boolean filled) {
        this._filled = filled;
    }

    public double getArrowThreshold() {
        return _length_threshold;
    }

    public void setArrowThreshold(double arrow_threshold) {
        this._length_threshold = arrow_threshold;
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
            _stroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0.0f);

        }


        Stroke old_stroke = g.getStroke();
        g.setColor(_color);

        g.setStroke(_stroke);

        if (_filled) {
            g.fill(_closed_arrow);
            g.draw(_closed_arrow);
            g.draw(_line);
        } else {
            g.draw(_line);
            g.draw(_open_arrow);
        }

        g.setStroke(old_stroke);
    }

    public static void main(String args[]) {
        class Canvas extends JPanel {

            private List<Vector> _vectors = new LinkedList<Vector>();

            public Canvas() {
                setBackground(Color.white);


                Vector v = new Vector(new Point(2, 4), new Point(10, 4));
                v.setColor(Color.red);
                _vectors.add(v);
                v = new Vector(new Point(4, 8), new Point(12, 8));
                v.setColor(Color.blue);
                _vectors.add(v);
                v = new Vector(new Point(6, 12), new Point(14, 12));
                v.setColor(Color.cyan);

                _vectors.add(v);
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
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
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
