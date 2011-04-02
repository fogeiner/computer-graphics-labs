package FIT_8201_Sviridov_Vect;

import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author admin
 */
public class Statusbar extends JPanel {

    public static final DecimalFormat DEFAULT_FORMAT = (DecimalFormat)NumberFormat.getInstance(Locale.ENGLISH);

    private final JLabel _c_value = new JLabel();
    private final JLabel _f_value = new JLabel();
    private final JLabel _l_value = new JLabel();
    private DecimalFormat _format = DEFAULT_FORMAT;

    public Statusbar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        _format.applyPattern("0.00");

        add(_c_value);
        add(_f_value);
        add(_l_value);
    }

    public void clear(){
        _c_value.setText(null);
        _f_value.setText(null);
    }
    public void setValues(double x, double y, double fx, double fy) {
        StringBuilder sb = new StringBuilder();
        sb.append("Position: (");
        sb.append(_format.format(x));
        sb.append(", ");
        sb.append(_format.format(y));
        sb.append(")");
        _c_value.setText(sb.toString());
        
        sb = new StringBuilder();
        sb.append("Vector: (");
        sb.append(_format.format(fx));
        sb.append(", ");
        sb.append(_format.format(fy));
        sb.append(")");
        _f_value.setText(sb.toString());

        sb = new StringBuilder();
        sb.append("Length: ");
        sb.append(_format.format(Math.sqrt(fx*fx+fy*fy)));
        _l_value.setText(sb.toString());
    }
}
