package FIT_8201_Sviridov_Lines;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
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

	private List<Point> _points;
	private int _type;
	private int _thickness;
	private int _radius;
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
	public Polyline(int type, int thickness, int radius, Color color) {
		_type = type;
		_thickness = thickness;
		_radius = radius;
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
	 * Creates stroke based on settings and saves it in field
	 */
	private void makeStroke() {

		if (_type == PolylineSettings.CONTINIOUS) {
			_stroke = new BasicStroke(_thickness, BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND);
		} else if (_type == PolylineSettings.DASHED) {
			_stroke = new BasicStroke(_thickness, BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND, 10.0f, new float[] {
							3 * _thickness, 3 * _thickness }, 0.0f);
		} else if (_type == PolylineSettings.DOTTED_DASHED) {
			_stroke = new BasicStroke(_thickness, BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND, 10.0f, new float[] {
							3 * _thickness, 3 * _thickness, 1 }, 0.0f);
		}

	}
	
	/**
	 * Returns <code>Stroke</code> of polyline
	 * @return <code>Stroke</code> of polyline
	 */
	public Stroke getStroke(){
		if(_stroke == null){
			makeStroke();
		}
		return _stroke;
	}
	
	/**
	 * Method for optimized drawing; draws the very last element of the
	 * polyline: nothing if it's empty, dot if it consists just of a dot and
	 * last edge if there's one at least
	 * 
	 * @param g
	 *            Graphics object
	 */
	public void drawPartial(Graphics g) {
		int size = _points.size();
		
		if(size == 0)
			return;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(_color);
		if (_stroke == null) {
			makeStroke();
		}
		g2.setStroke(_stroke);

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		
		if(size == 1){
			g2.fillOval(_points.get(0).x - _radius / 2, _points.get(0).y - _radius
					/ 2, _radius, _radius);
			return;
		}
		
		if(size > 1){
			Point p1 = _points.get(size - 2);
			Point p2 = _points.get(size - 1);

			int x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y;

			g2.drawLine(x1, y1, x2, y2);
			g2.fillOval(x2 - _radius / 2, y2 - _radius / 2, _radius, _radius);
		}
	}

	/**
	 * Method for optimized drawing; draws whole the polyline
	 * 
	 * @param g
	 *            Graphics object
	 */
	public void drawFull(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(_color);
		if (_stroke == null) {
			makeStroke();
		}
		g2.setStroke(_stroke);
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2.fillOval(_points.get(0).x - _radius / 2, _points.get(0).y - _radius
				/ 2, _radius, _radius);
		
		for (int i = 1; i < _points.size(); ++i) {
			Point p1 = _points.get(i - 1);
			Point p2 = _points.get(i);

			int x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y;

			g2.drawLine(x1, y1, x2, y2);
			g2.fillOval(x2 - _radius / 2, y2 - _radius / 2, _radius, _radius);
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

	/**
	 * Returns color of polyline
	 * 
	 * @return color of polyline
	 */
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

		sb.append(num_of_points + "\r\n" + _type + "\r\n" + _thickness + "\r\n"
				+ _color.getRed() + " " + _color.getGreen() + " "
				+ _color.getBlue() + "\r\n");

		for (Point point : _points) {
			sb.append(point.x + " " + point.y + "\r\n");
		}

		return sb.toString();
	}
}
