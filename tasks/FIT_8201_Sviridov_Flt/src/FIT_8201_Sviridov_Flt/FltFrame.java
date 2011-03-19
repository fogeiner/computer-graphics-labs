package FIT_8201_Sviridov_Flt;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import ru.nsu.cg.MainFrame;

/**
 * Frame - Application main frame
 * 
 * @author alstein
 */
public class FltFrame extends MainFrame implements FltFrameService {

    private static final long serialVersionUID = -5961249108882455789L;
    private ImageNavigationViewerPanel _zone_b = new ImageNavigationViewerPanel("Zone B", this);
    private ImageNavigationPanel _zone_a = new ImageNavigationPanel("Zone A", _zone_b, this);
    private ImagePanel _zone_c = new ImageResultPanel("Zone C", this);
    private ImagePanel _zones[] = new ImagePanel[]{_zone_a, _zone_b, _zone_c};

    /**
     * Sets application title to "<code>name</code> - App name"
     *
     * @param name
     *            first part of application title
     */
    private void setDocumentName(String name) {
        setTitle(name + " - " + FltSettings.FLT_NAME);
    }

    /**
     * Constructs application frame with given width and height
     *
     * @param width
     *            width of frame
     * @param height
     *            height of frame
     */
    public FltFrame(int width, int height) {
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
            addMenuItem("Edit/Select", "Select region", KeyEvent.VK_S, "select.gif", "onSelect");
            addMenuItem("Edit/From C to B", "Copy image fron zone C to zone B",
                    KeyEvent.VK_C, "back.gif", "onFromCtoB");

            addMenuItem("Edit/Grayscale", "Apply black&white (grayscale) filter",
                    KeyEvent.VK_G, "grayscale.gif", "onGrayscale");

            addMenuItem("Edit/Negative", "Apply negative filter",
                    KeyEvent.VK_N, "negative.gif", "onNegative");

            addMenuItem("Edit/Floyd-Steinberg dithering", "Apply Floyd-Steinberg dithering filter",
                    KeyEvent.VK_F, "fs_dithering.gif", "onFloydSteinbergDithering");

            addMenuItem("Edit/Orderted dithering", "Apply ordered dithering filter",
                    KeyEvent.VK_O, "o_dithering.gif", "onOrderedDithering");

            addMenuItem("Edit/Double scale", "Apply double scale filter",
                    KeyEvent.VK_D, "scale.gif", "onDoubleScale");

            addMenuItem("Edit/Roberts edge detecting", "Apply Roberts operator edge detection filter",
                    KeyEvent.VK_R, "roberts.gif", "onRoberts");

            addMenuItem("Edit/Sobel edge detecting", "Apply Sobel operator edge detection filter",
                    KeyEvent.VK_L, "sobel.gif", "onSobel");

            addMenuItem("Edit/Blur", "Apply blur filter",
                    KeyEvent.VK_B, "blur.gif", "onBlur");

            addMenuItem("Edit/Sharpen", "Apply sharpen filter",
                    KeyEvent.VK_S, "sharpen.gif", "onSharpen");

            addMenuItem("Edit/Emboss", "Apply emboss filter",
                    KeyEvent.VK_E, "emboss.gif", "onEmboss");


            addMenuItem("Edit/Aquarelle", "Apply aquarelle filter",
                    KeyEvent.VK_A, "aquarelle.gif", "onAquarelle");

            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About",
                    "View application version and author information",
                    KeyEvent.VK_A, "about.gif", "onAbout");
            // constructing Toolbar
            addToolBarButton("File/New");
            addToolBarButton("File/Load");
            addToolBarButton("File/Save as...");
            addToolBarSeparator();
            addToolBarButton("Edit/Select");
            addToolBarButton("Edit/From C to B");
            addToolBarSeparator();


