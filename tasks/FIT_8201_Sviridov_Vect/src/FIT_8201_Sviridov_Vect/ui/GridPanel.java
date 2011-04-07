/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Vect.ui;

import FIT_8201_Sviridov_Vect.utils.Grid;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class GridPanel extends JPanel {

    private BufferedImage imgBuffer = new BufferedImage(3000, 2000, BufferedImage.TYPE_INT_RGB);
    private static final long serialVersionUID = -1038991547314803513L;
    public static final Color DEFAULT_GRID_COLOR = Color.lightGray;
    private Color gridColor = DEFAULT_GRID_COLOR;
    private Stroke stroke;
    private Grid grid;
    private boolean gridDrawn;
    private Point[][] gridPoints;

    public GridPanel() {
    }

    public Point getLeftLowerGridPoint() {
        return gridPoints[0][0];
    }

    public Point getRightUpperGridPoint() {
        return gridPoints[grid.W - 1][grid.H - 1];
    }

    public Point[][] getGridPoints() {
        return gridPoints;
    }

    public void computeGridPoints() {
        if (grid == null) {
            return;
        }
        int height = getHeight();
        int width = getWidth();
        for (int w = 1; w < grid.W + 1; ++w) {
            int x = (int) ((double) width / (grid.W + 1) * w + 0.5);
            for (int h = 1; h < grid.H + 1; ++h) {
                int y = (int) ((double) height / (grid.H + 1) * h + 0.5);
                gridPoints[w - 1][h - 1] = new Point(x, y);
            }
        }
        paintBuffer();
    }

    public double getGridCellWidth() {
        return (double) getWidth() / (grid.W + 1);
    }

    public double getGridCellHeight() {
        return (double) getHeight() / (grid.H + 1);
    }

    public double getGridCellDiagonal() {
        double w = getGridCellWidth();
        double h = getGridCellHeight();
        double diagonal = Math.hypot(w, h);
        return diagonal;
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
        paintBuffer();
    }

    public GridPanel(int w, int h) {
        init(w, h);
    }

    private void init(int w, int h) {
        setGrid(new Grid(w, h));
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
        gridPoints = new Point[grid.W][grid.H];
        computeGridPoints();

        repaint();
    }

    public Grid getGrid() {
        return grid;
    }

    public boolean isGridDrawn() {
        return gridDrawn;
    }

    public void setGridDrawn(boolean gridDrawn) {
        this.gridDrawn = gridDrawn;
        paintBuffer();
    }

    private void paintBuffer() {
        if (grid == null) {
            return;
        }
        int width = getWidth(),
                height = getHeight();
        Graphics2D g2 = (Graphics2D) imgBuffer.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2.setColor(getBackground());
        g2.fillRect(0, 0, imgBuffer.getWidth(), imgBuffer.getHeight());
        if (stroke == null) {
            stroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND, 5.0f, new float[]{5.0f, 5.0f}, 0.0f);
        }

        g2.setStroke(stroke);
        g2.setColor(gridColor);

        for (int k = 1; k < grid.W + 1; ++k) {
            int x = (int) ((double) width / (grid.W + 1) * k + 0.5);
            g2.drawLine(x, 0, x, height);
        }

        for (int k = 1; k < grid.H + 1; ++k) {
            int y = (int) ((double) height / (grid.H + 1) * k + 0.5);
            g2.drawLine(0, y, width, y);
        }
        g2.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (grid == null || gridDrawn == false) {
            return;
        }

        g.drawImage(imgBuffer, 0, 0, null);
    }
}
