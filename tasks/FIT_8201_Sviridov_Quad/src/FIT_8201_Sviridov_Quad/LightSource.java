/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Quad;

/**
 *
 * @author alstein
 */
public class LightSource {

    private Vertex origin;
    private Coefficient3D color;

    public LightSource(Vertex origin, Coefficient3D color) {
        this.origin = origin;
        this.color = color;
    }

    public Coefficient3D getColor() {
        return color;
    }

    public void setColor(Coefficient3D color) {
        this.color = color;
    }

    public Vertex getOrigin() {
        return origin;
    }

    public void setOrigin(Vertex origin) {
        this.origin = origin;
    }
}
