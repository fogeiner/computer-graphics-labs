/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Vect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class PanelPositionTest {

    static class MyPanel extends JPanel {

        public MyPanel() {
            this.setPreferredSize(new Dimension(300, 400));
            this.setBorder(BorderFactory.createLineBorder(Color.blue));

        }
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.setSize(600, 800);
        frame.setLayout(new GridBagLayout());

        frame.add(new MyPanel());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
