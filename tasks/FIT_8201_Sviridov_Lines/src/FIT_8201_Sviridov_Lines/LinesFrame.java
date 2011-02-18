package FIT_8201_Sviridov_Lines;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import ru.nsu.cg.MainFrame;

/**
 * Lines Frame - Lines application main frame
 * 
 * @author alstein
 */
public class LinesFrame extends MainFrame {

	private static final long serialVersionUID = 5852264472785688626L;
	private final static int WIDTH = 600;
	private final static int HEIGHT = 400;
	private final static String LINES_NAME = "FIT_8201_Sviridov_Lines";
	private final static String UNTITLED_DOCUMENT = "Untitled";
	private boolean _is_document_saved = false;

	private LinesView _lines_view;

	private final static int VIEW_STATE = 0;
	private final static int EDIT_STATE = 1;
	private int _state = VIEW_STATE;

	private List<Polyline> _polylines = new ArrayList<Polyline>();
	private Polyline _new_polyline = null;

	private PreferencesDialog _preferences_dialog = null;

	public static final Color DEFAULT_POLYLINE_COLOR = Color.black;
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.white;
	public static final int DEFAULT_POLYLINE_TYPE = Polyline.CONTINIOUS;
	public static final int DEFAULT_POLYLINE_THICKNESS = 1;
	public static final int DEFAULT_CIRCLE_RADIUS = 5;

	public static final int MAX_THICKNESS = 36;
	public static final int MAX_RADIUS = 36;

	private Color _polyline_color = DEFAULT_POLYLINE_COLOR;
	private Color _background_color;
	private int _polyline_type;
	private int _polyline_thickness;
	private int _circle_radius;

	/**
	 * Returns list of current (i.e. which are present on screen right away)
	 * polylines
	 * 
	 * @return Unmodifiable list of current polylines
	 */
	public List<Polyline> getPolylines() {
		return Collections.unmodifiableList(_polylines);
	}

	/**
	 * Adds polyline to list of current polylines
	 * 
	 * @param polyline
	 *            Polyline to be added to list of current polylines
	 */
	private void addPolyline(Polyline polyline) {
		_polylines.add(polyline);
	}

	/**
	 * Clears list of current polylines
	 */
	private void clearPolylines() {
		_polylines.clear();
	}

	/**
	 * Returns current color of polyline
	 * 
	 * @return color of current polyline
	 */
	public Color getPolylineColor() {
		return _polyline_color;
	}

	/**
	 * Sets current polyline color
	 * 
	 * @param polyline_color
	 *            Color to be set as current polyline color
	 */
	public void setPolylineColor(Color polyline_color) {
		this._polyline_color = polyline_color;
	}

	/**
	 * Returns current color of canvas
	 * 
	 * @return current color of canvas
	 */
	public Color getBackgroundColor() {
		return _background_color;
	}

	/**
	 * Sets current color of canvas
	 * 
	 * @param background_color
	 *            Color to be set as color of canvas
	 */
	public void setBackgroundColor(Color background_color) {
		this._background_color = background_color;
	}

	/**
	 * Returns type of current polyline (one of <code>Polyline.CONTINIOUS</code>
	 * ,<code>Polyline.DASH_AND_DOT</code>,
	 * <code>Polyline.DOTTED_DASH_AND_DOT</code>).
	 * 
	 * @return type of current polyline
	 * @see Polyline
	 */
	public int getPolylineType() {
		return _polyline_type;
	}

	/**
	 * Sets type of current polyline (one of <code>Polyline.CONTINIOUS</code>,
	 * <code>Polyline.DASH_AND_DOT</code>,
	 * <code>Polyline.DOTTED_DASH_AND_DOT</code>).
	 * 
	 * @param polyline_type
	 *            new type of current polyline
	 */
	public void setPolylineType(int polyline_type) {
		this._polyline_type = polyline_type;
	}

	/**
	 * Returns thickness of current polyline
	 * 
	 * @return thickness of current polyline
	 */
	public int getPolylineThickness() {
		return _polyline_thickness;
	}

	/**
	 * Sets thickness of current polyline
	 * 
	 * @param polyline_thickness
	 *            new thickness of current polyline
	 */
	public void setPolylineThickness(int polyline_thickness) {
		this._polyline_thickness = polyline_thickness;
	}

	/**
	 * Returns current circles radius
	 * 
	 * @return current circles radius
	 */
	public int getCircleRadius() {
		return _circle_radius;
	}

	/**
	 * Sets current circles radius
	 * 
	 * @param circle_radius
	 *            current circles radius
	 */
	public void setCircleRadius(int circle_radius) {
		this._circle_radius = circle_radius;
	}

