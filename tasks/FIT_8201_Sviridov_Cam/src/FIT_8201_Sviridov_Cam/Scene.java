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
 *
 * @author alstein
 */
public class Scene extends JPanel {

    private static final double ORT_COEF = 1.5;
    // all scene objects
    private List<WireframeShape> sceneObjects;
    // orts; should not be clipped
    private List<WireframeShape> orts;
    // bound box
    private WireframeShape box;
    // projection matrix
    private double znear, zfar;
    // mouse handler
    private MouseHandler mouseHandler;

    {
        sceneObjects = new ArrayList<WireframeShape>(3);
        orts = new ArrayList<WireframeShape>(3);
        mouseHandler = new MouseHandler();
        znear = 1000;
        zfar = 5000;
    }

    /**
     * Class to handle MouseMotion and MouseWheel events,
     * alter settins of the model and schedule repaint procedure
     */
    class MouseHandler extends MouseAdapter implements MouseMotionListener, MouseWheelListener {

        private Point lastPoint;

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            lastPoint = null;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            //System.out.println("mouseDragged: " + e);
            Point currentPoint = e.getPoint();
            currentPoint = new Point(currentPoint.x, getHeight() - currentPoint.y);
            if (lastPoint == null) {
                lastPoint = currentPoint;
                return;
            }

            double dx = (lastPoint.getX() - currentPoint.getX()) / 1000,
                    dy = (lastPoint.getY() - currentPoint.getY()) / 1000;

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
            int wheelRotation = e.getWheelRotation();
            Transformation translate = Transformation.translate(0, 0, wheelRotation * 10);
            transformAllObjects(translate);
            repaint();
        }
    }

    /**
     * Create scene objects and set their origins in the world
     * coordinate system
     */
    private void initSceneObjects() {
        // cube, sphere, torus
        WireframeShape cube = WireframeShape.cube(100);
        cube.setOrigin(new Vertex(180, -100, 50));
        sceneObjects.add(cube);

        cube = WireframeShape.cube(100);
        cube.setOrigin(new Vertex(100, 50, 190));
        sceneObjects.add(cube);

        cube = WireframeShape.cube(100);
        cube.setOrigin(new Vertex(50, 300, 450));
        sceneObjects.add(cube);
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
            for (Segment s : shape.getTransformatedSegments()) {
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




        Transformation translate = Transformation.translate(-boxX, -boxY, -boxZ);
        transformAllObjects(translate);

        box = WireframeShape.parallelepiped(
                maxX - minX,
                maxY - minY,
                maxZ - minZ);

        box.setOrigin(new Vertex(0, 0, 0));
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
        // XYZ -> RGB
        x.setColor(Color.red);
        x.setOrigin(box.getTransformedOrigin());
        x.setWidth(3);

        y.setColor(Color.green);
        y.setOrigin(box.getTransformedOrigin());
        y.setWidth(3);

        z.setColor(Color.blue);
        z.setOrigin(box.getTransformedOrigin());
        z.setWidth(3);

        orts.add(x);
        orts.add(y);
        orts.add(z);
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

        double boxX = boxOrigin.getX(),
                boxY = boxOrigin.getY(),
                boxZ = boxOrigin.getZ();

//        Transformation translate = Transformation.translate(-boxX, -boxZ, 0);
//        transformAllObjects(translate);

        double boxWidth = box.getBoundRect3D().getWidth();
        double cameraX = boxX, cameraY = boxY, cameraZ = boxZ + boxWidth / 2 / Math.atan(Math.PI / 12);

        Transformation translate = Transformation.translate(
                -cameraX,
                -cameraY,
                -cameraZ);

        transformAllObjects(translate);
    }

    /**
     * Default ctor
     * Sets event handlers and inits the Scene
     */
    public Scene() {
        setBackground(Color.black);

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
        if (box != null) {
            objects.add(box);
        }
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
        Graphics2D g = (Graphics2D) g1;
        double halfWidth = getWidth() / 2,
                halfHeight = getHeight() / 2;
        g.translate(halfWidth, halfHeight);
        g.scale(1.0, -1.0);

        Transformation projection = Transformation.perspective(getWidth(), getHeight(), znear, zfar);

        Color oldColor = g.getColor();
        Stroke oldStroke = g.getStroke();
        for (WireframeShape shape : getAllObjects()) {
            g.setColor(shape.getColor());
            g.setStroke(new BasicStroke(shape.getWidth()));
            

            for (Segment s : shape.getTransformatedSegments()) {
                Vertex start = s.getStartPoint(),
                        end = s.getEndPoint();

                Vertex startProjected = projection.apply(start).normalize();
                Vertex endProjected = projection.apply(end).normalize();


                double sx = startProjected.getX(),
                        sy = startProjected.getY(),
                        sz = startProjected.getZ(),
                        ex = endProjected.getX(),
                        ey = endProjected.getY(),
                        ez = endProjected.getZ();
                System.out.println(startProjected);
                System.out.println(endProjected);
                if (sx < -1 || sx > 1 || sy < -1 || sy > 1 || sz < 0 || sz > 1
                        || ex < -1 || ex > 1 || ey < -1 || ey > 1 || ez < 0 || ez > 1) {
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
    }
}
