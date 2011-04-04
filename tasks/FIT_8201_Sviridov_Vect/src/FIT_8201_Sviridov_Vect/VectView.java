/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Vect;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class VectView extends GridPanel implements VectListener {

    private VectModel vectModel;
    private List<Vector> vectors;

    public VectView() {
        addComponentListener(new ComponentAdapter() {

            private void resetSize(ComponentEvent e) {

                if (e.getSource() == VectView.this) {
                    return;
                }
                Dimension maxSize = e.getComponent().getSize();

                double mRatio = vectModel.getRatio();
                double pRatio = (double) maxSize.width / maxSize.height;

                Dimension newSize;
                if (mRatio > pRatio) {
                    double ratio = mRatio / pRatio;
                    newSize = new Dimension(maxSize.width, (int) (maxSize.height / ratio + 0.5));
                } else if (mRatio < pRatio) {
                    double ratio = pRatio / mRatio;
                    newSize = new Dimension((int) (maxSize.width / ratio + 0.5), maxSize.height);
                } else {
                    newSize = maxSize;
                }

                VectView.this.setPreferredSize(newSize);
                VectView.this.setMinimumSize(newSize);
                ((JPanel) e.getSource()).revalidate();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                resetSize(e);
                repaint();
            }
        });
    }

    private void translateCoordinates(Point p){
        // substract left lower point
        // divide by panel width - 2 x_llp, panel - 2 y_llp
        // multiply by region width, height
        // add xs, ys
    }
    private void computeVectors(){
        
    }

    @Override
    protected void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.translate(0, this.getHeight() - 1);
        g.scale(1, -1);
        super.paintComponent(g);


        Point[][] points = getGridPoints();
        if (points != null) {
            for (int w = 0; w < points.length; ++w) {
                for (int h = 0; h < points[0].length; ++h) {
                    g.drawLine(points[w][h].x, points[w][h].y,
                            points[w][h].x + 5, points[w][h].y + 5);
                }
            }
        }
    }

    @Override
    public void modelChanged() {
        setGrid(vectModel.getGrid());
        vectModel.getGridColor();
        vectModel.getLengthMult();
        repaint();
    }

    @Override
    public void regionChanged() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void lengthMultChanged() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void gridChanged() {
        setGrid(vectModel.getGrid());
        repaint();
    }

    @Override
    public void gridColorChanged() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void colorsChanged() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public VectModel getVectModel() {
        return vectModel;
    }

    public void setVectModel(VectModel vectModel) {
        this.vectModel = vectModel;
    }

    @Override
    public void arrowModeChanged() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fieldModeChanged() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void gridDrawnChanged() {
        this.setGridDrawn(vectModel.isGridDrawn());
        repaint();
    }
}
