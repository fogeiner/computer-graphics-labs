package FIT_8201_Sviridov_Cam.primitives;

import FIT_8201_Sviridov_Cam.Vertex;
import java.awt.Color;

/**
 *
 * @author admin
 */
public class Segment {

    private final Vertex startPoint;
    private final Vertex endPoint;
    private final double length;
    private final Color color;
    private boolean visible;

    public Segment(Vertex startPoint, Vertex endPoint) {
        this(startPoint, endPoint, Vertex.DEFAULT_COLOR);
    }

    public Segment(Vertex startPoint, Vertex endPoint, Color color) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.color = color;

        double dx = endPoint.getX() - startPoint.getX(),
                dy = endPoint.getY() - startPoint.getY(),
                dz = endPoint.getZ() - startPoint.getZ();

        this.length = Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public String toString() {
        return startPoint + " " + endPoint;
    }

    public Vertex getEndPoint() {
        return endPoint;
    }

    public Vertex getStartPoint() {
        return startPoint;
    }

    public double getLength() {
        return length;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
