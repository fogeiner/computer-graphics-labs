package FIT_8201_Sviridov_Lines;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import ru.nsu.cg.MainFrame;

public class LinesFrame extends MainFrame {

	private static final long serialVersionUID = 5852264472785688626L;
	private final static int WIDTH = 600;
	private final static int HEIGHT = 400;
	private final static String LINES = "Lines";
	private final static String UNTITLED_DOCUMENT = "Untitled";
	private String _document_name = UNTITLED_DOCUMENT; 
	private boolean _is_document_saved = false;

	private LinesView _lines_view;

	private final static int VIEW_STATE = 0;
	private final static int EDIT_STATE = 1;
	private int _state = VIEW_STATE;

	private List<Polyline> _polylines = new LinkedList<Polyline>();
	private Polyline _new_polyline = null;

	private PreferencesDialog _preferences_dialog = null;

	public static final Color DEFAULT_POLYLINE_COLOR = Color.black;
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.white;
	public static final int DEFAULT_POLYLINE_TYPE = Polyline.CONTINIOUS;
	public static final int DEFAULT_POLYLINE_THICKNESS = 1;
	public static final int DEFAULT_CIRCLE_RADIUS = 5;

	public static final int MAX_THICKNESS = 16;
	public static final int MAX_RADIUS = 36;

	Color _polyline_color = DEFAULT_POLYLINE_COLOR;
	Color _background_color = DEFAULT_BACKGROUND_COLOR;
	int _polyline_type = DEFAULT_POLYLINE_TYPE;
	int _polyline_thickness = DEFAULT_POLYLINE_THICKNESS;
	int _circle_radius = DEFAULT_CIRCLE_RADIUS;

	public boolean isSaved(){
		return _is_document_saved;
	}
	
	public void setSaved(boolean value){
		_is_document_saved = value;
	}
	
