package FIT_8201_Sviridov_Lines;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.AbstractDocument;
import FIT_8201_Sviridov_Lines.TextFieldSliderDocumentFilter;

/**
 * Preferences dialog class. Lets user set all parameters. New parameters may be
 * confirmed and saved or dismissed
 * 
 * @author alstein
 * 
 */

class PreferencesDialog extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3486091859979955990L;

	private JLabel _bg_color_label = new JLabel();
	private JLabel _polyline_color_label = new JLabel();
	private ButtonGroup _polylines_button_group = new ButtonGroup();
	private JRadioButton _r1 = new JRadioButton("Solid");
	private JRadioButton _r2 = new JRadioButton("Dashed");
	private JRadioButton _r3 = new JRadioButton("Dashed-dotted");
	private JButton _ok_button = new JButton("OK");
	private JButton _cancel_button = new JButton("Cancel");
	private JSlider _radius_slider = new JSlider(1, PolylineSettings.MAX_RADIUS);
	private JTextField _radius_textfield = new JTextField(5);
	private JSpinner _thickness_spinner;
	private PolylineSettings _settings_object;

	/**
	 * Reads out current preferences, sets up UI widgets to correnspond to them
	 * and makes dialog visible
	 */
	public void showDialog() {
		_bg_color_label.setBackground(_settings_object.getBackgroundColor());
		_polyline_color_label
				.setBackground(_settings_object.getPolylineColor());

		int polyline_type = _settings_object.getPolylineType();

		if (polyline_type == PolylineSettings.CONTINIOUS) {
			_polylines_button_group.setSelected(_r1.getModel(), true);
		} else if (polyline_type == PolylineSettings.DASHED) {
			_polylines_button_group.setSelected(_r2.getModel(), true);
		} else if (polyline_type == PolylineSettings.DOTTED_DASHED) {
			_polylines_button_group.setSelected(_r3.getModel(), true);
		}

		_radius_slider.setValue(_settings_object.getCircleRadius());
		_thickness_spinner.setValue(_settings_object.getPolylineThickness());

		_radius_textfield.setText(Integer.toString(_settings_object
				.getCircleRadius()));

		setVisible(true);
	}

	/**
	 * Method called in case user confirms changes. Sets new parameters to
	 * application and hides dialog
	 */
	private void confirm() {
		_settings_object.setBackgroundColor(_bg_color_label.getBackground());
		_settings_object
				.setPolylineColor(_polyline_color_label.getBackground());

		if (_polylines_button_group.getSelection() == _r1.getModel()) {
			_settings_object.setPolylineType(PolylineSettings.CONTINIOUS);
		} else if (_polylines_button_group.getSelection() == _r2.getModel()) {
			_settings_object.setPolylineType(PolylineSettings.DASHED);
		} else if (_polylines_button_group.getSelection() == _r3.getModel()) {
			_settings_object.setPolylineType(PolylineSettings.DOTTED_DASHED);
		}

		_settings_object.setCircleRadius(_radius_slider.getValue());
		_settings_object.setPolylineThickness((Integer) _thickness_spinner
				.getValue());
		setVisible(false);
	}

	/**
	 * Constructor with given LinesFrame title, modality value
	 * 
	 * @param owner
	 *            Frame from which the dialog is displayed
	 * @param title
	 *            String to display in the dialog's title bar
	 * @param modal
	 *            specifies whether dialog blocks user input to other top-level
	 *            windows when shown
	 * @param linesFrame
	 *            reference to LinesFrame
	 */
	public PreferencesDialog(PolylineSettings settings_object, Frame owner,
			String title, boolean modal) {
		super(owner, title, modal);
		setLocationRelativeTo(owner);
		_settings_object = settings_object;

		_thickness_spinner = new JSpinner(new SpinnerNumberModel(
				_settings_object.getPolylineThickness(), 1, Integer.MAX_VALUE,
				1));

		setResizable(false);

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		JPanel upper_panel = new JPanel();
		upper_panel.setLayout(new GridLayout(1, 0));

		// Colors

		JPanel colors_panel = new JPanel();
		colors_panel.setBorder(BorderFactory.createTitledBorder("Colors"));

		colors_panel.setLayout(new GridLayout(0, 2, 5, 5));
		colors_panel.add(new JLabel("Background:"));
		JPanel color_button_panel = new JPanel();
		color_button_panel.setLayout(new BorderLayout());
		color_button_panel.add(_bg_color_label);

		colors_panel.add(color_button_panel);
		colors_panel.add(new JLabel("Polyline:"));
		color_button_panel = new JPanel();
		color_button_panel.setLayout(new BorderLayout());
		color_button_panel.add(_polyline_color_label);
		colors_panel.add(color_button_panel);

		upper_panel.add(colors_panel);

		// Polyline types

		JPanel polyline_type_group = new JPanel();
		polyline_type_group.setBorder(BorderFactory
				.createTitledBorder("Polyline Types"));

		_polylines_button_group.add(_r1);
		_polylines_button_group.add(_r2);
		_polylines_button_group.add(_r3);

		JPanel polylines_radio_panel = new JPanel(new GridLayout(0, 1));
		polylines_radio_panel.add(_r1);
		polylines_radio_panel.add(_r2);
		polylines_radio_panel.add(_r3);

		polyline_type_group.add(polylines_radio_panel);

		upper_panel.add(polyline_type_group);

		// Radius/Thickness

		JPanel radius_thickness_panel = new JPanel();
		radius_thickness_panel.setBorder(BorderFactory
				.createTitledBorder("Drawing"));
		radius_thickness_panel.setLayout(new GridLayout(0, 1));

		_radius_slider.setMajorTickSpacing(PolylineSettings.MAX_RADIUS / 5);
		_radius_slider.setMinorTickSpacing(1);
		_radius_slider.setPaintTicks(true);
		_radius_slider.setPaintLabels(true);

		JPanel radius_panel = new JPanel();
		radius_panel.setLayout(new BorderLayout());

		JLabel radius_label = new JLabel("Circle radius", JLabel.CENTER);
		radius_label.setAlignmentX(Component.CENTER_ALIGNMENT);

		radius_panel.add(_radius_slider, BorderLayout.CENTER);
		radius_panel.add(_radius_textfield, BorderLayout.EAST);

		JPanel thickness_panel = new JPanel();
		thickness_panel.setLayout(new BorderLayout());

		JLabel thickness_label = new JLabel("Polyline thickness", JLabel.CENTER);
		thickness_label.setAlignmentX(Component.CENTER_ALIGNMENT);

		thickness_panel.add(_thickness_spinner, BorderLayout.CENTER);

		radius_thickness_panel.add(radius_label);
		radius_thickness_panel.add(radius_panel);
		radius_thickness_panel.add(thickness_label);
		radius_thickness_panel.add(thickness_panel);

		// Buttons
		JPanel buttons_inner_panel = new JPanel();
		buttons_inner_panel.setLayout(new GridLayout(1, 2, 5, 0));
		JPanel buttons_panel = new JPanel();
		buttons_panel.setLayout(new FlowLayout(FlowLayout.TRAILING));

		_ok_button.setSize(_cancel_button.getSize());

		buttons_inner_panel.add(_ok_button);
		buttons_inner_panel.add(_cancel_button);
		buttons_panel.add(buttons_inner_panel);

		add(upper_panel);
		add(radius_thickness_panel);
		add(buttons_panel);
		pack();

		((JSpinner.DefaultEditor) _thickness_spinner.getEditor())
				.getTextField().addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) {
						if (!Character.isDigit(e.getKeyChar())) {
							e.consume();
						}
					}
				});
		
		((AbstractDocument) _radius_textfield.getDocument())
				.setDocumentFilter(new TextFieldSliderDocumentFilter(
						_radius_textfield, _radius_slider));

		_polyline_color_label.setOpaque(true);
		_polyline_color_label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color new_color = JColorChooser.showDialog(
						PreferencesDialog.this, "Choose polyline color",
						_polyline_color_label.getBackground());
				if (new_color != null) {
					_polyline_color_label.setBackground(new_color);
				}
			}
		});

		_bg_color_label.setOpaque(true);
		_bg_color_label.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				Color new_color = JColorChooser.showDialog(
						PreferencesDialog.this, "Choose canvas color",
						_bg_color_label.getBackground());
				if (new_color != null) {
					_bg_color_label.setBackground(new_color);
				}

			}
		});

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
	}
}