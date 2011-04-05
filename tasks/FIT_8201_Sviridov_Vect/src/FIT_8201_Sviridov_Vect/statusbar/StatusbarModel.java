package FIT_8201_Sviridov_Vect.statusbar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alstein
 */
public class StatusbarModel {

    private List<StatusbarListener> listeners;
    private boolean inRegion;
    private double x;
    private double y;
    private double fx;
    private double fy;

    public StatusbarModel(boolean inRegion) {
        this.inRegion = inRegion;
        listeners = new ArrayList<StatusbarListener>(1);
    }

    public boolean isInRegion() {
        return inRegion;
    }

    public void setInRegion(boolean inRegion) {
        this.inRegion = inRegion;
        for (StatusbarListener l : listeners) {
            l.stateChanged();
        }
    }

    public double getFx() {
        return fx;
    }

    public double getFy() {
        return fy;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setData(double x, double y,
            double fx, double fy) {
        this.x = x;
        this.y = y;
        this.fx = fx;
        this.fy = fy;

        for (StatusbarListener l : listeners) {
            l.dataChanged();
        }
    }

    public void addStatusbarListener(StatusbarListener l) {
        listeners.add(l);
    }

    public void removeStatusbarListener(StatusbarListener l) {
        listeners.remove(l);
    }
}
