package FIT_8201_Sviridov_Quad;

/**
 * 
 * @author alstein
 */
public class ColorModel {

	private final Coefficient3D ambientCoefficient;
	private final Coefficient3D diffuseCoefficient;
	private final Coefficient3D specularCoefficient;
	private final double power;
	private final double transparencyCoefficient;
	private final double refraction1;
	private final double refraction2;

	/**
	 * Default ctor
	 * 
	 * @param ambientCoefficient
	 *            ambient
	 * @param diffuseCoefficient
	 *            diffuse
	 * @param specularCoefficient
	 *            specular
	 * @param power
	 *            power
	 * @param transparencyCoefficient
	 *            transparency
	 * @param refraction1
	 *            refraction1
	 * @param refraction2
	 *            refraction2
	 */
	public ColorModel(Coefficient3D ambientCoefficient,
			Coefficient3D diffuseCoefficient,
			Coefficient3D specularCoefficient, double power,
			double transparencyCoefficient, double refraction1,
			double refraction2) {
		this.ambientCoefficient = ambientCoefficient;
		this.diffuseCoefficient = diffuseCoefficient;
		this.specularCoefficient = specularCoefficient;
		this.power = power;
		this.transparencyCoefficient = transparencyCoefficient;
		this.refraction1 = refraction1;
		this.refraction2 = refraction2;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(50);
		for (Coefficient3D c : new Coefficient3D[] { ambientCoefficient,
				diffuseCoefficient, specularCoefficient }) {
			sb.append(c.getR());
			sb.append(' ');
			sb.append(c.getG());
			sb.append(' ');
			sb.append(c.getB());
			sb.append(' ');
		}

		sb.append(power);
		sb.append(' ');
		sb.append(transparencyCoefficient);
		sb.append(' ');
		sb.append(refraction1);
		sb.append(' ');
		sb.append(refraction2);
		return sb.toString();
	}

	/**
	 * Returns refraction1
	 * 
	 * @return refraction1
	 */
	public double getRefraction1() {
		return refraction1;
	}

	/**
	 * Returns refraction2
	 * 
	 * @return refraction2
	 */
	public double getRefraction2() {
		return refraction2;
	}

	/**
	 * Returns ambient
	 * 
	 * @return ambient
	 */
	public Coefficient3D getAmbientCoefficient() {
		return ambientCoefficient;
	}

	/**
	 * Returns diffuse
	 * 
	 * @return diffuse
	 */
	public Coefficient3D getDiffuseCoefficient() {
		return diffuseCoefficient;
	}

	/**
	 * Returns power
	 * 
	 * @return power
	 */
	public double getPower() {
		return power;
	}

	/**
	 * Returns specular
	 * 
	 * @return specular
	 */
	public Coefficient3D getSpecularCoefficient() {
		return specularCoefficient;
	}

	/**
	 * Returns transparency
	 * 
	 * @return transparency
	 */
	public double getTransparencyCoefficient() {
		return transparencyCoefficient;
	}
}
