/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Flt;

import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author admin
 */
public class ImageNavigationViewerPanel extends ImagePanel {

    private int _offset_x;
    private int _offset_y;
    private boolean _image_chosen;

    public ImageNavigationViewerPanel(String title) {
        super(title);
    }

    public void fixateImage() {
        Image img = getImage();
        int width = Math.min(getWidth(), img.getWidth(null) - _offset_x);
        int height = Math.min(getHeight(), img.getHeight(null) - _offset_y);
        setImage(getImage().getSubimage(_offset_x, _offset_y, width, height));
        _image_chosen = true;
    }

    public void setImageOffset(int x, int y) {
        _image_chosen = false;
        _offset_x = x;
        _offset_y = y;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        clearBackground(g);

        Image img = getImage();

        if (img != null) {
            if (_image_chosen == true) {
                g.drawImage(img, 0, 0, this);
            } else {
                int width = getWidth();
                int height = getHeight();

                g.drawImage(img, 0, 0, width, height, _offset_x, _offset_y, _offset_x + width, _offset_y + height, null);
            }

        } else {
            drawBackgroundTitle(g);
        }

        drawBorder(g);
    }
}
