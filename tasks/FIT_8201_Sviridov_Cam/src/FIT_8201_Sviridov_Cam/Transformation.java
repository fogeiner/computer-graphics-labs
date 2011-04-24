package FIT_8201_Sviridov_Cam;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author alstein
 */
public class Transformation {

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

    public Transformation(double[][] m) {
        for (int i = 0; i < this.m.length; ++i) {
            for (int j = 0; j < this.m.length; ++j) {
                this.m[i][j] = m[i][j];
            }
        }
    }

    public Transformation(double m11, double m12, double m13, double m14,
            double m21, double m22, double m23, double m24,
            double m31, double m32, double m33, double m34,
            double m41, double m42, double m43, double m44) {
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
     * 1 0 0 0   
     * 0 1 0 0    
     * 0 0 1 0   
     * 0 0 0 1
     * @return
     */
    public static Transformation identity() {
        Transformation t = new Transformation(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
        return t;
    }

    public void compose(Transformation leftTransformation) {
        double m1[][] = leftTransformation.m; // n x k X k x m -> n (rows) x m (cols)
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

    // A B C x = A(B(Cx))
    // Transformation t1 = identity()
    // t1.compose(translate())
    // t1.compose(rotate())
    // first translate, then rotate, i.e. t2 is on the left!
    public static Transformation compose(Transformation t1, Transformation t2) {
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
        return new Transformation(m3);
    }

    public Vertex apply(Vertex vertex) {
        double v[] = vertex.getV();
        double newV[] = new double[v.length];
        // m11 m12 m13   v1
        // m21 m22 m23   v2
        // m31 m32 m33   v3

        for (int i = 0; i < m.length; ++i) {
            for (int j = 0; j < m[0].length; ++j) {
                newV[i] += m[i][j] * v[j];
            }
        }

        Vertex result = new Vertex(newV);
        return result;
    }

    public static Transformation uniformScale(double s) {
        return Transformation.scale(s, s, s);
    }

    public static Transformation scale(double xs, double ys, double zs) {
        Transformation t;
        t = new Transformation(
                xs, 0, 0, 0,
                0, ys, 0, 0,
                0, 0, zs, 0,
                0, 0, 0, 1);
        return t;
    }

    public static Transformation translate(double tx, double ty, double tz) {
        Transformation t;
        t = new Transformation(
                1, 0, 0, tx,
                0, 1, 0, ty,
                0, 0, 1, tz,
                0, 0, 0, 1);
        return t;
    }

    // cos -sin 0
    // sin  cos 0
    // 0    0   1
    public static Transformation rotate(double phi, int axis) {
        Transformation t;

        double c = Math.cos(phi);
        double s = Math.sin(phi);
        double m = -s;

        if (axis == Transformation.X_AXIS) {
            t = new Transformation(
                    1, 0, 0, 0,
                    0, c, m, 0,
                    0, s, c, 0,
                    0, 0, 0, 1);
        } else if (axis == Transformation.Y_AXIS) {
            t = new Transformation(
                    c, 0, s, 0,
                    0, 1, 0, 0,
                    m, 0, c, 0,
                    0, 0, 0, 1);
        } else if (axis == Transformation.Z_AXIS) {
            t = new Transformation(
                    c, m, 0, 0,
                    s, c, 0, 0,
                    0, 0, 1, 0,
                    0, 0, 0, 1);
        } else {
            throw new IllegalArgumentException("Unknown axis; use {X,Y,Z}_AXIS instead");
        }

        return t;
    }

    public static Transformation perspective(double l, double r, double b, double t, double n, double f) {
        Transformation transformation;
        double a11 = 2*n/(r-l),
                a12 = 0,
                a13 = (r+l)/(r-l),
                a14 = 0,
                a21 = 0,
                a22 = 2*n/(t-b),
                a23 = (t+b)/(t-b),
                a24 = 0,
                a31 = 0,
                a32 = 0,
                a33 = -(f+n)/(f-n),
                a34 = -2*f*n/(f-n),
                a41 = 0,
                a42 = 0,
                a43 = -1,
                a44 = 0;
        transformation = new Transformation(
                a11, a12, a13, a14,
                a21, a22, a23, a24,
                a31, a32, a33, a34,
                a41, a42, a43, a44);

        return transformation;
    }

    public static Transformation worldToView() {
        Transformation t;
        t = new Transformation(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, -1, 0,
                0, 0, 0, 1);
        return t;
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

    public static void main(String args[]) {
        Transformation scale = Transformation.uniformScale(2);
        Transformation perspective = Transformation.perspective(-10, 10, -20, 20, 10, 30);
        Vertex v = new Vertex(1, -1, 20);
        Vertex v1 = scale.apply(v);
        Vertex v2 = scale.apply(v1);
        Vertex r1 = perspective.apply(v1);
        r1 = r1.normalize();
        Vertex r2 = perspective.apply(v2);
        r2 = r2.normalize();
        System.out.println(r1);
        System.out.println(r2);

        Vertex v3 = new Vertex(1, 1, 1);
        Transformation rotX = Transformation.rotate(Math.PI/3, Transformation.X_AXIS);
        Transformation rotY = Transformation.rotate(Math.PI/6, Transformation.Y_AXIS);
        Transformation c1 = Transformation.compose(rotX, rotY);
        Transformation c2 = Transformation.compose(rotY, rotX);
        System.out.println(c1.apply(v3));
        System.out.println(c2.apply(v3));
    }
}
