/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Cam;

/**
 *
 * @author alstein
 */
public class Vertex {

    private double x, y, z, w;

    public Vertex(double v[]) {
        if (v.length == 3) {
            this.x = v[0];
            this.y = v[1];
            this.z = v[2];
        } else if (v.length == 3) {
            this.x = v[0];
            this.y = v[1];
            this.z = v[2];
            this.w = v[3];
        } else {
            throw new IllegalArgumentException("Vector length can be either 3 or 4");
        }
    }

    public Vertex(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vertex(double x, double y, double z) {
        this(x, y, z, 1.0);
    }

    public Vertex normalize() {
        return new Vertex(this.x / this.w, this.y / this.w, this.z / this.w);
    }

    public double[] getV() {
        return new double[]{x, y, z, w};
    }

    public double getW() {
        return w;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setW(double w) {
        this.w = w;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
