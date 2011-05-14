/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Quad.transformations;

/**
 *
 * @author alstein
 */
public class IdentityTransformation extends TransformationMatrixImpl {

    public IdentityTransformation() {
        super(1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
    }
}
