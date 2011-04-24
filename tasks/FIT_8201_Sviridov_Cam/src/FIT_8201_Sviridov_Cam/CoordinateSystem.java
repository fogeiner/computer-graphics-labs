package FIT_8201_Sviridov_Cam;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class represents coordinate system
 * @author admin
 */
public class CoordinateSystem {

    private static final double EPS = 1e-12;
    private Vertex origin;
    private Vector v1;
    private Vector v2;
    private Vector v3;
    private static final NumberFormat format;

    static {
        format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }

    /**
     * Default constructor; origin at (0,0,0) and vectors are (1,0,0) (0,1,0) (0,0,1)
     */
    public CoordinateSystem() {
        this(new Vertex(0, 0, 0));
    }

    /**
     * Constructor; origin is given and vectors are (1,0,0) (0,1,0) (0,0,1)
     * @param origin origin
     */
    public CoordinateSystem(Vertex origin) {
        this(
                origin,
                new Vector(1, 0, 0),
                new Vector(0, 1, 0),
                new Vector(0, 0, 1));
    }

    /**
     * Checks basis for right-handed and ortonormal
     */
    private void checkBasis() {
        // check that coordinate system given is right-handed and orthonormal
        double v1x = v1.getX(),
                v2x = v2.getX(),
                v3x = v3.getX(),
                v1y = v1.getY(),
                v2y = v2.getY(),
                v3y = v3.getY(),
                v1z = v1.getZ(),
                v2z = v2.getZ(),
                v3z = v3.getZ();

        double v1Length = Math.sqrt(v1x * v1x + v1y * v1y + v1z * v1z);
        double v2Length = Math.sqrt(v2x * v2x + v2y * v2y + v2z * v2z);
        double v3Length = Math.sqrt(v3x * v3x + v3y * v3y + v3z * v3z);

        double v1v2Product = v1x * v2x + v1y * v2y + v1z * v2z,
                v1v3Product = v1x * v3x + v1y * v3y + v1z * v3z,
                v2v3Product = v2x * v3x + v2y * v3y + v2z * v3z;

        double det = v1x * (v2y * v3z - v3y * v2z) - v2x * (v1y * v3z - v3y * v1z) + v3x * (v1y * v2z - v2y * v1z);

        if (Math.abs(v1Length - 1.0) > EPS) {
            throw new IllegalArgumentException("v1 is not normalized");
        }
        if (Math.abs(v2Length - 1.0) > EPS) {
            throw new IllegalArgumentException("v2 is not normalized");
        }
        if (Math.abs(v3Length - 1.0) > EPS) {
            throw new IllegalArgumentException("v3 is not normalized");
        }

        if (Math.abs(v1v2Product) > EPS) {
            throw new IllegalArgumentException("v1 and v2 not orthogonal");
        }
        if (Math.abs(v1v3Product) > EPS) {
            throw new IllegalArgumentException("v1 and v3 not orthogonal");
        }
        if (Math.abs(v2v3Product) > EPS) {
            throw new IllegalArgumentException("v2 and v3 not orthogonal");
        }

        if (det < 0) {
            throw new IllegalArgumentException("Right-hand coordinate system expected");
        }

    }

    /**
     * Ctor with given origin and vectors
     * @param origin origin
     * @param v1 1st vector
     * @param v2 2ns vector
     * @param v3 3rd vector
     */
    public CoordinateSystem(Vertex origin, Vector v1, Vector v2, Vector v3) {
        this.origin = origin;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;

        checkBasis();
    }

    /**
     * Return origin
     * @return origin
     */
    public Vertex getOrigin() {
        return origin;
    }

    /**
     * Sets origin
     * @param origin origin
     */
    public void setOrigin(Vertex origin) {
        this.origin = origin;
    }

    /**
     * Sets basis
     * @param v1 1st vector
     * @param v2 2nd vector
     * @param v3 3rd vector
     */
    public void setBasis(Vector v1, Vector v2, Vector v3) {
        Vector v1Old = this.v1,
                v2Old = this.v2,
                v3Old = this.v3;

        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;

        try {
            checkBasis();
        } catch (IllegalArgumentException ex) {
            this.v1 = v1Old;
            this.v2 = v2Old;
            this.v3 = v3Old;
            throw ex;
        }
    }

    /**
     * Returns transformation from frame to world
     * @return
     */
    public Transformation getFrameToCanonicalTransformation() {
        double v1x = v1.getX(),
                v2x = v2.getX(),
                v3x = v3.getX(),
                v1y = v1.getY(),
                v2y = v2.getY(),
                v3y = v3.getY(),
                v1z = v1.getZ(),
                v2z = v2.getZ(),
                v3z = v3.getZ();


        // compute transformation to world coordinate system
        double ox = origin.getX(),
                oy = origin.getY(),
                oz = origin.getZ();

        Transformation frameToCanonicalTransformation = new Transformation(
                v1x, v2x, v3x, ox,
                v1y, v2y, v3y, oy,
                v1z, v2z, v3z, oz,
                0, 0, 0, 1);

        return frameToCanonicalTransformation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("origin at ");
        sb.append(origin);
        sb.append(" with vectors ");
        sb.append(v1);
        sb.append(' ');
        sb.append(v2);
        sb.append(' ');
        sb.append(v3);
        return sb.toString();
    }
}
