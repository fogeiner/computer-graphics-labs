package FIT_8201_Sviridov_Quad.transformations;

/**
 *
 * @author alstein
 */
public class ScaleTransformation extends TransformationMatrixImpl {

    public ScaleTransformation(double xs, double ys, double zs) {
        super(xs, 0, 0, 0,
                0, ys, 0, 0,
                0, 0, zs, 0,
                0, 0, 0, 1);
    }
}
