package FIT_8201_Sviridov_Vect.state_history;

/**
 * Listener interface for StateHistory model
 *
 * @author admin
 */
public interface StateHistoryListener {

	/**
	 * Called by model when its state changes
	 */
    public void historyStateChanged();
}
