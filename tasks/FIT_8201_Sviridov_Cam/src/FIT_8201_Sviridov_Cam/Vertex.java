package FIT_8201_Sviridov_Cam;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author alstein
 */
public class Vertex {

    public static final Color DEFAULT_COLOR = Color.black;
    private double x, y, z, w;
    private Color color;
    private final static NumberFormat format;

    static {
        format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }

    public Vertex(double v[]) {
        if (v.length == 3) {
            this.x = v[0];
            this.y = v[1];
            this.z = v[2];
        } else if (v.length == 4) {
            this.x = v[0];
            this.y = v[1];
            this.z = v[2];
            this.w = v[3];
        } else {
            throw new IllegalArgumentException("Vector length can be either 3 or 4");
        }
    }

    public Vertex(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vertex(double x, double y, double z) {
        this(x, y, z, 1.0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(format.format(x));
        sb.append(';');
        sb.append(format.format(y));
        sb.append(';');
        sb.append(format.format(z));
        if (w != 1.0) {
            sb.append(';');
            sb.append(format.format(w));
        }
        sb.append(')');
        return sb.toString();
    }

    public Vertex normalize() {
        return new Vertex(this.x / this.w, this.y / this.w, this.z / this.w);
    }

    public double[] getV() {
        return new double[]{x, y, z, w};
    }

    public Color getColor() {
        return color;
    }

    public double getW() {
        return w;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setW(double w) {
        this.w = w;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
