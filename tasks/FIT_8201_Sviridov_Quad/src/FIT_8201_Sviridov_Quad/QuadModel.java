package FIT_8201_Sviridov_Quad;

import FIT_8201_Sviridov_Quad.primitives.Wireframe;
import java.util.List;

/**
 *
 * @author alstein
 */
public class QuadModel {
    private Wireframe box;
    private List<Wireframe> orts;
    private List<Renderable> objects;
    private List<LightSource> lightSources;
    private Coefficient3D ambient;

    private Transformation world;
    private Transformation camera;
}
