package FIT_8201_Sviridov_Weil;

import java.awt.Color;
import java.awt.Dimension;

public interface WeilSettings {

    public final static int MAX_DIMENSION_SIZE = 10000;
    public final static int FRAME_WIDTH = 800;
    public final static int FRAME_HEIGHT = 600;
    public final static int MODEL_PADDING = 50;
    public final static String WEIL_NAME = "FIT_8201_Sviridov_Weil_1";
    public final static String ABOUT_FILE = "FIT_8201_Sviridov_Weil_About.txt";
    public final static String DATA_DIR = "FIT_8201_Sviridov_Weil_Data";
    public final static String UNTITLED_DOCUMENT = "Untitled";
    public static final Color DEFAULT_SUBJECT_COLOR = Color.red;
    public static final Color DEFAULT_CLIP_COLOR = Color.blue;
    public static final Color DEFAULT_INTERSECTING_COLOR = Color.green;
    public static final int DEFAULT_THICKNESS = 1;
    public static final int DEFAULT_SUBJECT_THICKNESS = DEFAULT_THICKNESS;
    public static final int DEFAULT_CLIP_THICKNESS = DEFAULT_THICKNESS;
    public static final int DEFAULT_INTERSECTING_THICKNESS = DEFAULT_THICKNESS * 3;
    public static final int MAX_THICKNESS = 21;

    /**
     * Returns width of the visualization area
     *
     * @return width of the visualization area
     */
    public int getMaxX();

    /**
     * Returns height of the visualization area
     *
     * @return height of the visualization area
     */
    public int getMaxY();

    /**
     * Returns current color of subject polygon
     *
     * @return color of subject polygon
     */
    public Color getSubjectPolygonColor();

    /**
     * Returns current color of clip polygon
     *
     * @return color of clip polygon
     */
    public Color getClipPolygonColor();

    /**
     * Returns current color of intersecting polygon
     *
     * @return color of intersecting polygon
     */
    public Color getIntersectingPolygonColor();

    /**
     * Sets subject polygon color
     *
     * @param color
     *            Color to be set as subject polygon color
     */
    public void setSubjectPolygonColor(Color color);

    /**
     * Sets clip polygon color
     *
     * @param color
     *            Color to be set as clip polygon color
     */
    public void setClipPolygonColor(Color color);

    /**
     * Sets subject polygon color
     *
     * @param color
     *            Color to be set as intersecting polygon color
     */
    public void setIntersectingPolygonColor(Color color);

    /**
     * Returns current thickness of subject polygon
     *
     * @return thickness of subject polygon
     */
    public int getSubjectPolygonThickness();

    /**
     * Returns current thickness of clip polygon
     *
     * @return thickness of clip polygon
     */
    public int getClipPolygonThickness();

    /**
     * Returns current thickness of intersecting polygon
     *
     * @return thickness of intersecting polygon
     */
    public int getIntersectingPolygonThickness();

    /**
     * Sets subject polygon thickness
     *
     * @param thickness
     *            thickness to be set as subject polygon thickness
     */
    public void setSubjectPolygonThickness(int thickness);

    /**
     * Sets clip polygon thickness
     *
     * @param thickness
     *            thickness to be set as clip polygon thickness
     */
    public void setClipPolygonThickness(int thickness);

    /**
     * Sets subject polygon thickness
     *
     * @param thickness
     *            thickness to be set as intersecting polygon thickness
     */
    public void setIntersectingPolygonThickness(int thickness);

    /**
     * Sets preferred size of the model visualization
     */
    public void setPreferredSize(Dimension d);

    /**
     * Sets <code>p</code> as a subject polygon of the model
     *
     * @param p
     *            polygon to become subject polygon of the model
     */
    public void setSubjectPolygon(Polygon p);

    /**
     * Sets <code>p</code> as a hole polygon of the model
     *
     * @param p
     *            polygon to become hole polygon of the model
     */
    public void setHolePolygon(Polygon p);

    /**
     * Sets <code>p</code> as a clip polygon of the model
     *
     * @param p
     *            polygon to become clip polygon of the model
     */
    public void setClipPolygon(Polygon p);

    /**
     * Returns a subject polygon of the model
     *
     * @return subject polygon of the model
     */
    public Polygon getSubjectPolygon();

    /**
     * Returns a hole polygon of the model
     *
     * @return hole polygon of the model
     */
    public Polygon getHolePolygon();

    /**
     * Returns a clip polygon of the model
     *
     * @return clip polygon of the model
     */
    public Polygon getClipPolygon();

    /**
     * Commits changes to become visible after altering model
     */
    public void modelLoaded();

    /**
     * Methods should be invoked after changes in model settings to make them
     * appear on the screen
     */
    public void fullRepaint();

    /**
     * Returns canvas size
     * @return canvas size
     */
    public Dimension getSize();
}
