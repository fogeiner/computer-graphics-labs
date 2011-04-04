package FIT_8201_Sviridov_Vect;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alstein
 */
public class StatusbarModel {

    public static final int IN_REGION = 0;
    public static final int OUT_REGION = 1;
    private List<StatusbarListener> listeners;
    private int state;
    private double x;
    private double y;
    private double fx;
    private double fy;

    public StatusbarModel() {
        listeners = new ArrayList<StatusbarListener>(1);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
