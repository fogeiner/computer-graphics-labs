package FIT_8201_Sviridov_Flt;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 *
 * @author alstein
 */
public class ImageNavigationPanel extends ImagePanel {

    private static final long serialVersionUID = -230617714845237734L;
    // scaled image to be displayed
    private Image _display_img = null;
    // x coordingate of left upeer corner
    private int _selection_x;
    // y coordinate of left upper corner of selection
    private int _selection_y;
    private double _x_ratio;
    private double _y_ratio;
    // selection width
    private int _selection_width;
    // selection height
    private int _selection_height;
    // selecting should be started after click
    private boolean _selecting_after_pressed = false;
    // selecting region
    private boolean _selecting = false;
    // Stroke for dashed rectangle
    private Stroke _stroke;
    // panel to view original sized part of the image
    private ImageNavigationViewerPanel _viewer_panel;

    public void startSelecting() {
        Image img = getImage();
        int img_width = img.getWidth(null);
        int img_height = img.getHeight(null);
        int panel_width = _viewer_panel.getWidth();
        int panel_height = _viewer_panel.getHeight();

        if (img_width <= panel_width && img_height <= panel_height) {
            _viewer_panel.setImage(getImage());
            _viewer_panel.setImageOffset(0, 0);
            _viewer_panel.fixateImage();
            _frame.setSelectBlocked(false);
            return;
        }

        _selecting_after_pressed = true;
    }

    public ImageNavigationPanel(String title, ImageNavigationViewerPanel viewer_panel) {
        super(title);
        _viewer_panel = viewer_panel;

        class MouseHandler extends MouseAdapter {

            private void updateSelection(MouseEvent e) {
                Point p = e.getPoint();
                int x = p.x;
                int y = p.y;

                int display_width = _display_img.getWidth(null);
                int display_height = _display_img.getHeight(null);

                int left_upper_x = x - _selection_width / 2, left_upper_y = y - _selection_height / 2;
                int right_lower_x = x + _selection_width / 2, right_lower_y = y + _selection_height / 2;

                if (left_upper_x > 0 && right_lower_x < display_width) {
                    _selection_x = left_upper_x;
                } else if (left_upper_x <= 0) {
                    _selection_x = 0;
                } else if (right_lower_x >= display_width) {
                    // -1 for rect not to be go beyond right border
                    _selection_x = display_width - _selection_width;
                }

                if (left_upper_y > 0 && right_lower_y < display_height) {
                    _selection_y = left_upper_y;
                } else if (left_upper_y <= 0) {
                    _selection_y = 0;
                } else if (right_lower_y >= display_height) {
                    _selection_y = display_height - _selection_height;
                }

//                System.out.println("display width: " + display_width + " display height: " + display_height
//                        + " left_upper_x: " + left_upper_x + " left_upper_y: " + left_upper_y + " right_lower_x: " + right_lower_x
//                        + " right_lower_y: " + right_lower_y + " selection_x: " + _selection_x + " selection_y: " + _selection_y);

                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (_selecting == false) {
                    return;
                }

                if (_display_img == null) {
                    return;
                }

                updateSelection(e);

                _viewer_panel.setImageOffset((int) (_selection_x * _x_ratio + 0.5), (int) (_selection_y * _y_ratio + 0.5));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (_selecting_after_pressed == true) {
                    _selecting_after_pressed = false;
                    _selecting = true;
                    _viewer_panel.setImage(getImage());
                }

                if (_selecting == false) {
                    return;
                }

                updateSelection(e);
                _viewer_panel.setImageOffset((int) (_selection_x * _x_ratio + 0.5), (int) (_selection_y * _y_ratio + 0.5));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (_selecting != true) {
                    return;
                }
                _selecting = false;
                _viewer_panel.fixateImage();
                _frame.setSelectBlocked(false);

                repaint();
            }
        }

        MouseAdapter ma = new MouseHandler();

        this.addMouseListener(ma);
        this.addMouseMotionListener(ma);
    }

    @Override
    public void setImage(BufferedImage new_img) {
        super.setImage(new_img);
        _selecting = false;

        BufferedImage img = this.getImage();

        if (img == null) {
            _display_img = null;
            repaint();
            return;
        }

        int panel_width = getWidth(),
                panel_height = getHeight(),
                img_width = img.getWidth(),
                img_height = img.getHeight();



        int scale_opt = Image.SCALE_SMOOTH;

        if (img_width > panel_width && img_height > panel_height) {

            if ((double) img_width / panel_width > (double) img_height / panel_height) {
                _display_img = img.getScaledInstance(panel_width, -1, scale_opt);
            } else {
                _display_img = img.getScaledInstance(-1, panel_height, scale_opt);
            }
//            System.out.println("Image after scaling: " + _display_img.getWidth(null) + "x" + _display_img.getHeight(null));
            _selection_width = (int) ((double) panel_width * _display_img.getWidth(null) / img_width + 0.5);
            _selection_height = (int) ((double) panel_height * _display_img.getHeight(null) / img_height + 0.5);


            _x_ratio = (double) img_width / _display_img.getWidth(null);
            _y_ratio = (double) img_height / _display_img.getHeight(null);
        } else if (img_width > panel_width) {
            _display_img = img.getScaledInstance(panel_width, -1, scale_opt);

            _selection_height = _display_img.getHeight(null);

            //double ratio = (double) _display_img.getHeight(null) / img_height;
            _selection_width = (int) ((double) panel_width * _display_img.getWidth(null) / img_width + 0.5);
            _x_ratio = (double) img_width / _display_img.getWidth(null);
            _y_ratio = 1;
        } else if (img_height > panel_height) {
            _display_img = img.getScaledInstance(-1, panel_height, scale_opt);

            _selection_width = _display_img.getWidth(null);
            //double ratio = (double) _display_img.getWidth(null) / img_width;
            _selection_height = (int) ((double) panel_height * _display_img.getHeight(null) / img_height + 0.5);

            _x_ratio = 1;
            _y_ratio = (double) img_height / _display_img.getHeight(null);

        } else {
            _display_img = img;
            _selection_height = panel_height;
            _selection_width = panel_width;
            _x_ratio = 1;
            _y_ratio = 1;
        }


//        System.out.println("image width: " + img_width + " image height: " + img_height
//                + "\nselection_width: " + _selection_width + " selection height: " + _selection_height
//                + "\nx_ratio: " + _x_ratio + " y_ratio: " + _y_ratio
//                + "\nsel_widx_ratioth*x_ratio: " + _selection_width * _x_ratio + " sel_height*y_ratio: " + _selection_height * _y_ratio);

        _selection_x = 0;
        _selection_y = 0;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        clearBackground(g);

        if (_display_img != null) {
            g.drawImage(_display_img, 0, 0, null);
            if (_selecting) {
                if (_stroke == null) {
                    _stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10, new float[]{5, 3}, 0);
                }
                Stroke old_stroke = g.getStroke();
                g.setStroke(_stroke);
                g.drawRect(_selection_x, _selection_y, _selection_width - 1, _selection_height - 1);
                g.setStroke(old_stroke);
            }
        } else {
            drawBackgroundTitle(g);
        }

    }
}
