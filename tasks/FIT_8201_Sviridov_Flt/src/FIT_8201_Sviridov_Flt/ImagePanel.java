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
    private int _selection_x;
    // y coordinate of left upper corner of selection
    private int _selection_y;
    // selection width
    private int _selection_width;
    // selection height
    private int _selection_height;

    public ImagePanel(String title) {
        _title = title;
        setBackground(FltSettings.PANEL_COLOR);
        setPreferredSize(FltSettings.PANEL_SIZE);

        class MouseHandler extends MouseAdapter {

            private void updateSelection(MouseEvent e) {
                Point p = e.getPoint();
                int x = p.x;
                int y = p.y;

                int display_width = _display_img.getWidth(null);
                int display_height = _display_img.getHeight(null);

                int left_upper_x = x - _selection_width / 2, left_upper_y = y - _selection_height / 2;
                int right_lower_x = x + _selection_width / 2, right_lower_y = y + _selection_height / 2;

                if (left_upper_x >= 0 && right_lower_x <= display_width) {
                    _selection_x = left_upper_x;
                } else if (left_upper_x < 0) {
                    _selection_x = 0;
                } else if (right_lower_x > display_width) {
                    _selection_x = display_width - _selection_width;
                }

                if (left_upper_y >= 0 && right_lower_y <= display_height) {
                    _selection_y = left_upper_y;
                } else if (left_upper_y < 0) {
                    _selection_y = 0;
                } else if (right_lower_y > display_height) {
                    _selection_y = display_height - _selection_height;
                }


                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                System.out.println("mouseDragged");
                if (_dragging == false || _display_img == null) {
                    return;
                }

                updateSelection(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                System.out.println("mousePressed");
                _dragging = true;
                updateSelection(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                System.out.println("mouseReleased");
                _dragging = false;
                repaint();
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

        int panel_width = getWidth(),
                panel_height = getHeight(),
                img_width = _img.getWidth(),
                img_height = _img.getHeight();

        double w_ratio = (double) img_width / panel_width;
        double h_ratio = (double) img_height / panel_height;

        int scale_opt = Image.SCALE_SMOOTH;

        if (img_width > panel_width && img_height > panel_height) {
            // width and height are considered to be equal!
            if (w_ratio > h_ratio) {
                _display_img = _img.getScaledInstance(panel_width, -1, scale_opt);
            } else {
                _display_img = _img.getScaledInstance(-1, panel_height, scale_opt);
            }

            double ratio = (double) _display_img.getHeight(null) / _img.getHeight();
            _selection_width = (int) (ratio * _display_img.getWidth(null));
            _selection_height = (int) (ratio * _display_img.getHeight(null));
        } else if (img_width > panel_width) {
            _display_img = _img.getScaledInstance(panel_width, -1, scale_opt);

            double ratio = (double) _display_img.getHeight(null) / _img.getHeight();
            _selection_width = (int) (ratio * _display_img.getWidth(null));
            _selection_height = _display_img.getHeight(null);
        } else if (img_height > panel_height) {
            _display_img = _img.getScaledInstance(-1, panel_height, scale_opt);
            double ratio = (double) _display_img.getHeight(null) / _img.getHeight();
            _selection_width = _display_img.getWidth(null);
            _selection_height = (int) (ratio * _display_img.getHeight(null));
        } else {
            _display_img = _img;
            _selection_height = panel_height;
            _selection_width = panel_width;
        }

        _selection_x = 0;
        _selection_y = 0;

        repaint();
    }

    public BufferedImage getImage() {
        return _img;
    }

    @Override
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        g.drawLine(0, 0, 0, 10);
        if (_display_img != null) {
            g.drawImage(_display_img, 0, 0, null);
            if (_dragging == true) {

                g.drawRect(_selection_x, _selection_y, _selection_width - 1, _selection_height - 1);
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
