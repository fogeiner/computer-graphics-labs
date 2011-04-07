package FIT_8201_Sviridov_Vect.ui;

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
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 * 
 * Class for palette settings dialog
 * 
 * @author admin
 */
public class ColorsChoiceDialog extends JDialog {

	private static final long serialVersionUID = 4900728146049724199L;
	private final int MAX_COLORS = 21;
	private final int MIN_COLORS_TO_CHOOSE = 5;
	private VectModel vectModel;
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	private ColorChoiceLabel labels[] = new ColorChoiceLabel[MAX_COLORS];
	private JCheckBox checkboxes[] = new JCheckBox[MAX_COLORS];
	private MouseAdapter labelColorChooserHandler = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			ColorChoiceLabel l = (ColorChoiceLabel) e.getSource();
			l.showChooseColorDialog();
		}
	};

	/**
	 * Class uniting Label and Checkbox to act as color chooser
	 * 
	 * @author admin
	 * 
	 */
	private class CheckboxColorChooser implements ActionListener {

		private ColorChoiceLabel l;

		/**
		 * Default constructor
		 * 
		 * @param l
		 *            associated ColorChoiseLabel
		 */
		public CheckboxColorChooser(ColorChoiceLabel l) {
			this.l = l;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JCheckBox cb = (JCheckBox) e.getSource();
			if (cb.isSelected()) {
				if (!l.isChosen()) {
					l.showChooseColorDialog();

					if (l.isChosen() == false) {
						cb.setSelected(false);
					}
				}
			}
		}
	}

	/**
	 * Unites widgets to panel
	 * 
	 * @param l
	 *            label
	 * @param cb
	 *            checkbox
	 * @return new panel
	 */
	private JPanel makeLabelCheckBoxPanel(ColorChoiceLabel l, JCheckBox cb) {
		l.setToolTipText("Click to open color chooser dialog for "
				+ l.getText());
		cb.setToolTipText("Check if you want this color to be used");

		l.setOpaque(true);
		l.addMouseListener(labelColorChooserHandler);
		cb.addActionListener(new CheckboxColorChooser(l));

		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		p.add(l, BorderLayout.CENTER);
		p.add(cb, BorderLayout.EAST);
		return p;
	}

	/**
	 * Unites buttons to panel
	 * 
	 * @return new panel
	 */
	private JPanel makeButtonsPanel() {
		JPanel outer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel p = new JPanel(new GridLayout(0, 2, 5, 0));
		p.add(okButton);
		p.add(cancelButton);
		outer.add(p);
		return outer;
	}

	/**
	 * Makes dialog visible
	 */
	public void showDialog() {
		setVisible(true);
	}

	/**
	 * Called when user presses OK. Sets new values to model and disposes dialog
	 */
	private void confirm() {
		List<Color> newColors = new ArrayList<Color>(MAX_COLORS);

		for (int i = 0; i < MAX_COLORS; ++i) {
			if (checkboxes[i].isSelected()) {
				newColors.add(labels[i].getBackground());
			}
		}

		if (newColors.size() < MIN_COLORS_TO_CHOOSE) {
			JOptionPane.showMessageDialog(this, "At least "
					+ MIN_COLORS_TO_CHOOSE + " must be chosen",
					"Colors count error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		vectModel.setColors(newColors);
		setVisible(false);
	}

	/**
	 * Called when user presses Cancel; disposes dialog
	 */
	private void cancel() {
		setVisible(false);
	}

	/**
	 * Default constructor
	 * @param owner dialog owner
	 */
	public ColorsChoiceDialog(Frame owner) {
		super(owner, "Palette dialog", true);
		int cols = 7, rows = 3;
		setLayout(new BorderLayout());

		JPanel colorsPanel = new JPanel(new GridLayout(rows, cols));

		// for rows 5 and cols 4 generetes seq: 1 6 11 16 2 7 ...
		int index = 0;
		for (int i = 1; i < MAX_COLORS + 1; ++i) {
			checkboxes[index] = new JCheckBox();
			labels[index] = new ColorChoiceLabel("Color " + (index + 1),
					checkboxes[index]);

			colorsPanel.add(makeLabelCheckBoxPanel(labels[index],
					checkboxes[index]));

			index = i * rows % (MAX_COLORS - 1);
			if (index == 0) {
				index = (MAX_COLORS - 1);
			}
		}

		add(colorsPanel, BorderLayout.NORTH);
		add(makeButtonsPanel(), BorderLayout.SOUTH);
		pack();
		setResizable(false);

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
	}

	/**
	 * JLabel subclass with chosen-not chosen property
	 * @author admin
	 *
	 */
	class ColorChoiceLabel extends JLabel {

		private static final long serialVersionUID = 2768905515393286557L;
		private JCheckBox cb;
		private boolean chosen;

		public boolean isChosen() {
			return chosen;
		}

		public void setChosen(boolean chosen) {
			this.chosen = chosen;
			cb.setSelected(chosen);
		}

		public void showChooseColorDialog() {
			Color c = JColorChooser.showDialog(this,
					"Choose " + this.getText(), this.getBackground());
			if (c != null) {
				this.setBackground(c);
				setChosen(true);
			}
		}

		public ColorChoiceLabel(String text, JCheckBox cb) {
			super(text);
			this.cb = cb;
		}
	}

	/**
	 * Sets VectModel to the dialog and reads out values to display
	 * @param vectModel new VectModel
	 */
	public void setVectModel(VectModel vectModel) {
		this.vectModel = vectModel;

		if (vectModel == null)
			return;

		for (ColorChoiceLabel l : labels) {
			l.setChosen(false);
			l.setBackground(null);
		}

		for (JCheckBox cb : checkboxes) {
			cb.setSelected(false);
		}

		List<Color> vectModelColors = vectModel.getColors();
		for (int i = 0; i < vectModelColors.size() && i < MAX_COLORS; ++i) {
			labels[i].setBackground(vectModelColors.get(i));
			labels[i].setChosen(true);
		}

	}
}
