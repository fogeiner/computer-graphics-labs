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

    public static int[] generateGradations(int n, int count) {
        int a[] = new int[count];
        if (count == 1) {
            a[0] = n / 2;
        } else {
            double step = n / (count - 1);
            for (int i = 0; i < count; ++i) {
                // +0.5 is NOT needed
                a[i] = (int) (i * step);
            }
        }
        return a;
    }

    public static int findClosestInGradation(int n, int g[]) {
        int dif = Integer.MAX_VALUE;
        int closest = Integer.MAX_VALUE;
        for (int grad : g) {
            int new_dif = Math.abs(grad - n);
            if (new_dif < dif) {
                dif = new_dif;
                closest = grad;
            }
        }
        return closest;
    }

    public static BufferedImage getFloydSteinbergDitheredImage(BufferedImage o, int r_count, int g_count, int b_count) {
        int width = o.getWidth(), height = o.getHeight();
        int data[] = o.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

        double errors[][][] = new double[][][]{
            new double[width][height],
            new double[width][height],
            new double[width][height]
        };
        int gradations[][] = {
            generateGradations(255, r_count),
            generateGradations(255, g_count),
            generateGradations(255, b_count)
        };
        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < width; ++w) {
                int rgb = data[h * width + w];
                int RGB[] = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};
                for (int k = 0; k < 3; ++k) {
                    RGB[k] += (int) (errors[k][w][h] + 0.5);
                    int closest = findClosestInGradation(RGB[k], gradations[k]);
                    int error = RGB[k] - closest;
                    RGB[k] = closest;
                    if (w + 1 < width) {
                        errors[k][w + 1][h] = ((double) 7 / 16 * error);
                        if (h + 1 < height) {
                            errors[k][w + 1][h + 1] = ((double) 1 / 16 * error);
                        }
                    }
                    if (h + 1 < height) {
                        errors[k][w][h + 1] = ((double) 5 / 16 * error);
                        if (w - 1 > 0) {
                            errors[k][w - 1][h + 1] = ((double) 3 / 16 * error);
                        }
                    }
                }
                rgb = RGB[2] + RGB[1] * 256 + RGB[0] * 256 * 256;
                n_data[h * width + w] = rgb;
            }
        }
        n.setRGB(0, 0, width, height, n_data, 0, width);
        return n;
    }

    public void onFloydSteinbergDithering() {
        BufferedImage o = _zone_b.getImage();
        _zone_c.setImage(getFloydSteinbergDitheredImage(o, 2, 2, 2));
    }

    public void onDoubleScale() {
        BufferedImage o = _zone_b.getImage();
        int o_width = o.getWidth(), o_height = o.getHeight();
        int center_height, center_width;
        center_width = o_width / 2 + (o_width / 2 % 2 == 0 ? 0 : 1);
        center_height = o_height / 2 + (o_height / 2 % 2 == 0 ? 0 : 1);
        BufferedImage c = o.getSubimage(o_width / 4, o_height / 4, center_width, center_height);
        BufferedImage n = new BufferedImage(2 * center_width, 2 * center_height, BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < center_height; ++h) {
            for (int w = 0; w < center_width; ++w) {
                int h_offset = 2 * h;
                int w_offset = 2 * w;
                int rgb;
                int rgb11 = c.getRGB(w, h);
                int rgb12;
                if (w != center_width - 1) {
                    rgb12 = c.getRGB(w + 1, h);
                } else {
                    rgb12 = c.getRGB(w, h);
                }
                int rgb21;
                if (h != center_height - 1) {
                    rgb21 = c.getRGB(w, h + 1);
                } else {
                    rgb21 = c.getRGB(w, h);
                }
                int rgb22;
                if (h != center_height - 1 && w != center_width - 1) {
                    rgb22 = c.getRGB(w + 1, h + 1);
                } else if (h != center_height - 1) {
                    rgb22 = c.getRGB(w, h + 1);
                } else if (w != center_width - 1) {
                    rgb22 = c.getRGB(w + 1, h);
                } else {
                    rgb22 = c.getRGB(w, h);
                }
                int RGB11[] = {(rgb11 >> 16) & 0xFF, (rgb11 >> 8) & 0xFF, rgb11 & 0xFF};
                int RGB12[] = {(rgb12 >> 16) & 0xFF, (rgb12 >> 8) & 0xFF, rgb12 & 0xFF};
                int RGB21[] = {(rgb21 >> 16) & 0xFF, (rgb21 >> 8) & 0xFF, rgb21 & 0xFF};
                int RGB22[] = {(rgb22 >> 16) & 0xFF, (rgb22 >> 8) & 0xFF, rgb22 & 0xFF};
                int AR[] = new int[3];
                int AL[] = new int[3];
                int ARL[] = new int[3];
                for (int k = 0; k < 3; ++k) {
                    AR[k] = (int) ((double) (RGB11[k] + RGB12[k]) / 2 + 0.5);
                    AL[k] = (int) ((double) (RGB11[k] + RGB21[k]) / 2 + 0.5);
                    ARL[k] = (int) ((double) (RGB11[k] + RGB12[k] + RGB21[k] + RGB22[k]) / 4 + 0.5);
                }
                n.setRGB(w_offset, h_offset, rgb11);
                rgb = AR[2] + AR[1] * 256 + AR[0] * 256 * 256;
                n.setRGB(w_offset + 1, h_offset, rgb);
                rgb = AL[2] + AL[1] * 256 + AL[0] * 256 * 256;
                n.setRGB(w_offset, h_offset + 1, rgb);
                rgb = ARL[2] + ARL[1] * 256 + ARL[0] * 256 * 256;
                n.setRGB(w_offset + 1, h_offset + 1, rgb);
            }
        }
        
        _zone_c.setImage(n);
    }

    public static int[][] generateBayerMatrix(int n) {
        int dim = 1 << n;
        int m[][] = new int[dim][dim];
        for (int i = 0; i < dim; ++i) {
            m[i] = new int[dim];
        }
        for (int y = 0; y < dim; ++y) {
            for (int x = 0; x < dim; ++x) {
                int v = 0, mask = n - 1, yc = y, xc = x ^ y;
                for (int bit = 0; bit < 2 * n; --mask) {
                    v |= ((yc >> mask) & 1) << bit++;
                    v |= ((xc >> mask) & 1) << bit++;
                }
                m[x][y] = v;
            }
        }
        return m;
    }

    public void onOrderedDithering() {
        BufferedImage o = _zone_b.getImage();
        int width = o.getWidth(), height = o.getHeight();
        int data[] = o.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

        int m[][] = generateBayerMatrix(4);

        for (int h = 0; h < height; h += m.length) {
            for (int w = 0; w < width; w += m[0].length) {
                for (int i = 0; i < m.length && h + i < height; ++i) {
                    for (int j = 0; j < m[0].length && w + j < width; ++j) {
                        int rgb = data[(h + i) * width + (w + j)];

                        int RGB[] = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};
                        for (int k = 0; k < RGB.length; ++k) {
                            if (RGB[k] <= m[i][j]) {
                                RGB[k] = 0;
                            } else {
                                RGB[k] = 255;
                            }
                        }
                        rgb = RGB[2] + RGB[1] * 256 + RGB[0] * 256 * 256;
                        n_data[(h + i) * width + (w + j)] = rgb;
                    }
                }
            }
        }

        n.setRGB(0, 0, width, height, n_data, 0, width);
        _zone_c.setImage(n);
    }

    public void onAquarelle() {
        BufferedImage o = _zone_b.getImage();
        BufferedImage csi = getColorSmoothedImage(o, 5);
        BufferedImage si = applyConvolutionMatrix(csi,
                new double[][]{
                    new double[]{0, 0, 0, 0, 0},
                    new double[]{0, 0, -1, 0, 0},
                    new double[]{0, -1, 5, -1, 0},
                    new double[]{0, 0, -1, 0, 0},
                    new double[]{0, 0, 0, 0, 0}},
                0, 0, 0);
        _zone_c.setImage(si);
    }

    public static BufferedImage getColorSmoothedImage(BufferedImage o, int size) {
        int width = o.getWidth(), height = o.getHeight();
        int data[] = o.getRGB(0, 0, width, height, null, 0, width);
        int m = size / 2;

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

        int[] rs = new int[size * size];
        int[] gs = new int[size * size];
        int[] bs = new int[size * size];
        int middle = rs.length / 2;
        for (int h = m; h < height - m; ++h) {
            for (int w = m; w < width - m; ++w) {
                for (int i = 0; i < size; ++i) {
                    for (int j = 0; j < size; ++j) {
                        int rgb = data[(h + i - m) * width + (w + j - m)];

                        rs[i * size + j] = (rgb >> 16) & 0xFF;
                        gs[i * size + j] = (rgb >> 8) & 0xFF;
                        bs[i * size + j] = rgb & 0xFF;
                    }
                }

                Arrays.sort(rs);
                Arrays.sort(gs);
                Arrays.sort(bs);

                int R = rs[middle], G = gs[middle], B = bs[middle];
                n_data[h * width + w] = B + 256 * G + 256 * 256 * R;
            }
        }
        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < m; ++w) {
                n_data[h * width + w] = data[h * width + w];
            }
            for (int w = width - m; w < width; ++w) {
                n_data[h * width + w] = data[h * width + w];
            }
        }
        for (int w = 0; w < width; ++w) {
            for (int h = 0; h < m; ++h) {
                n_data[h * width + w] = data[h * width + w];
            }
            for (int h = height - m; h < height; ++h) {
                n_data[h * width + w] = data[h * width + w];
            }
        }
        n.setRGB(0, 0, width, height, n_data, 0, width);
        return n;
    }
    private static double _S = 50.0;

    public void onSobel() {
        BufferedImage o = _zone_b.getImage();

        int width = o.getWidth(), height = o.getHeight();

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage gs = getGreyscaleImage(o);
        int data[] = gs.getRGB(0, 0, width, height, null, 0, width);

        int white_rgb = Color.white.getRGB();
        int black_rgb = Color.black.getRGB();
        for (int h = 0; h < height - 2; ++h) {
            for (int w = 0; w < width - 2; ++w) {
                double g11 = data[h * width + w] & 0xFF,
                        g12 = data[(h + 1) * width + w] & 0xFF,
                        g13 = data[(h + 2) * width + w] & 0xFF,
                        g21 = data[h * width + (w + 1)] & 0xFF,
                        g22 = data[(h + 1) * width + (w + 1)] & 0xFF,
                        g23 = data[(h + 2) * width + (w + 1)] & 0xFF,
                        g31 = data[h * width + (w + 2)] & 0xFF,
                        g32 = data[(h + 1) * width + (w + 2)] & 0xFF,
                        g33 = data[(h + 2) * width + (w + 2)] & 0xFF;

                double Sx = (g13 + 2 * g23 + g33) - (g11 + 2 * g21 + g31);
                double Sy = (g31 + 2 * g32 + g33) - (g11 + 2 * g12 + g13);

                double S = Math.abs(Sx) + Math.abs(Sy);

                if (S > _S) {
                    n_data[h * width + w] = white_rgb;
                } else {
                    n_data[h * width + w] = black_rgb;
                }
            }
        }
        n.setRGB(0, 0, width, height, n_data, 0, width);
        _zone_c.setImage(n);
    }
    private double _R = 20.0;

    public void onRoberts() {
        BufferedImage o = _zone_b.getImage();


        int width = o.getWidth(), height = o.getHeight();

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage gs = getGreyscaleImage(o);
        int data[] = gs.getRGB(0, 0, width, height, null, 0, width);

        int white_rgb = Color.white.getRGB();
        int black_rgb = Color.black.getRGB();


        for (int h = 0; h < height - 1; ++h) {
            for (int w = 0; w < width - 1; ++w) {

                double g11 = data[h * width + w] & 0xFF,
                        g12 = data[(h + 1) * width + w] & 0xFF,
                        g21 = data[h * width + (w + 1)] & 0xFF,
                        g22 = data[(h + 1) * width + (w + 1)] & 0xFF;

                double R = Math.abs(g11 - g22) + Math.abs(g12 - g21);
                if (R > _R) {
                    n_data[h * width + w] = white_rgb;
                } else {
                    n_data[h * width + w] = black_rgb;
                }
            }
        }

        n.setRGB(0, 0, width, height, n_data, 0, width);
        _zone_c.setImage(n);
    }

    public static BufferedImage applyConvolutionMatrix(BufferedImage o, double m[][], double r_s, double g_s, double b_s) {
        int width = o.getWidth(), height = o.getHeight();
        int data[] = o.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

        int m_h = m.length / 2;
        int m_w = m[0].length / 2;
        for (int h = m_h; h < height - m_h; ++h) {
            for (int w = m_w; w < width - m_w; ++w) {
                double R = 0, G = 0, B = 0;
                for (int i = 0; i < m.length; ++i) {
                    for (int j = 0; j < m[0].length; ++j) {
                        double r = 0, g = 0, b = 0;
                        int k = h + i - m_h;
                        int l = w + j - m_w;

                        int rgb = data[k * width + l];
                        int RGB[] = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};

                        R += m[i][j] * RGB[0];
                        G += m[i][j] * RGB[1];
                        B += m[i][j] * RGB[2];
                    }
                }
                R = Math.min(Math.max(0, R + r_s), 255);
                G = Math.min(Math.max(0, G + g_s), 255);
                B = Math.min(Math.max(0, B + b_s), 255);
                n_data[h * width + w] = 256 * 256 * ((int) (R + 0.5)) + 256 * ((int) (G + 0.5)) + (int) (B + 0.5);
            }
        }
        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < m_w; ++w) {
                n_data[h * width + w] = data[h * width + w];
            }
            for (int w = width - m_w; w < width; ++w) {
                n_data[h * width + w] = data[h * width + w];
            }
        }
        for (int w = 0; w < width; ++w) {
            for (int h = 0; h < m_h; ++h) {
                n_data[h * width + w] = data[h * width + w];
            }
            for (int h = height - m_h; h < height; ++h) {
                n_data[h * width + w] = data[h * width + w];
            }
        }

        n.setRGB(0, 0, width, height, n_data, 0, width);
        return n;
    }

    public static BufferedImage getGreyscaleImage(BufferedImage o) {
        int width = o.getWidth(), height = o.getHeight();
        int data[] = o.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);
        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < width; ++w) {
                //  int rgb = o.getRGB(j, i);
                int rgb = data[h * width + w];
                int RGB[] = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};

                double R = RGB[0] / 255.0, G = RGB[1] / 255.0, B = RGB[2] / 255.0;

                int y = (int) (((double) 0.299 * R + 0.587 * G + 0.114 * B) * 255 + 0.5);

                n_data[h * width + w] = 256 * (256 * y + y) + y;
            }
        }

        n.setRGB(0, 0, width, height, n_data, 0, width);
        return n;
    }

    public void onBlur() {
        BufferedImage o = _zone_b.getImage();
        double m[][] = new double[][]{
            new double[]{1.0 / 74, 2.0 / 74, 3.0 / 74, 2.0 / 74, 1.0 / 74},
            new double[]{2.0 / 74, 4.0 / 74, 5.0 / 74, 4.0 / 74, 2.0 / 74},
            new double[]{3.0 / 74, 5.0 / 74, 6.0 / 74, 5.0 / 74, 3.0 / 74},
            new double[]{2.0 / 74, 4.0 / 74, 5.0 / 74, 4.0 / 74, 2.0 / 74},
            new double[]{1.0 / 74, 2.0 / 74, 3.0 / 74, 2.0 / 74, 1.0 / 74}};
        _zone_c.setImage(applyConvolutionMatrix(o, m, 0, 0, 0));
    }

    public void onEmboss() {
        BufferedImage o = _zone_b.getImage();
        double m[][] = new double[][]{new double[]{0, 1, 0}, new double[]{-1, 0, 1}, new double[]{0, -1, 0}};
        _zone_c.setImage(applyConvolutionMatrix(o, m, 128, 128, 128));
    }

    public void onSharpen() {
        BufferedImage o = _zone_b.getImage();
        double m[][] = new double[][]{new double[]{0, -1, 0}, new double[]{-1, 5, -1}, new double[]{0, -1, 0}};
        _zone_c.setImage(applyConvolutionMatrix(o, m, 0, 0, 0));
    }

    public void onNegative() {
        BufferedImage o = _zone_b.getImage();

        int width = o.getWidth(), height = o.getHeight();
        int data[] = o.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < width; ++w) {
                int rgb = data[h * width + w];
                int RGB[] = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};
                int R = 255 - RGB[0], G = 255 - RGB[1], B = 255 - RGB[2];
                n_data[h * width + w] = 256 * 256 * R + 256 * G + B;
            }
        }
        n.setRGB(0, 0, width, height, n_data, 0, width);

        _zone_c.setImage(n);
    }

    public void onGrayscale() {
        BufferedImage o = _zone_b.getImage();
        _zone_c.setImage(getGreyscaleImage(o));
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
