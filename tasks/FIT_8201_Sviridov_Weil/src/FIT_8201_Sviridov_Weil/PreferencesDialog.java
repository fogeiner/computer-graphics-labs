package FIT_8201_Sviridov_Weil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AbstractDocument;
import FIT_8201_Sviridov_Weil.TextFieldSliderDocumentFilter;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

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
    private JLabel _subject_color_label = new JLabel("Subject polygon color");
    private JLabel _clip_color_label = new JLabel("Clip polygon color");
    private JLabel _intersecting_color_label = new JLabel("Intersecting polygon color");
    private JSlider _subject_slider = new JSlider(1, WeilSettings.MAX_THICKNESS);
    private JTextField _subject_textfield = new JTextField(5);
    private JSlider _clip_slider = new JSlider(1, WeilSettings.MAX_THICKNESS);
    private JTextField _clip_textfield = new JTextField(5);
    private JSlider _intersecting_slider = new JSlider(1, WeilSettings.MAX_THICKNESS);
    private JTextField _intersecting_textfield = new JTextField(5);
    private JButton _ok_button = new JButton("OK");
    private JButton _cancel_button = new JButton("Cancel");
//	private JLabel _bg_color_label = new JLabel();
//	private JLabel _polyline_color_label = new JLabel();
//	private ButtonGroup _polylines_button_group = new ButtonGroup();
//	private JRadioButton _r1 = new JRadioButton("Solid");
//	private JRadioButton _r2 = new JRadioButton("Dashed");
//	private JRadioButton _r3 = new JRadioButton("Dashed-dotted");
//	private JSlider _radius_slider = new JSlider(1, WeilSettings.MAX_RADIUS);
//	private JTextField _radius_textfield = new JTextField(5);
//	private JSpinner _thickness_spinner;
    private WeilSettings _settings_object;

    /**
     * Reads out current preferences, sets up UI widgets to correnspond to them
     * and makes dialog visible
     */
    public void showDialog() {
//		_bg_color_label.setBackground(_settings_object.getBackgroundColor());
//		_polyline_color_label.setBackground(_settings_object.getPolylineColor());
//
//		int polyline_type = _settings_object.getPolylineType();
//
//		if (polyline_type == WeilSettings.CONTINIOUS) {
//			_polylines_button_group.setSelected(_r1.getModel(), true);
//		} else if (polyline_type == WeilSettings.DASHED) {
//			_polylines_button_group.setSelected(_r2.getModel(), true);
//		} else if (polyline_type == WeilSettings.DOTTED_DASHED) {
//			_polylines_button_group.setSelected(_r3.getModel(), true);
//		}
//
//		_radius_slider.setValue(_settings_object.getCircleRadius());
//		_thickness_spinner.setValue(_settings_object.getPolylineThickness());
//
//		_radius_textfield.setText(Integer.toString(_settings_object
//				.getCircleRadius()));

        setVisible(true);
    }

    /**
     * Method called in case user confirms changes. Sets new parameters to
     * application and hides dialog
     */
    private void confirm() {
//		_settings_object.setBackgroundColor(_bg_color_label.getBackground());
//		_settings_object.setPolylineColor(_polyline_color_label.getBackground());
//
//		if (_polylines_button_group.getSelection() == _r1.getModel()) {
//			_settings_object.setPolylineType(WeilSettings.CONTINIOUS);
//		} else if (_polylines_button_group.getSelection() == _r2.getModel()) {
//			_settings_object.setPolylineType(WeilSettings.DASHED);
//		} else if (_polylines_button_group.getSelection() == _r3.getModel()) {
//			_settings_object.setPolylineType(WeilSettings.DOTTED_DASHED);
//		}
//
//		_settings_object.setCircleRadius(_radius_slider.getValue());
//		_settings_object.setPolylineThickness((Integer) _thickness_spinner
//				.getValue());
        setVisible(false);
    }

    private JPanel makeSubpanel(String title, final JLabel color_label, JSlider slider, JTextField textfield) {

        ((AbstractDocument) textfield.getDocument()).setDocumentFilter(new TextFieldSliderDocumentFilter(
                textfield, slider));

        color_label.setHorizontalAlignment(SwingConstants.CENTER);
        color_label.setOpaque(true);

        color_label.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                Color new_color = JColorChooser.showDialog(PreferencesDialog.this,
                        "Choose color", color_label.getBackground());
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
        panel121.add(textfield);

        panel12.add(panel121, BorderLayout.SOUTH);

        panel1.add(panel11);
        panel1.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 5), new Dimension(0, 5)));
        panel1.add(panel12);

        return panel1;
    }

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
    public PreferencesDialog(WeilSettings settings_object, Frame owner, String title,
            boolean modal) {
        super(owner, title, modal);
        setLocationRelativeTo(owner);
        _settings_object = settings_object;
        setResizable(false);


        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(makeSubpanel("Subject polygon", _subject_color_label, _subject_slider, _subject_textfield));
        add(makeSubpanel("Clip polygon", _clip_color_label, _clip_slider, _clip_textfield));
        add(makeSubpanel("Intersecting polygon", _intersecting_color_label, _intersecting_slider, _intersecting_textfield));
        add(makeButtons());

        pack();

    }
}
