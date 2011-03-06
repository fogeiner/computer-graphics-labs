package FIT_8201_Sviridov_Weil;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Utility class primary geometry functions implementations
 * @author admin
 */
public class EuclideanGeometry {

    public static final double EPS = 10E-7;


    public static boolean areSegmentsParallel(Point2D p1, Point2D p2,
            Point2D q1, Point2D q2) {
        double x1 = p1.getX(),
                x2 = p2.getX(),
                x3 = q1.getX(),
                x4 = q2.getX(),
                y1 = p1.getY(),
                y2 = p2.getY(),
                y3 = q1.getY(),
                y4 = q2.getY();

        double d = (x1 - x2) * (y4 - y3) - (y1 - y2) * (x4 - x3);

        if (Math.abs(d) < EPS) {
            return true;
        }

        return false;
    }

    public static Point2D getIntersection(Point2D p1, Point2D p2,
            Point2D q1, Point2D q2) {
        double x1 = p1.getX(),
                x2 = p2.getX(),
                x3 = q1.getX(),
                x4 = q2.getX(),
                y1 = p1.getY(),
                y2 = p2.getY(),
                y3 = q1.getY(),
                y4 = q2.getY();

        double x, y, t, ta, tb, d, da, db;


        d = (x1 - x2) * (y4 - y3) - (y1 - y2) * (x4 - x3);
        da = (x1 - x3) * (y4 - y3) - (y1 - y3) * (x4 - x3);
        db = (x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3);


        if (Math.abs(d) < EPS) {
            return null;
        }

        ta = da / d;
        tb = db / d;
        if ((0 <= ta) && (ta <= 1)
                && (0 <= tb) && (tb <= 1)) {
            return new Point2D.Double(x1 + ta * (x2 - x1), y1 + ta * (y2 - y1));
        } else {
            return null;
        }

    }

    public static void main(String[] args) {
        Point2D p1 = new Point2D.Double(0, 0);
        Point2D p2 = new Point2D.Double(1, 0);
        Point2D p3 = new Point2D.Double(0, 1);
        Point2D p4 = new Point2D.Double(2, 1);

        System.out.println(areSegmentsParallel(p1, p2, p3, p4));

        Point2D p5 = new Point2D.Double(0, 1);
        Point2D p6 = new Point2D.Double(0, 0);
        Point2D p7 = new Point2D.Double(1, 0);
        Point2D p8 = new Point2D.Double(1, 2);

        System.out.println(getIntersection(p5, p6, p7, p8));
    }
}
