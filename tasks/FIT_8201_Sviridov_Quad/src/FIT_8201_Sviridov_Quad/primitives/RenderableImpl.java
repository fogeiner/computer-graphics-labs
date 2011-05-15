package FIT_8201_Sviridov_Quad.primitives;

import FIT_8201_Sviridov_Quad.Coefficient3D;
import FIT_8201_Sviridov_Quad.ColorModel;
import FIT_8201_Sviridov_Quad.IntersectionInfo;
import FIT_8201_Sviridov_Quad.Light;
import FIT_8201_Sviridov_Quad.Ray;
import FIT_8201_Sviridov_Quad.Vector;
import FIT_8201_Sviridov_Quad.Vertex;
import FIT_8201_Sviridov_Quad.transformations.Transformation;
import java.util.Collection;
import java.util.List;

/**
 * Abstarct implementation of the renderable interface
 * 
 * @author alstein
 */
public abstract class RenderableImpl extends Wireframe implements Renderable {

	private ColorModel colorModel;

	/**
	 * Return ColorModel
	 * 
	 * @return ColorModel
	 */
	public ColorModel getColorModel() {
		return colorModel;
	}

	/**
	 * Sets ColorModel
	 * 
	 * @param colorModel
	 *            ColorModel
	 */
	public void setColorModel(ColorModel colorModel) {
		this.colorModel = colorModel;
	}

	@Override
	public Wireframe getWireframe() {
		return this;
	}

	@Override
	public void transform(Transformation transformation) {
		super.transform(transformation);
	}

	/**
	 * Ctor
	 * 
	 * @param segments
	 *            segments
	 * @param v1
	 *            basis vector 1
	 * @param v2
	 *            basis vector 2
	 * @param v3
	 *            basis vector 3
	 * @param origin
	 *            origin
	 * @param colorModel
	 *            colorModel
	 */
	public RenderableImpl(List<Segment> segments, Vector v1, Vector v2,
			Vector v3, Vertex origin, ColorModel colorModel) {
		super(segments, v1, v2, v3, origin);
		this.colorModel = colorModel;
	}

	/**
	 * Ctor
	 * 
	 * @param segments
	 *            segments
	 * @param origin
	 *            origin
	 * @param colorModel
	 *            colorModel
	 */
	public RenderableImpl(List<Segment> segments, Vertex origin,
			ColorModel colorModel) {
		super(segments, origin);
		this.colorModel = colorModel;
	}

	/**
	 * Ctor
	 * 
	 * @param segments
	 *            segments
	 * @param colorModel
	 *            colorModel
	 */
	public RenderableImpl(List<Segment> segments, ColorModel colorModel) {
		super(segments);
		this.colorModel = colorModel;
	}

	@Override
	public Object clone() {
		return super.clone();
	}

	@Override
	public Coefficient3D trace(IntersectionInfo intersectionInfo,
			Collection<Renderable> objects, Collection<Light> lights,
			Coefficient3D ambient) {

		Vector n = intersectionInfo.getNormal();
		Vertex p = intersectionInfo.getIntersection();
		Vector e = new Vector(p, new Vertex(0, 0, 0)).normalize();

		ColorModel cm = getColorModel();

		Coefficient3D ambientCoefficient = cm.getAmbientCoefficient(), diffuseCoefficient = cm
				.getDiffuseCoefficient(), specularCoefficient = cm
				.getSpecularCoefficient();

		double R = ambient.getR() * ambientCoefficient.getR(), G = ambient
				.getG() * ambientCoefficient.getG(), B = ambient.getB()
				* ambientCoefficient.getB();

		lightCycle: for (Light light : lights) {
			Vector l = new Vector(p, light.getOrigin());

			double distanceToLight = l.length();
			l = l.normalize();
			double nl = n.dot(l);

			// is light visible?
			// 1. it's on the right side
			if (nl < 0) {
				continue;
			}
			// 2. it's not hidden by some other object
			// check all the objects (except current one!) if they intersect
			// ray from p to l and if so if there's at least one object
			// that's closer to point than light source -- it's hidden
			Ray ray = new Ray(p, l);
			for (Renderable renderable : objects) {
				if (renderable == this) {
					continue;
				}

				Collection<IntersectionInfo> intersections = renderable
						.intersect(ray);

				for (IntersectionInfo ii : intersections) {
					double distance = new Vector(p, ii.getIntersection())
							.length();

					if (distance < distanceToLight) {
						continue lightCycle;
					}
				}
			}

			Coefficient3D I = light.getColor();

			double fatt = 1.0 / (1 + distanceToLight);

			Vector h = new Vector(l.getX() + e.getX(), l.getY() + e.getY(),
					l.getZ() + e.getZ()).normalize();

			double nhpow = Math.pow(n.dot(h), cm.getPower());

			R += fatt
					* I.getR()
					* (diffuseCoefficient.getR() * nl + specularCoefficient
							.getR() * nhpow);
			G += fatt
					* I.getG()
					* (diffuseCoefficient.getG() * nl + specularCoefficient
							.getG() * nhpow);
			B += fatt
					* I.getB()
					* (diffuseCoefficient.getB() * nl + specularCoefficient
							.getB() * nhpow);
		}

		return new Coefficient3D(R, G, B);
	}
}
