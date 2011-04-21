package FIT_8201_Sviridov_Cam;

import FIT_8201_Sviridov_Cam.primitives.WireframeShape;
import java.awt.Color;
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

    // all scene objects
    private List<WireframeShape> sceneObjects;
    // orts; should not be clipped
    private List<WireframeShape> orts;
    // bound box
    private WireframeShape box;
    // projection matrix
    private double znear, zfar;
    // projection transformation
    private Transformation projection;
    // mouse handler
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

        @Override
        public void mouseDragged(MouseEvent e) {
            System.out.println("mouseDragged: " + e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            System.out.println("mouseWheelMoved: " + e);
        }
    }

    private void updateTransformationMatrix(){
        projection = Transformation.perspective(getWidth(), getHeight(), znear, zfar);
    }

    /**
     * Create scene objects and set their origins in the world
     * coordinate system
     */
    private void initSceneObjects() {
        // cube, sphere, torus
        WireframeShape cube = WireframeShape.cube(100);
        cube.setOrigin(
                new Vertex(zfar, zfar, zfar));

        sceneObjects.add(cube);
    }

    /**
     * Based on objects and their coordinates in the world
     * coordinate system computes the size and location
     * of the bound box
     */
    private void initBoundBox() {
        WireframeShape box = WireframeShape.parallelepiped(
                zfar, zfar,
                zfar, zfar,
                zfar, zfar);
    }

    /**
     * Based on bound box WireframeShape
     * creates 3 orts
     */
    private void initOrts() {
        WireframeShape x = WireframeShape.segment(zfar, zfar, zfar),
                y = WireframeShape.segment(zfar, zfar, zfar),
                z = WireframeShape.segment(zfar, zfar, zfar);
        // XYZ -> RGB
        x.setColor(Color.red);
        y.setColor(Color.green);
        z.setColor(Color.blue);
    }

    /**
     * Compute coordinates of the camera and
     * translate all points the way camera is in the center of the world coordinates
     */
    private void initCamera() {
    }

    /**
     * Default ctor
     * Sets event handlers and inits the Scene
     */
    public Scene() {
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
        initSceneObjects();
        initBoundBox();
        initOrts();
        initCamera();
    }
}
