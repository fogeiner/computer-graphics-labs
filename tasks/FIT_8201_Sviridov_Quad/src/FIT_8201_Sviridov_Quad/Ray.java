package FIT_8201_Sviridov_Quad;

/**
 * Class represents ray
 * 
 * @author alstein
 */
public class Ray {

	public static final double EPS = 10e-12;
	private final Vertex origin;
	private final Vector direction;

	/**
	 * Ctor
	 * 
	 * @param origin
	 *            origin
	 * @param direction
	 *            direction
	 */
	public Ray(Vertex origin, Vector direction) {
		this.origin = origin;
		this.direction = direction.normalize();
	}

	/**
	 * Ctor
	 * 
	 * @param start
	 *            start
	 * @param end
	 *            end
	 */
	public Ray(Vertex start, Vertex end) {
		this.origin = start;
		this.direction = new Vector(start, end).normalize();
	}

	/**
	 * Ctor origin at 0, 0, 0
	 * 
	 * @param direction
	 *            direction
	 */
	public Ray(Vector direction) {
		this.origin = new Vertex(0, 0, 0);
		this.direction = direction.normalize();
	}

	/**
	 * Returns direction
	 * 
	 * @return direction
	 */
	public Vector getDirection() {
		return direction;
	}

	/**
	 * Returns origin
	 * 
	 * @return origin
	 */
	public Vertex getOrigin() {
		return origin;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(20);
		sb.append("Ray ");
		sb.append(origin);
		sb.append(" + ");
		sb.append(direction);
		sb.append("t");
		return sb.toString();
	}
}
