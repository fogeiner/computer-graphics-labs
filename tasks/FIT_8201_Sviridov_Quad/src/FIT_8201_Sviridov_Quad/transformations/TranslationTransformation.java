package FIT_8201_Sviridov_Quad.transformations;

/**
 * Class represents ScaleTransformation
 * @author alstein
 */
public class TranslationTransformation extends TransformationMatrixImpl {

	/**
	 * Ctor
	 * @param tx x
	 * @param ty y
	 * @param tz z
	 */
    public TranslationTransformation(double tx, double ty, double tz) {
        super(1, 0, 0, tx,
                0, 1, 0, ty,
                0, 0, 1, tz,
                0, 0, 0, 1);
    }
}
