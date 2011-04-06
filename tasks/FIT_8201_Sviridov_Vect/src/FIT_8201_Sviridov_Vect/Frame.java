package FIT_8201_Sviridov_Vect;

import FIT_8201_Sviridov_Vect.state_history.StateHistoryListener;
import FIT_8201_Sviridov_Vect.state_history.StateHistoryModel;
import FIT_8201_Sviridov_Vect.utils.Region;
import FIT_8201_Sviridov_Vect.statusbar.StatusbarModel;
import FIT_8201_Sviridov_Vect.ui.LegendPanel;
import FIT_8201_Sviridov_Vect.ui.Statusbar;
import FIT_8201_Sviridov_Vect.ui.VectView;
import FIT_8201_Sviridov_Vect.vect.VectListener;
import FIT_8201_Sviridov_Vect.vect.VectModel;
import FIT_8201_Sviridov_Vect.vect.VectPersistence;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ru.nsu.cg.MainFrame;

/**
 * Frame - Application main frame
 * 
 * @author alstein
 */
public final class Frame extends MainFrame implements FrameService, StateHistoryListener, VectListener {

    private static final long serialVersionUID = 3501543956694236029L;
    private Color toggleColor = Color.lightGray;
    private StateHistoryModel<Region> regionsHistoryModel = new StateHistoryModel<Region>();
    private StatusbarModel statusbarModel = new StatusbarModel();
    private Statusbar statusbar = new Statusbar();
    private LegendPanel legendPanel = new LegendPanel();
    private VectView vectView = new VectView();
    private VectModel vectModel;
    private boolean modified;

    /**
     * Sets application title to "<code>name</code> - App name"
     *
     * @param name
     *            first part of application title
     */
    public void setTitle(String name) {
        super.setTitle(name + " - " + Settings.NAME);
    }