            addToolBarButton("Edit/Grayscale");
            addToolBarButton("Edit/Negative");
            addToolBarSeparator();
            addToolBarButton("Edit/Floyd-Steinberg dithering");
            addToolBarButton("Edit/Orderted dithering");
            addToolBarSeparator();
            addToolBarButton("Edit/Double scale");
            addToolBarSeparator();
            addToolBarButton("Edit/Roberts edge detecting");
            addToolBarButton("Edit/Sobel edge detecting");
            addToolBarSeparator();
            addToolBarButton("Edit/Blur");
            addToolBarButton("Edit/Sharpen");
            addToolBarSeparator();
            addToolBarButton("Edit/Emboss");
            addToolBarButton("Edit/Aquarelle");

            addToolBarSeparator();
            addToolBarButton("Help/About");
            addToolBarSeparator();
            addToolBarButton("File/Exit");
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        toolBar.setFloatable(false);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBorder(new EmptyBorder(FltSettings.PANEL_PADDING, 0, FltSettings.PANEL_PADDING, 0));
        for (JPanel p : _zones) {
            JPanel outer = new JPanel();
            outer.setBorder(new MatteBorder(new Insets(1, 1, 1, 1), Color.black));
            p.setBorder(new EmptyBorder(new Insets(1, 1, 1, 1)));
            outer.add(p);
            panel.add(outer);
        }
        JScrollPane scroll_pane = new JScrollPane(panel);
        add(scroll_pane);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });


        _zone_a.setFrameService(this);
        pack();
        reset();
    }

    public void reset() {
        setDocumentName(FltSettings.UNTITLED_DOCUMENT);
        setSelectBlocked(true);
        setSaveBlocked(true);
        setFromCtoBBlocked(true);
        setFiltersBlocked(true);
        for (ImagePanel p : _zones) {
            p.setImage(null);
        }
        setSelectBlocked(true);
        setDocumentName(FltSettings.UNTITLED_DOCUMENT);
    }

    public void onFloydSteinbergDithering() {
        BufferedImage o = _zone_b.getImage();
        _zone_c.setImage(Filters.getFloydSteinbergDitheredImage(o, 2, 2, 2));
    }

    public void onDoubleScale() {
        BufferedImage o = _zone_b.getImage();
        _zone_c.setImage(Filters.getDoubleScaleImage(o));
    }

    public void onOrderedDithering() {
        BufferedImage o = _zone_b.getImage();
        _zone_c.setImage(Filters.getOrderedDitherImage(o));
    }

    public void onAquarelle() {
        BufferedImage o = _zone_b.getImage();
        BufferedImage csi = Filters.getColorSmoothedImage(o, 5);
        BufferedImage si = Filters.getSharpen5Image(csi);
        _zone_c.setImage(si);
    }
    private static double _S = 50.0;

    public void onSobel() {
        BufferedImage o = _zone_b.getImage();
        _zone_c.setImage(Filters.getSobelImage(o, _S));
    }
    private double _R = 20.0;

    public void onRoberts() {
        BufferedImage o = _zone_b.getImage();
        _zone_c.setImage(Filters.getRobertsImage(o, _R));
    }

    public void onBlur() {
        BufferedImage o = _zone_b.getImage();
        _zone_c.setImage(Filters.getBlurImage(o));
    }

    public void onEmboss() {
        BufferedImage o = _zone_b.getImage();
        _zone_c.setImage(Filters.getEmbossImage(o));
    }

    public void onSharpen() {
        BufferedImage o = _zone_b.getImage();
        _zone_c.setImage(Filters.getSharpen3Image(o));
    }

    public void onNegative() {
        BufferedImage o = _zone_b.getImage();
        _zone_c.setImage(Filters.getNegativeImage(o));
    }

    public void onGrayscale() {
        BufferedImage o = _zone_b.getImage();
        _zone_c.setImage(Filters.getGreyscaleImage(o));
    }

    /**
     * Method is invoked after user presses Back toolbar buttons or chooses
     * Edit -> From C to B
     */
    public void onFromCtoB() {
        BufferedImage img = _zone_c.getImage();
        _zone_b.setImage(img.getSubimage(0, 0, img.getWidth(), img.getHeight()));
    }

    public void onSelect() {
        setSelectBlocked(true);
        _zone_a.startSelecting();
    }

    /**
     * Method called when user chooses "New" in menu or on toolbar. Asks user to
     * save current document (if needed) and sets application to new document
     * state
     */
    public void onNew() {
        reset();
    }

    /**
     * Method called when user chooses "Exit" in menu or on toolbar. Asks user
     * to save current document (if needed) and terminates application
     */
    public void onExit() {
        System.exit(0);
    }

    /**
     * Method called when user chooses "About" in menu or on toolbar. Loads text
     * editor with <tt>FIT_8201_Sviridov_About.txt</tt> open
     */
    public void onAbout() {
        String path = FltSettings.ABOUT_FILE;
        File f = new File(path);
        if (f.exists() == false) {
            path = ".." + System.getProperties().getProperty("file.separator")
                    + FltSettings.ABOUT_FILE;
            f = new File(path);
            if (f.exists() == false) {
                JOptionPane.showMessageDialog(
                        this,
                        "File "
                        + FltSettings.ABOUT_FILE
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
        try {
            File file = getOpenFileName("bmp", "24-bit color bitmap pictures");
            if (file == null) {
                return;
            }
            _zone_a.setImage(BmpImage.readBmpImage(file));
            _zone_b.setImage(null);
            _zone_c.setImage(null);
            setSelectBlocked(
                    false);
            setDocumentName(
                    file.getName());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(this,
                    "Document is of unknown format", "Loading document",
                    JOptionPane.ERROR_MESSAGE);
            setDocumentName(
                    FltSettings.UNTITLED_DOCUMENT);
        } catch (Exception ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(this,
                    "Error loading file: \n" + ex.getLocalizedMessage(),
                    "Loading document", JOptionPane.ERROR_MESSAGE);
            setDocumentName(
                    FltSettings.UNTITLED_DOCUMENT);
        }
    }

    /**
     * Method called when user chooses "Save" in menu or on toolbar. Shows
     * dialog to choose/create file and saves document to it
     */
    public void onSave() {
        try {
            File file = getSaveFileName("bmp", "24-bit color bitmap pictures");
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
            BmpImage.writeBmpImage(_zone_c.getImage(), file);
            setDocumentName(
                    file.getName());
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
                FltFrame frame = new FltFrame(FltSettings.FRAME_WIDTH,
                        FltSettings.FRAME_HEIGHT);
                frame.setVisible(true);
            }
        });
    }

    @Override
    public void setSelectBlocked(boolean value) {
        JMenuBar menu_bar = getJMenuBar();
        JMenu edit = (JMenu) menu_bar.getComponent(1);
        edit.getMenuComponent(0).setEnabled(!value);
        toolBar.getComponent(4).setEnabled(!value);
    }

    @Override
    public void setFiltersBlocked(boolean value) {
        JMenuBar menu_bar = getJMenuBar();
        JMenu edit = (JMenu) menu_bar.getComponent(1);
        for (int i = 2; i < 13; ++i) {
            edit.getMenuComponent(i).setEnabled(!value);
        }

        for (int i = 7; i < 23; ++i) {
            if (i != 9 && i != 12 && i != 14 && i != 17 && i != 20) {
                toolBar.getComponent(i).setEnabled(!value);
            }
        }

    }

    @Override
    public void setSaveBlocked(boolean value) {
        JMenuBar menu_bar = getJMenuBar();
        JMenu file = (JMenu) menu_bar.getComponent(0);
        file.getMenuComponent(2).setEnabled(!value);
        toolBar.getComponent(2).setEnabled(!value);
    }

    @Override
    public void setFromCtoBBlocked(boolean value) {
        JMenuBar menu_bar = getJMenuBar();
        JMenu edit = (JMenu) menu_bar.getComponent(1);
        edit.getMenuComponent(1).setEnabled(!value);
        toolBar.getComponent(5).setEnabled(!value);
    }
}
