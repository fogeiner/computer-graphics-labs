package FIT_8201_Sviridov_Vect;

import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author admin
 */
public class Statusbar extends JPanel {

    public static final DecimalFormat DEFAULT_FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
    private final JLabel cValue_ = new JLabel();
    private final JLabel fValue_ = new JLabel();
    private final JLabel lValue_ = new JLabel();

    private DecimalFormat format_ = DEFAULT_FORMAT;

    {
        format_.applyPattern("0.00");
    }

    public Statusbar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        add(cValue_);
        add(fValue_);
        add(lValue_);

        clear();
    }

    public void clear() {
        cValue_.setText("Out of the region");
        fValue_.setText(null);
        lValue_.setText(null);
    }

    public void setValues(double x, double y, double fx, double fy) {
        StringBuilder sb = new StringBuilder();
        sb.append("Position: (");
        sb.append(format_.format(x));
        sb.append(", ");
        sb.append(format_.format(y));
        sb.append(")");
        cValue_.setText(sb.toString());

        sb = new StringBuilder();
        sb.append("Vector: (");
        sb.append(format_.format(fx));
        sb.append(", ");
        sb.append(format_.format(fy));
        sb.append(")");
        fValue_.setText(sb.toString());

        sb = new StringBuilder();
        sb.append("Length: ");
        sb.append(format_.format(Math.sqrt(fx * fx + fy * fy)));
        lValue_.setText(sb.toString());
    }
}
