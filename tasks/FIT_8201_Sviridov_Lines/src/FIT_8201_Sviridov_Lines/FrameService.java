package FIT_8201_Sviridov_Lines;

/**
 * Interface for objects supporting blocking/unblocking
 * @author alstein
 *
 */
public interface FrameService {
	/**
	 * Method sets the block state of the object
	 */
	public void setBlocked(boolean value);

	/**
	 * Method to set/unset modified flag
	 */
	public void setModified(boolean value);
	
	/**
	 * Returns true if file needs saving after being modified, false otherwise
	 */
	public boolean isModified();
}
