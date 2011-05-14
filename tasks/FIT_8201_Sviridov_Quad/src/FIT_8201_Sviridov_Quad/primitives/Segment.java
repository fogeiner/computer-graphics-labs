package FIT_8201_Sviridov_Quad.primitives;

import FIT_8201_Sviridov_Quad.Vertex;
import FIT_8201_Sviridov_Quad.transformations.Transformation;

/**
 * Class represents segment
 * 
 * @author admin
 */
public class Segment {

    private Vertex startVertex;
    private Vertex endVertex;

    /**
     * Ctor with given points
     *
     * @param startVertex
     *            start vertex
     * @param endVertex
     *            end vertex
     */
    public Segment(Vertex startVertex, Vertex endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }

    public Segment(Vertex endVertex) {
        this.startVertex = new Vertex(0, 0, 0);
        this.endVertex = endVertex;
    }

    @Override
    public String toString() {
        return startVertex + " " + endVertex;
    }

    /**
     * Returns end vertex
     *
     * @return end vertex
     */
    public Vertex getEndVertex() {
        return endVertex;
    }

    /**
     * Returns start vertex
     *
     * @return start vertex
     */
    public Vertex getStartVertex() {
        return startVertex;
    }

    public void transform(Transformation transformation) {
        startVertex = transformation.apply(startVertex);
        endVertex = transformation.apply(endVertex);
    }
}
