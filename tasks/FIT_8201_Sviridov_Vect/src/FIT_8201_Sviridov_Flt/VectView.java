/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Vect;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class VectView extends GridPanel implements VectListener {

    private VectModel vectModel;
    

    public VectView() {
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                if (e.getSource() == VectView.this) {
                    return;
                }
                super.componentResized(e);

                Dimension maxSize = e.getComponent().getSize();

                double mRatio = vectModel.getRatio();
                double pRatio = (double) maxSize.width / maxSize.height;
                
                Dimension newSize;
                if (mRatio > pRatio) {
                    double ratio = mRatio/pRatio;
                    newSize = new Dimension(maxSize.width, (int) (maxSize.height / ratio + 0.5));
                } else if (mRatio < pRatio) {
                    double ratio = pRatio/mRatio;
                    newSize = new Dimension((int) (maxSize.width / ratio + 0.5), maxSize.height);
                } else {
                    newSize = maxSize;
                }

                VectView.this.setPreferredSize(newSize);
                VectView.this.setMinimumSize(newSize);
                ((JPanel)e.getSource()).revalidate();
            }
        });
    }

    @Override
    public void modelChanged() {
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
        throw new UnsupportedOperationException("Not supported yet.");
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
}
