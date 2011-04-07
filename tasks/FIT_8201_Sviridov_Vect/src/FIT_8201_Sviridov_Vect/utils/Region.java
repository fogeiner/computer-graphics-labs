package FIT_8201_Sviridov_Vect.utils;

/**
 * Class represents region of space in manner [a,b]x[c,d]
 * Object of the class are immutable and can be easily exchanged via
 * other objects
 * @author admin
 */
public class Region {

    public final double xs;
    public final double xe;
    public final double ys;
    public final double ye;

    /**
     * Constructor with given xs, xe, ys, ye
     * start and end can be swapped, constructor with reverse the interval
     * @param xs x start
     * @param xe x end 
     * @param ys y start
     * @param ye y end
     */
    public Region(double xs, double xe, double ys, double ye) {
        this.xs = Math.min(xs, xe);
        this.xe = Math.max(xs, xe);
        this.ys = Math.min(ys, ye);
        this.ye = Math.max(ys, ye);
    }


    /**
     * Returns width of the region
     * @return  width of the region
     */
    public double width() {
        return xe - xs;
    }

    /**
     * Returns height of the region
     * @return height of the region
     */
    public double height() {
        return ye - ys;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append("[");
        sb.append(Double.toString(xs));
        sb.append(";");
        sb.append(Double.toString(xe));
        sb.append("]x[");
        sb.append(Double.toString(ys));
        sb.append(";");
        sb.append(Double.toString(ye));
        sb.append("]");
        return sb.toString();
    }
}
