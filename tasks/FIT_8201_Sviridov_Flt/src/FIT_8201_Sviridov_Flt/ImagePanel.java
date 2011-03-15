package FIT_8201_Sviridov_Flt;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author alstein
 */
public class ImagePanel extends JPanel {

    private BufferedImage _img = null;
    private String _title = null;

    public ImagePanel(String title) {
        _title = title;
        setBackground(FltSettings.PANEL_COLOR);
        setPreferredSize(FltSettings.PANEL_SIZE);
    }

    public void setImage(BufferedImage img) {
        _img = img;
        repaint();
    }

    public BufferedImage getImage() {
        return _img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        if (_img != null) {
            g.drawImage(_img, 0, 0, _img.getWidth(), _img.getHeight(), null);

        } else {
            g.setFont(new Font("Monospaced", Font.BOLD, 48));
            FontMetrics fm = g.getFontMetrics();
            int width = fm.stringWidth(_title);
            int ascent = fm.getMaxAscent();
            int descent = fm.getMaxDescent();
            int msg_x = getWidth() / 2 - width / 2;
            int msg_y = getHeight() / 2 - descent / 2 + ascent / 2;
            g.drawString(_title, msg_x, msg_y);
        }

    }
}
