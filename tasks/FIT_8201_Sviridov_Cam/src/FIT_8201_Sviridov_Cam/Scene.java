package FIT_8201_Sviridov_Cam;

import FIT_8201_Sviridov_Cam.primitives.Segment;
import FIT_8201_Sviridov_Cam.primitives.WireframeShape;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * Class for scene
 * 
 * @author alstein
 */
public class Scene extends JPanel {

    private static final long serialVersionUID = -2311010553599403447L;
    private static final double DEFAULT_ROTATE_COEF = Math.PI / 32;
    private static final double DEFAULT_ROLL_COEF = 25.0;
    private static final double ORT_COEF = 1.1;
    private double rollCoef = DEFAULT_ROLL_COEF;
    private double rotateCoef = 1 / 5.0;
    // all scene objects
    private List<WireframeShape> sceneObjects;
    // orts
    private List<WireframeShape> orts;
    // bound box
    private WireframeShape box;
    // projection matrix
    private double znear, zfar;
    private boolean ortsHidden;
    private boolean boxHidden;
    private boolean objectsHidden;
    // mouse handler
    private MouseHandler mouseHandler;
    private Transformation worldToCamera;
    private Transformation worldTransformation;
    private Double savedZnear, savedZfar;

    {
        sceneObjects = new ArrayList<WireframeShape>(3);
        orts = new ArrayList<WireframeShape>(3);
        mouseHandler = new MouseHandler();
        worldTransformation = Transformation.identity();
    }

    /**
     * Class to handle MouseMotion and MouseWheel events, alter settins of the
     * model and schedule repaint procedure
     */
    class MouseHandler extends MouseAdapter implements MouseMotionListener,
            MouseWheelListener {

        private Point lastPoint;

        @Override
        public void mousePressed(MouseEvent e) {
            Point currentPoint = e.getPoint();
            lastPoint = currentPoint;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            lastPoint = null;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point currentPoint = e.getPoint();

            double dx = -(lastPoint.getX() - currentPoint.getX()), dy = -(lastPoint.getY() - currentPoint.getY());

            double length = Math.hypot(dx, dy);
            if (length == 0) {
                return;
            }
            dx = dx / length * rotateCoef * DEFAULT_ROTATE_COEF;
            dy = dy / length * rotateCoef * DEFAULT_ROTATE_COEF;

            lastPoint = currentPoint;

            Vertex boxOrigin = worldTransformation.apply(box.getOrigin());

            Transformation t = Transformation.identity();

            t.compose(Transformation.translate(-boxOrigin.getX(),
                    -boxOrigin.getY(), -boxOrigin.getZ()));
            t.compose(Transformation.rotate(dx, Transformation.Y_AXIS));
            t.compose(Transformation.rotate(dy, Transformation.X_AXIS));
            t.compose(Transformation.translate(boxOrigin.getX(),
                    boxOrigin.getY(), boxOrigin.getZ()));

            worldTransformation.compose(t);

            repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int wheelRotationSign = (int) Math.signum(e.getWheelRotation());
            Transformation t = Transformation.translate(
                    0, 0, wheelRotationSign * rollCoef);
            worldTransformation.compose(t);
            repaint();
        }
    }

    /**
     * Create scene objects and set their origins in the world coordinate system
     */
    private void initSceneObjects() {
//        WireframeShape cube = WireframeShape.cube(1000);
//        cube.setOrigin(new Vertex(0, 0, 0));
//        sceneObjects.add(cube);
        double c = 1 / Math.sqrt(2);
        int sSteps = 30, tSteps = 30;

        WireframeShape sq1 = WireframeShape.superquadric(115, tSteps, sSteps,
                0.5, 0.5);
        sq1.setOrigin(new Vertex(-150, 180, 30));
        sq1.setBasis(new Vector(1, 0, 0), new Vector(0, -c, c), new Vector(0,
                -c, -c));
        sceneObjects.add(sq1);

        WireframeShape sq2 = WireframeShape.superquadric(150, tSteps, sSteps,
                1, 3);
        sq2.setOrigin(new Vertex(0, 0, 0));
        sq2.setBasis(new Vector(0, c, c), new Vector(-1, 0, 0), new Vector(0,
                -c, c));
        sceneObjects.add(sq2);

        WireframeShape sq3 = WireframeShape.superquadric(120, tSteps, sSteps,
                2, 1);
        sq3.setOrigin(new Vertex(180, -140, 60));
        sq3.setBasis(new Vector(-1, 0, 0), new Vector(0, -c, -c), new Vector(0,
                -c, c));
        sceneObjects.add(sq3);
    }

