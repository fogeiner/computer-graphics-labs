package FIT_8201_Sviridov_Lines;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.MenuElement;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import ru.nsu.cg.MainFrame;

public class LinesFrame extends MainFrame {

	private static final long serialVersionUID = 5852264472785688626L;
	private final static int _width = 800;
	private final static int _height = 600;
	private final static String _title = "Lines";

	private final static int VIEW_STATE = 0;
	private final static int EDIT_STATE = 1;
	private int _state = VIEW_STATE;

	List<Polyline> _polylines;

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

			add(new LinesView(this));
		} catch (Exception ex) {
			System.err.print(ex.getStackTrace());
		}
	}

	public void onNew() {

	}

	public void onLoad() {

	}

	public void onSave() {

	}

	public void onExit() {

	}

	public void onPreferences() {

	}

	public void onAbout() {
		JOptionPane.showMessageDialog(this,
				"Lines, version 1.0\n2011 Valentin Sviridov, FIT, group 8201",
				"About Lines", JOptionPane.INFORMATION_MESSAGE);
	}

	private void switchState(int state) {

		String[] elems = new String[] { "File/New", "File/Load",
				"File/Save as...", "Edit/Preferences" };

		// this looks as weird as weird is the implementation of the MainFrame;
		// still works

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
						
						if (!button.getName().equals("Exit") && !button.getName().equals("About")){
							button.setEnabled(false);
							System.out.println(button.getName());
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

		} else if (_state == LinesFrame.EDIT_STATE) {
			// adding new Polyline point
		}
	}

	public void rightClick(Point point) {
		if (_state == LinesFrame.VIEW_STATE) {
			// ignore
		} else if (_state == LinesFrame.EDIT_STATE) {
			// add current polyline to polylines list
			switchState(LinesFrame.VIEW_STATE);
		}
	}

	public static void main(String args[]) {
		// TODO: alter _title with "Untitled"
		LinesFrame lines_frame = new LinesFrame(LinesFrame._width,
				LinesFrame._height, LinesFrame._title);
		lines_frame.setVisible(true);
	}
}
