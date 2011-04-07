package FIT_8201_Sviridov_Vect.ui;

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
import java.util.List;
import java.util.Locale;

import javax.swing.JPanel;

import FIT_8201_Sviridov_Vect.vect.VectListener;
import FIT_8201_Sviridov_Vect.vect.VectModel;

/**
 *
 * @author admin
 */
public class LegendPanel extends JPanel implements VectListener {

    private static final long serialVersionUID = -8211800501181030587L;
    public static final int DEFAULT_COLOR_SAMPLE_WIDTH = 40;
    public static final int DEFAULT_LEGEND_PADDING = 5;
    public static final Font DEFAULT_FONT = new Font("Monospaced", Font.BOLD, 14);
    public static final DecimalFormat DEFAULT_FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
    private int colorSampleWidth = DEFAULT_COLOR_SAMPLE_WIDTH;
    private int legendPadding = DEFAULT_LEGEND_PADDING;
    private Font font = DEFAULT_FONT;
    private DecimalFormat format = DEFAULT_FORMAT;
    private List<Double> values;
    private List<Color> colors;
    private VectModel vectModel;

    {
        format.applyPattern("0.0000");
    }

    /**
     * Default ctor
     */
    public LegendPanel() {
    }

    /**
     * Getter for VectModel
     * @return VectModel
     */
    public VectModel getVectModel() {
        return vectModel;
    }

    /**
     * Setter for VectModel
     * @param vectModel new VectModel
     */
    public void setVectModel(VectModel vectModel) {
        this.vectModel = vectModel;
        if (vectModel != null) {
            setModel(vectModel.getValues(), vectModel.getColors());
            if (vectModel.isFieldColor()) {
                setVisible(true);
            } else {
                setVisible(false);
            }
        }

        repaint();
    }

    /**
     * Sets values and colors to be displayed
     * @param values values
     * @param colors colors
     */
    private void setModel(List<Double> values, List<Color> colors) {
        if (colors.size() != values.size() + 1) {
            throw new IllegalArgumentException("There should be one colors more than values");
        }

        this.values = new ArrayList<Double>(values);

        this.colors = colors;
        updateSize();
    }

    /**
     * Recalculates size of the legend for layout manager
     */
    private void updateSize() {
        // _color_sample_width + padding + max{text strings} + padding
        setPreferredSize(new Dimension(colorSampleWidth + legendPadding + maxValueWidth() + legendPadding, 0));
    }

    /**
     * Calculates max width needed to display text on legend
     * @return max value
     */
    private int maxValueWidth() {
        Image img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();

        Font old_font = g.getFont();

        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int max = Integer.MIN_VALUE;
        for (Double v : values) {
            String value = format.format(v);
            int width = fm.stringWidth(value);
            if (width > max) {
                max = width;
            }
        }

        g.setFont(old_font);
        g.dispose();
        return max;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (colors == null || values == null || vectModel == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;

        int panel_height = getHeight();
        int panel_width = getWidth();

        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int ascent = fm.getMaxAscent();
        int descent = fm.getMaxDescent();
        double center = (ascent + descent) / 2.0;

        int size = colors.size();
        double step = (double) panel_height / size;
        for (int k = 0; k < size; ++k) {
            if (k != size - 1) {
                String value = format.format(values.get(k));
                int width = fm.stringWidth(value);
                int msg_x = panel_width - width - legendPadding;
                int msg_y = (int) (step * (k + 1) + center / 2 + 0.5);
                g2.drawString(value, msg_x, msg_y);
            }

            Color old_color = g2.getColor();
            g2.setColor(colors.get(k));
            int x = 0;
            int y = (int) (step * k + 0.5);
            int width = colorSampleWidth;
            int height = (int) (step + 0.5);
            g2.fillRect(x, y, width, height);
            g2.setColor(Color.black);
            if (k != size - 1) {
                g2.drawRect(x, y, width, height);
            } else {
                g2.drawRect(x, y, width, getHeight() - y - 1);
            }
            g2.setColor(old_color);
        }

    }


    @Override
    public void modelChanged() {
        setModel(vectModel.getValues(), vectModel.getColors());
        repaint();
    }

    @Override
    public void regionChanged() {
        setModel(vectModel.getValues(), vectModel.getColors());
        repaint();
    }

    @Override
    public void lengthMultChanged() {
    }

    @Override
    public void gridChanged() {
        setModel(vectModel.getValues(), vectModel.getColors());
        repaint();
    }

    @Override
    public void gridColorChanged() {
    }

    @Override
    public void colorsChanged() {
        setModel(vectModel.getValues(), vectModel.getColors());
        repaint();
    }

    @Override
    public void arrowModeChanged() {
    }

    @Override
    public void fieldModeChanged() {
        if (vectModel.isFieldColor()) {
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    @Override
    public void chessModeChanged() {
    }

    @Override
    public void gridDrawnChanged() {
    }
}
