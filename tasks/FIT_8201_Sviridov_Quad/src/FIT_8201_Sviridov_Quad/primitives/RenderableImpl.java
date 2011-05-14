package FIT_8201_Sviridov_Quad.primitives;

import FIT_8201_Sviridov_Quad.ColorModel;
import FIT_8201_Sviridov_Quad.Vector;
import FIT_8201_Sviridov_Quad.Vertex;
import FIT_8201_Sviridov_Quad.transformations.Transformation;
import java.util.List;

/**
 *
 * @author alstein
 */
public abstract class RenderableImpl extends Wireframe implements Renderable {

    private ColorModel colorModel;

    public ColorModel getColorModel() {
        return colorModel;
    }

    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;
    }

    @Override
    public Wireframe getWireframe() {
        return this;
    }

    @Override
    public void transform(Transformation transformation) {
        super.transform(transformation);
    }

    public RenderableImpl(List<Segment> segments, Vector v1, Vector v2, Vector v3, Vertex origin, ColorModel colorModel) {
        super(segments, v1, v2, v3, origin);
        this.colorModel = colorModel;
    }

    public RenderableImpl(List<Segment> segments, Vertex origin, ColorModel colorModel) {
        super(segments, origin);
        this.colorModel = colorModel;
    }

    public RenderableImpl(List<Segment> segments, ColorModel colorModel) {
        super(segments);
        this.colorModel = colorModel;
    }

}
