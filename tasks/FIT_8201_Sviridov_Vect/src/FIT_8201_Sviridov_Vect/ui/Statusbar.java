package FIT_8201_Sviridov_Vect.ui;

import FIT_8201_Sviridov_Vect.statusbar.StatusbarListener;
import FIT_8201_Sviridov_Vect.statusbar.StatusbarModel;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * Class for statusbar model representation
 * @author admin
 */
public class Statusbar extends JPanel implements StatusbarListener {

	public static final DecimalFormat DEFAULT_FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
	
	private static final long serialVersionUID = -6700823080898324182L;
    private final JLabel cValue = new JLabel();
    private final JLabel fValue = new JLabel();
    private final JLabel lValue = new JLabel();
    private StatusbarModel statusbarModel;
    private DecimalFormat format = DEFAULT_FORMAT;

    {
        format.applyPattern("0.000000");
    }

    /**
     * Default constructor
     */
    public Statusbar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        add(cValue);
        add(fValue);
        add(lValue);

        clear();
    }

    /**
     * Getter for StatusbarModel
     * @return StatusbarModel
     */
    public StatusbarModel getStatusbarModel() {
        return statusbarModel;
    }

    /**
     * Setter for StatusbarModel
     * @param statusbarModel new StatusbarModel
     */
    public void setStatusbarModel(StatusbarModel statusbarModel) {
        this.statusbarModel = statusbarModel;
    }

    /**
     * Sets statusbar data to null and displays
     * "out of the region" message
     */
    public void clear() {
        cValue.setText("Out of the region");
        fValue.setText(null);
        lValue.setText(null);
    }

    /**
     * Sets new data to statusbar
     * @param x new x
     * @param y new y
     * @param fx new fx
     * @param fy new fy
     */
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
        sb.append(format.format(Math.hypot(fx, fy)));
        lValue.setText(sb.toString());
    }

    @Override
    public void dataChanged() {
        if (statusbarModel.isInRegion()) {
            setValues(statusbarModel.getX(), statusbarModel.getY(), statusbarModel.getFx(), statusbarModel.getFy());
        }
    }

    @Override
    public void stateChanged() {
        if (statusbarModel.isInRegion() == false) {
            clear();
        }
    }
}
