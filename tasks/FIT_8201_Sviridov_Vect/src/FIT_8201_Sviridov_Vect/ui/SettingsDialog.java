/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Vect.ui;

import FIT_8201_Sviridov_Vect.state_history.StateHistoryModel;
import FIT_8201_Sviridov_Vect.utils.Grid;
import FIT_8201_Sviridov_Vect.utils.Region;
import FIT_8201_Sviridov_Vect.vect.VectListener;
import FIT_8201_Sviridov_Vect.vect.VectModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

/**
 * Class for Settings dialog
 * @author admin
 */
public class SettingsDialog extends JDialog implements VectListener {

	private static final long serialVersionUID = -903939050607469239L;

	private final int VECT_MULT_MIN = 500, VECT_MULT_MAX = 3000;

	private final double VECT_MULT_SCALE = 1000.0;
	private final double VECT_MULT_STEP = 0.01;

	private JFormattedTextField xs, xe, ys, ye;
	private JButton okButton;
	private JButton cancelButton;

	private JSpinner gridWidthSpinner;
	private JSpinner gridHeightSpinner;

	private JSlider vectMultSlider;
	private JSpinner vectMultSpinner;

	private JLabel gridColorLabel;
	private VectModel vectModel;
	private Region originalRegion;
	private Region savedRegion;
	private int savedGridWidth, savedGridHeight;
	private double savedVectMult;
	private Color savedGridColor;
	private StateHistoryModel<Region> regionsHistoryModel;

