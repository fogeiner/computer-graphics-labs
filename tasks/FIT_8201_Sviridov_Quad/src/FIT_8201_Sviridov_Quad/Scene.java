package FIT_8201_Sviridov_Quad;

import FIT_8201_Sviridov_Quad.primitives.Renderable;
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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
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
        private int buttonsPressed = 0;

        @Override
        public void mousePressed(MouseEvent e) {
            Point currentPoint = e.getPoint();
            lastPoint = currentPoint;
            buttonsPressed++;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            buttonsPressed--;
            if (buttonsPressed == 0) {
                lastPoint = null;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!wireframeMode) {
                return;
            }
            Point currentPoint = e.getPoint();
            double dx = -(lastPoint.getX() - currentPoint.getX()), dy = -(lastPoint.getY() - currentPoint.getY());
            lastPoint = currentPoint;

            double length = Math.hypot(dx, dy);

            if (length == 0) {
                return;
            }

            double step = rotateCoef * 2 * Math.PI / (128 + 256);

            dx = dx / length;
            dy = dy / length;

            Transformation rotation = null;
            rotation = RotationTransformation.makeRotation(dx * step, RotationTransformation.Y_AXIS);
            rotation.compose(RotationTransformation.makeRotation(dy * step, RotationTransformation.X_AXIS));

            model.centralRotation(rotation);

            repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (!wireframeMode) {
                return;
            }
            int wheelRotationSign = (int) Math.signum(e.getWheelRotation());

            double step = rollCoef * model.getInitialBoxRect3D().getMax() / 40;

            Transformation translation = new TranslationTransformation(0, 0, step * wheelRotationSign);
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
    private BufferedImage renderedImage;
    private MouseHandler mouseHandler = new MouseHandler();
    private double rollCoef = 1.0;
    private double rotateCoef = 1.0;

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
                halfWidth = width / 2, halfHeight = height / 2,
                minSize = Math.min(width, height),
                minHalfSize = minSize / 2;

        g.translate(halfWidth, halfHeight);
        g.scale(1.0, -1.0);

        Color oldColor = g.getColor();
        Stroke oldStroke = g.getStroke();

        Rect3D boxSize = model.getInitialBoxRect3D();
        double sw = Math.max(boxSize.getHeight(), boxSize.getWidth()),
                sh = sw;

        Transformation projection = null;
        projection = new PerspectiveProjectionTransformation(
                sw, sh, model.getZnear(), model.getZfar());

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

                int x1, x2, y1, y2;
                x1 = (int) (sx * minHalfSize + 0.5);
                y1 = (int) (sy * minHalfSize + 0.5);
                x2 = (int) (ex * minHalfSize + 0.5);
                y2 = (int) (ey * minHalfSize + 0.5);

                g.drawLine(x1, y1, x2, y2);
            }
        }
        g.setStroke(oldStroke);
        g.setColor(oldColor);

        g.scale(1.0, -1.0);
        g.translate(-halfWidth, -halfHeight);
    }

    private void render() {

        double height = getHeight(), width = getWidth(),
                halfWidth = width / 2, halfHeight = height / 2,
                minSize = Math.min(width, height),
                halfMinSize = minSize / 2;

        Rect3D boxSize = model.getInitialBoxRect3D();
        double sw = Math.max(boxSize.getHeight(), boxSize.getWidth()),
                sh = sw,
                znear = model.getZnear(),
                gamma = model.getGamma();

        double stepWidth = sw / (minSize + 1);
        double stepHeight = sh / (minSize + 1);

        List<Renderable> renderables = model.getRenderables();
        List<Light> lights = model.getLights();
        Coefficient3D ambientColor = model.getAmbient();

        double R[][] = new double[(int) (width + 0.5)][(int) (height + 0.5)],
                G[][] = new double[(int) (width + 0.5)][(int) (height + 0.5)],
                B[][] = new double[(int) (width + 0.5)][(int) (height + 0.5)];
        // fill with background color
        Color backgroundColor = getBackground();
        double backgroundRed = backgroundColor.getRed() / 255.0,
                backgroundGreen = backgroundColor.getGreen() / 255.0,
                backgroundBlue = backgroundColor.getBlue() / 255.0;

        for (int j = 0; j < R.length; ++j) {
            for (int i = 0; i < R[0].length; ++i) {
                R[j][i] = backgroundRed;
                G[j][i] = backgroundGreen;
                B[j][i] = backgroundBlue;
            }
        }

        int halfWidthIntUp = (int) (halfWidth + 0.5),
                halfWidthIntDown = (int) (halfWidth),
                halfHeightIntUp = (int) (halfHeight + 0.5),
                halfHeightIntDown = (int) (halfHeight);

        for (int w = -halfWidthIntUp; w < halfWidthIntDown; ++w) {
            for (int h = -halfHeightIntUp; h < halfHeightIntDown; ++h) {
                int j = w + halfWidthIntUp,
                        i = h + halfHeightIntUp;
                double y = h * stepHeight, x = w * stepWidth;

                Ray ray = new Ray(new Vector(x, y, -znear));

                List<IntersectionInfo> intersections = new ArrayList<IntersectionInfo>();
                for (Renderable renderable : renderables) {
                    Collection<IntersectionInfo> ii = renderable.intersect(ray);
                    intersections.addAll(ii);
                }

                if (!intersections.isEmpty()) {
                    // find closest intersection
                    IntersectionInfo closestIntersection = null;
                    double closestDistance = Double.POSITIVE_INFINITY;
                    for (IntersectionInfo ii : intersections) {
                        if (ii.length() < closestDistance) {
                            closestIntersection = ii;
                            closestDistance = ii.length();
                        }
                    }
                    // ask object to define its color
                    Coefficient3D objColor = closestIntersection.trace(renderables, lights, ambientColor);

                    // set color to array
                    R[j][i] = objColor.getR();
                    G[j][i] = objColor.getG();
                    B[j][i] = objColor.getB();
                }
            }
        }

        double maxValue = Double.NEGATIVE_INFINITY;
        for (int j = 0; j < R.length; ++j) {
            for (int i = 0; i < R[0].length; ++i) {
                if (R[j][i] > maxValue) {
                    maxValue = R[j][i];
                }
                if (B[j][i] > maxValue) {
                    maxValue = B[j][i];
                }
                if (G[j][i] > maxValue) {
                    maxValue = G[j][i];
                }
            }
        }

        for (int j = 0; j < R.length; ++j) {
            for (int i = 0; i < R[0].length; ++i) {
                R[j][i] /= maxValue;
                G[j][i] /= maxValue;
                B[j][i] /= maxValue;
            }
        }

        for (int j = 0; j < R.length; ++j) {
            for (int i = 0; i < R[0].length; ++i) {
                R[j][i] = Math.min(1.0, Math.max(0, Math.pow(R[j][i], gamma)));
                G[j][i] = Math.min(1.0, Math.max(0, Math.pow(G[j][i], gamma)));
                B[j][i] = Math.min(1.0, Math.max(0, Math.pow(B[j][i], gamma)));
            }
        }

        renderedImage = new BufferedImage((int) (width + 0.5),
                (int) (height + 0.5), BufferedImage.TYPE_INT_ARGB);

        for (int j = 0; j < R.length; ++j) {
            for (int i = 0; i < R[0].length; ++i) {
                renderedImage.setRGB(j, i, new Color((int) (R[j][i] * 255 + 0.5),
                        (int) (G[j][i] * 255 + 0.5), (int) (B[j][i] * 255 + 0.5)).getRGB());
            }
        }


    }

    private void paintRenderedImage(Graphics2D g) {
        g.scale(1.0, -1.0);
        g.drawImage(renderedImage, 0, -getHeight(), null);
        g.scale(1.0, -1.0);
    }

    @Override
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;

        if (wireframeMode) {
            paintWireframe(g);
        } else {
            if (renderedImage == null) {
                render();
            }
            paintRenderedImage(g);
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
        if (wireframeMode == false) {
            renderedImage = null;
        }
    }

    public Model getModel() {
        return model;
    }

    public double getRollCoef() {
        return rollCoef;
    }

    public void setRollCoef(double rollCoef) {
        this.rollCoef = rollCoef;
    }

    public double getRotateCoef() {
        return rotateCoef;
    }

    public void setRotateCoef(double rotateCoef) {
        this.rotateCoef = rotateCoef;
    }
}
