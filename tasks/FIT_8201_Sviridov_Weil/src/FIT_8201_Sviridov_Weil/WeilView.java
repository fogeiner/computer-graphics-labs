package FIT_8201_Sviridov_Weil;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

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
    private Graphics2D _back;
    private static int IMAGE_WIDTH = 2000;
    private static int IMAGE_HEIGHT = 1500;
    private boolean _full_repaint;
    // panel properties
    private Dimension _weil_view_size = new Dimension(600, 400);
    // drawing properties
    private int _refresh_period = 50;
    private FrameService _weil_frame;
    private boolean _rubber_line = false;
    private final Object _monitor = new Object();
    // app data
    private Polygon _current_polygon;
    private Polygon _subject_polygon = new Polygon(
            Polygon.COUNTERCLOCKWISE_ORIENTATION,
            WeilSettings.DEFAULT_SUBJECT_COLOR,
            WeilSettings.DEFAULT_SUBJECT_THICKNESS);
    private Polygon _hole_polygon = new Polygon(Polygon.CLOCKWISE_ORIENTATION,
            WeilSettings.DEFAULT_SUBJECT_COLOR,
            WeilSettings.DEFAULT_SUBJECT_THICKNESS);
    private Polygon _clip_polygon = new Polygon(
            Polygon.COUNTERCLOCKWISE_ORIENTATION,
            WeilSettings.DEFAULT_CLIP_COLOR,
            WeilSettings.DEFAULT_CLIP_THICKNESS);
    private Color _intersecting_polygons_color = WeilSettings.DEFAULT_INTERSECTING_COLOR;
    private int _intersecting_polygons_thickness = WeilSettings.DEFAULT_INTERSECTING_THICKNESS;
    private List<Polygon> _intersecting_polygons = new ArrayList<Polygon>();
    // state machine data
    public final static int VIEW_STATE = 0;
    public final static int EDIT_STATE = 1;
    private int _state = VIEW_STATE;

    /**
     * Paints background, polygons and rubber line
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (_offscreen == null) {
            return;
        }

        Graphics2D front = (Graphics2D) g;
        Graphics2D back = _back;

        front.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        front.translate(0, this.getHeight());
        front.scale(1, -1);

        if (isFullRepaint()) {
            back.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,
                    0.0f));
            back.fillRect(0, 0, _offscreen.getWidth(), _offscreen.getHeight());
            _back = _offscreen.createGraphics();
            _back.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            back = _back;

            if (_current_polygon != _subject_polygon) {
                _subject_polygon.draw(back);
            }
            if (_current_polygon != _hole_polygon) {
                _hole_polygon.draw(back);
            }
            if (_current_polygon != _clip_polygon) {
                _clip_polygon.draw(back);
            }

            if (_current_polygon != null) {
                _current_polygon.drawPartial(back);
            }

            for (Polygon p : _intersecting_polygons) {
                p.draw(back);
            }

            fullRepaint(false);
        }

        front.drawImage(_offscreen, 0, -1, null);

        if (_current_polygon != null && _rubber_line) {

            Point pointer_location = MouseInfo.getPointerInfo().getLocation();
            pointer_location.x = pointer_location.x
                    - this.getLocationOnScreen().x;
            pointer_location.y = pointer_location.y
                    - this.getLocationOnScreen().y;
            pointer_location.y = getHeight() - pointer_location.y;
            _current_polygon.drawLine(front, pointer_location);
        }

    }

    /**
     * Makes made model changed visible
     */
    @Override
    public void modelLoaded() {
        checkIntersectAbility();
        Polygon polygons[] = {_subject_polygon, _hole_polygon, _clip_polygon};
        for (Polygon p : polygons) {
            String names[] = {"subject", "hole", "clip"};
            String name = null;
            if (p == _subject_polygon) {
                name = names[0];
            }
            if (p == _hole_polygon) {
                name = names[1];
            }
            if (p == _clip_polygon) {
                name = names[2];
            }

            if (p.testOrientation() == false) {
                JOptionPane.showMessageDialog(
                        WeilView.this,
                        "Loaded "
                        + name
                        + " polygon has wrong orientation.",
                        "Error", JOptionPane.ERROR_MESSAGE);

                reset();

            }
            if (p.testSelfEntering() == false) {
                JOptionPane.showMessageDialog(
                        WeilView.this,
                        "Loaded "
                        + name
                        + " polygon has self-entering.\n",
                        "Error", JOptionPane.ERROR_MESSAGE);
                reset();

            }
        }

        fullRepaint(true);

        this.revalidate();
        repaint();
    }

    /**
     * Sets state to <code>state</code> state
     *
     * @param state
     *            new state
     */
    private void setState(int state) {
        _state = state;
    }

    /**
     * Returns state
     *
     * @return current state
     */
    public int getState() {
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
        setPreferredSize(_weil_view_size);
        revalidate();
        _subject_polygon.clear();
        _clip_polygon.clear();
        _hole_polygon.clear();
        _intersecting_polygons.clear();
        checkIntersectAbility();
        fullRepaint();
    }

    /**
     * Sets flag to full repaint and rerenders model
     */
    @Override
    public void fullRepaint() {
        fullRepaint(true);
        repaint();
    }

    /**
     * Method called when user chooses "Draw subject polygon" in menu or on
     * toolbar.
     */
    public void onSubject() {
        switchToPolygon(_subject_polygon);
    }

    /**
     * Method called when user chooses "Draw hole polygon" in menu or on
     * toolbar.
     */
    public void onHole() {
        switchToPolygon(_hole_polygon);
    }

    /**
     * Method called when user chooses "Draw clip polygon" in menu or on
     * toolbar.
     */
    public void onClip() {
        switchToPolygon(_clip_polygon);
    }

    /**
     * Sets <code>p</code> Polygon as current polygon for modification and
     * blocks frame buttons
     *
     * @param p
     */
    private void switchToPolygon(Polygon p) {
        _weil_frame.setIntersectBlocked(true);
        _current_polygon = p;
        _current_polygon.clear();
        _intersecting_polygons.clear();
        _weil_frame.setModified(true);
        _weil_frame.setBlocked(true);
        fullRepaint(true);
        repaint();
        synchronized (_monitor) {
            _rubber_line = true;
            _monitor.notifyAll();
        }
        setState(EDIT_STATE);
    }

    /**
     * Checks if intersect operation is possible and make call to block/unblock
     * intersect button
     */
    private void checkIntersectAbility() {
        if (!_subject_polygon.isEmpty() && !_clip_polygon.isEmpty()) {
            _weil_frame.setIntersectBlocked(false);
        } else {
            _weil_frame.setIntersectBlocked(true);
        }
    }

    /**
     * Adds intersection polygon using all points of another
     *
     * @param p
     *            Polygon to be used as source
     */
    private void addIntersectingPolygon(Polygon polygon) {
        Polygon p = new Polygon(polygon.getOrientation(),
                _intersecting_polygons_color, _intersecting_polygons_thickness);
        p.setPoints(polygon);
        _intersecting_polygons.add(p);
    }

    /**
     * Method called when user chooses "Intersect" in menu or on toolbar.
     */
    public void onIntersect() {
        if (!_hole_polygon.isEmpty()
                && !_subject_polygon.isInside(_hole_polygon)) {
            JOptionPane.showMessageDialog(WeilView.this,
                    "Hole polygon must be inside subject polygon", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean finished = false;
        _intersecting_polygons.clear();
        fullRepaint(true);
        repaint();

        // clip inside subject
        if (_subject_polygon.isInside(_clip_polygon)) {

            if (_hole_polygon.isEmpty()) {
                addIntersectingPolygon(_clip_polygon);
                finished = true;
            } else if (_clip_polygon.isInside(_hole_polygon)) {
                addIntersectingPolygon(_clip_polygon);
                addIntersectingPolygon(_hole_polygon);
                finished = true;
            } else if (_hole_polygon.isInside(_clip_polygon)) {
                // clip inside hole -> no result
                finished = true;
            } else if (!_clip_polygon.hasIntersection(_hole_polygon)) {
                addIntersectingPolygon(_clip_polygon);
                finished = true;
            }
        }
        // subject inside clip
        if (_clip_polygon.isInside(_subject_polygon)) {
            if (!_hole_polygon.isEmpty()) {
                addIntersectingPolygon(_subject_polygon);
                addIntersectingPolygon(_hole_polygon);
                finished = true;

            } else {
                addIntersectingPolygon(_subject_polygon);
                finished = true;
            }
        }

        if (!_hole_polygon.isEmpty()) {
            if (!_clip_polygon.hasIntersection(_subject_polygon)
                    && !_clip_polygon.hasIntersection(_hole_polygon)) {
                finished = true;
            }
        } else {
            if (!_clip_polygon.hasIntersection(_subject_polygon)) {
                finished = true;
            }
        }

        if (finished) {
            fullRepaint(true);
            repaint();
            return;
        }

        List<OrientedVertex> in_vertices = new ArrayList<OrientedVertex>();
        // transform polygons to OrientedVertex structure
        OrientedVertex s = _subject_polygon.getFirstOrientedVertex();
        OrientedVertex c = _clip_polygon.getFirstOrientedVertex();
        OrientedVertex h = _hole_polygon.getFirstOrientedVertex();

        if (!_hole_polygon.isEmpty()) {
            OrientedVertex.intersect(h, c, in_vertices);
        }
        OrientedVertex.intersect(s, c, in_vertices);

        if (!_hole_polygon.isEmpty()) {
            OrientedVertex.updateAltPaths(h, c);
        }
        OrientedVertex.updateAltPaths(s, c);

        while (!in_vertices.isEmpty()) {
            Polygon p = new Polygon(Polygon.COUNTERCLOCKWISE_ORIENTATION,
                    _intersecting_polygons_color,
                    _intersecting_polygons_thickness);
            OrientedVertex v = in_vertices.get(0);
            in_vertices.remove(v);
            p.addPoint(v.getPoint());
            OrientedVertex cur_v = v.getNext();
            do {
                if (cur_v.getNextAlt() != null) {
                    in_vertices.remove(cur_v);
                    p.addPoint(cur_v.getPoint());
                    cur_v = cur_v.getNextAlt();
                } else {
                    p.addPoint(cur_v.getPoint());
                    cur_v = cur_v.getNext();
                }
            } while (!cur_v.equals(v));

            _intersecting_polygons.add(p);
        }

        if (!_hole_polygon.isEmpty() && _clip_polygon.isInside(_hole_polygon)) {
            addIntersectingPolygon(_hole_polygon);
        }

        fullRepaint(true);
        repaint();
    }

    /**
     * Constructor with reference to LinesFrame
     *
     * @param lines_frame
     *            application main frame
     */
    public WeilView(FrameService weil_frame) {
        _weil_frame = weil_frame;
        setFocusable(true);
        setBackground(WeilSettings.DEFAULT_BACKGROUND_COLOR);
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (_offscreen == null) {
                    _offscreen = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,
                            BufferedImage.TYPE_INT_ARGB);
                    _back = _offscreen.createGraphics();
                    _back.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    fullRepaint();
                } else {
                    if (getWidth() > _offscreen.getWidth(null)
                            || getHeight() > _offscreen.getHeight(null)) {
                        _offscreen = new BufferedImage(getWidth(), getHeight(),
                                BufferedImage.TYPE_INT_ARGB);
                        _back = _offscreen.createGraphics();
                        _back.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        fullRepaint();
                    }
                }
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

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                "escHandler");
        getActionMap().put("escHandler", new AbstractAction() {

            private static final long serialVersionUID = -434100500034434L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (_current_polygon == null) {
                    return;
                }
                if (_current_polygon.verticesCount() > 0) {
                    _current_polygon.deleteLastPoint();
                    fullRepaint(true);
                } else {
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
            }
        });

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent event) {
                requestFocusInWindow();
                if (getState() != EDIT_STATE) {
                    return;
                }
                int button = event.getButton();
                Point point = event.getPoint();
                point.setLocation(point.x, getHeight() - point.y);
                // left mouse click
                if (button == MouseEvent.BUTTON1) {
                    if (_current_polygon.isPointValid(point)) {
                        _current_polygon.addPoint(point);
                        _current_polygon.drawPoint(_back);
                    } else {
                        JOptionPane.showMessageDialog(
                                WeilView.this,
                                "Invalid point: check self-intersections.\nPress Esc to delete the last point.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else if (button == MouseEvent.BUTTON3) {
                    if (_current_polygon.verticesCount() == 0) {
                        JOptionPane.showMessageDialog(WeilView.this,
                                "Press Esc to cancel polygon drawing.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (_current_polygon.verticesCount() < 3) {
                        JOptionPane.showMessageDialog(
                                WeilView.this,
                                "Polygon has at least 3 vertices.\nPress Esc to delete the last point.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (!_current_polygon.isFinished()) {
                        JOptionPane.showMessageDialog(
                                WeilView.this,
                                "Invalid point: check self-intersections.\nPress Esc to delete the last point.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (_current_polygon.testOrientation() == false) {
                        JOptionPane.showMessageDialog(
                                WeilView.this,
                                "Wrong orientation.\nPress Esc to delete the last point.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    _current_polygon.drawLastFirst(_back);

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
     * Returns max y coordinate among polygons
     *
     * @return max y coordinate among polygons
     */
    @Override
    public int getMaxY() {
        int max_y = 0;
        int y;
        Rectangle bounds;

        Polygon polygons[] = {_subject_polygon, _clip_polygon, _hole_polygon};

        for (Polygon p : polygons) {
            bounds = p.getBounds();
            y = bounds.height;
            if (y > max_y) {
                max_y = y;
            }
        }

        return max_y;
    }

    /**
     * Returns max x coordinate among polygons
     *
     * @return max x coordinate among polygons
     */
    @Override
    public int getMaxX() {
        int max_x = 0;
        int x;
        Rectangle bounds;

        Polygon polygons[] = {_subject_polygon, _clip_polygon, _hole_polygon};

        for (Polygon p : polygons) {
            bounds = p.getBounds();

            x = bounds.width;
            if (x > max_x) {
                max_x = x;
            }
        }

        return max_x;
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
        for (Polygon p : _intersecting_polygons) {
            p.setColor(color);
        }
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
        for (Polygon p : _intersecting_polygons) {
            p.setThickness(thickness);
        }
    }

    /**
     * Sets <code>p</code> as a subject polygon of the model
     *
     * @param p
     *            polygon to become subject polygon of the model
     */
    public void setSubjectPolygon(Polygon p) {
        _subject_polygon.clear();
        _subject_polygon.setPoints(p);
    }

    /**
     * Sets <code>p</code> as a hole polygon of the model
     *
     * @param p
     *            polygon to become hole polygon of the model
     */
    public void setHolePolygon(Polygon p) {
        _hole_polygon.clear();
        _hole_polygon.setPoints(p);
    }

    /**
     * Sets <code>p</code> as a clip polygon of the model
     *
     * @param p
     *            polygon to become clip polygon of the model
     */
    public void setClipPolygon(Polygon p) {
        _clip_polygon.clear();
        _clip_polygon.setPoints(p);
    }

    /**
     * Returns a subject polygon of the model
     *
     * @return subject polygon of the model
     */
    public Polygon getSubjectPolygon() {
        return _subject_polygon;
    }

    /**
     * Returns a hole polygon of the model
     *
     * @return hole polygon of the model
     */
    public Polygon getHolePolygon() {
        return _hole_polygon;
    }

    /**
     * Returns a clip polygon of the model
     *
     * @return clip polygon of the model
     */
    public Polygon getClipPolygon() {
        return _clip_polygon;
    }
}
