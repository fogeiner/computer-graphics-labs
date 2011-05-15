package FIT_8201_Sviridov_Quad.transformations;

import FIT_8201_Sviridov_Quad.Vector;

/**
 * Class represents PlaneProjectionTransformation
 * 
 * @author alstein
 */
public class PlaneProjectionTransformation extends TransformationMatrixImpl {

	/**
	 * Creates PlaneProjectionTransformation based on given plane normal vector
	 * 
	 * @param n normal vector
	 * @return PlaneProjectionTransformation
	 */
	public static PlaneProjectionTransformation makePlaneProjectionTransformation(
			Vector n) {
		n = n.normalize();
		double a = n.getX(), b = n.getY(), c = n.getZ();
		double d = a * a + b * b + c * c;
		return new PlaneProjectionTransformation(1 - a * a / d, a * b / d, a
				* c / d, 0, a * b / d, 1 - b * b / d, b * c / d, 0, a * c / d,
				b * c / d, 1 - c * c / d, 0, 0, 0, 0, 1);
	}

	/**
	 * Ctor for transformation with given matrix elements
	 */
	private PlaneProjectionTransformation(double m11, double m12, double m13,
			double m14, double m21, double m22, double m23, double m24,
			double m31, double m32, double m33, double m34, double m41,
			double m42, double m43, double m44) {
		super(m11, m12, m13, m14, m21, m22, m23, m24, m31, m32, m33, m34, m41,
				m42, m43, m44);
	}
}
