package FIT_8201_Sviridov_Quad;

import FIT_8201_Sviridov_Quad.primitives.Renderable;
import java.util.Collection;

/**
 * Class represents intersection info
 * 
 * @author alstein
 */
public class IntersectionInfo {

	private final Vertex intersection;
	private final Vector normal;
	private final Renderable object;

	/**
	 * Ctor
	 * 
	 * @param intersection
	 *            intersection
	 * @param normal
	 *            normal
	 * @param object
	 *            Renderable object
	 */
	public IntersectionInfo(Vertex intersection, Vector normal,
			Renderable object) {
		this.intersection = intersection;
		this.normal = normal.normalize();
		this.object = object;
	}

	/**
	 * Returns intersection
	 * 
	 * @return intersection
	 */
	public Vertex getIntersection() {
		return intersection;
	}

	/**
	 * Returns normal
	 * 
	 * @return normal
	 */
	public Vector getNormal() {
		return normal;
	}

	/**
	 * Returns length
	 * @return length
	 */
	public double length() {
		return new Vector(intersection, new Vertex(0, 0, 0)).length();
	}

	/**
	 * Traces color using model info
	 * @param objects Renderables
	 * @param lights Lights
	 * @param ambient ambient color
	 * @return color coefficients
	 */
	public Coefficient3D trace(Collection<Renderable> objects,
			Collection<Light> lights, Coefficient3D ambient) {
		return object.trace(this, objects, lights, ambient);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(200);
		sb.append("Intersection ");
		sb.append(intersection);
		sb.append(' ');
		sb.append(normal);
		sb.append("\r\n");
		sb.append(object);
		return sb.toString();
	}
}
