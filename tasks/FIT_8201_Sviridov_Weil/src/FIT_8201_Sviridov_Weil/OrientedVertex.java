package FIT_8201_Sviridov_Weil;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Class incapsulating vertex of the oriented Polygon. Keeps point and reference
 * to the next point.
 * 
 * @author alstein
 */
public class OrientedVertex {

	// actually it would be better to extend Point2D but
	// this would break reference equality
	private Point2D _point = null;
	private OrientedVertex _next = null;
	private OrientedVertex _next_alt = null;
	private boolean _first = false;

	/**
	 * Constructor with given point and "is first" value
	 * 
	 * @param point
	 *            initial point
	 * @param first
	 *            true if the Vertex is the first, false otherwise
	 */
	public OrientedVertex(Point2D point, boolean first) {
		_point = point;
		_first = first;
	}

	/**
	 * Constructor with given point; the vertex is meant to be NOT the first
	 * 
	 * @param point
	 *            initial point
	 */
	public OrientedVertex(Point2D point) {
		_point = point;
	}

	/**
	 * Constructor with given point, usual next and alternative next
	 * 
	 * @param point
	 * @param next
	 * @param next_alt
	 */
	public OrientedVertex(Point2D point, OrientedVertex next,
			OrientedVertex next_alt) {
		_point = point;
		_next = next;
		_next_alt = next_alt;
	}

	/**
	 * Returns true if vertex is first, false otherwise
	 * 
	 * @return true if vertex is first, false otherwise
	 */
	public boolean isFirst() {
		return _first;
	}

	/**
	 * Returns true if next vertex is first, false otherwise
	 * 
	 * @return true if next vertex is first, false otherwise
	 */
	public boolean isLast() {
		return _next.isFirst();
	}

	/**
	 * Tests equality of OrientedVertices based on kept Point2D <b>REFERENCE</b>
	 * 
	 * @param another
	 *            anther OrientedVertex
	 * @return true if Point2D's are the same
	 */
	public boolean equals(OrientedVertex another) {
		return _point == another._point;
	}

	/**
	 * Returns next OrientedVertex
	 * 
	 * @return next OrientedVertex
	 */
	public OrientedVertex getNext() {
		return _next;
	}

	/**
	 * Sets next OrientedVertex
	 * 
	 * @param vertex
	 *            OrientedVertex to be next
	 */
	public void setNext(OrientedVertex vertex) {
		_next = vertex;
	}

	/**
	 * Returns alternative next OrientedVertex
	 * 
	 * @return alternative next OrientedVertex
	 */
	public OrientedVertex getNextAlt() {
		return _next_alt;
	}

	/**
	 * Sets alternative next OrientedVertex
	 * 
	 * @param vertex
	 *            OrientedVertex to be alternative next
	 */
	public void setNextAlt(OrientedVertex vertex) {
		_next_alt = vertex;
	}

	/**
	 * Returns representation of OrientedVertex
	 * @return String representation of OrientedVertex
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		int x = (int) (_point.getX() + 0.5);
		int y = (int) (_point.getY() + 0.5);

		if (isFirst()) {
			sb.append("F");
		} else {
			sb.append(" ");
		}

		sb.append("(");
		sb.append(x);
		sb.append(",");
		sb.append(y);
		sb.append(")");

		if (isLast()) {
			sb.append("L");
		}
		return sb.toString();
	}

	/**
	 * Returns Point2D representing OrientedVertex coordinate
	 * 
	 * @return Point2D representing OrientedVertex coordinate
	 */
	public Point2D getPoint() {
		return _point;
	}

	/**
	 * Prints out path to last OrientedVertex
	 */
	public void printPath() {
		OrientedVertex v = this;
		do {
			System.out.println(v);
			v = v.getNext();
		} while (!v.isFirst());
	}