	{
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");

		gridWidthSpinner = new JSpinner(new SpinnerNumberModel(10, 4, 50, 1));
		gridHeightSpinner = new JSpinner(new SpinnerNumberModel(10, 4, 50, 1));
		vectMultSpinner = new JSpinner(new SpinnerNumberModel(VECT_MULT_MIN
				/ VECT_MULT_SCALE, VECT_MULT_MIN / VECT_MULT_SCALE,
				VECT_MULT_MAX / VECT_MULT_SCALE, VECT_MULT_STEP));

		vectMultSlider = new JSlider(VECT_MULT_MIN, VECT_MULT_MAX,
				VECT_MULT_MIN);

		gridColorLabel = new JLabel("Click to choose");

		xs = new JFormattedTextField(DecimalFormat.getNumberInstance());
		xe = new JFormattedTextField(DecimalFormat.getNumberInstance());

		ys = new JFormattedTextField(DecimalFormat.getNumberInstance());
		ye = new JFormattedTextField(DecimalFormat.getNumberInstance());

		PropertyChangeListener pcl = new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Double a = (Double) xs.getValue(), b = (Double) xe.getValue(), c = (Double) ys
						.getValue(), d = (Double) ye.getValue();
				if (a != null && b != null && c != null && d != null) {
					getVectModel().setRegion(new Region(a, b, c, d));
				}

			}
		};

		for (JFormattedTextField f : new JFormattedTextField[] { xs, xe, ys, ye }) {
			f.setColumns(10);
			f.addPropertyChangeListener("value", pcl);
		}

		syncSpinnerSlider(vectMultSlider, vectMultSpinner);
		vectMultSlider.setPaintTicks(true);
		vectMultSlider.setMajorTickSpacing((VECT_MULT_MAX - VECT_MULT_MIN) / 5);
		vectMultSlider
				.setMinorTickSpacing((VECT_MULT_MAX - VECT_MULT_MIN) / 10);

		ChangeListener gridUpdater = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Integer width = (Integer) gridWidthSpinner.getValue(), height = (Integer) gridHeightSpinner
						.getValue();
				if (width != null && height != null) {
					vectModel.setGrid(new Grid(width, height));
				}
			}
		};

		gridHeightSpinner.addChangeListener(gridUpdater);
		gridWidthSpinner.addChangeListener(gridUpdater);

		vectMultSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Double value = (Double) ((JSpinner) e.getSource()).getValue();
				if (value != null) {
					vectModel.setLengthMult(value);
				}
			}
		});

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				confirm();
			}
		});

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});

		gridColorLabel.setOpaque(true);
		gridColorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gridColorLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				JLabel l = (JLabel) e.getSource();
				Color c = JColorChooser.showDialog(SettingsDialog.this,
						"Choose grid color", l.getBackground());
				if (c == null) {
					return;
				}

				l.setBackground(c);
				vectModel.setGridColor(l.getBackground());
			}
		});
	}

	/**
	 * Make panel with buttons
	 * 
	 * @return panel
	 */
	private JPanel makeButtonsPanel() {
		JPanel outer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel inner = new JPanel(new GridLayout(0, 2, 5, 5));
		inner.add(okButton);
		inner.add(cancelButton);
		outer.add(inner);
		return outer;
	}

	/**
	 * Makes panel with interval settings
	 * 
	 * @param start
	 *            field for interval start
	 * @param end
	 *            field for interval end
	 * @param title
	 *            panel border title
	 * @return panel
	 */
	private JPanel makeIntervalPanel(JFormattedTextField start,
			JFormattedTextField end, String title) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		p.setBorder(BorderFactory.createTitledBorder(title));
		p.add(new JLabel("["));
		p.add(start);
		p.add(new JLabel(","));
		p.add(end);
		p.add(new JLabel("]"));
		return p;
	}

	/**
	 * Makes panel with grid settings
	 * 
	 * @return panel
	 */
	private JPanel makeGridPanel() {
		JPanel p = new JPanel(new GridLayout(0, 2, 5, 0));
		p.setBorder(BorderFactory.createTitledBorder("Width x Height (MxN)"));
		p.add(gridWidthSpinner);
		p.add(gridHeightSpinner);
		return p;
	}

	/**
	 * Makes panel with vector length multiplier settings
	 * 
	 * @return panel
	 */
	private JPanel makeMultPanel() {
		// 0.5 <-> 3.0
		// step 0.01
		// 5 <-> 30
		// 500 <-> 3000
		// mult 1000
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		p.setBorder(BorderFactory
				.createTitledBorder("Vector length coefficient (C0)"));
		p.add(vectMultSlider);
		p.add(vectMultSpinner);

		return p;
	}

	/**
	 * Makes panel with grid color settings
	 * 
	 * @return panel
	 */
	private JPanel makeGridColorPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder("Grid color"));
		p.add(gridColorLabel, BorderLayout.CENTER);
		return p;
	}

	/**
	 * Syncs slider and spinner
	 * 
	 * @param slider
	 *            slider to sync
	 * @param spinner
	 *            spinner to sync
	 */
	private void syncSpinnerSlider(final JSlider slider, final JSpinner spinner) {

		JComponent editor = spinner.getEditor();
		JFormattedTextField textfield = ((JSpinner.DefaultEditor) editor)
				.getTextField();
		NumberFormatter n_format = (NumberFormatter) textfield.getFormatter();
		n_format.setCommitsOnValidEdit(true);
		n_format.setAllowsInvalid(false);

		spinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				slider.setValue((int) ((Double) ((JSpinner) e.getSource())
						.getValue() * VECT_MULT_SCALE));
			}
		});

		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				spinner.setValue(((Integer) ((JSlider) e.getSource())
						.getValue()) / VECT_MULT_SCALE);
			}
		});
	}

	/**
	 * Reads out values from model and makes dialog visible
	 */
	public void showDialog() {
		getValuesFromModel();
		setVisible(true);
	}

	/**
	 * Called when user presses OK Saves changes to model and makes dialog
	 * invisible
	 */
	private void confirm() {
		regionsHistoryModel.add(vectModel.getRegion());
		setVisible(false);
	}

	/**
	 * Called when user presses Cancel Cancels changes to model and makes dialog
	 * invisible
	 */
	private void cancel() {
		vectModel.setRegion(savedRegion);
		vectModel.setGrid(new Grid(savedGridWidth, savedGridHeight));
		vectModel.setLengthMult(savedVectMult);
		vectModel.setGridColor(savedGridColor);
		setVisible(false);
	}

	/**
	 * Retrieve data from model
	 */
	private void getValuesFromModel() {

		Region currentRegion = vectModel.getRegion();
		xs.setValue(currentRegion.xs);
		xe.setValue(currentRegion.xe);
		ys.setValue(currentRegion.ys);
		ye.setValue(currentRegion.ye);

		Grid curGrid = vectModel.getGrid();
		gridWidthSpinner.setValue(curGrid.W);
		gridHeightSpinner.setValue(curGrid.H);

		vectMultSpinner.setValue(vectModel.getLengthMult());

		gridColorLabel.setBackground(vectModel.getGridColor());

		savedRegion = currentRegion;
		savedGridWidth = curGrid.W;
		savedGridHeight = curGrid.H;
		savedVectMult = vectModel.getLengthMult();
		savedGridColor = vectModel.getGridColor();

	}

	/**
	 * Setter for HistoryModel with regions history
	 * 
	 * @param regionsHistoryModel
	 *            new history model
	 */
	public void setRegionsHistoryModel(
			StateHistoryModel<Region> regionsHistoryModel) {
		this.regionsHistoryModel = regionsHistoryModel;
	}

	/**
	 * Getter for VectModel
	 * 
	 * @return VectModel
	 */
	public VectModel getVectModel() {
		return vectModel;
	}

	/**
	 * Setter for VectModel
	 * 
	 * @param vectModel
	 *            new VectModel
	 */
	public void setVectModel(VectModel vectModel) {
		this.vectModel = vectModel;
		if (vectModel == null) {
			setVisible(false);
			return;
		}

		originalRegion = vectModel.getRegion();

		for (JFormattedTextField ftf : new JFormattedTextField[] { xs, xe }) {
			NumberFormatter nf = (NumberFormatter) ftf.getFormatter();
			nf.setMinimum(originalRegion.xs);
			nf.setMaximum(originalRegion.xe);
			nf.setCommitsOnValidEdit(true);
			nf.setAllowsInvalid(false);
			nf.setFormat(new DecimalFormat("0.000"));
		}

		for (JFormattedTextField ftf : new JFormattedTextField[] { ys, ye }) {
			NumberFormatter nf = (NumberFormatter) ftf.getFormatter();
			nf.setMinimum(originalRegion.ys);
			nf.setMaximum(originalRegion.ye);
			nf.setCommitsOnValidEdit(true);
			nf.setAllowsInvalid(false);
			nf.setFormat(new DecimalFormat("0.000"));
		}

		getValuesFromModel();
	}

	/**
	 * Default constructor
	 * 
	 * @param owner
	 *            owner of the dialog
	 */
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
		mainPanel.add(makeGridColorPanel());
		add(mainPanel, BorderLayout.CENTER);

		add(makeButtonsPanel(), BorderLayout.SOUTH);
		pack();
	}

	@Override
	public void modelChanged() {
	}

	@Override
	public void regionChanged() {
		Region currentRegion = vectModel.getRegion();
		xs.setValue(currentRegion.xs);
		xe.setValue(currentRegion.xe);
		ys.setValue(currentRegion.ys);
		ye.setValue(currentRegion.ye);
	}

	@Override
	public void lengthMultChanged() {
	}

	@Override
	public void gridChanged() {
	}

	@Override
	public void gridColorChanged() {
	}

	@Override
	public void colorsChanged() {
	}

	@Override
	public void fieldModeChanged() {
	}

	@Override
	public void gridDrawnChanged() {
	}

	@Override
	public void arrowModeChanged() {
	}

	@Override
	public void chessModeChanged() {
	}
}
