/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Flt;

import java.awt.image.BufferedImage;

/**
 *
 * @author admin
 */
public class ImageResultPanel extends ImagePanel {

	private static final long serialVersionUID = -3234449880812469456L;

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
        }
    }
}
