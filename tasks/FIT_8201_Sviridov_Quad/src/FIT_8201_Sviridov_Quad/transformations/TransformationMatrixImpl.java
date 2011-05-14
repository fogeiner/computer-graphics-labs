package FIT_8201_Sviridov_Quad.transformations;

import FIT_8201_Sviridov_Quad.Vertex;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author alstein
 */
public abstract class TransformationMatrixImpl implements Transformation {

    private double m[][] = new double[4][4];
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
    public TransformationMatrixImpl(double[][] m) {
        for (int i = 0; i < this.m.length; ++i) {
            for (int j = 0; j < this.m.length; ++j) {
                this.m[i][j] = m[i][j];
            }
        }
    }

    /**
     * Ctor for transformation with given matrix elements
     */
    public TransformationMatrixImpl(double m11, double m12, double m13, double m14,
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

    @Override
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

    @Override
    public void compose(Transformation leftTransformation) {
        double m1[][] = leftTransformation.getMatrixRepresentation(); // n x k X k x m -> n (rows) x m
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

    @Override
    public double[][] getMatrixRepresentation() {
        return m;
    }
}