	public LinesFrame(int width, int height) {
		super(width, height, "");
		setTitle(_document_name + " - " + LINES);
		setSaved(false);
		
		try {

			// constructing Menu
			addSubMenu("File", KeyEvent.VK_F);

			addMenuItem("File/New", "New document", KeyEvent.VK_N, "new.gif",
					"onNew");
			addMenuItem("File/Load", "Load document", KeyEvent.VK_L,
					"load.gif", "onLoad");
			addMenuItem("File/Save as...", "Save document", KeyEvent.VK_S,
					"save.gif", "onSave");
			addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X,
					"exit.gif", "onExit");

			addSubMenu("Edit", KeyEvent.VK_E);

			addMenuItem("Edit/Preferences", "Change preferences",
					KeyEvent.VK_P, "preferences.gif", "onPreferences");

			addSubMenu("Help", KeyEvent.VK_H);

			addMenuItem("Help/About",
					"View application version and author information",
					KeyEvent.VK_A, "about.gif", "onAbout");

			// constructing Toolbar
			addToolBarButton("File/New");
			addToolBarButton("File/Load");
			addToolBarButton("File/Save as...");
			addToolBarButton("File/Exit");
			addToolBarSeparator();
			addToolBarButton("Edit/Preferences");
			addToolBarSeparator();
			addToolBarButton("Help/About");

			_lines_view = new LinesView(this);
			add(_lines_view);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onNew() {
		_polylines = new LinkedList<Polyline>();

		_polyline_color = DEFAULT_POLYLINE_COLOR;
		_background_color = DEFAULT_BACKGROUND_COLOR;
		_polyline_type = DEFAULT_POLYLINE_TYPE;
		_polyline_thickness = DEFAULT_POLYLINE_THICKNESS;
		_circle_radius = DEFAULT_CIRCLE_RADIUS;

		if(!isSaved()){
			
		}
		
		setTitle(_document_name + " - " + LINES);
		setSaved(false);
		
		_lines_view.repaint();
	}

	public void onLoad() {

	}

	public void onSave() {
		int size = _polylines.size();
		System.out.println(size);
		for (int i = 0; i < size; ++i) {
			System.out.print(_polylines.get(i).toString());
		}
		
		setSaved(true);
	}

	public void onExit() {
		if(!isSaved()){
			
		}
		
		System.exit(0);
	}

	public void onPreferences() {
		if (_preferences_dialog == null) {
			_preferences_dialog = new PreferencesDialog(this,
					"Lines Preferences", true);
		}
		_preferences_dialog.showDialog();
		repaint();
	}

	public void onAbout() {
		JOptionPane.showMessageDialog(this,
				"Lines, version 1.0\n2011 Valentin Sviridov, FIT, group 8201",
				"About Lines", JOptionPane.INFORMATION_MESSAGE);
	}

	private void switchState(int state) {

		String[] elems = new String[] { "File/New", "File/Load",
				"File/Save as...", "Edit/Preferences" };

		if (_state == LinesFrame.EDIT_STATE && state == LinesFrame.VIEW_STATE) {
			// enable UI elements
			for (String name : elems) {
				((JMenuItem) getMenuElement(name)).setEnabled(true);
				for (Component comp : toolBar.getComponents()) {
					if (comp instanceof JButton) {
						((JButton) comp).setEnabled(true);
					}
				}
			}
		} else if (_state == LinesFrame.VIEW_STATE
				&& state == LinesFrame.EDIT_STATE) {
			// disable UI elements
			for (String name : elems) {
				JMenuItem menuitem = (JMenuItem) getMenuElement(name);

				menuitem.setEnabled(false);

				for (Component comp : toolBar.getComponents()) {
					if (comp instanceof JButton) {
						JButton button = (JButton) comp;

						if (!button.getName().equals("Exit")
								&& !button.getName().equals("About")) {
							button.setEnabled(false);
						}
					}
				}
			}
		}
		_state = state;
	}

	public void leftClick(Point point) {
		if (_state == LinesFrame.VIEW_STATE) {
			switchState(LinesFrame.EDIT_STATE);
			_new_polyline = new Polyline(_polyline_type, _polyline_thickness,
					_polyline_color);
			_new_polyline.addPoint(point);
			_polylines.add(_new_polyline);

			_lines_view.enableRubberLine();
		} else if (_state == LinesFrame.EDIT_STATE) {
			// adding new Polyline point
			_new_polyline.addPoint(point);
		}
	}

	public void rightClick(Point point) {
		if (_state == LinesFrame.VIEW_STATE) {
			// ignore
		} else if (_state == LinesFrame.EDIT_STATE) {
			switchState(LinesFrame.VIEW_STATE);
			_lines_view.disableRubberLine();
			_new_polyline = null;
		}
	}

	public int getCircleRadius() {
		return _circle_radius;
	}

	public Color getBackgroundColor() {
		return new Color(_background_color.getRGB());
	}

	public List<Polyline> getPolylines() {
		return Collections.unmodifiableList(_polylines);
	}

	private class PreferencesDialog extends javax.swing.JDialog {

		private static final long serialVersionUID = -3486091859979955990L;

		private JButton _bg_color_button = new JButton();
		private JButton _polyline_color_button = new JButton();
		private ButtonGroup _polylines_button_group = new ButtonGroup();
		private JRadioButton _r1 = new JRadioButton("Continious");
		private JRadioButton _r2 = new JRadioButton("Dash and dot");
		private JRadioButton _r3 = new JRadioButton("Dotted dash and dot");
		private JButton _confirm_button = new JButton("Confirm");
		private JButton _cancel_button = new JButton("Cancel");
		private JSlider _radius_slider = new JSlider(1, MAX_RADIUS);
		private JSlider _thickness_slider = new JSlider(1, MAX_THICKNESS);
		private JFormattedTextField _radius_textfield = new JFormattedTextField(
				NumberFormat.getInstance());
		private JFormattedTextField _thickness_textfield = new JFormattedTextField(
				NumberFormat.getInstance());

		public void showDialog() {
			_bg_color_button.setBackground(_background_color);
			_polyline_color_button.setBackground(_polyline_color);

			if (_polyline_type == Polyline.CONTINIOUS) {
				_polylines_button_group.setSelected(_r1.getModel(), true);
			} else if (_polyline_type == Polyline.DASH_AND_DOT) {
				_polylines_button_group.setSelected(_r2.getModel(), true);
			} else if (_polyline_type == Polyline.DOTTED_DASH_AND_DOT) {
				_polylines_button_group.setSelected(_r3.getModel(), true);
			}

			_radius_textfield.setText(Integer.toString(_circle_radius));
			_thickness_textfield.setText(Integer.toString(_polyline_thickness));

			_radius_slider.setValue(_circle_radius);
			_thickness_slider.setValue(_polyline_thickness);

			setVisible(true);
		}

		public void confirm() {
			_background_color = _bg_color_button.getBackground();
			_polyline_color = _polyline_color_button.getBackground();

			if (_polylines_button_group.getSelection() == _r1.getModel()) {
				_polyline_type = Polyline.CONTINIOUS;
			} else if (_polylines_button_group.getSelection() == _r2.getModel()) {
				_polyline_type = Polyline.DASH_AND_DOT;
			} else if (_polylines_button_group.getSelection() == _r3.getModel()) {
				_polyline_type = Polyline.DOTTED_DASH_AND_DOT;
			}

			_circle_radius = _radius_slider.getValue();
			_polyline_thickness = _thickness_slider.getValue();
			setVisible(false);
		}

		public PreferencesDialog(LinesFrame lines_frame, String title,
				boolean modal) {
			super(lines_frame, title, modal);
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
			color_button_panel.add(_bg_color_button);

			colors_panel.add(color_button_panel);
			colors_panel.add(new JLabel("Polyline:"));
			color_button_panel = new JPanel();
			color_button_panel.setLayout(new BorderLayout());
			color_button_panel.add(_polyline_color_button);
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

			_radius_slider.setMajorTickSpacing(MAX_RADIUS / 5);
			_radius_slider.setMinorTickSpacing(1);
			_radius_slider.setPaintTicks(true);
			_radius_slider.setPaintLabels(true);

			_thickness_slider.setMajorTickSpacing(MAX_THICKNESS / 5);
			_thickness_slider.setMinorTickSpacing(1);
			_thickness_slider.setPaintTicks(true);
			_thickness_slider.setPaintLabels(true);

			JPanel radius_panel = new JPanel();
			radius_panel.setLayout(new FlowLayout());

			JLabel radius_label = new JLabel("Circle radius", JLabel.CENTER);
			radius_label.setAlignmentX(Component.CENTER_ALIGNMENT);

			radius_panel.add(_radius_textfield);
			radius_panel.add(_radius_slider);
			_radius_textfield.setColumns(5);

			JPanel thickness_panel = new JPanel();
			thickness_panel.setLayout(new FlowLayout());

			JLabel thickness_label = new JLabel("Polyline thickness",
					JLabel.CENTER);
			thickness_label.setAlignmentX(Component.CENTER_ALIGNMENT);

			_thickness_textfield.setColumns(5);
			thickness_panel.add(_thickness_textfield);
			thickness_panel.add(_thickness_slider);

			radius_thickness_panel.add(radius_label);
			radius_thickness_panel.add(radius_panel);
			radius_thickness_panel.add(thickness_label);
			radius_thickness_panel.add(thickness_panel);

			// Buttons
			JPanel buttons_panel = new JPanel();
			buttons_panel.setLayout(new FlowLayout(FlowLayout.TRAILING));

			buttons_panel.add(_confirm_button);
			buttons_panel.add(_cancel_button);

			add(upper_panel);
			add(radius_thickness_panel);
			add(buttons_panel);
			pack();

			_radius_textfield.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					Long value = (Long) _radius_textfield.getValue();

					if (value == null || value <= 0
							|| value > Integer.MAX_VALUE) {
						_radius_textfield.setValue(1);
						_radius_slider.setValue(1);
						return;
					}

					if (value >= _radius_slider.getMaximum()) {
						_radius_slider.setValue(_radius_slider.getMaximum());
						_radius_textfield.setValue(_radius_slider.getMaximum());
					} else {
						_radius_slider.setValue(value.intValue());
					}
				}
			});

