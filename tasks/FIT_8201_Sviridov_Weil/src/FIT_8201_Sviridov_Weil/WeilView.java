package FIT_8201_Sviridov_Weil;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Canvas class of application. Does drawing and mouse tracking.
 * 
 * @author alstein
 * 
 */
public class WeilView extends JPanel implements WeilSettings {

    private static final long serialVersionUID = -456307332612783435L;
    // double buffer image
    private BufferedImage _offscreen;
    private static int IMAGE_WIDTH = 2000;
    private static int IMAGE_HEIGHT = 1500;
    private boolean _full_repaint;
    // drawing properties
    private int _refresh_period = 50;
    private FrameService _weil_frame;
    private boolean _rubber_line = false;
    private final Object _monitor = new Object();
    // app data
    private Polygon _current_polygon;
    private Polygon _subject_polygon = new Polygon(Polygon.COUNTERCLOCKWISE_ORIENTATION, WeilSettings.DEFAULT_SUBJECT_COLOR, WeilSettings.DEFAULT_SUBJECT_THICKNESS);
    private Polygon _hole_polygon = new Polygon(Polygon.CLOCKWISE_ORIENTATION, WeilSettings.DEFAULT_SUBJECT_COLOR, WeilSettings.DEFAULT_SUBJECT_THICKNESS);
    private Polygon _clip_polygon = new Polygon(Polygon.COUNTERCLOCKWISE_ORIENTATION, WeilSettings.DEFAULT_CLIP_COLOR, WeilSettings.DEFAULT_CLIP_THICKNESS);
    private Color _intersecting_polygons_color = WeilSettings.DEFAULT_INTERSECTING_COLOR;
    private int _intersecting_polygons_thickness = WeilSettings.DEFAULT_INTERSECTING_THICKNESS;
    private List<Polygon> _intersecting_polygons = new ArrayList<Polygon>();
    // state machine data
    private final static int VIEW_STATE = 0;
    private final static int EDIT_STATE = 1;
    private int _state = VIEW_STATE;

    /**
     * Paints background, polylines and rubber line
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D front = (Graphics2D) g;

        front.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        front.translate(0, this.getHeight() - 1);
        front.scale(1, -1);

        if (_current_polygon != _subject_polygon) {
            _subject_polygon.draw(front);
        }
        if (_current_polygon != _hole_polygon) {
            _hole_polygon.draw(front);
        }
        if (_current_polygon != _clip_polygon) {
            _clip_polygon.draw(front);
        }

        if (_current_polygon != null) {
            _current_polygon.drawPartial(front);
            if (_rubber_line) {
                Point pointer_location = MouseInfo.getPointerInfo().getLocation();
                pointer_location.x = pointer_location.x - this.getLocationOnScreen().x;
                pointer_location.y = pointer_location.y - this.getLocationOnScreen().y;
                pointer_location.y = getHeight() - pointer_location.y;
                _current_polygon.drawLine(front, pointer_location);
            }
        }

        for (Polygon p : _intersecting_polygons) {
            p.draw(front);
        }


    }

    /**
     * Makes made model changed visible
     */
    @Override
    public void modelLoaded() {
        _weil_frame.pack();
        checkIntersectAbility();
        fullRepaint(true);
        repaint();
    }

    /**
     * Sets state to <code>state</code> state
     *
     * @param state new state
     */
    private void setState(int state) {
        _state = state;
    }

    /**
     * Returns state
     *
     * @return current state
     */
    private int getState() {
        return _state;
    }

    /**
     * Returns true if full repaint was requested; false otherwise
     *
     * @return <code>true</code> if full repaint was requested;
     *         <code>false</code> otherwise
     */
    public boolean isFullRepaint() {
        return _full_repaint;
    }

    /**
     * Plan full repainting of the picture during next repaint
     *
     * @param value
     *            true to enqueue, false to reset
     */
    public void fullRepaint(boolean value) {
        _full_repaint = value;
    }

    /**
     * Resets canvas
     */
    public void reset() {
        _subject_polygon.clear();
        _clip_polygon.clear();
        _hole_polygon.clear();
        _intersecting_polygons.clear();
        fullRepaint(true);
        repaint();
    }

    /**
     * Method called when user chooses "Draw subject polygon" in menu or on toolbar.
     */
    public void onSubject() {
        switchToPolygon(_subject_polygon);
    }