    /**
     * Based on objects and their coordinates in the world coordinate system
     * computes the size and location of the bound box
     */
    private void initBoundBox() {
        double maxX = Double.NEGATIVE_INFINITY,
                minX = Double.POSITIVE_INFINITY,
                maxY = Double.NEGATIVE_INFINITY,
                minY = Double.POSITIVE_INFINITY,
                maxZ = Double.NEGATIVE_INFINITY,
                minZ = Double.POSITIVE_INFINITY;

        for (WireframeShape shape : sceneObjects) {
            for (Segment s : shape.getSegments()) {
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

        double boxX = (maxX + minX) / 2,
                boxY = (maxY + minY) / 2,
                boxZ = (maxZ + minZ) / 2;
        box = WireframeShape.parallelepiped(maxX - minX, maxY - minY, maxZ - minZ);
        box.setOrigin(new Vertex(boxX, boxY, boxZ));
    }

    /**
     * Based on bound box WireframeShape creates 3 orts
     */
    private void initOrts() {
        Rect3D rect = this.box.getBoundRect3D();

        WireframeShape x = WireframeShape.segment(ORT_COEF * rect.getWidth() / 2, 0, 0),
                y = WireframeShape.segment(0, ORT_COEF * rect.getHeight() / 2, 0),
                z = WireframeShape.segment(0, 0, ORT_COEF * rect.getDepth() / 2);

        Vertex boxOrigin = box.getOrigin();
        // XYZ -> RGB
        x.setColor(Color.red);
        x.setOrigin(boxOrigin);
        x.setWidth(3);

        y.setColor(Color.green);
        y.setOrigin(boxOrigin);
        y.setWidth(3);

        z.setColor(Color.blue);
        z.setOrigin(boxOrigin);
        z.setWidth(3);

        orts.add(x);
        orts.add(y);
        orts.add(z);
    }

    /**
     * Compute coordinates of the camera and translate all points the way camera
     * is in the center of the world coordinates
     */
    private void initCamera() {
        Vertex boxOrigin = box.getOrigin();
        Rect3D boxSizes = box.getBoundRect3D();
        double d = (boxSizes.getWidth() / 2) / Math.tan(Math.toRadians(15));
        double cameraX = boxOrigin.getX(),
                cameraY = boxOrigin.getY(),
                cameraZ = boxOrigin.getZ() + boxSizes.getDepth() / 2 + d;

        worldToCamera = Transformation.translate(-cameraX, -cameraY, -cameraZ);

        znear = d;
        zfar = znear + 2.5 * boxSizes.getDepth();

        if (savedZnear == null) {
            savedZnear = znear;
        }
        if (savedZfar == null) {
            savedZfar = zfar;
        }
    }

    /**
     * Default ctor Sets event handlers and inits the Scene
     */
    public Scene() {
        setBackground(Color.white);

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);

        initSceneObjects();
        initBoundBox();
        initOrts();
        initCamera();
    }

    /**
     * Returns all objects on scene
     *
     * @return all objects on scene
     */
    private List<WireframeShape> getAllObjects() {
        List<WireframeShape> objects = new ArrayList<WireframeShape>();
        objects.addAll(sceneObjects);
        objects.addAll(orts);
        objects.add(box);
        return objects;
    }

    @Override
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;

        double height = getHeight(), width = getWidth(), halfWidth = width / 2, halfHeight = height / 2;

        g.translate(halfWidth, halfHeight);
        g.scale(1.0, -1.0);

        Color oldColor = g.getColor();
        Stroke oldStroke = g.getStroke();

        Transformation projection = Transformation.perspective(-halfWidth,
                halfWidth, -halfHeight, halfHeight, znear, zfar);

        Transformation t = Transformation.compose(worldToCamera, worldTransformation);
        t.compose(projection);

        for (WireframeShape shape : getAllObjects()) {
            g.setColor(shape.getColor());
            g.setStroke(new BasicStroke(shape.getWidth()));

            if ((orts.contains(shape) && ortsHidden)
                    || (shape == box && boxHidden)
                    || (sceneObjects.contains(shape) && objectsHidden)) {
                continue;
            }

            for (Segment s : shape.getSegments()) {

                Vertex start = s.getStartVertex(),
                        end = s.getEndVertex();

                Vertex startProjected = t.apply(start).normalize();
                Vertex endProjected = t.apply(end).normalize();

                double sx = startProjected.getX(),
                        sy = startProjected.getY(),
                        sz = startProjected.getZ(),
                        ex = endProjected.getX(),
                        ey = endProjected.getY(),
                        ez = endProjected.getZ();

                if (sx > 1 || sx < -1 || ex > 1 || ex < -1 || sy < -1 || sy > 1
                        || ey < -1 || ey > 1 || sz > 1 || sz < -1 || ez > 1
                        || ez < -1) {
                    continue;
                }

                int x1 = (int) (sx * halfWidth + 0.5), y1 = (int) (sy
                        * halfHeight + 0.5), x2 = (int) (ex * halfWidth + 0.5), y2 = (int) (ey
                        * halfHeight + 0.5);

                g.drawLine(x1, y1, x2, y2);
            }
        }
        g.setStroke(oldStroke);
        g.setColor(oldColor);

        g.scale(1.0, -1.0);
        g.translate(-halfWidth, -halfHeight);
    }

