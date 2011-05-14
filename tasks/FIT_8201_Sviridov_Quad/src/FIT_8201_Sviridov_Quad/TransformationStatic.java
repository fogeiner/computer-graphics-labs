package FIT_8201_Sviridov_Quad;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class for transformations
 * 
 * @author alstein
 */
public class TransformationStatic {

    private double[][] m = new double[4][4];
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    public static final int Z_AXIS = 2;
    private static final NumberFormat format;

    static {
        format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }

    /**
     * Ctor for tranformation with given matrix m
     *
     * @param m
     */
    public TransformationStatic(double[][] m) {
        for (int i = 0; i < this.m.length; ++i) {
            for (int j = 0; j < this.m.length; ++j) {
                this.m[i][j] = m[i][j];
            }
        }
    }

    /**
     * Ctor for transformation with given matrix elements
     *
     * @param m11
     *            m11
     * @param m12
     *            m12
     * @param m13
     *            m13
     * @param m14
     *            m14
     * @param m21
     *            m21
     * @param m22
     *            m22
     * @param m23
     *            m23
     * @param m24
     *            n24
     * @param m31
     *            m31
     * @param m32
     *            m32
     * @param m33
     *            m33
     * @param m34
     *            m34
     * @param m41
     *            m41
     * @param m42
     *            m42
     * @param m43
     *            m43
     * @param m44
     *            m44
     */
    public TransformationStatic(double m11, double m12, double m13, double m14,
            double m21, double m22, double m23, double m24, double m31,
            double m32, double m33, double m34, double m41, double m42,
            double m43, double m44) {
        m[0][0] = m11;
        m[0][1] = m12;
        m[0][2] = m13;
        m[0][3] = m14;
        m[1][0] = m21;
        m[1][1] = m22;
        m[1][2] = m23;
        m[1][3] = m24;
        m[2][0] = m31;
        m[2][1] = m32;
        m[2][2] = m33;
        m[2][3] = m34;
        m[3][0] = m41;
        m[3][1] = m42;
        m[3][2] = m43;
        m[3][3] = m44;
    }

    /**
     * Returns identity transformation 1 0 0 0 0 1 0 0 0 0 1 0 0 0 0 1
     *
     * @return identity transformation
     */
    public static TransformationStatic identity() {
        TransformationStatic t = new TransformationStatic(1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1,
                0, 0, 0, 0, 1);
        return t;
    }

    /**
     * Composes this transformation with given (given is on the left)
     *
     * @param leftTransformation
     *            another transformation
     */
    public void compose(TransformationStatic leftTransformation) {
        double m1[][] = leftTransformation.m; // n x k X k x m -> n (rows) x m
        // (cols)
        double m2[][] = this.m;
        double m3[][] = new double[m1.length][m2[0].length];
        for (int i = 0; i < m1.length; ++i) {
            for (int j = 0; j < m2[0].length; ++j) {
                double v = 0;
                for (int k = 0; k < m1[0].length; ++k) {
                    v += m1[i][k] * m2[k][j];
                }
                m3[i][j] = v;
            }
        }
        this.m = m3;
    }

    /**
     * Composes two given transformations <code>
    A B C x = A(B(Cx))
    Transformation t1 = identity()
    t1.compose(translate())
    t1.compose(rotate())
    </code> first translate, then rotate,
     * i.e. t2 is on the left!
     *
     * @param t1
     *            1st transformation
     * @param t2
     *            2nd transformation
     */
    public static TransformationStatic compose(TransformationStatic t1, TransformationStatic t2) {
        double m1[][] = t1.m; // n x k X k x m -> n (rows) x m (cols)
        double m2[][] = t2.m;
        double m3[][] = new double[m1.length][m2[0].length];
        for (int i = 0; i < m1.length; ++i) {
            for (int j = 0; j < m2[0].length; ++j) {
                double v = 0;
                for (int k = 0; k < m1[0].length; ++k) {
                    v += m1[i][k] * m2[k][j];
                }
                m3[i][j] = v;
            }
        }
        return new TransformationStatic(m3);
    }

