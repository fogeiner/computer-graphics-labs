package FIT_8201_Sviridov_Weil;

import java.awt.Color;
import java.util.List;

public interface WeilSettings {

    public final static int FRAME_WIDTH = 800;
    public final static int FRAME_HEIGHT = 600;
    public final static String LINES_NAME = "FIT_8201_Sviridov_Weil_1";
    public final static String UNTITLED_DOCUMENT = "Untitled";
    public static final Color DEFAULT_SUBJECT_COLOR = Color.black;
    public static final Color DEFAULT_CLIP_COLOR = Color.blue;
    public static final Color DEFAULT_INTERSECTING_COLOR = Color.red;
    public static final int DEFAULT_THICKNESS = 1;
    public static final int DEFAULT_SUBJECT_THICKNESS = DEFAULT_THICKNESS;
    public static final int DEFAULT_CLIP_THICKNESS = DEFAULT_THICKNESS;
    public static final int DEFAULT_INTERSECTING_THICKNESS = DEFAULT_THICKNESS;
    public static final int MAX_THICKNESS = 101;
    
    /**
     * Returns list of current (i.e. which are present on screen right away)
     * polylines
     *
     * @return Unmodifiable list of current polylines
     */
    public List<Polyline> getPolylines();

    /**
     * Adds polyline to list of current polylines
     *
     * @param polyline
     *            Polyline to be added to list of current polylines
     */
    public void addPolyline(Polyline polyline);

    /**
     * Clears list of current polylines
     */
    public void clearPolylines();

    /**
     * Returns current color of polyline
     *
     * @return color of current polyline
     */
    public Color getPolylineColor();

    /**
     * Sets current polyline color
     *
     * @param polyline_color
     *            Color to be set as current polyline color
     */
    public void setPolylineColor(Color polyline_color);

    public int getPolylineThickness();

    /**
     * Sets thickness of current polyline
     *
     * @param polyline_thickness
     *            new thickness of current polyline
     */
    public void setPolylineThickness(int polyline_thickness);

    public void modelLoaded();
}
