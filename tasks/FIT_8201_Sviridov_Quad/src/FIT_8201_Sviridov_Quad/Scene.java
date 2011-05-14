package FIT_8201_Sviridov_Quad;

import FIT_8201_Sviridov_Quad.primitives.Segment;
import FIT_8201_Sviridov_Quad.primitives.Wireframe;
import FIT_8201_Sviridov_Quad.transformations.PerspectiveProjectionTransformation;
import FIT_8201_Sviridov_Quad.transformations.RotationTransformation;
import FIT_8201_Sviridov_Quad.transformations.Transformation;
import FIT_8201_Sviridov_Quad.transformations.TranslationTransformation;
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

    /**
     * Class to handle MouseMotion and MouseWheel events, alter settins of the
     * model and schedule repaint procedure
     */
    private class MouseHandler extends MouseAdapter implements MouseMotionListener,
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
            lastPoint = currentPoint;

            double length = Math.hypot(dx, dy);

            if (length == 0) {
                return;
            }

            dx = dx / length / 100;
            dy = dy / length / 100;

            Transformation rotation = null;
            rotation = RotationTransformation.makeRotation(dx, RotationTransformation.Y_AXIS);
            rotation.compose(RotationTransformation.makeRotation(dy, RotationTransformation.X_AXIS));

            model.centralRotation(rotation);

            repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int wheelRotationSign = (int) Math.signum(e.getWheelRotation());

            Transformation translation = new TranslationTransformation(0, 0, wheelRotationSign);
            model.transform(translation);

            repaint();
        }
    }
    private Model model;
    private boolean boxVisible = true;
    private boolean ortsVisible = true;
    private boolean renderablesVisible = true;
    // TODO
    private boolean wireframeMode = true;
    private MouseHandler mouseHandler = new MouseHandler();

    public void setModel(Model model) {
        this.model = model;
        setBackground(model.getBackgroundColor());
        repaint();
    }

    public Scene(Model model) {
        this.model = model;

        setBackground(model.getBackgroundColor());
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
    }

    private void paintWireframe(Graphics2D g) {

        double height = getHeight(), width = getWidth(),
                halfWidth = width / 2, halfHeight = height / 2;

        g.translate(halfWidth, halfHeight);
        g.scale(1.0, -1.0);

        Color oldColor = g.getColor();
        Stroke oldStroke = g.getStroke();

        Wireframe box = model.getSavedModel().getBox();
        Rect3D boxSize = box.getBoundRect3D();
        double sw, sh;
        sw = sh = Math.max(boxSize.getHeight(), boxSize.getWidth())*2;

        Transformation projection = new PerspectiveProjectionTransformation(
                sw, sh, model.getZnear(), model.getZfar());

        double maxHalfSize = Math.max(halfHeight, halfWidth);

        List<Wireframe> wireframes = new ArrayList<Wireframe>(10);

        if (boxVisible) {
            wireframes.add(model.getBox());
        }
        if (ortsVisible) {
            wireframes.addAll(model.getOrts());
        }
        if (renderablesVisible) {
            wireframes.addAll(model.getRenderablesWireframes());
        }

        for (Wireframe wireframe : wireframes) {
            g.setColor(wireframe.getColor());
            g.setStroke(new BasicStroke(wireframe.getWidth()));


            for (Segment s : wireframe.getSegments()) {

                Vertex start = s.getStartVertex(),
                        end = s.getEndVertex();

                Vertex startProjected = projection.apply(start).normalize();
                Vertex endProjected = projection.apply(end).normalize();

                double sx = startProjected.getX(),
                        sy = startProjected.getY(),
                        sz = startProjected.getZ(),
                        ex = endProjected.getX(),
                        ey = endProjected.getY(),
                        ez = endProjected.getZ();

                int x1 = (int) (sx * maxHalfSize + 0.5),
                        y1 = (int) (sy * maxHalfSize + 0.5),
                        x2 = (int) (ex * maxHalfSize + 0.5),
                        y2 = (int) (ey * maxHalfSize + 0.5);

                g.drawLine(x1, y1, x2, y2);
            }
        }
        g.setStroke(oldStroke);
        g.setColor(oldColor);

        g.scale(1.0, -1.0);
        g.translate(-halfWidth, -halfHeight);
    }

    private void paintRender(Graphics2D g) {
    }

    @Override
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;

        if (wireframeMode) {
            paintWireframe(g);
        } else {
            paintRender(g);
        }
    }

    public boolean isBoxVisible() {
        return boxVisible;
    }

    public void setBoxVisible(boolean boxVisible) {
        this.boxVisible = boxVisible;
        repaint();
    }

    public boolean isRenderablesVisible() {
        return renderablesVisible;
    }

    public void setRenderablesVisible(boolean objectsVisible) {
        this.renderablesVisible = objectsVisible;
        repaint();
    }

    public boolean isOrtsVisible() {
        return ortsVisible;
    }

    public void setOrtsVisible(boolean ortsVisible) {
        this.ortsVisible = ortsVisible;
        repaint();
    }

    public boolean isWireframeMode() {
        return wireframeMode;
    }

    public void setWireframeMode(boolean wireframeMode) {
        this.wireframeMode = wireframeMode;
    }

    public Model getModel() {
        return model;
    }
}
