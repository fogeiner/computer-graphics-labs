package FIT_8201_Sviridov_Flt;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import ru.nsu.cg.MainFrame;

/**
 * Frame - Application main frame
 * 
 * @author alstein
 */
public class FltFrame extends MainFrame implements FltFrameService {

    private ImageNavigationViewerPanel _zone_b = new ImageNavigationViewerPanel("Zone B");
    private ImageNavigationPanel _zone_a = new ImageNavigationPanel("Zone A", _zone_b);
    private ImagePanel _zone_c = new ImagePanel("Zone C");
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
            panel.add(p);
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
        setDocumentName(FltSettings.UNTITLED_DOCUMENT);
        pack();

        _zone_a.setFrameService(this);
        setSelectBlocked(true);
        //setSaveBlocked(true);
        //setFromCtoBBlocked(true);

        _zone_b.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                onEmboss();
            }
        });
    }

    private BufferedImage applyConvolutionMatrix(BufferedImage o, int m[][], int r_offset, int g_offset, int b_offset) {

        int width = o.getWidth(), height = o.getHeight();
        BufferedImage n = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < width; ++w) {
                int R = 0, G = 0, B = 0;

                for (int i = 0; i < m.length; ++i) {
                    for (int j = 0; j < m[0].length; ++j) {
                        int r = 0, g = 0, b = 0;

                        int k = h + i - 1;
                        int l = w + j - 1;

                        if (k >= 0 && k < height && l >= 0 && l < width) {
                            Color c = new Color(o.getRGB(l, k));
                            r = c.getRed();
                            g = c.getGreen();
                            b = c.getBlue();
                        }

                        R += m[i][j] * r;
                        G += m[i][j] * g;
                        B += m[i][j] * b;
                    }
                }


                R = Math.min(Math.max(0, R + r_offset), 255);
                G = Math.min(Math.max(0, G + g_offset), 255);
                B = Math.min(Math.max(0, B + b_offset), 255);

                n.setRGB(w, h, (new Color(R, G, B)).getRGB());
            }
        }
        return n;
    }

    public void onEmboss() {

        BufferedImage o = _zone_b.getImage();

        int m[][] = new int[][]{new int[]{0, 1, 0}, new int[]{-1, 0, 1}, new int[]{0, -1, 0}};

        _zone_c.setImage(applyConvolutionMatrix(o, m, 128, 128, 128));
    }

    public void onSharpen() {

        BufferedImage o = _zone_b.getImage();
        int m[][] = new int[][]{new int[]{0, -1, 0}, new int[]{-1, 5, -1}, new int[]{0, -1, 0}};
        _zone_c.setImage(applyConvolutionMatrix(o, m, 0, 0, 0));
    }

    /**
     *
     */
    public void onNegative() {

        BufferedImage o = _zone_b.getImage();
        int width = o.getWidth(), height = o.getHeight();
        BufferedImage n = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                Color c = new Color(o.getRGB(j, i));
                int R = c.getRed(), G = c.getGreen(), B = c.getBlue();

                n.setRGB(j, i, (new Color(255 - R, 255 - G, 255 - B)).getRGB());
            }
        }

        _zone_c.setImage(n);
    }

    /**
     *
     */
    public void onGrayscale() {

        BufferedImage o = _zone_b.getImage();
        int width = o.getWidth(), height = o.getHeight();
        BufferedImage n = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                Color c = new Color(o.getRGB(j, i));
                double R = c.getRed() / 255.0, G = c.getGreen() / 255.0, B = c.getBlue() / 255.0;
                int y = (int) (((double) 0.299 * R + 0.587 * G + 0.114 * B) * 255 + 0.5);
                n.setRGB(j, i, (new Color(y, y, y)).getRGB());
            }
        }

        _zone_c.setImage(n);
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

        for (ImagePanel p : _zones) {
            p.setImage(null);
        }
        setSelectBlocked(true);
        setDocumentName(FltSettings.UNTITLED_DOCUMENT);
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
            setSelectBlocked(false);
            setDocumentName(file.getName());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(this,
                    "Document is of unknown format", "Loading document",
                    JOptionPane.ERROR_MESSAGE);
            setDocumentName(FltSettings.UNTITLED_DOCUMENT);
        } catch (Exception ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(this,
                    "Error loading file: \n" + ex.getLocalizedMessage(),
                    "Loading document", JOptionPane.ERROR_MESSAGE);
            setDocumentName(FltSettings.UNTITLED_DOCUMENT);
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

            BmpImage.writeBmpImage((BmpImage) _zone_a.getImage(), file);

            setDocumentName(file.getName());
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
