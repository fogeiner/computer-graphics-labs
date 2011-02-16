package FIT_8201_Sviridov_Lines;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing polyline object. Encapsulates polyline type, thickness,
 * color. Caches stroke object for faster drawing.
 * 
 * @author alstein
 * 
 */
public class Polyline {
	public static final int CONTINIOUS = 1;
	public static final int DASH_AND_DOT = 2;
	public static final int DOTTED_DASH_AND_DOT = 3;

	private List<Point> _points;
	private int _type;
	private int _thickness;
	private Color _color;
	private BasicStroke _stroke = null;

	/**
	 * Constructor with given type (one of one of
	 * <code>Polyline.CONTINIOUS</code> , <code>Polyline.DASH_AND_DOT</code>,
	 * <code>Polyline.DOTTED_DASH_AND_DOT</code>), thickness and color
	 * 
	 * @param type
	 *            type of polyline
	 * @param thickness
	 *            thickness of polyline
	 * @param color
	 *            color of polyline
	 */
	public Polyline(int type, int thickness, Color color) {
		_type = type;
		_thickness = thickness;
		_color = new Color(color.getRGB());
		_points = new ArrayList<Point>();
	}

	/**
	 * Returns list of points of polyline
	 * 
	 * @return list of ponts of polyline
	 */
	public List<Point> getPoints() {
		return Collections.unmodifiableList(_points);
	}

	/**
	 * Adds new point to polyline
	 * 
	 * @param p
	 *            new point coordinates
	 */
	public void addPoint(Point p) {
		_points.add(p);
	}

	/**
	 * Draws polyline with given <code>Graphics</code> object making circles of
	 * the given <code>radius</code> upon points
	 * 
	 * @param g
	 *            Graphics object
	 * @param radius
	 *            radius of circle to draw upon points
	 */
	public void draw(Graphics g, int radius) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(_color);
		if (_stroke == null) {

			if (_type == Polyline.CONTINIOUS) {
				_stroke = new BasicStroke(_thickness);
			} else if (_type == Polyline.DASH_AND_DOT) {

				_stroke = new BasicStroke(_thickness, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 20.0f,
								10.0f }, 0.0f);
			} else if (_type == Polyline.DOTTED_DASH_AND_DOT) {
				_stroke = new BasicStroke(_thickness, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 10.0f,
								5.0f, _thickness, 5.0f }, 0.0f);

			}

		}

		g2.setStroke(_stroke);

		g2.fillOval(_points.get(0).x - radius / 2, _points.get(0).y - radius
				/ 2, radius, radius);
		for (int i = 1; i < _points.size(); ++i) {
			Point p1 = _points.get(i - 1);
			Point p2 = _points.get(i);

			int x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y;

			g2.drawLine(x1, y1, x2, y2);
			g2.fillOval(x2 - radius / 2, y2 - radius / 2, radius, radius);
		}
	}

	/**
	 * Sets type of polyline
	 * 
	 * @param type
	 *            polyline type
	 */
	public void setType(int type) {
		_type = type;
	}

	/**
	 * Returns type of polyline
	 * 
	 * @return polyline type
	 */
	public int getType() {
		return _type;
	}

	/**
	 * Sets thickness of polyline
	 * 
	 * @param thickness
	 *            new thickness of polyline
	 */
	public void setThickness(int thickness) {
		_thickness = thickness;
	}

	/**
	 * Returns thickness of polyline
	 * 
	 * @return thickness of polyline
	 */
	public int getThickness() {
		return _thickness;
	}

	/**
	 * Sets color of polyline
	 * 
	 * @param color
	 *            new color of polyline
	 */
	public void setColor(Color color) {
		_color = color;
	}

	public Color getColor() {
		return new Color(_color.getRGB());
	}

	/**
	 * Returns polyline object in standard representation: number of points,
	 * type of polyline, thickness of polyline, color of polyline and
	 * coordinates of points
	 * 
	 * @return String with standard polyline representation
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		int num_of_points = _points.size();

		sb.append(num_of_points + "\n" + _type + "\n" + _thickness + "\n"
				+ _color.getRed() + " " + _color.getGreen() + " "
				+ _color.getBlue() + "\n");

		for (Point point : _points) {
			sb.append(point.x + " " + point.y + "\n");
		}

		return sb.toString();
	}
}