    /**
     * Method called when user chooses "Draw hole polygon" in menu or on toolbar.
     */
    public void onHole() {
        switchToPolygon(_hole_polygon);
    }

    /**
     * Method called when user chooses "Draw clip polygon" in menu or on toolbar.
     */
    public void onClip() {
        switchToPolygon(_clip_polygon);
    }

    /**
     * Sets <code>p</code> Polygon as current polygon for modification
     * and blocks frame buttons
     * @param p
     */
    private void switchToPolygon(Polygon p) {
        _weil_frame.setIntersectBlocked(true);
        _current_polygon = p;
        _current_polygon.clear();
        _weil_frame.setBlocked(true);
        synchronized (_monitor) {
            _rubber_line = true;
            _monitor.notifyAll();
        }
        setState(EDIT_STATE);
    }

    /**
     * Checks if intersect operation is possible
     * and make call to block/unblock intersect button
     */
    private void checkIntersectAbility() {
        if (!_subject_polygon.isEmpty() && !_clip_polygon.isEmpty()) {
            _weil_frame.setIntersectBlocked(false);
        } else {
            _weil_frame.setIntersectBlocked(true);
        }
    }

    /**
     * Method called when user chooses "Intersect" in menu or on toolbar.
     */
    public void onIntersect() {
        if (!_hole_polygon.isEmpty() && !_subject_polygon.isInside(_hole_polygon)) {
            JOptionPane.showMessageDialog(WeilView.this, "Hole polygon must be inside subject polygon");
            return;
        }

        for (Polygon p : _intersecting_polygons) {
            p.clear();
        }

        List<OrientedVertex> in_vertices = new ArrayList<OrientedVertex>();

        // transform polygons to OrientedVertex structure
        OrientedVertex s = _subject_polygon.getFirstOrientedVertex();
        OrientedVertex c = _clip_polygon.getFirstOrientedVertex();
        OrientedVertex h = _hole_polygon.getFirstOrientedVertex();

        if (!_hole_polygon.isEmpty()) {
            OrientedVertex.intersect(h, c, null);
        }

        OrientedVertex.intersect(s, c, null);

        if (!_hole_polygon.isEmpty()) {
            h.printPath();
        }
        c.printPath();

        drawPoints(c);
        //    drawPoints(s);
        //   drawPoints(h);

    }

    private void drawPoints(OrientedVertex v) {
        Graphics g = (Graphics2D) getGraphics();
        int i = 0;
        g.setColor(Color.blue);
        do {
            g.fillOval((int) (v.getPoint().getX() + 0.5) - 2, getHeight() - (int) (v.getPoint().getY() + 0.5) - 2, 5, 5);
            g.drawString(Integer.toString(i++), (int) (v.getPoint().getX() + 0.5), getHeight() - (int) (v.getPoint().getY() + 0.5));
            v = v.getNext();

        } while (!v.isFirst());
    }

    /**
     * Constructor with reference to LinesFrame
     *
     * @param lines_frame
     *            application main frame
     */
    public WeilView(FrameService weil_frame) {
        _weil_frame = weil_frame;
        setBackground(Color.white);

        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                if (_offscreen == null) {
                    _offscreen = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,
                            BufferedImage.TYPE_INT_ARGB);
                    fullRepaint(true);
                } else {
                    if (getWidth() > _offscreen.getWidth(null)
                            || getHeight() > _offscreen.getHeight(null)) {
                        _offscreen = new BufferedImage(getWidth(), getHeight(),
                                BufferedImage.TYPE_INT_ARGB);
                        fullRepaint(true);
                    }
                }

