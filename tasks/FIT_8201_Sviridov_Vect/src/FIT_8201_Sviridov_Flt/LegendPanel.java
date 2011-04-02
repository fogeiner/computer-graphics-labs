package FIT_8201_Sviridov_Vect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author admin
 */
public class LegendPanel extends JPanel {

    public static final int DEFAULT_COLOR_SAMPLE_WIDTH = 40;
    public static final int DEFAULT_LEGEND_PADDING = 5;
    public static final Font DEFAULT_FONT = new Font("Monospaced", Font.BOLD, 14);
    public static final DecimalFormat DEFAULT_FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
    private int _color_sample_width = DEFAULT_COLOR_SAMPLE_WIDTH;
    private int _legend_padding = DEFAULT_LEGEND_PADDING;
    private Font _font = DEFAULT_FONT;
    private DecimalFormat _format = DEFAULT_FORMAT;
    private List<Double> _values;
    private List<Color> _colors;

    public LegendPanel(List<Double> values, List<Color> colors) {
        if (colors.size() != values.size() + 1) {
            throw new IllegalArgumentException("There should be one colors more than values");
        }
        _values = new ArrayList<Double>(values);
        
        Collections.sort(_values);
        Collections.reverse(_values);
        _colors = colors;

        _format.applyPattern("0.00");
        // _color_sample_width + padding + max{text strings} + padding
        setPreferredSize(new Dimension(_color_sample_width + _legend_padding + maxValueWidth() + _legend_padding, 0));
    }

    private int maxValueWidth() {
        Image img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();

        Font old_font = g.getFont();

        g.setFont(_font);
        FontMetrics fm = g.getFontMetrics();
        int max = Integer.MIN_VALUE;
        for (Double v : _values) {
            String value = _format.format(v);
            int width = fm.stringWidth(value);
            if (width > max) {
                max = width;
            }
        }

        g.setFont(old_font);
        return max;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        int panel_height = getHeight();
        int panel_width = getWidth();

        g.setFont(_font);
        FontMetrics fm = g.getFontMetrics();
        int ascent = fm.getMaxAscent();
        int descent = fm.getMaxDescent();
        int center = (ascent + descent) / 2;

        int size = _colors.size();
        for (int k = 0; k < size; ++k) {
            if (k != size - 1) {
                String value = _format.format(_values.get(k));
                int width = fm.stringWidth(value);
                int msg_x = panel_width - width - _legend_padding;
                int msg_y = (int) ((double) panel_height / size * (k + 1) + center / 2.0 + 0.5);
                g.drawString(value, msg_x, msg_y);
            }

            Color old_color = g.getColor();
            g.setColor(_colors.get(k));
            g.fillRect(0, (int) ((double) panel_height / size * k + 0.5),
                    _color_sample_width, (int) ((double) panel_height / size + 0.5));
            g.setColor(old_color);
        }
    }

    public static void main(String args[]) {

        JFrame frame = new JFrame();
        frame.setSize(new Dimension(600, 400));

        JPanel main_panel = new JPanel();
        main_panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        main_panel.setLayout(new BorderLayout(5, 5));

        JPanel p_left = new GridPanel(10, 20);
        p_left.setBorder(BorderFactory.createLineBorder(Color.black));

        List<Color> colors = Arrays.asList(new Color[]{Color.red,
                    Color.orange, Color.yellow, Color.green, Color.blue, Color.cyan, Color.pink});
        List<Double> values = Arrays.asList(new Double[]{50.0, 400.0, 3000.0, 2.0, 1.0, 0.0});

        JPanel p_right = new LegendPanel(values, colors);
        p_right.setBorder(BorderFactory.createLineBorder(Color.black));

        Statusbar p_lower = new Statusbar();
        p_lower.setValues(1.033, 0.420, 100.0545, 10009.0);
        p_lower.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        main_panel.add(p_left, BorderLayout.CENTER);
        main_panel.add(p_right, BorderLayout.EAST);
        main_panel.add(p_lower, BorderLayout.SOUTH);

        frame.add(main_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
