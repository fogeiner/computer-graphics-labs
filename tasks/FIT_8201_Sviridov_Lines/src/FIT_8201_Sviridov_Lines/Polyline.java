package FIT_8201_Sviridov_Lines;

import java.awt.Color;
import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Polyline {
	public static final int CONTINIOUS = 0;
	public static final int DASH_AND_DOT = 1;
	public static final int DOTTED_DASH_AND_DOT = 2;

	private List<Point> _points;
	private int _type;
	private int _thickness;
	private Color _color;

	public Polyline() {
		_points = new LinkedList<Point>();
	}

	public List<Point> getPoints() {
		return Collections.unmodifiableList(_points);
	}

	public void addPoint(Point p) {
		_points.add(p);
	}

	public void setType(int type) {
		_type = type;
	}

	public int getType() {
		return _type;
	}

	public void setThickness(int thickness) {
		_thickness = thickness;
	}

	public int getThickness() {
		return _thickness;
	}

	public void setColor(Color color) {
		_color = color;
	}

	public Color getColor() {
		return new Color(_color.getRGB());
	}

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
