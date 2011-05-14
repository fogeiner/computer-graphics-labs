package FIT_8201_Sviridov_Quad;

import FIT_8201_Sviridov_Quad.primitives.Renderable;
import FIT_8201_Sviridov_Quad.primitives.Segment;
import FIT_8201_Sviridov_Quad.primitives.Wireframe;
import FIT_8201_Sviridov_Quad.transformations.Transformation;
import FIT_8201_Sviridov_Quad.transformations.TranslationTransformation;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author alstein
 */
public class Model {

    public static final Coefficient3D DEFAULT_AMBIENT = new Coefficient3D(1.0, 1.0, 1.0);
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.white;
    public static final double DEFAULT_GAMMA = 1.0;
    private Coefficient3D ambient = DEFAULT_AMBIENT;
    private Wireframe box;
    private List<Wireframe> orts = new ArrayList<Wireframe>(3);
    private List<Renderable> renderables = new ArrayList<Renderable>(5);
    private List<Light> lights = new ArrayList<Light>(5);
    private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private boolean finished;
    private double gamma;
    private int ntree;
    private double znear;
    private double zfar;

    private class ModelSavedState {

        private Coefficient3D ambient = DEFAULT_AMBIENT;
        private Wireframe box;
        private List<Wireframe> orts = new ArrayList<Wireframe>(3);
        private List<Renderable> renderables = new ArrayList<Renderable>(5);
        private List<Light> lights = new ArrayList<Light>(5);
        private Color backgroundColor;
        private boolean finished;
        private double gamma;
        private int ntree;
        private double znear;
        private double zfar;
    }
    private ModelSavedState savedState = new ModelSavedState();

    public List<Wireframe> getRenderablesWireframes() {
        if (!finished) {
            throw new IllegalStateException("Model is not finished");
        }
        List<Wireframe> wireframes = new ArrayList<Wireframe>(renderables.size());
        for (Renderable renderable : renderables) {
            wireframes.add(renderable.getWireframe());
        }
        return wireframes;
    }

    private List<SceneObject> getAllSceneObjects() {
        if (!finished) {
            throw new IllegalStateException("Model is not finished");
        }
        List<SceneObject> sceneObjects = new ArrayList<SceneObject>(8);
        sceneObjects.add(box);
        sceneObjects.addAll(orts);
        sceneObjects.addAll(getRenderablesWireframes());
        return sceneObjects;
    }

    public void finishModel() {
        finished = true;
        {// {max, min} {x,y,z} coordinates of objects
            double maxX = Double.NEGATIVE_INFINITY,
                    minX = Double.POSITIVE_INFINITY,
                    maxY = Double.NEGATIVE_INFINITY,
                    minY = Double.POSITIVE_INFINITY,
                    maxZ = Double.NEGATIVE_INFINITY,
                    minZ = Double.POSITIVE_INFINITY;

            for (Wireframe wireframe : getRenderablesWireframes()) {
                for (Segment s : wireframe.getSegments()) {
                    Vertex start = s.getStartVertex();
                    Vertex end = s.getEndVertex();
                    double sx = start.getX(),
                            sy = start.getY(),
                            sz = start.getZ(),
                            ex = end.getX(),
                            ey = end.getY(),
                            ez = end.getZ();

                    maxX = Math.max(maxX, Math.max(sx, ex));
                    maxY = Math.max(maxY, Math.max(sy, ey));
                    maxZ = Math.max(maxZ, Math.max(sz, ez));

                    minX = Math.min(minX, Math.min(sx, ex));
                    minY = Math.min(minY, Math.min(sy, ey));
                    minZ = Math.min(minZ, Math.min(sz, ez));
                }
            }

            // box

            box = new Wireframe(
                    Wireframe.box(maxX - minX, maxY - minY, maxZ - minZ),
                    new Vertex((maxX + minX) / 2, (maxY + minY) / 2, (maxZ + minZ) / 2));
        }

        {
            // orts
            Vertex boxOrigin = box.getOrigin();
            Rect3D boxSize = this.box.getBoundRect3D();

            Wireframe ortX = new Wireframe(Arrays.asList(new Segment[]{new Segment(
                        new Vertex(1.1 * boxSize.getWidth() / 2, 0, 0))}), boxOrigin),
                    ortY = new Wireframe(Arrays.asList(new Segment[]{new Segment(
                        new Vertex(0, 1.1 * boxSize.getHeight() / 2, 0))}), boxOrigin),
                    ortZ = new Wireframe(Arrays.asList(new Segment[]{new Segment(
                        new Vertex(0, 0, 1.1 * boxSize.getDepth() / 2))}), boxOrigin);

            // XYZ -> RGB
            ortX.setColor(Color.red);
            ortX.setWidth(3);

            ortY.setColor(Color.green);
            ortY.setWidth(3);

            ortZ.setColor(Color.blue);
            ortZ.setWidth(3);

            orts.add(ortX);
            orts.add(ortY);
            orts.add(ortZ);

            // znear/zfar
            double d = (boxSize.getWidth() / 2) / Math.tan(Math.toRadians(15.0));
            double cameraX = boxOrigin.getX(),
                    cameraY = boxOrigin.getY(),
                    cameraZ = boxOrigin.getZ() + boxSize.getDepth() / 2.0 + d;

            transform(new TranslationTransformation(-cameraX, -cameraY, -cameraZ));

            znear = d;
            zfar = znear + 2.5 * boxSize.getDepth();
        }

        {
            savedState.ambient = this.ambient;
            savedState.backgroundColor = this.backgroundColor;
            savedState.gamma = this.gamma;
            savedState.ntree = this.ntree;
            savedState.box = (Wireframe) this.box.clone();

            for (Wireframe ort : orts) {
                savedState.orts.add((Wireframe) ort.clone());
            }

            for (Renderable renderable : renderables) {
                savedState.renderables.add((Renderable) renderable.clone());
            }
            for (Light light : lights) {
                savedState.lights.add((Light) light.clone());
            }
            savedState.finished = this.finished;
            savedState.znear = this.znear;
            savedState.zfar = this.zfar;
        }
    }