    /**
     * Applies transformation to given vertex
     *
     * @param vertex
     *            original vertex
     * @return transformed vertex
     */
    public Vertex apply(Vertex vertex) {
        double v[] = vertex.getV();
        double newV[] = new double[v.length];
        // m11 m12 m13 v1
        // m21 m22 m23 v2
        // m31 m32 m33 v3

        for (int i = 0; i < m.length; ++i) {
            for (int j = 0; j < m[0].length; ++j) {
                newV[i] += m[i][j] * v[j];
            }
        }

        Vertex result = new Vertex(newV);
        return result;
    }

    /**
     * Returns uniform scale transformation
     *
     * @param s
     *            scale coefficient
     * @return uniform scale transformation
     */
    public static TransformationStatic uniformScale(double s) {
        return TransformationStatic.scale(s, s, s);
    }

    /**
     * Returns scale transformation
     *
     * @param xs
     *            x coefficient
     * @param ys
     *            y coefficient
     * @param zs
     *            z coefficient
     * @return scale transformation
     */
    public static TransformationStatic scale(double xs, double ys, double zs) {
        TransformationStatic t;
        t = new TransformationStatic(xs, 0, 0, 0, 0, ys, 0, 0, 0, 0, zs, 0, 0, 0, 0,
                1);
        return t;
    }

    /**
     * Returns translate transformation
     *
     * @param tx
     *            x shift
     * @param ty
     *            y shift
     * @param tz
     *            z shift
     * @return translate transformation
     */
    public static TransformationStatic translate(double tx, double ty, double tz) {
        TransformationStatic t;
        t = new TransformationStatic(1, 0, 0, tx, 0, 1, 0, ty, 0, 0, 1, tz, 0, 0, 0,
                1);
        return t;
    }

    /**
     * Returns rotate transformation
     *
     * @param phi
     *            angle
     * @param axis
     *            axis
     * @return rotate transformation
     */
    public static TransformationStatic rotate(double phi, int axis) {
        TransformationStatic t;

        double c = Math.cos(phi);
        double s = Math.sin(phi);
        double m = -s;

        if (axis == TransformationStatic.X_AXIS) {
            t = new TransformationStatic(1, 0, 0, 0, 0, c, m, 0, 0, s, c, 0, 0, 0, 0,
                    1);
        } else if (axis == TransformationStatic.Y_AXIS) {
            t = new TransformationStatic(c, 0, s, 0, 0, 1, 0, 0, m, 0, c, 0, 0, 0, 0,
                    1);
        } else if (axis == TransformationStatic.Z_AXIS) {
            t = new TransformationStatic(c, m, 0, 0, s, c, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                    1);
        } else {
            throw new IllegalArgumentException(
                    "Unknown axis; use {X,Y,Z}_AXIS instead");
        }

        return t;
    }

    /**
     * Returns perspective projection (by OpenGL specification)
     *
     * @param l
     *            left
     * @param r
     *            right
     * @param b
     *            bottom
     * @param t
     *            top
     * @param n
     *            near
     * @param f
     *            far
     * @return perspective projection (by OpenGL glFrustum specification)
     */
    public static TransformationStatic perspective(double l, double r, double b,
            double t, double n, double f) {
        TransformationStatic transformation;
        double a11 = 2 * n / (r - l), a12 = 0, a13 = (r + l) / (r - l), a14 = 0, a21 = 0, a22 = 2
                * n / (t - b), a23 = (t + b) / (t - b), a24 = 0, a31 = 0, a32 = 0, a33 = -(f + n)
                / (f - n), a34 = -2 * f * n / (f - n), a41 = 0, a42 = 0, a43 = -1, a44 = 0;
        transformation = new TransformationStatic(a11, a12, a13, a14, a21, a22, a23,
                a24, a31, a32, a33, a34, a41, a42, a43, a44);

        return transformation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < m.length; ++i) {
            for (int j = 0; j < m[0].length; ++j) {
                sb.append(format.format(m[i][j]));
                sb.append(' ');
            }
            sb.append('\n');
        }

        return sb.toString();
    }
}