	/**
	 * Checks if there unsaved data in current document
	 * 
	 * @return <code>true</code> if document needs no saving, <code>false</code>
	 *         otherwise
	 */
	private boolean isSaved() {
		return _is_document_saved;
	}

	/**
	 * Sets saved flag (are there unsaved data in current document or not)
	 * 
	 * @param value
	 *            value of saved flag
	 */
	private void setSaved(boolean value) {
		_is_document_saved = value;
	}

	/**
	 * Sets polyline color, polyline thickness, polyline type, canvas color and
	 * circle radius to default values
	 */
	private void resetPreferences() {
		_polyline_color = DEFAULT_POLYLINE_COLOR;
		_background_color = DEFAULT_BACKGROUND_COLOR;
		_polyline_type = DEFAULT_POLYLINE_TYPE;
		_polyline_thickness = DEFAULT_POLYLINE_THICKNESS;
		_circle_radius = DEFAULT_CIRCLE_RADIUS;
	}

	/**
	 * Brings application to the state of new document: resets preferences
	 * changes, sets document name to "Untitled" and clears all current
	 * polylines
	 */
	private void newDocument() {
		setDocumentName(UNTITLED_DOCUMENT);
		clearPolylines();
		setSaved(true);
		repaint();
	}

	/**
	 * Sets application title to "<code>name</code> - Lines"
	 * 
	 * @param name
	 *            first part of application title
	 */

	private void setDocumentName(String name) {
		setTitle(name + " - " + LINES_NAME);
	}

