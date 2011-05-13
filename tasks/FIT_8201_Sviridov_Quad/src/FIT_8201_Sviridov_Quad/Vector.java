package FIT_8201_Sviridov_Quad;

/**
 *
 * @author alstein
 */
public class Vector {

    private Vertex vertex;

    public Vector(Vertex start, Vertex end) {
        Vertex s = start.normalize(),
                e = end.normalize();

        vertex = new Vertex(
                e.getX() - s.getX(),
                e.getY() - s.getY(),
                e.getZ() - s.getZ());
    }

    public static double length(Vector v){
        return v.length();
    }
    public double length() {
        double x = vertex.getX(),
                y = vertex.getY(),
                z = vertex.getZ();
        double length = Math.sqrt(x * x + y * y + z * z);
        return length;
    }

    public double dot(Vector other) {
        double x1 = this.getX(), y1 = this.getY(), z1 = this.getZ(),
                x2 = other.getX(), y2 = other.getY(), z2 = other.getZ();
        double dot = x1 * x2 + y1 * y2 + z1 * z2;
        return dot;
    }

    public static double dot(Vector v1, Vector v2) {
        double dot = v1.dot(v2);
        return dot;
    }

    public Vector cross(Vector other) {
        // (a_z b_y - a_y b_z,\; a_x b_z - a_z b_x,\; a_y b_x - a_x b_y).
        double ax = this.getX(), ay = this.getY(), az = this.getZ(),
                bx = other.getX(), by = other.getY(), bz = other.getZ();
        double x = ay*bz-by*az,
                y = -(ax * bz - az * bx),
                z = ax * by - ay * bx;
        return new Vector(x, y, z);
    }

    public static Vector cross(Vector v1, Vector v2) {
        Vector cross = v1.cross(v2);
        return cross;
    }


    public Vector normalize() {
        double x = getX(), y = getY(), z = getZ();
        double length = Math.sqrt(x * x + y * y + z * z);
        double ux = x / length, uy = y / length, uz = z / length;
        Vector v = new Vector(ux, uy, uz);
        return v;
    }

    public double getZ() {
        return vertex.getZ();
    }

    public double getY() {
        return vertex.getY();
    }

    public double getX() {
        return vertex.getX();
    }

    @Override
    public String toString() {
        return vertex.toString();
    }

    public Vector(double x, double y, double z) {
        vertex = new Vertex(x, y, z);
    }

    public Vector(Vertex vertex) {
        this.vertex = vertex.normalize();
    }
}
