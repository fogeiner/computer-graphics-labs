package FIT_8201_Sviridov_Quad.transformations;

/**
 *
 * @author alstein
 */
public class PerspectiveProjectionTransformation extends TransformationMatrixImpl {

    public PerspectiveProjectionTransformation(double sw, double sh, double n, double f) {
//        super(2 * n / (r - l), 0, (r + l) / (r - l), 0,
//                0, 2 * n / (t - b), (t + b) / (t - b), 0,
//                0, 0, -(f + n) / (f - n), -2 * f * n / (f - n),
//                0, 0, -1, 0);

           super(2 * n / sw, 0, 0, 0,
                0, 2 * n / sh, 0, 0,
                0, 0, n / (f - n), - f * n / (f - n),
                0, 0, -1, 0);

    }
}
