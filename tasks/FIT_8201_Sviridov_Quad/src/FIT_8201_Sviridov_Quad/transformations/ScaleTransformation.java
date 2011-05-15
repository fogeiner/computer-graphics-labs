package FIT_8201_Sviridov_Quad.transformations;

/**
 * Class represents ScaleTransformation
 * 
 * @author alstein
 */
public class ScaleTransformation extends TransformationMatrixImpl {

	/**
	 * Ctor
	 * 
	 * @param xs
	 *            x factor
	 * @param ys
	 *            y factor
	 * @param zs
	 *            z factor
	 */
	public ScaleTransformation(double xs, double ys, double zs) {
		super(xs, 0, 0, 0, 0, ys, 0, 0, 0, 0, zs, 0, 0, 0, 0, 1);
	}
}
