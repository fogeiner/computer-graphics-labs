/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Cam;

/**
 *
 * @author admin
 */
public class Vector extends Vertex {

    private final double length;

    public Vector(double x, double y, double z) {
        super(x, y, z);
        this.length = Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
    }

    public double getLength() {
        return length;
    }
}
