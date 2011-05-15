package FIT_8201_Sviridov_Quad.transformations;

import FIT_8201_Sviridov_Quad.Vector;
import FIT_8201_Sviridov_Quad.Vertex;

/**
 * Class represenets FrameToWorldTransformation
 * 
 * @author alstein
 */
public class FrameToWorldTransformation extends TransformationMatrixImpl {
	/**
	 * Ctor
	 * 
	 * @param v1
	 *            basis vector 1
	 * @param v2
	 *            basis vector 2
	 * @param v3
	 *            basis vector 3
	 * @param origin
	 *            origin
	 */
	public FrameToWorldTransformation(Vector v1, Vector v2, Vector v3,
			Vertex origin) {
		super(v1.getX(), v2.getX(), v3.getX(), origin.getX(), v1.getY(), v2
				.getY(), v3.getY(), origin.getY(), v1.getZ(), v2.getZ(), v3
				.getZ(), origin.getZ(), 0, 0, 0, 1);
	}
}
