/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Flt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

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

    private static void copySrcIntoDstAt(final BufferedImage src, final BufferedImage dst, final int dx, final int dy) {
        // TODO: replace this by a much more efficient method
        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                dst.setRGB(dx + x, dy + y, src.getRGB(x, y));
            }
        }
    }

    public void fixateImage() {
        BufferedImage img = getImage();
        int width = Math.min(getWidth(), img.getWidth(null) - _offset_x);
        int height = Math.min(getHeight(), img.getHeight(null) - _offset_y);
        //   setImage(getImage().getSubimage(_offset_x, _offset_y, width, height));

        BufferedImage n = new BufferedImage(FltSettings.FRAME_WIDTH, FltSettings.FRAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = n.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, FltSettings.FRAME_WIDTH, FltSettings.FRAME_HEIGHT);

        copySrcIntoDstAt(img.getSubimage(_offset_x, _offset_y, width, height), n, 0, 0);
        setImage(n);

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

   //     drawBorder(g);
    }
}
