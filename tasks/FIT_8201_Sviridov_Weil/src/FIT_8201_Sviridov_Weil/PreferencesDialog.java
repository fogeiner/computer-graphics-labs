package FIT_8201_Sviridov_Weil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

/**
 * Preferences dialog class. Lets user set all parameters. New parameters may be
 * confirmed and saved or dismissed
 * 
 * @author alstein
 * 
 */
class PreferencesDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = -3486091859979955990L;
	private JLabel _canvas_color_label = new JLabel();
	private JLabel _subject_color_label = new JLabel();
	private JLabel _clip_color_label = new JLabel();
	private JLabel _intersecting_color_label = new JLabel();
	private JSlider _subject_slider = new JSlider(1,
			WeilSettings.MAX_THICKNESS, WeilSettings.DEFAULT_SUBJECT_THICKNESS);
	private JSpinner _subject_spinner = new JSpinner(new SpinnerNumberModel(
			WeilSettings.DEFAULT_SUBJECT_THICKNESS, 1,
			WeilSettings.MAX_THICKNESS, 1));
	private JSlider _clip_slider = new JSlider(1, WeilSettings.MAX_THICKNESS,
			WeilSettings.DEFAULT_CLIP_THICKNESS);
	private JSpinner _clip_spinner = new JSpinner(new SpinnerNumberModel(
			WeilSettings.DEFAULT_CLIP_THICKNESS, 1, WeilSettings.MAX_THICKNESS,
			1));
	private JSlider _intersecting_slider = new JSlider(1,
			WeilSettings.MAX_THICKNESS,
			WeilSettings.DEFAULT_INTERSECTING_THICKNESS);
	private JSpinner _intersecting_spinner = new JSpinner(
			new SpinnerNumberModel(WeilSettings.DEFAULT_INTERSECTING_THICKNESS,
					1, WeilSettings.MAX_THICKNESS, 1));
	private JSpinner _width_spinner;
	private JSpinner _height_spinner;
	private JButton _ok_button = new JButton("OK");
	private JButton _cancel_button = new JButton("Cancel");
	private WeilSettings _settings_object;

	/**
	 * Reads out current preferences, sets up UI widgets to correnspond to them
	 * and makes dialog visible
	 */
	public void showDialog() {
		_subject_color_label.setBackground(_settings_object
				.getSubjectPolygonColor());
		_clip_color_label.setBackground(_settings_object.getClipPolygonColor());
		_intersecting_color_label.setBackground(_settings_object
				.getIntersectingPolygonColor());

		_canvas_color_label.setBackground(_settings_object.getBackground());

		_subject_slider.setValue(_settings_object.getSubjectPolygonThickness());
		_clip_slider.setValue(_settings_object.getClipPolygonThickness());
		_intersecting_slider.setValue(_settings_object
				.getIntersectingPolygonThickness());

		Dimension size = _settings_object.getSize();

		_width_spinner.setValue(size.width);
		_height_spinner.setValue(size.height);

		setVisible(true);
	}

	/**
	 * Method called in case user confirms changes. Sets new parameters to
	 * application and hides dialog
	 */
	private void confirm() {
		Color colors[] = new Color[] { _subject_color_label.getBackground(),
				_clip_color_label.getBackground(),
				_intersecting_color_label.getBackground(),
				_canvas_color_label.getBackground() };
		for (int i = 0; i < colors.length; ++i) {
			for (int j = i + 1; j < colors.length; ++j) {
				if (colors[i].equals(colors[j])) {
					JOptionPane.showMessageDialog(this,
							"All colors have to be different.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}

		if (_canvas_color_label.getBackground().equals(
				UIManager.getColor("menu"))) {
			JOptionPane.showMessageDialog(this,
					"Canvas color cannot be the same as frame background.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;

		}

		_settings_object.setSubjectPolygonColor(_subject_color_label
				.getBackground());
		_settings_object.setClipPolygonColor(_clip_color_label.getBackground());
		_settings_object.setIntersectingPolygonColor(_intersecting_color_label
				.getBackground());
		_settings_object.setBackground(_canvas_color_label.getBackground());

		_settings_object.setSubjectPolygonThickness(_subject_slider.getValue());
		_settings_object.setClipPolygonThickness(_clip_slider.getValue());
		_settings_object.setIntersectingPolygonThickness(_intersecting_slider
				.getValue());

		_settings_object.fullRepaint();

		_settings_object.setPreferredSize(new Dimension(
				(Integer) _width_spinner.getValue(), (Integer) _height_spinner
						.getValue()));

		_settings_object.modelLoaded();
		setVisible(false);
	}

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
				slider.setValue((Integer) ((JSpinner) e.getSource()).getValue());
			}
		});

		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				spinner.setValue((Integer) ((JSlider) e.getSource()).getValue());
			}
		});
	}

	/**
	 * Makes panel with settings for every type of polygon: color and thickness
	 * 
	 * @param title
	 *            Title for <code>TitledBorder</code>
	 * @param color_label
	 *            color laber to add <code>MouseListener</code>
	 * @param slider
	 *            slider assiciated with thickness
	 * @param textfield
	 *            textfield assiciated with thickness
	 * @return panel with UI widgets
	 */
	private JPanel makeSubpanel(String title, final JLabel color_label,
			JSlider slider, JSpinner spinner) {

		syncSpinnerSlider(slider, spinner);

		color_label.setHorizontalAlignment(SwingConstants.CENTER);
		color_label.setOpaque(true);

		color_label.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				Color new_color = JColorChooser.showDialog(
						PreferencesDialog.this, "Choose color",
						color_label.getBackground());
				if (new_color != null) {
					color_label.setBackground(new_color);
				}

			}
		});

		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		panel1.setBorder(new TitledBorder(title));

		JPanel panel111 = new JPanel();
		panel111.add(new JLabel("Color:"));

		JPanel panel11 = new JPanel(new BorderLayout(5, 5));
		panel11.add(panel111, BorderLayout.WEST);
		panel11.add(color_label, BorderLayout.CENTER);

		JPanel panel12 = new JPanel(new BorderLayout());

		JLabel thickness_label = new JLabel("Thickness", JLabel.CENTER);
		thickness_label.setAlignmentX(Component.CENTER_ALIGNMENT);

		panel12.add(thickness_label, BorderLayout.CENTER);

		slider.setMajorTickSpacing(WeilSettings.MAX_THICKNESS / 5);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		JPanel panel121 = new JPanel();
		panel121.add(slider);
		panel121.add(spinner);

		panel12.add(panel121, BorderLayout.SOUTH);

		panel1.add(panel11);
		panel1.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 5),
				new Dimension(0, 5)));
		panel1.add(panel12);

		return panel1;
	}

	/**
	 * Makes <code>JPanel</code> with OK and Canel buttons; adds ActionListeners
	 * as well
	 * 
	 * @return panel with OK and Canel buttons
	 */
	private JPanel makeButtons() {
		JPanel outer_buttons_panel = new JPanel(new BorderLayout());
		JPanel buttons_panel = new JPanel(new GridLayout(1, 0, 5, 0));

		_ok_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				confirm();
			}
		});

		_cancel_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		buttons_panel.add(_ok_button);
		buttons_panel.add(_cancel_button);

		outer_buttons_panel.add(buttons_panel, BorderLayout.EAST);
		return outer_buttons_panel;
	}

	/**
	 * Makes <code>JPanel</code> with JSpinner for width x height representation
	 * 
	 * @return panel with JSpinner for width x height representation
	 */
	private JPanel makeCanvasPanel() {

		_width_spinner = new JSpinner(
				new SpinnerNumberModel(WeilSettings.FRAME_WIDTH, 1,
						WeilSettings.MAX_DIMENSION_SIZE, 1));
		_height_spinner = new JSpinner(new SpinnerNumberModel(
				WeilSettings.FRAME_HEIGHT, 1, WeilSettings.MAX_DIMENSION_SIZE,
				1));

		for (JSpinner s : new JSpinner[] { _width_spinner, _height_spinner }) {
			JComponent editor = s.getEditor();
			JFormattedTextField textfield = ((JSpinner.DefaultEditor) editor)
					.getTextField();
			NumberFormatter n_format = (NumberFormatter) textfield
					.getFormatter();
			n_format.setCommitsOnValidEdit(true);
			n_format.setAllowsInvalid(false);
		}

		JPanel p0 = new JPanel(new GridLayout(2, 1));
		JPanel p1 = new JPanel(new GridLayout(1, 2));
		p0.setBorder(new TitledBorder("Canvas"));
		JPanel p2 = new JPanel();
		p2.add(new JLabel("Width: "));
		p2.add(_width_spinner);
		JPanel p3 = new JPanel();

		p3.add(new JLabel("Height: "));
		p3.add(_height_spinner);
		p1.add(p2);
		p1.add(p3);

		_canvas_color_label.setOpaque(true);
		_canvas_color_label.setHorizontalAlignment(SwingConstants.CENTER);
		_canvas_color_label.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				Color new_color = JColorChooser.showDialog(
						PreferencesDialog.this, "Choose color",
						_canvas_color_label.getBackground());
				if (new_color != null) {
					_canvas_color_label.setBackground(new_color);
				}

			}
		});

		JPanel p5 = new JPanel();
		p5.add(new JLabel("Color:"));

		JPanel p6 = new JPanel(new BorderLayout(5, 5));
		p6.add(p5, BorderLayout.WEST);
		p6.add(_canvas_color_label, BorderLayout.CENTER);

		p0.add(p6);
		p0.add(p1);

		return p0;

	}

	/**
	 * Constructor with given title, modality value
	 * 
	 * @param owner
	 *            Frame from which the dialog is displayed
	 * @param title
	 *            String to display in the dialog's title bar
	 * @param modal
	 *            specifies whether dialog blocks user input to other top-level
	 *            windows when shown
	 * @param settings_object
	 *            reference to settings object
	 */
	public PreferencesDialog(WeilSettings settings_object, Frame owner,
			String title, boolean modal) {
		super(owner, title, modal);
		setLocationRelativeTo(owner);
		_settings_object = settings_object;
		setResizable(false);

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		add(makeSubpanel("Subject polygon", _subject_color_label,
				_subject_slider, _subject_spinner));
		add(makeSubpanel("Clip polygon", _clip_color_label, _clip_slider,
				_clip_spinner));
		add(makeSubpanel("Intersecting polygon", _intersecting_color_label,
				_intersecting_slider, _intersecting_spinner));

		add(makeCanvasPanel());

		add(makeButtons());

		pack();

	}
}
