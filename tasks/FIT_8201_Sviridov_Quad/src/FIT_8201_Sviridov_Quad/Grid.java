/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Quad;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author alstein
 */
public class Grid {

    public static void main(String args[]) {
        double znear = 100;
        int halfWidth = 10;
        int halfHeight = 5;
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumIntegerDigits(2);
        for(int w = -halfWidth; w < halfWidth; ++w){
            for(int h = -halfHeight; h < halfHeight; ++h){
                Vertex v = new Vertex(w, h, znear);
                Ray ray = new Ray(new Vector(v));
                System.out.println("("+w+","+h+")"+ " -> " + v);
                System.out.println(ray);
            }
        }
    }
}
