package FIT_8201_Sviridov_Quad.transformations;

/**
 * Class represents identity transformation

 * @author alstein
 */
public class IdentityTransformation extends TransformationMatrixImpl {

	/**
	 * Ctor
	 */
    public IdentityTransformation() {
        super(1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
    }
}
