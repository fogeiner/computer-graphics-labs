package FIT_8201_Sviridov_Vect.statusbar;

/**
 *	Listener interface for StatusbatModel model
 *
 * @author alstein
 */
public interface StatusbarListener {
	/**
	 * Called by model when its data changes
	 */
    public void dataChanged();
    
	/**
	 * Called by model when its state changes
	 */
    public void stateChanged();
}
