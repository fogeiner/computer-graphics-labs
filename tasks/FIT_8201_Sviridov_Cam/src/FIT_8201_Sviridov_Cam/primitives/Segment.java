package FIT_8201_Sviridov_Cam.primitives;

import FIT_8201_Sviridov_Cam.Vertex;

/**
 * Class represents segment
 * 
 * @author admin
 */
public class Segment {

	private final Vertex startVertex;
	private final Vertex endVertex;

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
}
