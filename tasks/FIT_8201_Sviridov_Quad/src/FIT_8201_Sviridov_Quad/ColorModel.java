package FIT_8201_Sviridov_Quad;

/**
 *
 * @author alstein
 */
public class ColorModel {

    private final Coefficient3D ambientCoefficient;
    private final Coefficient3D diffuseCoefficient;
    private final Coefficient3D specularCoefficient;
    private final int power;
    private final double transparencyCoefficient;
    private final double Refraction1;
    private final double Refraction2;

    public ColorModel(Coefficient3D ambientCoefficient, Coefficient3D diffuseCoefficient, Coefficient3D specularCoefficient, int power, double transparencyCoefficient, double Refraction1, double Refraction2) {
        this.ambientCoefficient = ambientCoefficient;
        this.diffuseCoefficient = diffuseCoefficient;
        this.specularCoefficient = specularCoefficient;
        this.power = power;
        this.transparencyCoefficient = transparencyCoefficient;
        this.Refraction1 = Refraction1;
        this.Refraction2 = Refraction2;
    }

    public double getRefraction1() {
        return Refraction1;
    }

    public double getRefraction2() {
        return Refraction2;
    }

    public Coefficient3D getAmbientCoefficient() {
        return ambientCoefficient;
    }

    public Coefficient3D getDiffuseCoefficient() {
        return diffuseCoefficient;
    }

    public int getPower() {
        return power;
    }

    public Coefficient3D getSpecularCoefficient() {
        return specularCoefficient;
    }

    public double getTransparencyCoefficient() {
        return transparencyCoefficient;
    }
}
