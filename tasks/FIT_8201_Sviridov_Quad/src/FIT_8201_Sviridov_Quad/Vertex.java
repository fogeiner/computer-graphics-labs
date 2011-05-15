package FIT_8201_Sviridov_Quad;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class represents vertex
 * @author alstein
 */
public class Vertex {

    public static final Color DEFAULT_COLOR = Color.black;
    private final double x, y, z, w;
    private final static NumberFormat format;

    static {
        format = NumberFormat.getInstance(Locale.ENGLISH);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setGroupingUsed(false);
        }
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }

    /**
     * Ctor
     * @param v array of x,y,z{,w}
     */
    public Vertex(double v[]) {
        if (v.length == 3) {
            this.x = v[0];
            this.y = v[1];
            this.z = v[2];
            this.w = 1.0;
        } else if (v.length == 4) {
            this.x = v[0];
            this.y = v[1];
            this.z = v[2];
            this.w = v[3];
        } else {
            throw new IllegalArgumentException(
                    "Vector length can be either 3 or 4");
        }
    }

    /**
     * Ctor
     * @param x x
     * @param y y
     * @param z z
     * @param w w
     */
    public Vertex(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Ctor w == 1.0
     * @param x x
     * @param y y 
     * @param z z
     */
    public Vertex(double x, double y, double z) {
        this(x, y, z, 1.0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(format.format(x));
        sb.append(' ');
        sb.append(format.format(y));
        sb.append(' ');
        sb.append(format.format(z));

        // if w!=1.0 it's better to return w as well, alas!

        return sb.toString();
    }

    /**
     * Returns normalized vertex
     * @return normalized vertex
     */
    public Vertex normalize() {
        return new Vertex(this.x / this.w, this.y / this.w, this.z / this.w);
    }

    /**
     * Returns array with x, y, z, w
     * @return array with x, y, z, w
     */
    public double[] getV() {
        return new double[]{x, y, z, w};
    }

   
    /**
     * Returns w
     * @return w
     */
    public double getW() {
        return w;
    }

    /**
     * Returns x
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * Returns y
     * @return y
     */
    public double getY() {
        return y;
    }

    /**
     * Returns z
     * @return z
     */
    public double getZ() {
        return z;
    }
}
