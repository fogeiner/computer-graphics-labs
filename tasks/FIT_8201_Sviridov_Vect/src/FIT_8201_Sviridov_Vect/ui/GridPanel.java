/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Vect.ui;

import FIT_8201_Sviridov_Vect.utils.Grid;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class GridPanel extends JPanel {

    public static final Color DEFAULT_GRID_COLOR = Color.lightGray;
    private Color gridColor = DEFAULT_GRID_COLOR;
    private Stroke stroke;
    private Grid grid;
    private boolean gridDrawn;
    private Point[][] gridPoints;

    public GridPanel() {
    }

    public void computeGridPoints() {
        int height = getHeight();
        int width = getWidth();

        for (int w = 1; w < grid.W + 1; ++w) {
            int x = (int) ((double) width / (grid.W + 1) * w + 0.5);
            for (int h = 1; h < grid.H + 1; ++h) {
                int y = (int) ((double) height / (grid.H + 1) * h + 0.5);
                gridPoints[w - 1][h - 1] = new Point(x, y);
            }
        }
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
        double diagonal = Math.sqrt(w * w + h * h);
        return diagonal;
    }

    public Point[][] getGridPoints() {
        return gridPoints;
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    public GridPanel(int w, int h) {
        setGrid(new Grid(w, h));
    }

    public void setGrid(Grid grid) {
        this.grid = grid;

        gridPoints = new Point[grid.W][grid.H];
    }

    public Grid getGrid() {
        return grid;
    }

    public boolean isGridDrawn() {
        return gridDrawn;
    }

    public void setGridDrawn(boolean gridDrawn) {
        this.gridDrawn = gridDrawn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (grid == null || gridDrawn == false) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g;
        if (stroke == null) {
            stroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND, 5.0f, new float[]{5.0f, 5.0f}, 0.0f);
        }
        Stroke oldStroke = g2.getStroke();
        Color oldColor = g2.getColor();

        g2.setStroke(stroke);
        g2.setColor(gridColor);

        int height = getHeight();
        int width = getWidth();

        for (int k = 1; k < grid.W + 1; ++k) {
            int x = (int) ((double) width / (grid.W + 1) * k + 0.5);
            g2.drawLine(x, 0, x, height);
        }

        for (int k = 1; k < grid.H + 1; ++k) {
            int y = (int) ((double) height / (grid.H + 1) * k + 0.5);
            g2.drawLine(0, y, width, y);
        }

        g2.setColor(oldColor);
        g2.setStroke(oldStroke);
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
