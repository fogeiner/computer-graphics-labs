package FIT_8201_Sviridov_Quad;

/**
 * Class for 3d coefficients
 * 
 * @author alstein
 */
public class Coefficient3D {

	private final double c1, c2, c3;

	/**
	 * Ctor with given coefs
	 * 
	 * @param c1
	 *            coef 1
	 * @param c2
	 *            coef 2
	 * @param c3
	 *            coef 3
	 */
	public Coefficient3D(double c1, double c2, double c3) {
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
	}

	/**
	 * Returns 1st coef
	 * 
	 * @return 1st coef
	 */
	public double getR() {
		return c1;
	}

	/**
	 * Returns 2nd coef
	 * 
	 * @return 2nd coef
	 */
	public double getG() {
		return c2;
	}

	/**
	 * Returns 3rd coef
	 * 
	 * @return 3rd coef
	 */
	public double getB() {
		return c3;
	}

	/**
	 * Returns 1st coef
	 * 
	 * @return 1st coef
	 */
	public double getX() {
		return c1;
	}

	/**
	 * Returns 2nd coef
	 * 
	 * @return 2nd coef
	 */
	public double getY() {
		return c2;
	}

	/**
	 * Returns 3rd coef
	 * 
	 * @return 3rd coef
	 */
	public double getZ() {
		return c3;
	}

	@Override
	public String toString() {
		return c1 + " " + c2 + " " + c3;
	}

}
