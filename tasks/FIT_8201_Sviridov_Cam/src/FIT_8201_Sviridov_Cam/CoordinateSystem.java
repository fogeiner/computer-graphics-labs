package FIT_8201_Sviridov_Cam;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author admin
 */
public class CoordinateSystem {

    private static final double EPS = 1e-12;
    private Vertex origin;
    private Vector v1;
    private Vector v2;
    private Vector v3;
    private Transformation frameToCanonicalTransformation;
    private static final NumberFormat format;

    static {
        format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }

    public CoordinateSystem(Vertex origin, Vector v1, Vector v2, Vector v3) {
        this.origin = origin;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;

        // check that coordinate system given is right-handed and orthonormal
        double a = v1.getX(),
                b = v2.getX(),
                c = v3.getX(),
                d = v1.getY(),
                e = v2.getY(),
                f = v3.getY(),
                g = v1.getZ(),
                h = v2.getZ(),
                i = v3.getZ();

        double v1Length = Math.sqrt(a * a + d * d + g * g);
        double v2Length = Math.sqrt(b * b + e * e + h * h);
        double v3Length = Math.sqrt(c * c + f * f + i * i);

        double v1v2Product = a * d + b * e + c * f,
                v1v3Product = a * g + b * h + c * i,
                v2v3Product = d * g + e * h + f * i;

        double det = a * (e * i - f * h) - b * (d * i - f * g) + c * (d * h - e * g);

        if (v1Length - 1.0 > EPS) {
            throw new IllegalArgumentException("v1 is not normalized");
        }
        if (v2Length - 1.0 > EPS) {
            throw new IllegalArgumentException("v2 is not normalized");
        }
        if (v3Length - 1.0 > EPS) {
            throw new IllegalArgumentException("v3 is not normalized");
        }

        if (v1v2Product > EPS) {
            throw new IllegalArgumentException("v1 and v2 not orthogonal");
        }
        if (v1v3Product > EPS) {
            throw new IllegalArgumentException("v1 and v3 not orthogonal");
        }
        if (v2v3Product > EPS) {
            throw new IllegalArgumentException("v2 and v3 not orthogonal");
        }

        if (det < 0) {
            throw new IllegalArgumentException("Right-hand coordinate system expected");
        }

        // compute transformation to world coordinate system
        double ox = origin.getX(),
                oy = origin.getY(),
                oz = origin.getZ();

        double v1xv = v1.getX() - ox,
                v2xv = v2.getX() - ox,
                v3xv = v3.getX() - ox,
                v1yv = v1.getY() - oy,
                v2yv = v2.getY() - oy,
                v3yv = v3.getY() - oy,
                v1zv = v1.getZ() - oz,
                v2zv = v2.getZ() - oz,
                v3zv = v3.getZ() - oz;

        frameToCanonicalTransformation = new Transformation(
                v1xv, v2xv, v3xv, ox,
                v1yv, v2yv, v3yv, oy,
                v1zv, v2zv, v3zv, oz,
                0, 0, 0, 1);
    }

    public Transformation getFrameToCanonicalTransformation() {
        return frameToCanonicalTransformation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("at ");
        sb.append(origin);
        sb.append(" with ");
        sb.append(v1);
        sb.append(' ');
        sb.append(v2);
        sb.append(' ');
        sb.append(v3);
        return sb.toString();

    }

    public static void main(String args[]) {
        CoordinateSystem cs = new CoordinateSystem(
                new Vertex(100, 50, 75),
                new Vector(1, 0, 0),
                new Vector(0, 1, 0),
                new Vector(0, 0, 1));
        System.out.println(cs);
        System.out.println(cs.getFrameToCanonicalTransformation());
    }
}
