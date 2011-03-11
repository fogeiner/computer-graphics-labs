package FIT_8201_Sviridov_Weil;

import java.awt.BorderLayout;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ru.nsu.cg.MainFrame;

/**
 * Lines Frame - Lines application main frame
 * 
 * @author alstein
 */
public class WeilFrame extends MainFrame implements FrameService {

    private static final long serialVersionUID = 5852264472785688626L;
    public boolean _is_modified = false;
    private WeilView _weil_view;
    private PreferencesDialog _preferences_dialog = null;

    /**
     * Sets application title to "<code>name</code> - Lines"
     *
     * @param name
     *            first part of application title
     */
    private void setDocumentName(String name) {
        setTitle(name + " - " + WeilSettings.WEIL_NAME);
    }

    /**
     * Constructs application frame with given width and height
     *
     * @param width
     *            width of frame
     * @param height
     *            height of frame
     */
    public WeilFrame(int width, int height) {
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

            addMenuItem("Edit/Subject polygon", "Draw subject polygon",
                    KeyEvent.VK_P, "subject.gif", "onSubject");

            addMenuItem("Edit/Hole polygon", "Draw hole polygon",
                    KeyEvent.VK_H, "hole.gif", "onHole");

            addMenuItem("Edit/Clip polygon", "Draw clip polygon",
                    KeyEvent.VK_C, "clip.gif", "onClip");

            addMenuItem("Edit/Intersect", "Get intersection", KeyEvent.VK_I,
                    "intersect.gif", "onIntersect");

            addMenuItem("Edit/Settings", "Change settings", KeyEvent.VK_S,
                    "preferences.gif", "onPreferences");

            addSubMenu("Help", KeyEvent.VK_H);

            addMenuItem("Help/About",
                    "View application version and author information",
                    KeyEvent.VK_A, "about.gif", "onAbout");

            // constructing Toolbar
            addToolBarButton("File/New");
            addToolBarButton("File/Load");
            addToolBarButton("File/Save as...");
            addToolBarSeparator();


            addToolBarButton("Edit/Subject polygon");
            addToolBarButton("Edit/Hole polygon");
            addToolBarButton("Edit/Clip polygon");
            addToolBarButton("Edit/Intersect");
            addToolBarSeparator();
            addToolBarButton("Edit/Settings");
            addToolBarSeparator();
            addToolBarButton("Help/About");
            addToolBarSeparator();
            addToolBarButton("File/Exit");

            toolBar.setFloatable(false);

            JPanel panel = new JPanel(new BorderLayout());
            _weil_view = new WeilView(this);
            JScrollPane scrollPane = new JScrollPane(_weil_view);
            panel.add(scrollPane, BorderLayout.CENTER);

            add(panel);

            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    onExit();
                }
            });

            setDocumentName(WeilSettings.UNTITLED_DOCUMENT);
            setModified(false);
            setIntersectBlocked(true);
            _weil_view.reset();
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
     * Method called when user chooses "Draw subject polygon" in menu or on
     * toolbar.
     */
    public void onSubject() {
        _weil_view.onSubject();
    }

    /**
     * Method called when user chooses "Draw hole polygon" in menu or on
     * toolbar.
     */
    public void onHole() {
        _weil_view.onHole();
    }

    /**
     * Method called when user chooses "Draw clip polygon" in menu or on
     * toolbar.
     */
    public void onClip() {
        _weil_view.onClip();
    }

    /**
     * Method called when user chooses "Intersect" in menu or on toolbar.
     */
    public void onIntersect() {
        _weil_view.onIntersect();
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

        setDocumentName(WeilSettings.UNTITLED_DOCUMENT);
        setModified(false);
        _weil_view.reset();
    }

    /**
     * Method called when user chooses "Exit" in menu or on toolbar. Asks user
     * to save current document (if needed) and terminates application
     */
    public void onExit() {
        if (_weil_view.getState() == WeilView.EDIT_STATE) {
            int answer = JOptionPane.showConfirmDialog(
                    WeilFrame.this,
                    "Document cannot be saved until you finish editing it.\nDo you want to continue editing?",
                    "Saving file", JOptionPane.YES_NO_CANCEL_OPTION);

            if (answer == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
            return;
        }


        if (isModified() == true) {
            // dirty way to find out if model is finished or not
            if (toolBar.getComponent(2).isEnabled() == false) {
                int answer = JOptionPane.showConfirmDialog(
                        WeilFrame.this,
                        "Document cannot be saved until you finish the model.\nDo you want to continue editing?",
                        "Saving file", JOptionPane.YES_NO_CANCEL_OPTION);

                if (answer == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
                return;
            }


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
            _preferences_dialog = new PreferencesDialog(_weil_view, this,
                    "Settings", true);
        }
        _preferences_dialog.showDialog();
    }

    /**
     * Method called when user chooses "About" in menu or on toolbar. Loads text
     * editor with <tt>FIT_8201_Sviridov_About.txt</tt> open
     */
    public void onAbout() {
        String path = WeilSettings.ABOUT_FILE;

        File f = new File(path);
        if (f.exists() == false) {
            path = ".." + System.getProperties().getProperty("file.separator") + WeilSettings.ABOUT_FILE;
            f = new File(path);
            if (f.exists() == false) {
                JOptionPane.showMessageDialog(this, "File " + WeilSettings.ABOUT_FILE + " could not be found neither in application directory nor in the preceding directory.", "Opening about file",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        try {
            String editor;
            if (System.getProperty("os.name").startsWith("Windows")) {
                editor = "notepad.exe";
            } else {
                editor = "gedit";
            }

            Runtime.getRuntime().exec(editor + " " + path);
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
            _weil_view.reset();
            WeilPersistenceManager.loadFromFile(file, _weil_view);
            setDocumentName(file.getName());
            setModified(false);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Document is of unknown format", "Loading document",
                    JOptionPane.ERROR_MESSAGE);
            setDocumentName(WeilSettings.UNTITLED_DOCUMENT);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading file: \n" + e.getLocalizedMessage(),
                    "Loading document", JOptionPane.ERROR_MESSAGE);
            setDocumentName(WeilSettings.UNTITLED_DOCUMENT);
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
                int answer = JOptionPane.showConfirmDialog(
                        this,
                        "File already exists. Are you sure you want to overwrite it?",
                        "Saving file", JOptionPane.YES_NO_CANCEL_OPTION);

                if (answer != JOptionPane.OK_OPTION) {
                    return;
                }
            }

            WeilPersistenceManager.saveToFile(file, _weil_view);

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

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                WeilFrame lines_frame = new WeilFrame(WeilSettings.FRAME_WIDTH,
                        WeilSettings.FRAME_HEIGHT);
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
            if (i != 11 && i != 13) {
                toolBar.getComponent(i).setEnabled(!value);
            }
        }
    }

    @Override
    public void setIntersectBlocked(boolean value) {
        JMenuBar menu_bar = getJMenuBar();
        JMenu edit = (JMenu) menu_bar.getComponent(1);
        edit.getMenuComponent(3).setEnabled(!value);
        toolBar.getComponent(7).setEnabled(!value);

        JMenu file = (JMenu) menu_bar.getComponent(0);
        file.getMenuComponent(2).setEnabled(!value);
        toolBar.getComponent(2).setEnabled(!value);
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
