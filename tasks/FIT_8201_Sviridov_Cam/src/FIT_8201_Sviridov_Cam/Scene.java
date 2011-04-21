package FIT_8201_Sviridov_Cam;

import FIT_8201_Sviridov_Cam.primitives.Segment;
import FIT_8201_Sviridov_Cam.primitives.WireframeShape;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

    private static final double ORT_COEF = 1.1;
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
        znear = 300;
        zfar = 400;
    }

    /**
     * Class to handle MouseMotion and MouseWheel events,
     * alter settins of the model and schedule repaint procedure
     */
    class MouseHandler extends MouseAdapter implements MouseMotionListener, MouseWheelListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            System.out.println("mouseDragged: " + e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            System.out.println("mouseWheelMoved: " + e);
        }
    }

    /**
     * Create scene objects and set their origins in the world
     * coordinate system
     */
    private void initSceneObjects() {
        // cube, sphere, torus
        WireframeShape cube = WireframeShape.cube(100);
        cube.setOrigin(new Vertex(150, 0, 50));
        sceneObjects.add(cube);

        cube = WireframeShape.cube(100);
        cube.setOrigin(new Vertex(100, 0, 150));
        sceneObjects.add(cube);

        cube = WireframeShape.cube(100);
        cube.setOrigin(new Vertex(50, 0, 250));
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
        double boxX = (maxX - minX) / 2,
                boxY = (maxY - minY) / 2,
                boxZ = (maxZ - minZ) / 2;

        this.box = WireframeShape.parallelepiped(
                maxX, minX,
                maxY, minY,
                maxZ, minZ);

        this.box.setOrigin(new Vertex(boxX, boxY, boxZ));
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
        x.setWidth(3);
        y.setColor(Color.green);
        y.setWidth(3);
        z.setColor(Color.blue);
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
        // 1. find the center of the bound box
        // 2. find the coordinates of the camera
        // 3. translate all the way that camera is in the center

        // Camera is fixed, aimed against z axis and in world coordinates (0,0,0)
        Vertex boxOrigin = this.box.getOrigin();

        double boxX = boxOrigin.getX(),
                boxY = boxOrigin.getY(),
                boxZ = boxOrigin.getZ();

        double boxWidth = this.box.getBoundRect3D().getWidth();
        double cameraX = boxX, cameraY = boxY, cameraZ = boxZ + boxWidth / 2 / Math.atan(Math.PI / 12);

        Transformation translate = Transformation.translate(
                -cameraX,
                -cameraY,
                -cameraZ);
        for (WireframeShape shape : sceneObjects) {
            shape.transform(translate);
        }

        for (WireframeShape ort : orts) {
            ort.transform(translate);
        }

        box.transform(translate);

//        System.out.println("Objects:");
//
//
//        for (int i = 0; i
//                < sceneObjects.size();
//                ++i) {
//            System.out.println("Object " + i);
//            System.out.println(sceneObjects.get(i));
//
//
//        }
//        System.out.println("Bounded box:");
//        System.out.println(box);
//
//        System.out.println("Orts:");
//
//
//        for (int i = 0; i
//                < orts.size();
//                ++i) {
//            System.out.println("Ort " + i);
//            System.out.println(orts.get(i));
//
//
//        }
    }

    /**
     * Default ctor
     * Sets event handlers and inits the Scene
     */
    public Scene() {
        setBackground(Color.black);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(                mouseHandler);
        initSceneObjects();
        initBoundBox();
        initOrts();
        initCamera();
    }

    @Override
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        double halfWidth = getWidth() / 2,
                halfHeight = getHeight() / 2;
        g.translate((int) halfWidth, (int) halfHeight);
        g.scale(1.0, -1.0);

        Transformation projection = Transformation.perspective(getWidth(), getHeight(), znear, zfar);
        List<WireframeShape> objects = new ArrayList<WireframeShape>();
           objects.addAll(sceneObjects);
          objects.addAll(orts);
        objects.add(box);

        Color oldColor = g.getColor();
        Stroke oldStroke = g.getStroke();
        for (WireframeShape shape : objects) {
            g.setColor(shape.getColor());
            g.setStroke(new BasicStroke(shape.getWidth()));
            for (Segment s : shape.getTransformatedSegments()) {
                Vertex start = s.getStartPoint(),
                        end = s.getEndPoint();
                Vertex startProjected = projection.apply(start);
                Vertex endProjected = projection.apply(end);
                startProjected = startProjected.normalize();
                endProjected = endProjected.normalize();
                
                int x1 = (int) (startProjected.getX() * halfWidth + 0.5),
                        y1 = (int) (startProjected.getY() * halfHeight + 0.5),
                        x2 = (int) (endProjected.getX() * halfWidth + 0.5),
                        y2 = (int) (endProjected.getY() * halfHeight + 0.5);

                g.drawLine(x1, y1, x2, y2);
            }
        }
        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }
}
