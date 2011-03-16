package FIT_8201_Sviridov_Flt;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author alstein
 */
public class ImagePanel extends JPanel {

    // saved image
    private BufferedImage _img = null;
    // scaled image to be displayed
    private Image _display_img = null;
    // background zone title
    private String _title = null;
    // is gragging active
    private boolean _dragging = false;
    // x coordingate of left upeer corner
    private int _selection_x = 10;
    // y coordinate of left upper corner of selection
    private int _selection_y = 10;
    // selection width
    private int _selection_width = 100;
    // selection height
    private int _select_height = 100;
    // saved selection stroke
    private Stroke _stroke;

    public ImagePanel(String title) {
        _title = title;
        setBackground(FltSettings.PANEL_COLOR);
        setPreferredSize(FltSettings.PANEL_SIZE);

        class MouseHandler extends MouseAdapter {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                System.out.println("mouseDragged");
                if (_dragging == false || _display_img == null) {
                    return;
                }

                Graphics g = getGraphics();

                Point p = e.getPoint();
                int x = p.x;
                int y = p.y;


                /*
                 * ._________.
                 * |         |
                 * |         |
                 * |         |
                 * ._________. 
                 */

                int img_width = _img.getWidth(),
                        img_height = _img.getHeight();

                repaint();

                int width = 300 * 300 / img_width;
                int height = 300 * 300 / img_height;

                int xlu, ylu, xrl, yrl;
                xlu = x - width / 2 - 1;
                ylu = y - height / 2 - 1;

                xrl = x + width / 2;
                yrl = y + height / 2;

                if (xlu < 0) {
                    xlu = 0;
                }
                if (ylu < 0) {
                    ylu = 0;
                }

                if (xrl > img_width) {
                    xrl = img_width - 1;
                }
                if (xrl > img_height) {
                    yrl = img_height - 1;
                }

                g.drawRect(xlu, ylu, width, height);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                System.out.println("mousePressed");
                _dragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                System.out.println("mouseReleased");
                _dragging = false;
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
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;

        if (_display_img != null) {
            g.drawImage(_display_img, 0, 0, null);
            if (_dragging == true) {
                if (_stroke == null) {
                    _stroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10,
                            new float[]{1, 1}, 0);
                }
                Stroke old_stroke = g.getStroke();
                g.setStroke(_stroke);
                g.drawRect(_selection_x, _selection_y, 10, 10);
                g.setStroke(old_stroke);
            }
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