    public void setAmbient(Coefficient3D ambient) {
        this.ambient = ambient;
    }

    public void addRenderable(Renderable renderable) {
        renderables.add(renderable);
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public void transform(Transformation transformation) {
        if (!finished) {
            throw new IllegalStateException("Model is not finished");
        }
        for (SceneObject so : getAllSceneObjects()) {
            so.transform(transformation);
        }
    }

    public void centralRotation(Transformation rotation) {
        if (!finished) {
            throw new IllegalStateException("Model is not finished");
        }
        Vertex boxOrigin = box.getOrigin();
        Transformation centralRotation = null;
        centralRotation = new TranslationTransformation(-boxOrigin.getX(), -boxOrigin.getY(), -boxOrigin.getZ());
        centralRotation.compose(rotation);
        centralRotation.compose(new TranslationTransformation(boxOrigin.getX(), boxOrigin.getY(), boxOrigin.getZ()));

        transform(centralRotation);
    }

    public Wireframe getBox() {
        if (!finished) {
            throw new IllegalStateException("Model is not finished");
        }
        return box;
    }

    public List<Wireframe> getOrts() {
        if (!finished) {
            throw new IllegalStateException("Model is not finished");
        }
        return orts;
    }

    public List<Renderable> getRenderables() {
        if (!finished) {
            throw new IllegalStateException("Model is not finished");
        }
        return renderables;
    }

    public Coefficient3D getAmbient() {
        return ambient;
    }

    public List<Light> getLights() {
        return lights;
    }

    public double getZfar() {
        return zfar;
    }

    public double getZnear() {
        return znear;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public int getNtree() {
        return ntree;
    }

    public void setNtree(int ntree) {
        this.ntree = ntree;
    }

    public Rect3D getInitialBoxRect3D() {
        return savedState.box.getBoundRect3D();
    }

    public Vertex getInitialModelCenter() {
        return savedState.box.getOrigin();
    }

    public void resetModel() {
        this.ambient = savedState.ambient;
        this.backgroundColor = savedState.backgroundColor;
        this.gamma = savedState.gamma;
        this.ntree = savedState.ntree;
        this.box = (Wireframe) savedState.box.clone();

        this.orts.clear();
        for (Wireframe ort : savedState.orts) {
            this.orts.add((Wireframe) ort.clone());
        }

        this.renderables.clear();
        for (Renderable renderable : savedState.renderables) {
            this.renderables.add((Renderable) renderable.clone());
        }

        this.lights.clear();
        for (Light light : savedState.lights) {
            this.lights.add((Light) light.clone());
        }

        this.finished = savedState.finished;
        this.znear = savedState.znear;
        this.zfar = savedState.zfar;
    }
}
