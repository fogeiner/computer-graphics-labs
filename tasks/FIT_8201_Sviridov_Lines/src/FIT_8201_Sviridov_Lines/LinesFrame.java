package FIT_8201_Sviridov_Lines;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import ru.nsu.cg.MainFrame;

/**
 * Lines Frame - Lines application main frame
 * 
 * @author alstein
 */
public class LinesFrame extends MainFrame implements FrameService {

	private static final long serialVersionUID = 5852264472785688626L;
	public boolean _is_modified = false;
	private LinesView _lines_view;
	private PreferencesDialog _preferences_dialog = null;

	/**
	 * Sets application title to "<code>name</code> - Lines"
	 * 
	 * @param name
	 *            first part of application title
	 */

	private void setDocumentName(String name) {
		setTitle(name + " - " + PolylineSettings.LINES_NAME);
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

			addToolBarSeparator();
			addToolBarButton("Edit/Preferences");
			addToolBarSeparator();
			addToolBarButton("Help/About");
			addToolBarSeparator();
			addToolBarButton("File/Exit");

			toolBar.setFloatable(false);

			_lines_view = new LinesView(this);
			add(_lines_view);

			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					if (isModified() == true) {
						switch (showSaveMessage()) {
						case JOptionPane.OK_OPTION:
							onSave();

							// still not saved
							if (isModified()) {
								return;
							}
							break;
						case JOptionPane.CLOSED_OPTION:
						case JOptionPane.CANCEL_OPTION:
							return;
						case JOptionPane.NO_OPTION:
						}
					}

					System.exit(0);
				}
			});

			setDocumentName(PolylineSettings.UNTITLED_DOCUMENT);
			setModified(false);
			_lines_view.reset();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Shows message to find out if user wills to save current document
	 * 
	 * @return <code>true</code> if user confirms, <code>false</code> otherwise
	 */

	private int showSaveMessage() {
		int answer = JOptionPane.showConfirmDialog(this,
				"The document was modified. Save?");
		return answer;
	}

	/**
	 * Method called when user chooses "New" in menu or on toolbar. Asks user to
	 * save current document (if needed) and sets application to new document
	 * state
	 */

	public void onNew() {

		if (isModified() == true) {
			switch (showSaveMessage()) {
			case JOptionPane.OK_OPTION:
				onSave();

				// still not saved
				if (isModified()) {
					return;
				}
				break;
			case JOptionPane.CLOSED_OPTION:
			case JOptionPane.CANCEL_OPTION:
				return;
			case JOptionPane.NO_OPTION:
			}
		}

		setDocumentName(PolylineSettings.UNTITLED_DOCUMENT);
		setModified(false);
		_lines_view.reset();
	}

	/**
	 * Method called when user chooses "Exit" in menu or on toolbar. Asks user
	 * to save current document (if needed) and terminates application
	 */

	public void onExit() {
		if (isModified() == true) {
			switch (showSaveMessage()) {
			case JOptionPane.OK_OPTION:
				onSave();

				// still not saved
				if (isModified()) {
					return;
				}
				break;
			case JOptionPane.CLOSED_OPTION:
			case JOptionPane.CANCEL_OPTION:
				return;
			case JOptionPane.NO_OPTION:
			}
		}

		System.exit(0);
	}

	/**
	 * Method called when user chooses "Preferences" in menu or on toolbar.
	 * Shows dialog where user can set parameters
	 */
	public void onPreferences() {
		if (_preferences_dialog == null) {
			_preferences_dialog = new PreferencesDialog(_lines_view, this,
					"Lines Preferences", true);
		}
		_preferences_dialog.showDialog();
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
		}
	}

	/**
	 * Method called when user chooses "Load" in menu or on toolbar. Asks user
	 * to save current document (if needed), shows dialog to choose file and
	 * loads document from it
	 */
	public void onLoad() {
		if (isModified() == true) {
			switch (showSaveMessage()) {
			case JOptionPane.OK_OPTION:
				onSave();

				// still not saved
				if (isModified()) {
					return;
				}
				break;
			case JOptionPane.CLOSED_OPTION:
			case JOptionPane.CANCEL_OPTION:
				return;
			case JOptionPane.NO_OPTION:
			}
		}
		try {
			File file = getOpenFileName("txt", "Text files");
			if (file == null) {
				return;
			}
			_lines_view.reset();
			PolylinePersistenceManager.loadFromFile(file, _lines_view);
			setDocumentName(file.getName());
			setModified(false);
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this,
					"Document is of unknown format", "Loading document",
					JOptionPane.ERROR_MESSAGE);
			setDocumentName(PolylineSettings.UNTITLED_DOCUMENT);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Error loading file: \n" + e.getLocalizedMessage(),
					"Loading document", JOptionPane.ERROR_MESSAGE);
			setDocumentName(PolylineSettings.UNTITLED_DOCUMENT);
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

			if (file.exists()) {
				int answer = JOptionPane
						.showConfirmDialog(
								this,
								"File already exists. Are you sure you want to overwrite it?",
								"Saving file", JOptionPane.YES_NO_CANCEL_OPTION);

				if (answer != JOptionPane.OK_OPTION)
					return;
			}

			PolylinePersistenceManager.saveToFile(file, _lines_view);

			setModified(false);
			setDocumentName(file.getName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Error saving file: \n" + e.getLocalizedMessage(),
					"Saving document", JOptionPane.ERROR_MESSAGE);
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
				LinesFrame lines_frame = new LinesFrame(
						PolylineSettings.FRAME_WIDTH,
						PolylineSettings.FRAME_HEIGHT);
				lines_frame.setVisible(true);
			}
		});
	}

	/**
	 * Method blocks/unblocks menu items and toolbar icons: New, Save, Load,
	 * Preferences
	 * 
	 * @param value
	 *            <code>true</code> to block, <code>false</code> to unblock
	 */
	@Override
	public void setBlocked(boolean value) {
		JMenuBar menu_bar = getJMenuBar();

		JMenu file = (JMenu) menu_bar.getComponent(0);
		JMenu edit = (JMenu) menu_bar.getComponent(1);

		for (int i = 0; i < file.getMenuComponentCount() - 1; ++i) {
			file.getMenuComponent(i).setEnabled(!value);
		}

		for (int i = 0; i < edit.getMenuComponentCount(); ++i) {
			edit.getMenuComponent(i).setEnabled(!value);
		}

		for (int i = 0; i < toolBar.getComponentCount(); ++i) {
			if (i != 6 && i != 8)
				toolBar.getComponent(i).setEnabled(!value);
		}
	}

	@Override
	public void setModified(boolean value) {
		_is_modified = value;
	}

	@Override
	public boolean isModified() {
		return _is_modified;
	}
}
