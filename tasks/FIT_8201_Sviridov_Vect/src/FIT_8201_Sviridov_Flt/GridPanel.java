/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Vect;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class GridPanel extends JPanel {

    public static final Color DEFAULT_GRID_COLOR = Color.lightGray;
    private Color _color = DEFAULT_GRID_COLOR;
    private Stroke _stroke;
    private int _grid_x;
    private int _grid_y;

    public GridPanel() {
    }

    public GridPanel(int w, int h) {
        setGrid(w, h);
    }

    final public void setGrid(int w, int h) {
        _grid_x = w;
        _grid_y = h;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (_stroke == null) {
            _stroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND, 5.0f, new float[]{5.0f, 5.0f}, 0.0f);
        }
        Stroke old_stroke = g2.getStroke();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(_stroke);
        g2.setColor(_color);

        int height = getHeight();
        int width = getWidth();

        for (int k = 1; k < _grid_x + 1; ++k) {
            int x = (int) ((double) width / (_grid_x + 1) * k + 0.5);
            g2.drawLine(x, 0, x, height);
        }

        for (int k = 1; k < _grid_y + 1; ++k) {
            int y = (int) ((double) height / (_grid_y + 1) * k + 0.5);
            g2.drawLine(0, y, width, y);
        }

        g2.setStroke(old_stroke);
    }

    public static void main(String args[]) {
        class Canvas extends GridPanel {

            public Canvas() {
                super(10, 20);
            }
        }

        JFrame frame = new JFrame();
        frame.setSize(new Dimension(600, 400));
        frame.setLayout(new BorderLayout());
        frame.add(new Canvas(), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
