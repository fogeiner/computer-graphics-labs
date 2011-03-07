package FIT_8201_Sviridov_Weil;

import java.awt.geom.Point2D;

/**
 * Class incapsulating vertex of the oriented Polygon.
 * Keeps point and reference to the next point.
 * @author admin
 */
public class OrientedVertex {

    // actually it would be better to extend Point2D but
    // this would break reference equality
    private Point2D _point = null;
    private OrientedVertex _next = null;
    private boolean _first = false;

    /**
     * Constructor with given point and "is first" value
     * @param point initial point
     * @param first true if the Vertex is the first, false otherwise
     */
    public OrientedVertex(Point2D point, boolean first) {
        _point = point;
        _first = first;
    }

    /**
     * Constructor with given point; the vertex is meant to be NOT the first
     * @param point initial point
     */
    public OrientedVertex(Point2D point) {
        _point = point;
    }

    /**
     * Returns true if vertex is first, false otherwise
     * @return true if vertex is first, false otherwise
     */
    public boolean isFirst() {
        return _first;
    }

    /**
     * Returns true if next vertex is first, false otherwise
     * @return true if next vertex is first, false otherwise
     */
    public boolean isLast() {
        return _next.isFirst();
    }

    /**
     * Tests equality of OrientedVertices based on kept Point2D <b>REFERENCE</b>
     * @param another anther OrientedVertex
     * @return true if Point2D's are the same
     */
    public boolean equals(OrientedVertex another) {
        return _point == another._point;
    }

    /**
     * Returns next OrientedVertex
     * @return next OrientedVertex
     */
    public OrientedVertex getNext() {
        return _next;
    }

    /**
     * Sets next OrientedVertex
     * @param vertex OrientedVertex to be next
     */
    public void setNext(OrientedVertex vertex) {
        _next = vertex;
    }

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
     * Adds new point after given saving links
     * Was: -> A -> B -> ... ; adding N will lead to
     * Now: -> A -> N -> B -> ...
     */
    public void split(Point2D point) {
        OrientedVertex old_next = getNext();
        OrientedVertex new_point = new OrientedVertex(point);
        new_point.setNext(old_next);
        setNext(new_point);
    }

    /**
     * Finds points of intersection and adds them to
     * both v and c. v and c are meant to be first OrientedVertices
     * If that is not hold, IllegalArgumentException is throws
     * @param v first OrientedVertex
     * @param c first OrientedVertex
     */
    public static void intersect(OrientedVertex v, OrientedVertex c) {

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
                if (!(s1.equals(c1) || s1.equals(c2) || s2.equals(c1) || s2.equals(c2))) {
                    intersection = EuclideanGeometry.getIntersection(s1.getPoint(), s2.getPoint(),
                            c1.getPoint(), c2.getPoint());
                }

                if (intersection != null) {
                    s1.split(intersection);
                    s2 = s1.getNext();

                    c1.split(intersection);
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
}
