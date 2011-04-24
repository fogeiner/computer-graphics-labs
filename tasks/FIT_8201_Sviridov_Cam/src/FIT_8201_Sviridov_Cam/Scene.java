package FIT_8201_Sviridov_Cam;

import FIT_8201_Sviridov_Cam.primitives.Segment;
import FIT_8201_Sviridov_Cam.primitives.WireframeShape;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
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
 *
 * @author alstein
 */
public class Scene extends JPanel {

    private static final double DEFAULT_ROTATE_COEF = Math.PI / 96;
    private static final double DEFAULT_ROLL_COEF = 25.0;
    private static final double ORT_COEF = 1.1;
    private double rollCoef = DEFAULT_ROLL_COEF;
    private double rotateCoef = DEFAULT_ROTATE_COEF;
    // all scene objects
    private List<WireframeShape> sceneObjects;
    // orts
    private List<WireframeShape> orts;
    // bound box
    private WireframeShape box;
    // projection matrix
    private double znear, zfar;
    // mouse handler
    private double aspectRatio;

    private MouseHandler mouseHandler;

    {
        sceneObjects = new ArrayList<WireframeShape>(3);
        orts = new ArrayList<WireframeShape>(3);
        mouseHandler = new MouseHandler();
    }

    /**
     * Class to handle MouseMotion and MouseWheel events,
     * alter settins of the model and schedule repaint procedure
     */
    class MouseHandler extends MouseAdapter implements MouseMotionListener, MouseWheelListener {

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
            //System.out.println("mouseDragged: " + e);
            Point currentPoint = e.getPoint();

            double dx = (lastPoint.getX() - currentPoint.getX()),
                    dy = (lastPoint.getY() - currentPoint.getY());

            double length = Math.hypot(dx, dy);
            if (length == 0) {
                return;
            }
            dx = dx / length * rotateCoef;
            dy = dy / length * rotateCoef;

            lastPoint = currentPoint;

            Vertex boxOrigin = box.getTransformedOrigin();

            Transformation t = Transformation.identity();
            t.compose(Transformation.translate(-boxOrigin.getX(), -boxOrigin.getY(), -boxOrigin.getZ()));
            t.compose(Transformation.rotate(dx, Transformation.Y_AXIS));
            t.compose(Transformation.rotate(dy, Transformation.X_AXIS));
            t.compose(Transformation.translate(boxOrigin.getX(), boxOrigin.getY(), boxOrigin.getZ()));
            transformAllObjects(t);

            repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int wheelRotationSign = (int) Math.signum(e.getWheelRotation());
            Transformation t = Transformation.translate(0, 0, wheelRotationSign * rollCoef);
            transformAllObjects(t);
            repaint();
        }
    }

    /**
     * Create scene objects and set their origins in the world
     * coordinate system
     */
    private void initSceneObjects() {
        // cube, sphere, torus
//        WireframeShape cube = WireframeShape.cube(100);
//        cube.setOrigin(new Vertex(100, 100, 100));
//        sceneObjects.add(cube);

        double c = 1 / Math.sqrt(2);
        int sSteps = 30,
            tSteps = 30;

        WireframeShape sq1 = WireframeShape.superquadric(115, tSteps, sSteps, 0.5, 0.5);
        sq1.setOrigin(new Vertex(-150, 180, 30));
        sq1.setBasis(new Vector(1, 0, 0), new Vector(0, -c, c), new Vector(0, -c, -c));
        sceneObjects.add(sq1);

        WireframeShape sq2 = WireframeShape.superquadric(150, tSteps, sSteps, 1, 3);
        sq2.setOrigin(new Vertex(0, 0, 0));
        sq2.setBasis(new Vector(0, c, c), new Vector(-1, 0, 0), new Vector(0, -c, c));
        sceneObjects.add(sq2);

        WireframeShape sq3 = WireframeShape.superquadric(120, tSteps, sSteps, 2, 1);
        sq3.setOrigin(new Vertex(180, -140, 60));
        sq3.setBasis(new Vector(-1, 0, 0), new Vector(0, -c, -c), new Vector(0, -c, c));
        sceneObjects.add(sq3);
    }

    /**
     * Based on objects and their coordinates in the world
     * coordinate system computes the size and location
     * of the bound box
     */
    private void initBoundBox() {
        double maxX = Double.NEGATIVE_INFINITY, minX = Double.POSITIVE_INFINITY,
                maxY = Double.NEGATIVE_INFINITY, minY = Double.POSITIVE_INFINITY,
                maxZ = Double.NEGATIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;

        for (WireframeShape shape : sceneObjects) {
            for (Segment s : shape.getTransformedSegments()) {
                Vertex start = s.getStartPoint();
                Vertex end = s.getEndPoint();
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


//        Transformation translate = Transformation.translate(-boxX, -boxY, -boxZ);
//        transformAllObjects(translate);

        box = WireframeShape.parallelepiped(
                maxX - minX,
                maxY - minY,
                maxZ - minZ);

        box.transform(Transformation.translate(boxX, boxY, boxZ));

        Rect3D boxSizes = box.getBoundRect3D();
        aspectRatio = boxSizes.getWidth()/boxSizes.getHeight();
        
        System.out.println("Created bound box: " + box);
    }

    /**
     * Based on bound box WireframeShape
     * creates 3 orts
     */
    private void initOrts() {
        Rect3D rect = this.box.getBoundRect3D();

        WireframeShape x = WireframeShape.segment(ORT_COEF * rect.getWidth() / 2, 0, 0),
                y = WireframeShape.segment(0, ORT_COEF * rect.getHeight() / 2, 0),
                z = WireframeShape.segment(0, 0, ORT_COEF * rect.getDepth() / 2);

        Vertex boxOrigin = box.getTransformedOrigin();
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

        System.out.println("Created x ort: " + x);
        System.out.println("Created y ort: " + y);
        System.out.println("Created z ort: " + z);
    }

    /**
     * Compute coordinates of the camera and
     * translate all points the way camera is in the center of the world coordinates
     */
    private void initCamera() {
        // 0. box should lie on z, i.e. it's centers x and y are zero
        // 1. find the center of the bound box
        // 2. find the coordinates of the camera
        // 3. translate all the way that camera is in the center

        // Camera is fixed, aimed against z axis and in world coordinates (0,0,0)
        Vertex boxOrigin = box.getTransformedOrigin();

        System.out.println("Translating all objects (dx,dy,dx)" + "(" + -boxOrigin.getX() + ","
                + -boxOrigin.getY() + "," + 0 + ")");

        Transformation translate = Transformation.translate(-boxOrigin.getX(), -boxOrigin.getY(), -boxOrigin.getZ());
        transformAllObjects(translate);

        System.out.println("Bound box now: " + box);

        Rect3D boxSizes = box.getBoundRect3D();

        double d = (boxSizes.getWidth() / 2) / Math.tan(Math.toRadians(15));

        double cameraX = 0,
                cameraY = 0,
                cameraZ = d + boxSizes.getDepth() / 2 + 1;

        System.out.println("Translating all objects (dx,dy,dx)" + "(" + -cameraX + ","
                + -cameraY + "," + cameraZ + ")");


        translate = Transformation.translate(
                -cameraX,
                -cameraY,
                -cameraZ);

        transformAllObjects(translate);

        System.out.println("Bound box now: " + box);

        znear = d;
        zfar = znear + 2.5 * boxSizes.getDepth();

//        transformAllObjects(Transformation.translate(0, 0, 2*zfar));
        System.out.println("znear: " + znear + " zfar: " + zfar);
    }

    /**
     * Default ctor
     * Sets event handlers and inits the Scene
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

    private List<WireframeShape> getAllObjects() {
        List<WireframeShape> objects = new ArrayList<WireframeShape>();
        objects.addAll(sceneObjects);
        objects.addAll(orts);
        objects.add(box);
        return objects;
    }

    private void transformAllObjects(Transformation transformation) {
        for (WireframeShape shape : getAllObjects()) {
            shape.transform(transformation);
        }
    }

    @Override
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        System.out.println("paintComponent");
        Graphics2D g = (Graphics2D) g1;

        double height = getHeight(),
                width = getWidth(),
                halfWidth = width / 2,
                halfHeight = height / 2;

        g.translate(halfWidth, halfHeight);
        g.scale(1.0, -1.0);

        

        Color oldColor = g.getColor();
        Stroke oldStroke = g.getStroke();

        Transformation projection = Transformation.perspective(
                -halfWidth, halfWidth,
                -halfHeight, halfHeight,
                znear, zfar);

//        System.out.println(projection);

        for (WireframeShape shape : getAllObjects()) {
            g.setColor(shape.getColor());
            g.setStroke(new BasicStroke(shape.getWidth()));

            boolean ortsHidden = false;
            boolean boxHidden = false;
            boolean objectsHidden = false;
            // hide orts and box only if flags are set
            if ((orts.contains(shape) && ortsHidden) || (shape == box && boxHidden) || (sceneObjects.contains(shape) && objectsHidden)) {
                continue;
            }


            for (Segment s : shape.getTransformedSegments()) {
                Vertex start = s.getStartPoint(),
                        end = s.getEndPoint();

                Vertex startProjected = projection.apply(start).normalize();
                Vertex endProjected = projection.apply(end).normalize();

//                System.out.println("from " + start + " to " + startProjected);
//                System.out.println("from " + end + " to " + endProjected);

                double sx = startProjected.getX(),
                        sy = startProjected.getY(),
                        sz = startProjected.getZ(),
                        ex = endProjected.getX(),
                        ey = endProjected.getY(),
                        ez = endProjected.getZ();

                if (sz > 1 || sz < -1
                        || ez > 1 || ez < -1) {
                    continue;
                }
                int x1 = (int) (sx * halfWidth + 0.5),
                        y1 = (int) (sy * halfHeight + 0.5),
                        x2 = (int) (ex * halfWidth + 0.5),
                        y2 = (int) (ey * halfHeight + 0.5);

                g.drawLine(x1, y1, x2, y2);
            }
        }
        g.setStroke(oldStroke);
        g.setColor(oldColor);

        g.scale(1.0, -1.0);
        g.translate(-halfWidth, -halfHeight);
    }
}
