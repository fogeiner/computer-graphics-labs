package FIT_8201_Sviridov_Quad.transformations;

/**
 *
 * @author alstein
 */
public class TranslationTransformation extends TransformationMatrixImpl {

    public TranslationTransformation(double tx, double ty, double tz) {
        super(1, 0, 0, tx,
                0, 1, 0, ty,
                0, 0, 1, tz,
                0, 0, 0, 1);
    }
}
