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

    public ImageResultPanel(String title, FltFrameService frame) {
        super(title, frame);
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
