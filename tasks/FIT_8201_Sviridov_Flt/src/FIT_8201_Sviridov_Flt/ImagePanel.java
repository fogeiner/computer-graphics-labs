package FIT_8201_Sviridov_Flt;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Base class for JPanels with Images
 * @author alstein
 */
public class ImagePanel extends JPanel {

    private static final long serialVersionUID = -4771755251258011769L;
    // saved image
    private BufferedImage _img = null;
    // background zone title
    private String _title = null;
    // x coordinate of left upper corner
    protected FltFrameService _frame;

    /**
     * Constructor with background title
     * @param title background title
     */
    public ImagePanel(String title) {

        _title = title;
        setBackground(FltSettings.PANEL_COLOR);
        setPreferredSize(FltSettings.PANEL_SIZE);

    }

    /**
     * Sets new FrameService object
     * @param frame frame service object
     */
    public void setFrameService(FltFrameService frame) {
        _frame = frame;
    }

    /**
     * Sets new image
     * @param img new image
     */
    public void setImage(BufferedImage img) {
        _img = img;
        repaint();
    }

    /**
     * Returns image
     * @return image
     */
    public BufferedImage getImage() {
        return _img;
    }

    /**
     * Draws title in the background of panel
     * @param g Graphics object
     */
    public void drawBackgroundTitle(Graphics g) {
        g.setFont(new Font("Monospaced", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(_title);
        int ascent = fm.getMaxAscent();
        int descent = fm.getMaxDescent();
        int msg_x = getWidth() / 2 - width / 2;
        int msg_y = getHeight() / 2 - descent / 2 + ascent / 2;
        g.drawString(_title, msg_x, msg_y);

    }

    /**
     * Cleans background
     * @param g Graphics object
     */
    public void clearBackground(Graphics g) {
        Color c = g.getColor();
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(c);
    }

    @Override
    protected void paintComponent(Graphics g) {
        clearBackground(g);

        if (_img != null) {
            g.drawImage(_img, 0, 0, null);
        } else {
            drawBackgroundTitle(g);
        }
    }
}
