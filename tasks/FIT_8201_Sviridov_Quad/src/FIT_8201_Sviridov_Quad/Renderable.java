package FIT_8201_Sviridov_Quad;

import java.awt.Color;
import java.util.Collection;

/**
 *
 * @author alstein
 */
public interface Renderable {

    public Collection<Vertex> intersect(Ray ray);

    public Color trace(Ray ray, Collection<LightSource> lights,
            Collection<Renderable> objects, Color background);
}
