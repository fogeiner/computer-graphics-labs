package FIT_8201_Sviridov_Quad.transformations;

/**
 *
 * @author alstein
 */
public class IdentityTransformation extends TransformationMatrixImpl {

    public IdentityTransformation() {
        super(1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
    }
}