	/**
	 * Finds points of intersection and adds them to both v and c. v and c are
	 * meant to be first OrientedVertices If that is not hold,
	 * IllegalArgumentException is thrown. All vetrices where v enters c are
	 * stored in List l (it's cleared beforehand)
	 * 
	 * @param v
	 *            first OrientedVertex
	 * @param c
	 *            first OrientedVertex
	 * @param l
	 *            list with entering vertices
	 */
	public static void intersect(OrientedVertex v, OrientedVertex c,
			List<OrientedVertex> l) {

		if ((v == c) || (!v.isFirst()) || (!c.isFirst())) {
			throw new IllegalArgumentException();
		}

		OrientedVertex s1 = v;
		OrientedVertex s2 = s1.getNext();

		OrientedVertex c1 = c;
		OrientedVertex c2 = c1.getNext();

		do {
			while (true) {

				Point2D intersection = null;
				if (!(s1.equals(c1) || s1.equals(c2) || s2.equals(c1) || s2
						.equals(c2))) {
					intersection = EuclideanGeometry.getIntersection(
							s1.getPoint(), s2.getPoint(), c1.getPoint(),
							c2.getPoint());
				}

				if (intersection != null) {
					OrientedVertex v1 = new OrientedVertex(intersection, s2, c2), v2 = new OrientedVertex(
							intersection, c2, s2);

					if (doEnter(s1, s2, c1, c2)) {
						l.add(v1);
					}

					s1.setNext(v1);
					s2 = s1.getNext();

					c1.setNext(v2);
					c2 = c1.getNext();
				} else {
					c1 = c2;
					c2 = c1.getNext();

					if (c1.isFirst()) {
						break;
					}
				}
			}

			s1 = s2;
			s2 = s1.getNext();
		} while (!s1.isFirst());
	}

	/**
	 * Update alt paths (some points were not known at the moment of
	 * intersecting points creation)
	 * 
	 * @param v
	 *            first vertex of one path
	 * @param c
	 *            first vertex of another path
	 */
	public static void updateAltPaths(OrientedVertex v, OrientedVertex c) {
		OrientedVertex v_cur = v, c_cur = c;

		do {
			if (v_cur.getNextAlt() == null) {
				v_cur = v_cur.getNext();
				continue;
			}

			do {

				if (c_cur.getNextAlt() == null) {
					c_cur = c_cur.getNext();
					continue;
				}

				if (v_cur.equals(c_cur)) {
					v_cur.setNextAlt(c_cur.getNext());
					c_cur.setNextAlt(v_cur.getNext());

					v_cur = v_cur.getNext();
					c_cur = c;
				} else {
					c_cur = c_cur.getNext();
				}
			} while (!c_cur.isFirst());
		} while (!v_cur.isFirst());
	}

	/**
	 * Checks if vector (c2 - c1) enters (s2 - s1) and end is on the 'right'
	 * 
	 * @param s1
	 *            first point x coordinate
	 * @param s2
	 *            first point y coordinate
	 * @param c1
	 *            second point x coordinate
	 * @param c2
	 *            second point y coordinate
	 * @return true if the end on the right, false otherwise
	 */
	private static boolean doEnter(OrientedVertex s1, OrientedVertex s2,
			OrientedVertex c1, OrientedVertex c2) {

		Point2D sv = new Point2D.Double();
		Point2D cv = new Point2D.Double();

		// take vector s
		// take vector c
		// rotate c: (x,y) -> (-y,x)
		// dot mult s x c >= 0 -> win
		// else -> fail

		double s1x = s1.getPoint().getX(), s1y = s1.getPoint().getY(), c1x = c1
				.getPoint().getX(), c1y = c1.getPoint().getY();

		double s2x = s2.getPoint().getX(), s2y = s2.getPoint().getY(), c2x = c2
				.getPoint().getX(), c2y = c2.getPoint().getY();

		double dot;

		sv.setLocation(s2x - s1x, s2y - s1y);
		cv.setLocation(c2x - c1x, c2y - c1y);
		cv.setLocation(-cv.getY(), cv.getX());
		dot = sv.getX() * cv.getX() + sv.getY() * cv.getY();

		if (dot >= 0) {
			return true;
		}
		return false;
	}
}
