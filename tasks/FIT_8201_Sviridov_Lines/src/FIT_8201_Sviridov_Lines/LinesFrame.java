package FIT_8201_Sviridov_Lines;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ru.nsu.cg.MainFrame;

public class LinesFrame extends MainFrame {

	private static final long serialVersionUID = 5852264472785688626L;
	private final static int WIDTH = 600;
	private final static int HEIGHT = 400;
	private final static String TITLE = "Lines";

	private LinesView _lines_view;

	private final static int VIEW_STATE = 0;
	private final static int EDIT_STATE = 1;
	private int _state = VIEW_STATE;

	private List<Polyline> _polylines = new LinkedList<Polyline>();
	private Polyline _new_polyline;
	
	private PreferencesDialog _preferences_dialog = null;

	Color _polyline_color = Color.black;
	Color _background_color = Color.white;
	int _polyline_type = Polyline.CONTINIOUS;
	int _polyline_thickness = 1;
	int _circle_radius = 5;
	
	public LinesFrame(int width, int height, String title) {
		super(width, height, title);

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
		_lines_view.repaint();
	}

	public void onLoad() {
		
	}

	public void onSave() {
		int size = _polylines.size();
		System.out.println(size);
		for(int i = 0; i < size; ++i){
			System.out.print(_polylines.get(i).toString());
		}
	}

	public void onExit() {
		System.exit(0);
	}

	public void onPreferences() {
		if(_preferences_dialog == null){
			_preferences_dialog = new PreferencesDialog(this, "Lines Preferences", true);
		}
		_preferences_dialog.setVisible(true);
		
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
			_new_polyline = new Polyline(_polyline_type, _polyline_thickness, _polyline_color);
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

	public int getCircleRadius(){
		return _circle_radius;
	}
	
	public Color getBackgroundColor(){
		return new Color(_background_color.getRGB());
	}
	
	public List<Polyline> getPolylines() {
		return Collections.unmodifiableList(_polylines);
	}

	private class PreferencesDialog extends javax.swing.JDialog {
		
		private static final long serialVersionUID = -3486091859979955990L;
		
		public PreferencesDialog(LinesFrame lines_frame, String title, boolean modal) {
			super(lines_frame, title, modal);

			setLayout(new BorderLayout());
			
			JPanel groups_group = new JPanel();
			groups_group.setLayout(new BorderLayout());
			
			JPanel colors_group = new JPanel();
			colors_group.setBorder(BorderFactory.createTitledBorder("Colors"));
			groups_group.add(colors_group, BorderLayout.WEST);
			
			JPanel polyline_type_group = new JPanel();
			polyline_type_group.setBorder(BorderFactory.createTitledBorder("Polyline Types"));
			groups_group.add(polyline_type_group, BorderLayout.CENTER);
			
			JPanel radius_thickness_group = new JPanel();
			radius_thickness_group.setBorder(BorderFactory.createTitledBorder("Radius/Thickness"));
			groups_group.add(radius_thickness_group, BorderLayout.EAST);
			
			add(groups_group, BorderLayout.CENTER);
			pack();
		}
	}

	
	public static void main(String args[]) {
		// TODO: alter _title with "Untitled"
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				LinesFrame lines_frame = new LinesFrame(LinesFrame.WIDTH,
						LinesFrame.HEIGHT, LinesFrame.TITLE);
				lines_frame.setVisible(true);
			}
		});
	}
}