    /**
     * Returns all object to initial state
     */
    public void init() {
        worldTransformation = Transformation.identity();
        zfar = savedZfar;
        znear = savedZnear;
        repaint();
    }

    /**
     * Sets orts visibility to value
     *
     * @param value
     *            boolean value
     */
    public void setOrtsVisible(boolean value) {
        ortsHidden = !value;
        repaint();
    }

    /**
     * Sets box visibility to value
     *
     * @param value
     *            boolean value
     */
    public void setBoxVisible(boolean value) {
        boxHidden = !value;
        repaint();
    }

    /**
     * Sets objects visibility to value
     *
     * @param value
     *            boolean value
     */
    public void setObjectsVisible(boolean value) {
        objectsHidden = !value;
        repaint();
    }

    /**
     * Returns roll coefficient
     *
     * @return roll coefficient
     */
    public double getRollCoef() {
        return rollCoef;
    }

    /**
     * Sets roll coefficient
     *
     * @param rollCoef
     *            roll coefficient
     */
    public void setRollCoef(double rollCoef) {
        this.rollCoef = rollCoef;
    }

    /**
     * Returns rotate coefficient
     *
     * @return rotate coefficient
     */
    public double getRotateCoef() {
        return rotateCoef;
    }

    /**
     * Sets rotate coefficient
     *
     * @param rotateCoef
     *            rotate coefficient
     */
    public void setRotateCoef(double rotateCoef) {
        this.rotateCoef = rotateCoef;
    }

    /**
     * Returns initial zfar
     *
     * @return initial zfar
     */
    public Double getSavedZfar() {
        return savedZfar;
    }

    /**
     * Returns initial znear
     *
     * @return initial zfar
     */
    public Double getSavedZnear() {
        return savedZnear;
    }

    /**
     * Returns current zfar
     *
     * @return current zfar
     */
    public double getZfar() {
        return zfar;
    }

    /**
     * Sets current zfar
     *
     * @param zfar
     *            current zfar
     */
    public void setZfar(double zfar) {
        this.zfar = zfar;
        repaint();
    }

    /**
     * Returns current znear
     *
     * @return current znear
     */
    public double getZnear() {
        return znear;
    }

    /**
     * Sets current znear
     *
     * @param znear
     *            current znear
     */
    public void setZnear(double znear) {
        this.znear = znear;
        repaint();
    }
}
