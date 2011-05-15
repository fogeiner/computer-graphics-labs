package FIT_8201_Sviridov_Quad;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class for 3D rect with width, height and depth
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
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setGroupingUsed(false);
        }
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }

    /**
     * Ctor with given width, height and depth
     *
     * @param width
     *            width
     * @param height
     *            height
     * @param depth
     *            depth
     */
    public Rect3D(double width, double height, double depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    /**
     * Returns depth
     *
     * @return depth
     */
    public double getDepth() {
        return depth;
    }

    /**
     * Returns height
     *
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Return width
     *
     * @return width
     */
    public double getWidth() {
        return width;
    }

    public double getMax() {
        return Math.max(Math.max(width, height), depth);
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
