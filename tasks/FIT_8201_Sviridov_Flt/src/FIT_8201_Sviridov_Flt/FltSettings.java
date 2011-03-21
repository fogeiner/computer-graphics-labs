package FIT_8201_Sviridov_Flt;

import java.awt.Color;
import java.awt.Dimension;

/**
 * Interface encapsulates app settings
 * @author alstein
 */
public interface FltSettings {

    public static final String FLT_NAME = "FIT_8201_Sviridov_Flt";
    public static final String UNTITLED_DOCUMENT = "Untitled";
    public static final String ABOUT_FILE = "FIT_8201_Sviridov_Flt_About.txt";
    public static final int FRAME_WIDTH = 1000;
    public static final int FRAME_HEIGHT = 500;
    public static final int PANEL_WIDTH = 520;
    public static final int PANEL_HEIGHT = 520;
    public static final Dimension PANEL_SIZE = new Dimension(PANEL_WIDTH,
            PANEL_HEIGHT);
    public static final Color PANEL_COLOR = Color.white;
    public static final int PANEL_PADDING = 10;
}
