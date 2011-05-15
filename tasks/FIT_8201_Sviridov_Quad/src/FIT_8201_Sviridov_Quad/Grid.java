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
        double height = 50, width = 30,
                halfWidth = width / 2, halfHeight = height / 2,
                minSize = Math.min(width, height),
                halfMinSize = minSize / 2;
        double sw = 100, sh = 60;
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumIntegerDigits(2);

        double stepWidth = sw / (minSize + 1);
        double stepHeight = sh / (minSize + 1);


        double r[][] = new double[(int) (width + 0.5)][(int) (height + 0.5)];

        int minHalfSizeIntUp = (int) (halfMinSize + 0.5);
        int minHalfSizeIntDown = (int) (halfMinSize);

        int widthOffset = (int) ((width - minSize) / 2 + 0.5),
                heightOffset = (int) ((height - minSize) / 2 + 0.5);

        for (int w = -minHalfSizeIntUp; w < minHalfSizeIntDown; ++w) {
            for (int h = -minHalfSizeIntUp; h < minHalfSizeIntDown; ++h) {
                int j = w + minHalfSizeIntUp + widthOffset,
                        i = h + minHalfSizeIntUp + heightOffset;
                double y = h * stepHeight, x = w * stepWidth;
                r[j][i] = 1.0;
            }
        }


//        for (int w = 0; w < (int) (width + 0.5); ++w) {
//            for (int h = 0; h < (int) (height + 0.5); ++h) {
//                  System.out.println(w + " " + h);
//                //  System.out.println(w * stepWidth + " " + h * stepHeight + " " + znear);
////                System.out.println((w + offsetWidth + (int)(halfMinSize + 0.5)) + " " +
////                        (h + offsetHeight + (int)(halfMinSize + 0.5)));
////                r[w + offsetWidth + (int)(halfMinSize + 0.5)][h + offsetHeight + (int)(halfMinSize + 0.5)] = 1.0;
//                double cu = ((2 * w + 1) / (2*width) - 1.0 / 2) * sw,
//                        cv = ((2 * h + 1) / (2*height) - 1.0 / 2) * sh;
//                System.out.println(cu + " " + cv);
//            }
//        }
    }
}
