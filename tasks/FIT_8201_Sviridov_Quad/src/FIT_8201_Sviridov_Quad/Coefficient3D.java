package FIT_8201_Sviridov_Quad;


/**
 *
 * @author alstein
 */
public class Coefficient3D {

    private final double c1, c2, c3;

    public Coefficient3D(double c1, double c2, double c3) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

    public double getR() {
        return c1;
    }

    public double getG() {
        return c2;
    }

    public double getB() {
        return c3;
    }

    public double getX() {
        return c1;
    }

    public double getY() {
        return c2;
    }

    public double getZ() {
        return c3;
    }

    @Override
    public String toString() {
        return c1 + " " + c2 + " " + c3;
    }


}
