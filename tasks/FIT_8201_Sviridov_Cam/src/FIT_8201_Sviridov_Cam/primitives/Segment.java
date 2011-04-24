package FIT_8201_Sviridov_Cam.primitives;

import FIT_8201_Sviridov_Cam.Vertex;
import java.awt.Color;

/**
 * Class represents segment
 * @author admin
 */
public class Segment {

    private final Vertex startVertex;
    private final Vertex endVertex;

    /**
     * Ctor with given points
     * @param startVertex start vertex
     * @param endVertex end vertex
     */
    public Segment(Vertex startPoint, Vertex endPoint) {
        this.startVertex = startPoint;
        this.endVertex = endPoint;

        double dx = endPoint.getX() - startPoint.getX(),
                dy = endPoint.getY() - startPoint.getY(),
                dz = endPoint.getZ() - startPoint.getZ();

    }

    @Override
    public String toString() {
        return startVertex + " " + endVertex;
    }

    /**
     * Returns end vertex
     * @return end vertex
     */
    public Vertex getEndVertex() {
        return endVertex;
    }

    /**
     * Returns start vertex
     * @return start vertex
     */
    public Vertex getStartVertex() {
        return startVertex;
    }
}
