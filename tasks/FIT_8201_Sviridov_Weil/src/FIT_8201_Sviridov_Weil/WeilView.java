package FIT_8201_Sviridov_Weil;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;

/**
 * Canvas class of application. Does drawing and mouse tracking.
 * 
 * @author alstein
 * 
 */
public class WeilView extends JPanel implements WeilSettings {

    private static final long serialVersionUID = -456307332612783435L;
    private BufferedImage _offscreen;

    private static int IMAGE_WIDTH = 2000;
    private static int IMAGE_HEIGHT = 1500;

    private boolean _full_repaint;
    private FrameService _weil_frame;

    private boolean _rubber_line = false;
    private final Object _monitor = new Object();

    private final static int VIEW_STATE = 0;
    private final static int EDIT_STATE = 1;
    private int _state = VIEW_STATE;

    private List<Polyline> _polylines = new ArrayList<Polyline>();
    private Polyline _new_polyline = null;
    
    
    private Color _polyline_color;
    private int _polyline_type;
    private int _polyline_thickness;
    private int _circle_radius;

    private int _refresh_period = 30;

    /**
     * Returns list of current (i.e. which are present on screen right away)
     * polylines
     *
     * @return Unmodifiable list of current polylines
     */
    public List<Polyline> getPolylines() {
        return Collections.unmodifiableList(_polylines);
    }

    /**
     * Adds polyline to list of current polylines
     *
     * @param polyline
     *            Polyline to be added to list of current polylines
     */
    public void addPolyline(Polyline polyline) {
        _polylines.add(polyline);
    }

    /**
     * Clears list of current polylines
     */
    public void clearPolylines() {
        _polylines.clear();
    }

    /**
     * Returns current color of polyline
     *
     * @return color of current polyline
     */
    public Color getPolylineColor() {
        return _polyline_color;
    }

    /**
     * Sets current polyline color
     *
     * @param polyline_color
     *            Color to be set as current polyline color
     */
    public void setPolylineColor(Color polyline_color) {
        this._polyline_color = polyline_color;
    }

    /**
     * Returns current color of canvas
     *
     * @return current color of canvas
     */
    public Color getBackgroundColor() {
        return getBackground();
    }

    /**
     * Sets current color of canvas
     *
     * @param background_color
     *            Color to be set as color of canvas
     */
    public void setBackgroundColor(Color background_color) {
        setBackground(background_color);
        repaint();
    }

    /**
     * Returns type of current polyline (one of <code>Polyline.CONTINIOUS</code>
     * , <code>Polyline.DASH_AND_DOT</code>,
     * <code>Polyline.DOTTED_DASH_AND_DOT</code>).
     *
     * @return type of current polyline
     * @see Polyline
     */
    public int getPolylineType() {
        return _polyline_type;
    }

    /**
     * Sets type of current polyline (one of <code>Polyline.CONTINIOUS</code>,
     * <code>Polyline.DASH_AND_DOT</code>,
     * <code>Polyline.DOTTED_DASH_AND_DOT</code>).
     *
     * @param polyline_type
     *            new type of current polyline
     */
    public void setPolylineType(int polyline_type) {
        this._polyline_type = polyline_type;
    }

    /**
     * Returns thickness of current polyline
     *
     * @return thickness of current polyline
     */
    public int getPolylineThickness() {
        return _polyline_thickness;
    }

    /**
     * Sets thickness of current polyline
     *
     * @param polyline_thickness
     *            new thickness of current polyline
     */
    public void setPolylineThickness(int polyline_thickness) {
        this._polyline_thickness = polyline_thickness;
    }

    /**
     * Returns current circles radius
     *
     * @return current circles radius
     */
    public int getCircleRadius() {
        return _circle_radius;
    }

    /**
     * Sets current circles radius
     *
     * @param circle_radius
     *            current circles radius
     */
    public void setCircleRadius(int circle_radius) {
        this._circle_radius = circle_radius;
    }

    /**
     * Sets polyline color, polyline thickness, polyline type, canvas color and
     * circle radius to default values
     */
    private void resetPreferences() {
//		_polyline_color = DEFAULT_POLYLINE_COLOR;
//		_polyline_type = DEFAULT_POLYLINE_TYPE;
//		_polyline_thickness = DEFAULT_POLYLINE_THICKNESS;
//		_circle_radius = DEFAULT_CIRCLE_RADIUS;
    }

    /**
     * Paints background, polylines and rubber line
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (_offscreen == null) {
            return;
        }

        Graphics2D front = (Graphics2D) g;
        Graphics2D back = (Graphics2D) _offscreen.createGraphics();

        front.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (isFullRepaint()) {
            back.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,
                    0.0f));
            back.fillRect(0, 0, _offscreen.getWidth(), _offscreen.getHeight());

            back = _offscreen.createGraphics();

            List<Polyline> polylines = getPolylines();

            for (Polyline polyline : polylines) {
                polyline.drawFull(back);
            }

            fullRepaint(false);
        }

        front.drawImage(_offscreen, 0, 0, this);

        if (_rubber_line) {
            Polyline last_polyline = _new_polyline;
            front.setStroke(last_polyline.getStroke());
            front.setColor(last_polyline.getColor());

            List<Point> last_points = last_polyline.getPoints();
            Point last_point = last_points.get(last_points.size() - 1);

            Point pointer_location = MouseInfo.getPointerInfo().getLocation();

            int x1 = last_point.x, y1 = last_point.y, x2 = pointer_location.x
                    - this.getLocationOnScreen().x, y2 = pointer_location.y
                    - this.getLocationOnScreen().y;

            front.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * Makes made model changed visible
     */
    @Override
    public void modelLoaded() {
        fullRepaint(true);
        repaint();
    }

    /**
     * Sets state to <code>state</code> state (meant to be VIEW and EDIT)
     *
     * @param state
     *            either VIEW_STATE or EDIT_STATE
     */
    private void setState(int state) {
        _state = state;
    }

    /**
     * Returns state(meant to be VIEW and EDIT)
     *
     * @return either VIEW_STATE or EDIT_STATE
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
        fullRepaint(true);
        repaint();
    }

    /**
     * Constructor with reference to LinesFrame
     *
     * @param lines_frame
     *            application main frame
     */
    public WeilView(FrameService lines_frame) {
        _weil_frame = lines_frame;
        setBackground(Color.white);
        resetPreferences();

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
                int button = event.getButton();
                Point point = event.getPoint();
                // left mouse click
                if (button == MouseEvent.BUTTON1) {
                } else if (button == MouseEvent.BUTTON3) {
                }
            }
        });
    }
}
