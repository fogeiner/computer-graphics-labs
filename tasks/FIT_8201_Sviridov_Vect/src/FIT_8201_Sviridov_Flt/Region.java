package FIT_8201_Sviridov_Vect;

/**
 *
 * @author admin
 */
public class Region {

    public final double xs;
    public final double xe;
    public final double ys;
    public final double ye;

    public Region(double xs, double xe, double ys, double ye) {
        this.xs = Math.min(xs, xe);
        this.xe = Math.max(xs, xe);
        this.ys = Math.min(ys, ye);
        this.ye = Math.max(ys, ye);
    }

    public Region(final Region region) {
        this.xs = region.xs;
        this.xe = region.xe;
        this.ys = region.ys;
        this.ye = region.ye;
    }

    public double width() {
        return xe - xs;
    }

    public double height() {
        return ye - ys;
    }
}
