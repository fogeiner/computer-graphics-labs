/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Cam;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author alstein
 */
public class Rect3D {

    private final double width;
    private final double height;
    private final double depth;
    private static final NumberFormat format;

    static {
        format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }

    public Rect3D(double width, double height, double depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public double getDepth() {
        return depth;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(format.format(width));
        sb.append(",");
        sb.append(format.format(height));
        sb.append(",");
        sb.append(format.format(depth));
        sb.append(")");
        return sb.toString();
    }
}