                repaint();
            }
        });

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (true) {
                        synchronized (WeilView.this._monitor) {
                            if (WeilView.this._rubber_line == false) {
                                _monitor.wait();
                                continue;
                            }
                        }
                        WeilView.this.repaint();
                        Thread.sleep(WeilView.this._refresh_period);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent event) {
                if (getState() != EDIT_STATE) {
                    return;
                }

                int button = event.getButton();
                Point point = event.getPoint();
                point = new Point(point.x, getHeight() - point.y);

                // left mouse click
                if (button == MouseEvent.BUTTON1) {
                    if (_current_polygon.isPointValid(point)) {
                        _current_polygon.addPoint(point);
                    } else {
                        JOptionPane.showMessageDialog(WeilView.this, "Invalid point: check self-intersections and orientation");
                        return;
                    }

                } else if (button == MouseEvent.BUTTON3) {
                    if (_current_polygon.verticesCount() < 3) {
                        _current_polygon.clear();
                    }

                    synchronized (_monitor) {
                        _rubber_line = false;
                        _monitor.notifyAll();
                    }
                    _current_polygon = null;
                    setState(VIEW_STATE);
                    _weil_frame.setModified(true);
                    _weil_frame.setBlocked(false);
                    checkIntersectAbility();
                }

                repaint();
            }
        });
    }

    /**
     * Returns current color of subject polygon
     *
     * @return color of subject polygon
     */
    @Override
    public Color getSubjectPolygonColor() {
        return _subject_polygon.getColor();
    }

    /**
     * Returns current color of clip polygon
     *
     * @return color of clip polygon
     */
    @Override
    public Color getClipPolygonColor() {
        return _clip_polygon.getColor();
    }

    /**
     * Returns current color of intersecting polygon
     *
     * @return color of intersecting polygon
     */
    @Override
    public Color getIntersectingPolygonColor() {
        return _intersecting_polygons_color;
    }

    /**
     * Sets subject polygon color
     *
     * @param color
     *            Color to be set as subject polygon color
     */
    @Override
    public void setSubjectPolygonColor(Color color) {
        _subject_polygon.setColor(color);
        _hole_polygon.setColor(color);
    }

    /**
     * Sets clip polygon color
     *
     * @param color
     *            Color to be set as clip polygon color
     */
    @Override
    public void setClipPolygonColor(Color color) {
        _clip_polygon.setColor(color);
    }

    /**
     * Sets subject polygon color
     *
     * @param color
     *            Color to be set as intersecting polygon color
     */
    @Override
    public void setIntersectingPolygonColor(Color color) {
        _intersecting_polygons_color = color;
    }

    /**
     * Returns current thickness of subject polygon
     *
     * @return thickness of subject polygon
     */
    @Override
    public int getSubjectPolygonThickness() {
        return _subject_polygon.getThickness();
    }

    /**
     * Returns current thickness of clip polygon
     *
     * @return thickness of clip polygon
     */
    @Override
    public int getClipPolygonThickness() {
        return _clip_polygon.getThickness();
    }

    /**
     * Returns current thickness of intersecting polygon
     *
     * @return thickness of intersecting polygon
     */
    @Override
    public int getIntersectingPolygonThickness() {
        return _intersecting_polygons_thickness;
    }

    /**
     * Sets subject polygon thickness
     *
     * @param thickness
     *            thickness to be set as subject polygon thickness
     */
    @Override
    public void setSubjectPolygonThickness(int thickness) {
        _subject_polygon.setThickness(thickness);
        _hole_polygon.setThickness(thickness);
    }

    /**
     * Sets clip polygon thickness
     *
     * @param thickness
     *            thickness to be set as clip polygon thickness
     */
    @Override
    public void setClipPolygonThickness(int thickness) {
        _clip_polygon.setThickness(thickness);
    }

    /**
     * Sets subject polygon thickness
     *
     * @param thickness
     *            thickness to be set as intersecting polygon thickness
     */
    public void setIntersectingPolygonThickness(int thickness) {
        _intersecting_polygons_thickness = thickness;
    }

    /**
     * Sets <code>p</code> as a subject polygon of the model
     * @param p polygon to become subject polygon of the model
     */
    public void setSubjectPolygon(Polygon p) {
        _subject_polygon.clear();
        _subject_polygon.setPoints(p);
    }

    /**
     * Sets <code>p</code> as a hole polygon of the model
     * @param p polygon to become hole polygon of the model
     */
    public void setHolePolygon(Polygon p) {
        _hole_polygon.clear();
        _hole_polygon.setPoints(p);
    }

    /**
     * Sets <code>p</code> as a clip polygon of the model
     * @param p polygon to become clip polygon of the model
     */
    public void setClipPolygon(Polygon p) {
        _clip_polygon.clear();
        _clip_polygon.setPoints(p);
    }

    /**
     * Returns  a subject polygon of the model
     * @return subject polygon of the model
     */
    public Polygon getSubjectPolygon() {
        return _subject_polygon;
    }

    /**
     * Returns  a hole polygon of the model
     * @return hole polygon of the model
     */
    public Polygon getHolePolygon() {
        return _hole_polygon;
    }

    /**
     * Returns  a clip polygon of the model
     * @return clip polygon of the model
     */
    public Polygon getClipPolygon() {
        return _clip_polygon;
    }
}
