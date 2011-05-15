package FIT_8201_Sviridov_Quad.transformations;

/**
 * Class represenets PerspectiveProjectionTransformation
 * 
 * @author alstein
 */
public class PerspectiveProjectionTransformation extends
		TransformationMatrixImpl {

	/**
	 * Ctor
	 * 
	 * @param sw
	 *            width
	 * @param sh
	 *            height
	 * @param n
	 *            znear
	 * @param f
	 *            zfar
	 */
	public PerspectiveProjectionTransformation(double sw, double sh, double n,
			double f) {
		super(2 * n / sw, 0, 0, 0, 0, 2 * n / sh, 0, 0, 0, 0, n / (f - n), -f
				* n / (f - n), 0, 0, -1, 0);

	}
}
