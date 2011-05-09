package FIT_8201_Sviridov_Quad;

import FIT_8201_Sviridov_Quad.primitives.WireframeObject;
import java.util.List;

/**
 *
 * @author alstein
 */
public class QuadModel {
    private WireframeObject box;
    private List<WireframeObject> orts;
    private List<Renderable> objects;
    private List<LightSource> lightSources;

    private Transformation world;
    private Transformation camera;
}
