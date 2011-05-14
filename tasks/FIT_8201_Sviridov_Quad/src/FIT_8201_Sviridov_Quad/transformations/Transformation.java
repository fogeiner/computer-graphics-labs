package FIT_8201_Sviridov_Quad.transformations;

import FIT_8201_Sviridov_Quad.Vertex;

/**
 *
 * @author alstein
 */
public interface Transformation {

    /**
     * Applies transformation to given vertex
     *
     * @param vertex original vertex
     * @return transformed vertex
     */
    public Vertex apply(Vertex vertex);

    /**
     * Composes this transformation with given (given is on the left)
     *
     * @param leftTransformation another transformation
     */
    public void compose(Transformation leftTransformation);

    public double[][] getMatrixRepresentation();
}
