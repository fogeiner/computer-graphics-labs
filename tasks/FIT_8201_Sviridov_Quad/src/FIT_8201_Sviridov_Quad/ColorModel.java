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

    public ColorModel(Coefficient3D ambientCoefficient,
            Coefficient3D diffuseCoefficient,
            Coefficient3D specularCoefficient,
            double power,
            double transparencyCoefficient,
            double Refraction1,
            double Refraction2) {
        this.ambientCoefficient = ambientCoefficient;
        this.diffuseCoefficient = diffuseCoefficient;
        this.specularCoefficient = specularCoefficient;
        this.power = power;
        this.transparencyCoefficient = transparencyCoefficient;
        this.refraction1 = Refraction1;
        this.refraction2 = Refraction2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        for (Coefficient3D c : new Coefficient3D[]{
                    ambientCoefficient,
                    diffuseCoefficient,
                    specularCoefficient}) {
            sb.append(c.getR());
            sb.append(' ');
            sb.append(c.getG());
            sb.append(' ');
            sb.append(c.getB());
            sb.append(' ');
        }

        sb.append(power);
        sb.append(' ');
        sb.append(refraction1);
        sb.append(' ');
        sb.append(refraction2);
        return sb.toString();
    }

    public double getRefraction1() {
        return refraction1;
    }

    public double getRefraction2() {
        return refraction2;
    }

    public Coefficient3D getAmbientCoefficient() {
        return ambientCoefficient;
    }

    public Coefficient3D getDiffuseCoefficient() {
        return diffuseCoefficient;
    }

    public double getPower() {
        return power;
    }

    public Coefficient3D getSpecularCoefficient() {
        return specularCoefficient;
    }

    public double getTransparencyCoefficient() {
        return transparencyCoefficient;
    }
}
