package FIT_8201_Sviridov_Vect;

/**
 * Class encapsulates app frame service interface
 * 
 * @author alstein
 */
public interface FrameService {
	/**
	 * Blocks/unblocks save menu entry/button
	 * 
	 * @param value
	 *            new value
	 */
	public void setSaveBlocked(boolean value);


	/**
	 * Method to set/unset modified flag
	 */
	public void setModified(boolean value);

	/**
	 * Returns true if file needs saving after being modified, false otherwise
	 */
	public boolean isModified();
}
