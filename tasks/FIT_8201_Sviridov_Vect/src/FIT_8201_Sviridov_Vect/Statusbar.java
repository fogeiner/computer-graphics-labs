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
public class Statusbar extends JPanel implements StatusbarListener {

    public static final DecimalFormat DEFAULT_FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
    private final JLabel cValue = new JLabel();
    private final JLabel fValue = new JLabel();
    private final JLabel lValue = new JLabel();
    private StatusbarModel statusbarModel;
    private DecimalFormat format = DEFAULT_FORMAT;

    {
        format.applyPattern("0.00");
    }

    public Statusbar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        add(cValue);
        add(fValue);
        add(lValue);
    }

    public StatusbarModel getStatusbarModel() {
        return statusbarModel;
    }

    public void setStatusbarModel(StatusbarModel statusbarModel) {
        this.statusbarModel = statusbarModel;
    }

    public void clear() {
        cValue.setText("Out of the region");
        fValue.setText(null);
        lValue.setText(null);
    }

    public void setValues(double x, double y, double fx, double fy) {
        StringBuilder sb = new StringBuilder();
        sb.append("Position: (");
        sb.append(format.format(x));
        sb.append(", ");
        sb.append(format.format(y));
        sb.append(")");
        cValue.setText(sb.toString());

        sb = new StringBuilder();
        sb.append("Vector: (");
        sb.append(format.format(fx));
        sb.append(", ");
        sb.append(format.format(fy));
        sb.append(")");
        fValue.setText(sb.toString());

        sb = new StringBuilder();
        sb.append("Length: ");
        double r1 = x - fx;
        double r2 = y - fy;
        sb.append(format.format(Math.sqrt(r1 * r1 + r2 * r2)));
        lValue.setText(sb.toString());
    }

    @Override
    public void dataChanged() {
        if (statusbarModel.getState() == StatusbarModel.IN_REGION) {
            setValues(statusbarModel.getX(), statusbarModel.getY(), statusbarModel.getFx(), statusbarModel.getFy());
        }
    }

    @Override
    public void stateChanged() {
        if (statusbarModel.getState() == StatusbarModel.OUT_REGION) {
            clear();
        }
    }
}
