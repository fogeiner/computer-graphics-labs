package FIT_8201_Sviridov_Flt;

import java.awt.image.BufferedImage;

/**
 * Zone C panel class
 * @author admin
 */
public class ImageResultPanel extends ImagePanel {

    private static final long serialVersionUID = -3234449880812469456L;

    /**
     * Constructor with background title
     * @param title
     */
    public ImageResultPanel(String title) {
        super(title);
    }

    @Override
    public void setImage(BufferedImage img) {
        super.setImage(img);
        if (img == null) {
            _frame.setFromCtoBBlocked(true);
            _frame.setSaveBlocked(true);
        } else {
            _frame.setFromCtoBBlocked(false);
            _frame.setSaveBlocked(false);
            _frame.setModified(true);
        }
    }
}