    /**
     * Constructs application frame with given width and height
     *
     * @param width
     *            width of frame
     * @param height
     *            height of frame
     */
    public Frame(int width, int height) {
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
            addMenuItem("Edit/Grid", "Show/hide grid", KeyEvent.VK_G,
                    "grid.gif", "onGrid");
            addMenuItem("Edit/Chess mode", "Switch vectors chess order", KeyEvent.VK_K,
                    "checker.gif", "onChess");
            addMenuItem("Edit/Switch field mode", "Invert field mode (B&W vs color)", KeyEvent.VK_B,
                    "bw_arrows.gif", "onBWField");
            addMenuItem("Edit/Switch arrow mode", "Invert arrow mode (plain vs filled)", KeyEvent.VK_F,
                    "filled_arrow.gif", "onFilledArrow");
            addMenuItem("Edit/Previous zoom level", "Previous zoom state", KeyEvent.VK_O,
                    "zoom_previous.gif", "onZoomPrevious");
            addMenuItem("Edit/Next zoom level", "Next zoom state", KeyEvent.VK_I,
                    "zoom_next.gif", "onZoomNext");
            addMenuItem("Edit/Settings", "Show settings dialog", KeyEvent.VK_S,
                    "settings.gif", "onSettings");
            addSubMenu("Help", KeyEvent.VK_H);

            addMenuItem("Help/About",
                    "View application version and author information",
                    KeyEvent.VK_A, "about.gif", "onAbout");
            // constructing Toolbar
            addToolBarButton("File/New");
            addToolBarButton("File/Load");
            addToolBarButton("File/Save as...");
            addToolBarSeparator();
            addToolBarButton("Edit/Grid");
            addToolBarButton("Edit/Chess mode");
            addToolBarSeparator();
            addToolBarButton("Edit/Switch field mode");

            addToolBarSeparator();
            addToolBarButton("Edit/Switch arrow mode");
            addToolBarSeparator();
            addToolBarButton("Edit/Previous zoom level");
            addToolBarButton("Edit/Next zoom level");
            addToolBarSeparator();
            addToolBarButton("Edit/Settings");
            addToolBarSeparator();
            addToolBarButton("Help/About");
            addToolBarSeparator();
            addToolBarButton("File/Exit");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        final JPanel mainPanel = new JPanel(new BorderLayout(Settings.PANEL_PADDING, Settings.PANEL_PADDING));
        final JPanel outerFieldPanel = new JPanel(new GridBagLayout());
        outerFieldPanel.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                vectView.dispatchEvent(e);
            }
        });

        mainPanel.setBackground(Color.white);
        vectView.setBackground(Color.white);
        legendPanel.setBackground(Color.white);
        outerFieldPanel.setBackground(Color.white);

        outerFieldPanel.add(vectView, new GridBagConstraints());

        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                Settings.PANEL_PADDING, Settings.PANEL_PADDING,
                Settings.PANEL_PADDING, Settings.PANEL_PADDING));
        mainPanel.setBackground(Color.white);
        mainPanel.add(outerFieldPanel, BorderLayout.CENTER);
        mainPanel.add(legendPanel, BorderLayout.EAST);


        this.add(mainPanel, BorderLayout.CENTER);
        this.add(statusbar, BorderLayout.SOUTH);


        toolBar.setFloatable(false);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });


        reset();

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
            File file = getOpenFileName("txt", "plain text file");
            if (file == null) {
                return;
            }

            vectModel = VectPersistence.loadFromFile(file);
            regionsHistoryModel.add(vectModel.getRegion());

            statusbar.setStatusbarModel(statusbarModel);
            vectView.setStatusbarModel(statusbarModel);
            vectView.setVectModel(vectModel);
            vectView.setRegionsHistory(regionsHistoryModel);

            legendPanel.setVectModel(vectModel);

            vectModel.addVectListener(vectView);
            vectModel.addVectListener(legendPanel);
            vectModel.addVectListener(this);
            statusbarModel.addStatusbarListener(statusbar);
            regionsHistoryModel.addListener(this);

            setBlockedAll(false);
            setZoomNextBlocked(true);
            setZoomPreviousBlocked(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(this,
                    "Document is of unknown format", "Loading document",
                    JOptionPane.ERROR_MESSAGE);
            setTitle(Settings.UNTITLED_DOCUMENT);
        } /*catch (Exception ex) {
        System.out.println(ex);
        JOptionPane.showMessageDialog(this,
        "Error loading file: \n" + ex.getLocalizedMessage(),
        "Loading document", JOptionPane.ERROR_MESSAGE);
        setTitle(Settings.UNTITLED_DOCUMENT);
        }*/
    }

    public void onChess() {
        vectModel.setChessMode(!vectModel.isChessMode());
    }

    public void onGrid() {
        vectModel.setGridDrawn(!vectModel.isGridDrawn());

    }

    public void onBWField() {
        vectModel.setFieldColor(!vectModel.isFieldColor());
    }

    public void onFilledArrow() {
        vectModel.setArrowPlain(!vectModel.isArrowPlain());
    }

    public void onSettings() {
    }

    /**
     * Resets application to state with no images loaded and panels blocked
     */
    public void reset() {
        setTitle(Settings.UNTITLED_DOCUMENT);

        if (vectModel != null) {
            vectModel.clearListeners();
        }
        statusbarModel.clearListeners();
        regionsHistoryModel.clearListeners();

        vectView.setVectModel(null);
        vectView.setStatusbarModel(null);
        vectView.setRegionsHistory(null);
        statusbar.setStatusbarModel(null);
        legendPanel.setVectModel(null);

        setBlockedAll(true);
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

        reset();
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
     * Method called when user chooses "About" in menu or on toolbar. Loads text
     * editor with <tt>FIT_8201_Sviridov_About.txt</tt> open
     */
    public void onAbout() {
        String path = Settings.ABOUT_FILE;
        File f = new File(path);
        if (f.exists() == false) {
            path = ".." + System.getProperties().getProperty("file.separator")
                    + Settings.ABOUT_FILE;
            f = new File(path);
            if (f.exists() == false) {
                JOptionPane.showMessageDialog(
                        this,
                        "File "
                        + Settings.ABOUT_FILE
                        + " could not be found neither in the application directory nor in the preceding directory.",
                        "Opening about file", JOptionPane.ERROR_MESSAGE);
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
     * Method called when user chooses "Save" in menu or on toolbar. Shows
     * dialog to choose/create file and saves document to it
     */
    public void onSave() {

        try {
            File file = getSaveFileName("txt", "plain text file");
            if (file == null) {
                return;
            }
            if (file.exists()) {
                int answer = JOptionPane.showConfirmDialog(
                        this,
                        "File already exists. Are you sure you want to overwrite it?",
                        "Select an Option",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (answer == JOptionPane.NO_OPTION) {
                    onSave();
                    return;
                }
                if (answer != JOptionPane.OK_OPTION) {
                    return;
                }
            }
            VectPersistence.saveToFile(vectModel, file);
            setTitle(file.getName());
            setModified(false);
        } catch (Exception e) {
            System.out.println(e);
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
                Frame frame = new Frame(Settings.FRAME_WIDTH,
                        Settings.FRAME_HEIGHT);
                frame.setVisible(true);
                frame.setSize(800, 600);
            }
        });
    }

    @Override
    public void setSaveBlocked(boolean value) {
        JMenuBar menu_bar = getJMenuBar();
        JMenu file = (JMenu) menu_bar.getComponent(0);
        file.getMenuComponent(2).setEnabled(!value);
        toolBar.getComponent(2).setEnabled(!value);
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean value) {
        modified = value;
    }

    public void setBlockedAll(boolean blocked) {
        JMenuBar menu_bar = getJMenuBar();
        JMenu file = (JMenu) menu_bar.getComponent(0);
        JMenu edit = (JMenu) menu_bar.getComponent(1);
        file.getMenuComponent(2).setEnabled(!blocked);
        for (int i = 0; i < 7; ++i) {
            edit.getMenuComponent(i).setEnabled(!blocked);
        }
        for (int i = 2; i < 15; ++i) {
            if (i != 3 && i != 6 && i != 8 && i != 10 && i != 13) {
                toolBar.getComponent(i).setEnabled(!blocked);
            }
        }
    }

    public void onZoomPrevious() {
        vectModel.setRegion(regionsHistoryModel.prev());
        setZoomPreviousBlocked(!regionsHistoryModel.hasPrev());
        setZoomNextBlocked(!regionsHistoryModel.hasNext());
    }

    public void onZoomNext() {
        vectModel.setRegion(regionsHistoryModel.next());
        setZoomPreviousBlocked(!regionsHistoryModel.hasPrev());
        setZoomNextBlocked(!regionsHistoryModel.hasNext());
    }

    public void setZoomPreviousBlocked(boolean blocked) {
        JMenuBar menu_bar = getJMenuBar();
        JMenu edit = (JMenu) menu_bar.getComponent(1);
        edit.getMenuComponent(4).setEnabled(!blocked);
        toolBar.getComponent(11).setEnabled(!blocked);
    }

    public void setZoomNextBlocked(boolean blocked) {
        JMenuBar menu_bar = getJMenuBar();
        JMenu edit = (JMenu) menu_bar.getComponent(1);
        edit.getMenuComponent(5).setEnabled(!blocked);
        toolBar.getComponent(12).setEnabled(!blocked);
    }

    public void setVectModel(VectModel vectModel) {
        this.vectModel = vectModel;
    }

    @Override
    public void historyStateChanged() {
        setZoomPreviousBlocked(!regionsHistoryModel.hasPrev());
        setZoomNextBlocked(!regionsHistoryModel.hasNext());
    }

    @Override
    public void modelChanged() {
    }

    @Override
    public void regionChanged() {
    }

    @Override
    public void lengthMultChanged() {
    }

    @Override
    public void gridChanged() {
    }

    @Override
    public void gridColorChanged() {
    }

    @Override
    public void colorsChanged() {
    }

    @Override
    public void fieldModeChanged() {
        Color c = null;
        if (!vectModel.isFieldColor()) {
            c = toggleColor;
        }
        toolBar.getComponent(7).setBackground(c);
    }

    @Override
    public void gridDrawnChanged() {
        Color c = null;
        if (vectModel.isGridDrawn()) {
            c = toggleColor;
        }
        toolBar.getComponent(4).setBackground(c);
    }

    @Override
    public void arrowModeChanged() {

        Color c = null;
        if (!vectModel.isArrowPlain()) {
            c = toggleColor;
        }
        toolBar.getComponent(9).setBackground(c);
    }

    @Override
    public void chessModeChanged() {
        Color c = null;
        if (vectModel.isChessMode()) {
            c = toggleColor;
        }
        toolBar.getComponent(5).setBackground(c);
    }
}
