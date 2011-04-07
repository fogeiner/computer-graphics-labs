/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FIT_8201_Sviridov_Vect.utils;

/**
 * Class represents grid with given height and width
 * Object of the class are immutable and can be easily exchanged among 
 * objects 
 * @author admin
 */
public class Grid {
    public final int W;
    public final int H;


    /**
     * Constructor with given width and height
     * @param W
     * @param H
     */
    public Grid(final int W, final int H){
        this.W = W;
        this.H = H;
    }
}