	/**
	 * Constructs application frame with given width and height
	 * 
	 * @param width
	 *            width of frame
	 * @param height
	 *            height of frame
	 */
	public LinesFrame(int width, int height) {
		super(width, height, "");
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

			toolBar.setFloatable(false);

			_lines_view = new LinesView(this);
			add(_lines_view);

			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			resetPreferences();
			newDocument();
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					if ((isSaved() == false) && (showSaveMessage() == false)) {
						return;
					}

					System.exit(0);
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Shows message to find out if user wills to save current document
	 * 
	 * @return <code>true</code> if user confirms, <code>false</code> otherwise
	 */

	private boolean showSaveMessage() {
		int answer = JOptionPane.showConfirmDialog(this,
				"All unsaved data will be lost. Continue?");

		if (answer != JOptionPane.OK_OPTION) {
			return false;
		}

		return true;
	}

	/**
	 * Method called when user chooses "New" in menu or on toolbar. Asks user to
	 * save current document (if needed) and sets application to new document
	 * state
	 */

	public void onNew() {

		if ((isSaved() == false) && (showSaveMessage() == false)) {
			return;
		}

		newDocument();
	}

	/**
	 * Method called when user chooses "Exit" in menu or on toolbar. Asks user
	 * to save current document (if needed) and terminates application
	 */

	public void onExit() {
		if ((isSaved() == false) && (showSaveMessage() == false)) {
			return;
		}

		System.exit(0);
	}

	/**
	 * Method called when user chooses "Load" in menu or on toolbar. Asks user
	 * to save current document (if needed), shows dialog to choose file and
	 * loads document from it
	 */
	public void onLoad() {
		try {
			if ((isSaved() == false) && (showSaveMessage() == false)) {
				return;
			}

			File file = getOpenFileName("txt", "Text files");
			if (file == null) {
				return;
			}

			newDocument();
			
			FileInputStream fstream = new FileInputStream(file);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			int polylines_count = Integer.parseInt(LineParseUtils
					.nextNormalizedLine(br));

			if (polylines_count < 0)
				throw new IllegalArgumentException();

			for (int i = 0; i < polylines_count; ++i) {
				int points_count = Integer.parseInt(LineParseUtils
						.nextNormalizedLine(br));

				if (points_count < 0)
					throw new IllegalArgumentException();

				int type = Integer.parseInt(LineParseUtils
						.nextNormalizedLine(br));

				if (type != Polyline.CONTINIOUS && type != Polyline.DASHED
						&& type != Polyline.DOTTED_DASHED)
					throw new IllegalArgumentException();

				int thickness = Integer.parseInt(LineParseUtils
						.nextNormalizedLine(br));

				if (thickness < 1)
					throw new IllegalArgumentException();

				String str = LineParseUtils.nextNormalizedLine(br);
				String rgb[] = str.split(" ");

				Color color = new Color(Integer.parseInt(rgb[0]),
						Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));

				Polyline polyline = new Polyline(type, thickness, color);

				for (int j = 0; j < points_count; ++j) {
					str = LineParseUtils.nextNormalizedLine(br);

					String coord[] = str.split(" ");

					polyline.addPoint(new Point(Integer.parseInt(coord[0]),
							Integer.parseInt(coord[1])));
				}

				addPolyline(polyline);
			}

			resetPreferences();
			setDocumentName(file.getName());

			repaint();

		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this,
					"Document is of unknown format", "Loading document",
					JOptionPane.ERROR_MESSAGE);
			newDocument();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Error loading file: \n" + e.getLocalizedMessage(),
					"Loading document", JOptionPane.ERROR_MESSAGE);
			newDocument();
		}
	}

	/**
	 * Method called when user chooses "Save" in menu or on toolbar. Shows
	 * dialog to choose/create file and saves document to it
	 */

	public void onSave() {

		try {
			File file = getSaveFileName("txt", "Text files");
			if (file == null) {
				return;
			}

			FileWriter fw = new FileWriter(file);
			
			List<Polyline> polylines = getPolylines();
			int size = polylines.size();

			fw.write(size + "\r\n");
			for (int i = 0; i < size; ++i) {
				fw.write(polylines.get(i).toString());
			}
			fw.close();

			setTitle(file.getName() + " - " + LINES_NAME);

			setSaved(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Error saving file: \n" + e.getLocalizedMessage(),
					"Saving document", JOptionPane.ERROR_MESSAGE);
			newDocument();
		}

	}

	/**
	 * Method called when user chooses "Preferences" in menu or on toolbar.
	 * Shows dialog where user can set parameters
	 */
	public void onPreferences() {
		if (_preferences_dialog == null) {
			_preferences_dialog = new PreferencesDialog(this, this,
					"Lines Preferences", true);
		}
		_preferences_dialog.showDialog();
		repaint();
	}

	/**
	 * Method called when user chooses "About" in menu or on toolbar. Loads text
	 * editor with <tt>FIT_8201_Sviridov_About.txt</tt> open
	 */
	public void onAbout() {
		try {
			if (System.getProperty("os.name").startsWith("Windows"))
				Runtime.getRuntime().exec(
						"notepad.exe FIT_8201_Sviridov_Lines_About.txt");
			else
				Runtime.getRuntime().exec(
						"gedit FIT_8201_Sviridov_Lines_About.txt");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error staring text editor: \n"
					+ e.getLocalizedMessage(), "Exec error",
					JOptionPane.ERROR_MESSAGE);
			newDocument();
		}
	}

	/**
	 * Changes state of the application state machine (drawing or viewing modes)
	 * which enables/disables menu entries and toolbar icons depending on state
	 * 
	 * @param state
	 *            state for state machine to come (one of
	 *            <code>LinesFrane.VIEW_STATE</code>,
	 *            <code>LinesFrane.EDIT_STATE</code>)
	 */
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

	/**
	 * Method called when user clicks with left button on canvas and
	 * corresponding coordinate. Depending on current state either starts new
	 * polyline or continues current
	 * 
	 * @param point
	 *            coordinates of mouse click
	 */

	public void leftClick(Point point) {
		setSaved(false);

		if (_state == LinesFrame.VIEW_STATE) {
			switchState(LinesFrame.EDIT_STATE);
			_new_polyline = new Polyline(_polyline_type, _polyline_thickness,
					_polyline_color);
			_new_polyline.addPoint(point);

			addPolyline(_new_polyline);

			_lines_view.enableRubberLine();
		} else if (_state == LinesFrame.EDIT_STATE) {
			// adding new Polyline point
			_new_polyline.addPoint(point);
		}
	}

	/**
	 * Method called when user clicks with right button on canvas and
	 * corresponding coordinate. If current state is
	 * <code>LidesFrame.EDIT_STATE</code> ends current polyline, otherwise does
	 * nothing
	 * 
	 * @param point
	 *            coordinates of mouse click
	 */
	public void rightClick(Point point) {
		if (_state == LinesFrame.VIEW_STATE) {
			// ignore
		} else if (_state == LinesFrame.EDIT_STATE) {
			switchState(LinesFrame.VIEW_STATE);
			_lines_view.disableRubberLine();
			_new_polyline = null;
		}
	}

	/**
	 * Application main entry point
	 * 
	 * @param args
	 *            command line arguments (unused)
	 */
	public static void main(String args[]) {
		System.setProperty("user.dir", System.getProperty("user.dir") + "/"
				+ "FIT_8201_Sviridov_Lines_Data");

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
