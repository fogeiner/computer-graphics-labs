package FIT_8201_Sviridov_Quad;

import FIT_8201_Sviridov_Quad.transformations.Transformation;

/**
 *
 * @author alstein
 */
public class Light implements SceneObject {

    private Vertex origin;
    private final Coefficient3D color;

    public Light(Vertex origin, Coefficient3D color) {
        this.origin = origin;
        this.color = color;
    }

    public Coefficient3D getColor() {
        return color;
    }

    public Vertex getOrigin() {
        return origin;
    }

    @Override
    public void transform(Transformation transformation) {
        origin = transformation.apply(origin);
    }

    public Light clone(){
        Light light = new Light(origin, color);
        return light;
    }

    @Override
    public String toString() {
        return origin + " " + color;
    }

}
