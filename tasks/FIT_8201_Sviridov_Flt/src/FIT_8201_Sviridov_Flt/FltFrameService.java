package FIT_8201_Sviridov_Flt;

/**
 * Class incapsulates app frame service interface
 * @author admin
 */
public interface FltFrameService {

    /**
     * Blocks/unblocks select menu entry/button
     * @param value new value
     */
    public void setSelectBlocked(boolean value);

    /**
     * Blocks/unblocks C to B menu entry/button
     * @param value new value
     */
    public void setFromCtoBBlocked(boolean value);

    /**
     * Blocks/unblocks save menu entry/button
     * @param value new value
     */
    public void setSaveBlocked(boolean value);

    /**
     * Blocks/unblocks filters menu entries/buttons
     * @param value new value
     */
    public void setFiltersBlocked(boolean value);
}
