package FIT_8201_Sviridov_Quad.transformations;

/**
 *
 * @author alstein
 */
public class RotationTransformation extends TransformationMatrixImpl {

    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    public static final int Z_AXIS = 2;

    public static RotationTransformation makeRotation(double phi, int axis) {

        double c = Math.cos(phi);
        double s = Math.sin(phi);
        double m = -s;

        RotationTransformation rotation = null;

        if (axis == RotationTransformation.X_AXIS) {
            rotation = new RotationTransformation(1, 0, 0, 0, 0, c, m, 0, 0, s, c, 0, 0, 0, 0, 1);
        } else if (axis == RotationTransformation.Y_AXIS) {
            rotation = new RotationTransformation(c, 0, s, 0, 0, 1, 0, 0, m, 0, c, 0, 0, 0, 0, 1);
        } else if (axis == RotationTransformation.Z_AXIS) {
            rotation = new RotationTransformation(c, m, 0, 0, s, c, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
        } else {
            throw new IllegalArgumentException(
                    "Unknown axis; use {X,Y,Z}_AXIS instead");
        }

        return rotation;
    }

    private RotationTransformation(double m11, double m12, double m13, double m14, double m21, double m22, double m23, double m24, double m31, double m32, double m33, double m34, double m41, double m42, double m43, double m44) {
        super(m11, m12, m13, m14, m21, m22, m23, m24, m31, m32, m33, m34, m41, m42, m43, m44);
    }
}