			_thickness_textfield.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					Long value = (Long) _thickness_textfield.getValue();

					if (value == null || value <= 0
							|| value > Integer.MAX_VALUE) {
						_thickness_textfield.setValue(1);
						_thickness_slider.setValue(1);
						return;
					}

					if (value >= _thickness_slider.getMaximum()) {
						_thickness_slider.setValue(_radius_slider.getMaximum());
						_thickness_textfield.setValue(_radius_slider
								.getMaximum());
					} else {
						_thickness_slider.setValue(value.intValue());
					}
				}
			});

			_radius_slider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					_radius_textfield.setText(Integer.toString(_radius_slider
							.getValue()));
				}
			});

			_thickness_slider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					_thickness_textfield.setText(Integer
							.toString(_thickness_slider.getValue()));
				}
			});

			_polyline_color_button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Color new_color = JColorChooser.showDialog(
							PreferencesDialog.this, "Choose polyline color",
							_polyline_color_button.getBackground());
					if (new_color != null) {
						_polyline_color_button.setBackground(new_color);
					}
				}
			});

			_bg_color_button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Color new_color = JColorChooser.showDialog(
							PreferencesDialog.this, "Choose canvas color",
							_bg_color_button.getBackground());
					if (new_color != null) {
						_bg_color_button.setBackground(new_color);
					}
				}
			});

			_confirm_button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
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

	public static void main(String args[]) {
		// TODO: alter _title with "Untitled"
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				LinesFrame lines_frame = new LinesFrame(LinesFrame.WIDTH,
						LinesFrame.HEIGHT);
				lines_frame.setVisible(true);
			}
		});
	}
}
