/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Vect.ui;


import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class SettingsDialog extends JDialog {

	private static final long serialVersionUID = -903939050607469239L;

	public SettingsDialog(Frame owner) {
        super(owner, "Color set chooser dialog");
        JPanel leftPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel rightPanel = new JPanel();

        JButton addButton = new JButton("Add"),
                removeButton = new JButton("Remove"),
                upButton = new JButton("Up"),
                downButton = new JButton("Down");

        leftPanel.add(addButton);
        leftPanel.add(removeButton);
        leftPanel.add(upButton);
        leftPanel.add(downButton);
        this.setLayout(new BorderLayout(5, 5));

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        pack();
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SettingsDialog d = new SettingsDialog(frame);
        d.setVisible(true);
    }
}
