package FIT_8201_Sviridov_Flt;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author alstein
 */
public class ImagePanel extends JPanel {

    private BufferedImage _img = null;
    private Image _display_img = null;
    private String _title = null;

    public ImagePanel(String title) {
        _title = title;
        setBackground(FltSettings.PANEL_COLOR);
        setPreferredSize(FltSettings.PANEL_SIZE);

        class MouseHandler extends MouseAdapter{

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                System.out.println("mouseDragged");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                System.out.println("mousePressed");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                System.out.println("mouseReleased");
            }

        }

        MouseAdapter ma = new MouseHandler();

        this.addMouseListener(ma);
        this.addMouseMotionListener(ma);
    }



    public void setImage(BufferedImage img) {
        _img = img;
        if (_img == null) {
            _display_img = null;
            repaint();
            return;
        }

        int width = getWidth(),
                height = getHeight(),
                img_width = _img.getWidth(),
                img_height = _img.getHeight();

        double w_ratio = (double) img_width / width;
        double h_ratio = (double) img_height / height;


        int scale_opt = Image.SCALE_SMOOTH;

        if (img_width > width && img_height > height) {
            if (w_ratio > h_ratio) {
                _display_img = _img.getScaledInstance(width, -1, scale_opt);
            } else {
                _display_img = _img.getScaledInstance(-1, height, scale_opt);
            }
        } else if (img_width > width) {
            _display_img = _img.getScaledInstance(width, -1, scale_opt);
        } else if (img_height > height) {
            _display_img = _img.getScaledInstance(-1, height, scale_opt);
        } else {
            _display_img = _img;
        }
        repaint();
    }

    public BufferedImage getImage() {
        return _img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (_display_img != null) {
            g.drawImage(_display_img, 0, 0, null);
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
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
