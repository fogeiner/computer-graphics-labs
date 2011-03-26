package FIT_8201_Sviridov_Flt;

/**
 * Class encapsulates app frame service interface
 * 
 * @author alstein
 */
public interface FltFrameService {

	/**
	 * Blocks/unblocks select menu entry/button
	 * 
	 * @param value
	 *            new value
	 */
	public void setSelectBlocked(boolean value);

	/**
	 * Blocks/unblocks C to B menu entry/button
	 * 
	 * @param value
	 *            new value
	 */
	public void setFromCtoBBlocked(boolean value);

	/**
	 * Blocks/unblocks B to C menu entry/button
	 * 
	 * @param value
	 *            new value
	 */
	public void setFromBtoCBlocked(boolean value);

	/**
	 * Blocks/unblocks save menu entry/button
	 * 
	 * @param value
	 *            new value
	 */
	public void setSaveBlocked(boolean value);

	/**
	 * Blocks/unblocks filters menu entries/buttons
	 * 
	 * @param value
	 *            new value
	 */
	public void setFiltersBlocked(boolean value);

	/**
	 * Method to set/unset modified flag
	 */
	public void setModified(boolean value);

	/**
	 * Returns true if file needs saving after being modified, false otherwise
	 */
	public boolean isModified();
}
