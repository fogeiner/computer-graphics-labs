/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Vect.ui;

import FIT_8201_Sviridov_Vect.utils.Grid;
import FIT_8201_Sviridov_Vect.utils.Region;
import FIT_8201_Sviridov_Vect.vect.VectModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author admin
 */
public class SettingsDialog extends JDialog {

    private static final long serialVersionUID = -903939050607469239L;
    private final int VECT_MULT_MIN = 500,
            VECT_MULT_MAX = 3000;
    private final double VECT_MULT_SCALE = 1000.0;
    private final double VECT_MULT_STEP = 0.01;
    private JFormattedTextField xs, xe, ys, ye;
    private JButton okButton = new JButton("OK"),
            cancelButton = new JButton("Cancel");
    private final JSpinner gridWidthSpinner = new JSpinner(new SpinnerNumberModel(10, 4, 50, 1)),
            gridHeightSpinner = new JSpinner(new SpinnerNumberModel(10, 4, 50, 1));
    private JSlider vectMultSlider = new JSlider(VECT_MULT_MIN, VECT_MULT_MAX, VECT_MULT_MIN);
    private JSpinner vectMultSpinner = new JSpinner(new SpinnerNumberModel(VECT_MULT_MIN / VECT_MULT_SCALE,
            VECT_MULT_MIN / VECT_MULT_SCALE, VECT_MULT_MAX / VECT_MULT_SCALE, VECT_MULT_STEP));
    private VectModel vectModel;
    private Region originalRegion;
    private Region savedRegion;
    private int savedGridWidth, savedGridHeight;
    private double savedVectMult;
    // region -- max and min are determined at setting model time
    // vectMult, grid is saved on every showDialog call

    {
        xs = new JFormattedTextField();
        xe = new JFormattedTextField();

        ys = new JFormattedTextField();
        ye = new JFormattedTextField();

        PropertyChangeListener pcl = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                getVectModel().setRegion(
                        new Region(
                        (Double) xs.getValue(),
                        (Double) xe.getValue(),
                        (Double) ys.getValue(),
                        (Double) ye.getValue()));

            }
        };


//        for (JFormattedTextField f : new JFormattedTextField[]{xs, xe, ys, ye}) {
//            f.setColumns(10);
//            f.addPropertyChangeListener("value", pcl);
//        }

        syncSpinnerSlider(vectMultSlider, vectMultSpinner);
        vectMultSlider.setPaintTicks(true);
        vectMultSlider.setMajorTickSpacing((VECT_MULT_MAX - VECT_MULT_MIN) / 5);
        vectMultSlider.setMinorTickSpacing((VECT_MULT_MAX - VECT_MULT_MIN) / 10);

        ChangeListener gridUpdater = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Integer width = (Integer) gridWidthSpinner.getValue(),
                        height = (Integer) gridHeightSpinner.getValue();
                if (width != null && height != null) {
                    vectModel.setGrid(new Grid(width, height));
                }
            }
        };

        gridHeightSpinner.addChangeListener(gridUpdater);
        gridWidthSpinner.addChangeListener(gridUpdater);
    }

    private JPanel makeButtonsPanel() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel inner = new JPanel(new GridLayout(0, 2, 5, 5));
        inner.add(okButton);
        inner.add(cancelButton);
        outer.add(inner);
        return outer;
    }

    private JPanel makeIntervalPanel(JFormattedTextField start, JFormattedTextField end, String title) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(new JLabel("["));
        p.add(start);
        p.add(new JLabel(","));
        p.add(end);
        p.add(new JLabel("]"));
        return p;
    }

    private JPanel makeGridPanel() {
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 0));
        p.setBorder(BorderFactory.createTitledBorder("Width x Height (MxN)"));
        p.add(gridWidthSpinner);
        p.add(gridHeightSpinner);
        return p;
    }

    private JPanel makeMultPanel() {
        // 0.5 <-> 3.0
        // step 0.01
        // 5 <-> 30
        // 500 <-> 3000
        // mult 1000
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setBorder(BorderFactory.createTitledBorder("Vector length coefficient (C0)"));
        p.add(vectMultSlider);
        p.add(vectMultSpinner);

        return p;
    }

    private void syncSpinnerSlider(final JSlider slider, final JSpinner spinner) {

        JComponent editor = spinner.getEditor();
        JFormattedTextField textfield = ((JSpinner.DefaultEditor) editor).getTextField();
        NumberFormatter n_format = (NumberFormatter) textfield.getFormatter();
        n_format.setCommitsOnValidEdit(true);
        n_format.setAllowsInvalid(false);

        spinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                slider.setValue(
                        (int) ((Double) ((JSpinner) e.getSource()).getValue() * VECT_MULT_SCALE));
            }
        });

        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                spinner.setValue(
                        ((Integer) ((JSlider) e.getSource()).getValue()) / VECT_MULT_SCALE);
            }
        });
    }

    public void showDialog() {
        Region currentRegion = vectModel.getRegion();
        xs.setValue(currentRegion.xs);
        xe.setValue(currentRegion.xe);
        ys.setValue(currentRegion.ys);
        ye.setValue(currentRegion.ye);

        Grid curGrid = vectModel.getGrid();
        gridWidthSpinner.setValue(curGrid.W);
        gridHeightSpinner.setValue(curGrid.H);

        vectMultSpinner.setValue(vectModel.getLengthMult());
        setVisible(true);
    }

    private void confirm() {
    }

    private void cancel() {
    }

    public void setVectModel(VectModel vectModel) {
        this.vectModel = vectModel;
        if(vectModel == null) return;
        
        originalRegion = vectModel.getRegion();


        for(JFormattedTextField ftf: new JFormattedTextField[]{xs,xe}){
            NumberFormatter nf = (NumberFormatter)ftf.getFormatter();
            nf.setMinimum(originalRegion.xs);
            nf.setMaximum(originalRegion.xe);
            nf.setCommitsOnValidEdit(true);
            nf.setFormat(new DecimalFormat("0.000"));
        }

        for(JFormattedTextField ftf: new JFormattedTextField[]{ys,ye}){
            NumberFormatter nf = (NumberFormatter)ftf.getFormatter();
            nf.setMinimum(originalRegion.ys);
            nf.setMaximum(originalRegion.ye);
            nf.setCommitsOnValidEdit(true);
            nf.setFormat(new DecimalFormat("0.000"));
        }
    }

    public VectModel getVectModel() {
        return vectModel;
    }

    public SettingsDialog(Frame owner) {
        super(owner, "Vect settings dialog");
        // region 4 jformattedtextfield
        // M N
        // C0
        setLayout(new BorderLayout(5, 5));
        JPanel mainPanel = new JPanel(new GridLayout(0, 1));
        mainPanel.add(makeIntervalPanel(xs, xe, "x [a,b] | [b,a]"));
        mainPanel.add(makeIntervalPanel(ys, ye, "y [c,d] | [d,c]"));
        mainPanel.add(makeGridPanel());
        mainPanel.add(makeMultPanel());
        add(mainPanel, BorderLayout.CENTER);
        add(makeButtonsPanel(), BorderLayout.SOUTH);
        pack();
    }

    public static void main(String[] args) {
        SettingsDialog d = new SettingsDialog(null);
        d.setVectModel(new VectModel(new Region(-1, 1, -1, 1), 1.0, new Grid(10, 10),
                Arrays.asList(new Color[]{Color.red, Color.blue}), Color.gray, true, true, true, true));
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        d.showDialog();
    }
}
