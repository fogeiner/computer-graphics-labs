package FIT_8201_Sviridov_Vect.vect;

/**
 *
 * @author admin
 */
public interface VectListener {

    /**
     * Explicitly called when notifyListners is called on
     * model
     */
    public void modelChanged();

    /**
     * Called after model region changes
     */
    public void regionChanged();

    /**
     * Called after model vector length multiplier
     */
    public void lengthMultChanged();

    /**
     * Called after model grid changes
     */
    public void gridChanged();

    /**
     * Called after model grid color changes
     */
    public void gridColorChanged();

    /**
     * Called after model field color changes
     */
    public void fieldColorChanged();

    /**
     * Called after model color set changes
     */
    public void colorsChanged();

    /**
     * Called after model field mode changes
     */
    public void fieldModeChanged();

    /**
     * Called after model grid drawn changes
     */
    public void gridDrawnChanged();

    /**
     * Called after model arrow drawn style changes
     */
    public void arrowModeChanged();

    /**
     * Called after model vectors chess mode changes
     */
    public void chessModeChanged();
}
