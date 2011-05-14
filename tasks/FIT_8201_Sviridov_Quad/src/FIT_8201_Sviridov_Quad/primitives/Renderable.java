package FIT_8201_Sviridov_Quad.primitives;

import FIT_8201_Sviridov_Quad.Coefficient3D;
import FIT_8201_Sviridov_Quad.IntersectionInfo;
import FIT_8201_Sviridov_Quad.Light;
import FIT_8201_Sviridov_Quad.Ray;
import FIT_8201_Sviridov_Quad.SceneObject;
import java.util.Collection;

/**
 *
 * @author alstein
 */
public interface Renderable extends SceneObject, Cloneable {

    public Collection<IntersectionInfo> intersect(Ray ray);

    public Coefficient3D trace(IntersectionInfo intersectionInfo, Collection<Light> lights, Collection<Renderable> objects);

    public Wireframe getWireframe();

    public Object clone();
}
