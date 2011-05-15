package FIT_8201_Sviridov_Quad.primitives;

import FIT_8201_Sviridov_Quad.Coefficient3D;
import FIT_8201_Sviridov_Quad.IntersectionInfo;
import FIT_8201_Sviridov_Quad.Light;
import FIT_8201_Sviridov_Quad.Ray;
import FIT_8201_Sviridov_Quad.SceneObject;
import java.util.Collection;

/**
 * Interface for Renderable object
 * @author alstein
 */
public interface Renderable extends SceneObject, Cloneable {

	/**
	 * Returns collection of all intersectrion with the given ray
	 * @param ray ray
	 * @return collection of all intersectrion with the given ray
	 */
    public Collection<IntersectionInfo> intersect(Ray ray);

    /**
     * Traces color with given intersectionInfo
     * @param intersectionInfo intersection information
     * @param objects renderable objects
     * @param lights lights
     * @param ambient ambient color
     * @return color
     */
    public abstract Coefficient3D trace(IntersectionInfo intersectionInfo, Collection<Renderable> objects, Collection<Light> lights, Coefficient3D ambient);

    /**
     * Return wireframe
     * @return wireframe
     */
    public Wireframe getWireframe();

    /**
     * Clone operation
     * @return objects copy
     */
    public Object clone();
}
