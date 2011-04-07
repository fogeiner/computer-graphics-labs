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

    /**
     * Default constructor
     */
    public StatusbarModel() {
        this.inRegion = false;
        listeners = new ArrayList<StatusbarListener>(1);
    }

    /**
     * Returns true if current state is 'in region'
     * @return true if  current state is 'in region' false otherwise
     */
    public boolean isInRegion() {
        return inRegion;
    }

    /**
     * Sets current state to inRegion
     * @param inRegion true if in region, false otherwise
     */
    public void setInRegion(boolean inRegion) {
        this.inRegion = inRegion;
        for (StatusbarListener l : listeners) {
            l.stateChanged();
        }
    }

    /**
     * Returns current Fx value
     * @return current Fx value
     */
    public double getFx() {
        return fx;
    }
    /**
     * Returns current Fy value
     * @return current Fy value
     */
    public double getFy() {
        return fy;
    }

    /**
     * Returns current x value
     * @return current x value
     */
    public double getX() {
        return x;
    }

    /**
     * Returns current y value
     * @return current y value
     */
    public double getY() {
        return y;
    }

    /**
     * Sets current data to given
     * @param x x
     * @param y y
     * @param fx fx
     * @param fy fy
     */
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

    /**
     * Add new listener
     * @param l new listener
     */
    public void addStatusbarListener(StatusbarListener l) {
        listeners.add(l);
    }

    /**
     * Remove listener
     * @param l listener to delete
     */
    public void removeStatusbarListener(StatusbarListener l) {
        listeners.remove(l);
    }

    /**
     * Delete all listeners
     */
    public void clearListeners(){
        listeners.clear();
    }
}
