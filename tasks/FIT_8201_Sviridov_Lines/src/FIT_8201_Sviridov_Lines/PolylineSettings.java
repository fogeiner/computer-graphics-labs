package FIT_8201_Sviridov_Lines;

import java.awt.Color;
import java.util.List;

public interface PolylineSettings {
	public static final Color DEFAULT_POLYLINE_COLOR = Color.black;
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.white;
	public static final int DEFAULT_POLYLINE_TYPE = PolylineSettings.CONTINIOUS;
	public static final int DEFAULT_POLYLINE_THICKNESS = 1;
	public static final int DEFAULT_CIRCLE_RADIUS = 5;

	public static final int CONTINIOUS = 1;
	public static final int DASHED = 2;
	public static final int DOTTED_DASHED = 3;

	public static final int MAX_THICKNESS = 36;
	public static final int MAX_RADIUS = 101;

	public final static int FRAME_WIDTH = 800;
	public final static int FRAME_HEIGHT = 600;
	public final static String LINES_NAME = "FIT_8201_Sviridov_Lines";
	public final static String UNTITLED_DOCUMENT = "Untitled";

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

	/**
	 * Returns current color of canvas
	 * 
	 * @return current color of canvas
	 */
	public Color getBackgroundColor();

	/**
	 * Sets current color of canvas
	 * 
	 * @param background_color
	 *            Color to be set as color of canvas
	 */
	public void setBackgroundColor(Color background_color);

	/**
	 * Returns type of current polyline (one of <code>Polyline.CONTINIOUS</code>
	 * ,<code>Polyline.DASH_AND_DOT</code>,
	 * <code>Polyline.DOTTED_DASH_AND_DOT</code>).
	 * 
	 * @return type of current polyline
	 * @see Polyline
	 */
	public int getPolylineType();

	/**
	 * Sets type of current polyline (one of <code>Polyline.CONTINIOUS</code>,
	 * <code>Polyline.DASH_AND_DOT</code>,
	 * <code>Polyline.DOTTED_DASH_AND_DOT</code>).
	 * 
	 * @param polyline_type
	 *            new type of current polyline
	 */
	public void setPolylineType(int polyline_type);

	/**
	 * Returns thickness of current polyline
	 * 
	 * @return thickness of current polyline
	 */
	public int getPolylineThickness();

	/**
	 * Sets thickness of current polyline
	 * 
	 * @param polyline_thickness
	 *            new thickness of current polyline
	 */
	public void setPolylineThickness(int polyline_thickness);

	/**
	 * Returns current circles radius
	 * 
	 * @return current circles radius
	 */
	public int getCircleRadius();

	/**
	 * Sets current circles radius
	 * 
	 * @param circle_radius
	 *            current circles radius
	 */
	public void setCircleRadius(int circle_radius);
	
	/**
	 * Makes made model changed visible
	 */
	public void modelLoaded();
}
