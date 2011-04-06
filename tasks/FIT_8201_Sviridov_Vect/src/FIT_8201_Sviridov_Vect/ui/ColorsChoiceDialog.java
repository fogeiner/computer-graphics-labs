/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Vect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author admin
 */
public class ColorsChoiceDialog extends JDialog {

    JButton okButton = new JButton("OK");
    JButton cancelButton = new JButton("Cancel");
    int N = 20;
    JLabel labels[] = new JLabel[N];
    JCheckBox checkBoxes[] = new JCheckBox[N];
    MouseAdapter chooser = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel l = (JLabel) e.getSource();
            Color c = JColorChooser.showDialog(l.getParent(), "Choose " + l.getText(), l.getBackground());
            if (c != null) {
                l.setBackground(c);
            }
        }
    };

    private JPanel makeLabelCheckBoxPanel(JLabel l, JCheckBox cb) {
        JPanel p = new JPanel(new BorderLayout());
        l.setToolTipText("Click to open color chooser dialog");
        cb.setToolTipText("Check if you want this color to be used");
        l.setOpaque(true);
        l.addMouseListener(chooser);
        p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        p.add(l, BorderLayout.CENTER);
        p.add(cb, BorderLayout.EAST);
        return p;
    }

    private JPanel makeButtonsPanel() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 0));
        p.add(okButton);
        p.add(cancelButton);
        outer.add(p);
        return outer;
    }

    public ColorsChoiceDialog(Frame owner, String title) {
        super(owner, title, true);
        int cols = 4,
                rows = 5;
        setLayout(new BorderLayout());
        JPanel colorsPanel = new JPanel(new GridLayout(rows, cols));

        int index = 0;
        for (int i = 1; i < N + 1; ++i) {
            labels[index] = new JLabel("Color " + (index + 1));
            checkBoxes[index] = new JCheckBox();

            colorsPanel.add(makeLabelCheckBoxPanel(labels[index], checkBoxes[index]));

            index = i * rows % (N - 1);
            if (index == 0) {
                index = (N - 1);
            }
        }
        add(colorsPanel, BorderLayout.NORTH);
        add(makeButtonsPanel(), BorderLayout.SOUTH);
        pack();
        setResizable(false);
    }

    public static void main(String args[]) {
        ColorsChoiceDialog d = new ColorsChoiceDialog(null, "Title");
        d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        d.setVisible(true);
    }
}
