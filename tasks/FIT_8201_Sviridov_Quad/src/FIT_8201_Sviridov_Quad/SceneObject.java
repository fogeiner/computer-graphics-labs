package FIT_8201_Sviridov_Quad;

import FIT_8201_Sviridov_Quad.transformations.Transformation;

/**
 * Interface of all scene objects that support transformations
 * 
 * @author alstein
 */
public interface SceneObject {

	public void transform(Transformation transformation);
}
