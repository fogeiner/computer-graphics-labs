package FIT_8201_Sviridov_Vect.ui;

import FIT_8201_Sviridov_Vect.state_history.StateHistoryModel;
import FIT_8201_Sviridov_Vect.utils.Grid;
import FIT_8201_Sviridov_Vect.utils.Region;
import FIT_8201_Sviridov_Vect.statusbar.StatusbarModel;
import FIT_8201_Sviridov_Vect.vect.VectListener;
import FIT_8201_Sviridov_Vect.vect.VectModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class VectView extends GridPanel implements VectListener {

    private VectModel vectModel;
    private StatusbarModel statusbarModel;
    private StateHistoryModel<Region> regionsHistoryModel;
    private List<Vector> vectors;
    private boolean selectionRectActive;
    private Point selectionRectStart;
    private Point selectionRectCurrent;
    private Point mouseCurrentPoint;

    public VectView() {
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (e.getSource() == VectView.this) {
                    return;
                }
                updateSize();
                computeGridPoints();
                computeVectors();
                revalidate();
            }
        });
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                updateStatusbar(new Point(e.getPoint()));
                mouseCurrentPoint = new Point(e.getPoint());
                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent me) {
                super.mouseEntered(me);
                statusbarModel.setInRegion(true);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                super.mouseExited(me);
                statusbarModel.setInRegion(false);
                mouseCurrentPoint = null;
                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Point p = e.getPoint();
                selectionRectStart = new Point(p.x, getHeight() - p.y);
                selectionRectCurrent = new Point(p.x, getHeight() - p.y);
                selectionRectActive = true;
                mouseCurrentPoint = null;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                selectionRectActive = false;
                Point p = e.getPoint();

                Point2D p1 = translateCoordinates(selectionRectStart);
                Point2D p2 = translateCoordinates(new Point2D.Double(p.x, getHeight() - p.y));

                Region newRegion = new Region(p1.getX(), p2.getX(), p1.getY(), p2.getY());


                // change region of the model
                vectModel.setRegion(newRegion);
                regionsHistoryModel.add(newRegion);
                repaint();
            }
        });
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                Point p = e.getPoint();
                updateStatusbar(new Point2D.Double(p.getX(), p.getY()));

                int x = Math.max(0, Math.min(getWidth(), p.x));
                int y = Math.max(0, Math.min(getHeight(), p.y));
                selectionRectCurrent = new Point(x, getHeight() - y);
                repaint();
            }
        });
    }

    private void updateStatusbar(Point2D mPoint) {
        mPoint.setLocation(mPoint.getX(), getHeight() - mPoint.getY());
        mPoint = translateCoordinates(mPoint);
        double x = mPoint.getX(),
                y = mPoint.getY(),
                fx = vectModel.fx(x, y),
                fy = vectModel.fy(x, y);
        statusbarModel.setData(x, y, fx, fy);
    }

    private void updateSize() {
        Dimension maxSize = ((JPanel) getParent()).getSize();

        double mRatio = vectModel.getRatio();
        double pRatio = (double) maxSize.width / maxSize.height;

        Dimension newSize;
        if (mRatio > pRatio) {
            double ratio = mRatio / pRatio;
            newSize = new Dimension(maxSize.width, (int) (maxSize.height / ratio + 0.5));
        } else if (mRatio < pRatio) {
            double ratio = pRatio / mRatio;
            newSize = new Dimension((int) (maxSize.width / ratio + 0.5), maxSize.height);
        } else {
            newSize = maxSize;
        }

        VectView.this.setSize(newSize);
        VectView.this.setPreferredSize(newSize);
        VectView.this.setMinimumSize(newSize);
    }

    public void setRegionsHistory(StateHistoryModel<Region> regionsHistory) {
        this.regionsHistoryModel = regionsHistory;
    }

    private Point2D translateCoordinates(Point2D p) {
        // 1. substract left lower point
        // 2. divide by panel width - 2 x_llp, panel - 2 y_llp
        // 3. multiply by region width, height
        // 4. add xs, ys (region starts anywhere)
        Point pll = getGridPoints()[0][0];
        Region currentRegion = vectModel.getRegion();
        double xs = currentRegion.xs,
                xe = currentRegion.xe,
                ys = currentRegion.ys,
                ye = currentRegion.ye;
        double regionWidth = xe - xs,
                regionHeight = ye - ys;

        double panelWidth = getWidth(),
                panelHeight = getHeight();

        double px = p.getX(), py = p.getY();
        double x1 = px - pll.getX(),
                y1 = py - pll.getY();
        double x2 = x1 / (panelWidth - 2 * pll.getX()),
                y2 = y1 / (panelHeight - 2 * pll.getY());

        double x3 = x2 * regionWidth,
                y3 = y2 * regionHeight;

        double x = x3 + xs,
                y = y3 + ys;

        return new Point2D.Double(x, y);
    }

    private void computeVectors() {
        Grid grid = getGrid();
        if (vectors != null) {
            vectors.clear();
        } else {
            vectors = new ArrayList<Vector>(grid.W * grid.H);
        }


        Point[][] points = getGridPoints();
        for (int w = 0; w < points.length; ++w) {
            for (int h = 0; h < points[w].length; ++h) {
                if (vectModel.isChessMode() && ((w + h) % 2 == 0)) {
                    continue;
                }
                Point p = points[w][h];
                Vector v = computeVector(p);
                vectors.add(v);
            }
        }
    }

    private Vector computeVector(Point p) {
        double lCoeff = getGridCellDiagonal() * vectModel.getLengthMult();
        double maxLength = vectModel.getMaxVectorLength();

        Point2D modelPoint = translateCoordinates(p);
        double x = modelPoint.getX(),
                y = modelPoint.getY();

        double fx = vectModel.fx(x, y),
                fy = vectModel.fy(x, y);


        double xs = p.getX(),
                ys = p.getY();

        double xCoeff, yCoeff, xe, ye;

        Color vectColor;
        if (vectModel.isFieldColor()) {
            double length = Math.hypot(fx, fy);
            vectColor = vectModel.getClosest(length);

            xCoeff = fx / length;
            yCoeff = fy / length;

            xe = xs + xCoeff * lCoeff;
            ye = ys + yCoeff * lCoeff;
        } else {
            vectColor = Color.black;

            xCoeff = fx / maxLength;
            yCoeff = fy / maxLength;

            xe = xs + xCoeff * lCoeff;
            ye = ys + yCoeff * lCoeff;
        }
        Vector v = new Vector(
                new Point2D.Double(xs, ys),
                new Point2D.Double(xe, ye));
        v.setColor(vectColor);

        return v;
    }

    @Override
    protected void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.translate(0, this.getHeight() - 1);
        g.scale(1, -1);
        // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        super.paintComponent(g);


        if (vectors != null) {
            for (Vector v : vectors) {
                v.setFilled(!vectModel.isArrowPlain());
                v.draw(g);
            }
        }

        if (selectionRectActive) {
            int x = Math.min(selectionRectCurrent.x, selectionRectStart.x),
                    y = Math.min(selectionRectCurrent.y, selectionRectStart.y);
            int width = Math.abs(selectionRectCurrent.x - selectionRectStart.x),
                    height = Math.abs(selectionRectCurrent.y - selectionRectStart.y);

            g.drawRect(x, y, width, height);
        }

        if (mouseCurrentPoint != null) {
            Point p = new Point(mouseCurrentPoint.x, getHeight() - mouseCurrentPoint.y);
            Vector v = computeVector(p);
            v.setFilled(!vectModel.isArrowPlain());
            v.draw(g);
        }
    }

    public StatusbarModel getStatusbarModel() {
        return statusbarModel;
    }

    public void setStatusbarModel(StatusbarModel statusbarModel) {
        this.statusbarModel = statusbarModel;
        setGridDrawn(vectModel.isGridDrawn());
    }

    @Override
    public void modelChanged() {

        setGrid(vectModel.getGrid());
        repaint();
    }

    @Override
    public void regionChanged() {
        updateSize();
        computeGridPoints();
        computeVectors();
        revalidate();
    }

    @Override
    public void lengthMultChanged() {
    }

    @Override
    public void gridChanged() {
        setGrid(vectModel.getGrid());
        repaint();
    }

    @Override
    public void gridColorChanged() {
        setGridColor(vectModel.getGridColor());
        repaint();
    }

    @Override
    public void colorsChanged() {
    }

    public VectModel getVectModel() {
        return vectModel;
    }

    public void setVectModel(VectModel vectModel) {
        this.vectModel = vectModel;
    }

    @Override
    public void arrowModeChanged() {
        repaint();
    }

    @Override
    public void fieldModeChanged() {
        computeVectors();
        repaint();
    }

    @Override
    public void gridDrawnChanged() {
        this.setGridDrawn(vectModel.isGridDrawn());
        repaint();
    }

    @Override
    public void chessModeChanged() {
        computeVectors();
        repaint();
    }
}
