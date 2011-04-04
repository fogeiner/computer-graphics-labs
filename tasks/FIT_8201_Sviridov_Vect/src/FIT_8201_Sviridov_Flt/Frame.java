package FIT_8201_Sviridov_Vect;

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
import java.io.IOException;
import java.util.Arrays;
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
public class Frame extends MainFrame implements FrameService {

    private static final long serialVersionUID = 3501543956694236029L;
    private VectModel vectModel;
    private boolean _modified;

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
            addMenuItem("Edit/Color field mode", "Set field to color mode", KeyEvent.VK_C,
                    "color_arrows.gif", "onColorField");
            addMenuItem("Edit/B&W field mode", "Set field to black and white mode", KeyEvent.VK_B,
                    "bw_arrows.gif", "onBWField");
            addMenuItem("Edit/Plain arrow mode", "Set arrow to plain mode", KeyEvent.VK_P,
                    "plain_arrow.gif", "onPlainArrow");
            addMenuItem("Edit/Filled arrow mode", "Set arrow to filled mode", KeyEvent.VK_F,
                    "filled_arrow.gif", "onFilledArrow");
            addMenuItem("Edit/Zoom in", "Zoom in", KeyEvent.VK_I,
                    "zoom_in.gif", "onZoomIn");
            addMenuItem("Edit/Zoom out", "Zoom out", KeyEvent.VK_O,
                    "zoom_out.gif", "onZoomOut");
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
            addToolBarSeparator();
            addToolBarButton("Edit/Color field mode");
            addToolBarButton("Edit/B&W field mode");
            addToolBarSeparator();
            addToolBarButton("Edit/Plain arrow mode");
            addToolBarButton("Edit/Filled arrow mode");
            addToolBarSeparator();
            addToolBarButton("Edit/Zoom in");
            addToolBarButton("Edit/Zoom out");
            addToolBarSeparator();
            addToolBarButton("Edit/Settings");
            addToolBarSeparator();
            addToolBarButton("Help/About");
            addToolBarSeparator();
            addToolBarButton("File/Exit");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        vectModel = new VectModel(new Region(0.0, 10.0, 0.0, 5.0));
        vectModel.setGrid(new Grid(2, 3));
        vectModel.setColors(Arrays.asList(new Color[]{Color.red, Color.orange, Color.yellow}));
        vectModel.setGridColor(Color.gray);
        vectModel.setLengthMult(0.3);

        vectModel.setNotifyActive(true);

        JPanel mainPanel = new JPanel(new BorderLayout(Settings.PANEL_PADDING, Settings.PANEL_PADDING));
        JPanel outerFieldPanel = new JPanel(new GridBagLayout());


        final VectView vectView = new VectView();
        Statusbar statusbar = new Statusbar();
        LegendPanel legendPanel = new LegendPanel();

        vectView.setBorder(BorderFactory.createLineBorder(Settings.BORDER_COLOR));
        vectView.setBackground(Color.white);

        outerFieldPanel.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                vectView.dispatchEvent(e);
            }
        });
        
        legendPanel.setBorder(BorderFactory.createLineBorder(Settings.BORDER_COLOR));
        legendPanel.setBackground(Color.white);

        outerFieldPanel.setBorder(BorderFactory.createLineBorder(Settings.BORDER_COLOR));
        outerFieldPanel.setBackground(Settings.PANEL_COLOR);
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

        vectView.setVectModel(vectModel);
        legendPanel.setVectModel(vectModel);
        vectModel.addVectListener(vectView);
        vectModel.addVectListener(legendPanel);
        vectModel.notifyListeners();

        vectModel.setGridDrawn(true);
        vectModel.setGrid(new Grid(5, 10));

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

    public void onGrid() {
        if (vectModel != null) {
            vectModel.setGridDrawn(!vectModel.isGridDrawn());
        }
    }

    public void onColorField() {
    }

    public void onBWField() {
    }

    public void onPlainArrow() {
    }

    public void onFilledArrow() {
    }

    public void onZoomIn() {
    }

    public void onZoomOut() {
    }

    public void onSettings() {
    }

    /**
     * Resets application to state with no images loaded and panels blocked
     */
    public void reset() {
        setTitle(Settings.UNTITLED_DOCUMENT);
        setModified(false);
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
            setTitle(file.getName());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(this,
                    "Document is of unknown format", "Loading document",
                    JOptionPane.ERROR_MESSAGE);
            setTitle(Settings.UNTITLED_DOCUMENT);
        } catch (Exception ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(this,
                    "Error loading file: \n" + ex.getLocalizedMessage(),
                    "Loading document", JOptionPane.ERROR_MESSAGE);
            setTitle(Settings.UNTITLED_DOCUMENT);
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
        return _modified;
    }

    @Override
    public void setModified(boolean value) {
        _modified = value;
    }
}
