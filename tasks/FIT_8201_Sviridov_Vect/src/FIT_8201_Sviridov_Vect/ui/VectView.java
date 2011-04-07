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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class VectView extends GridPanel implements VectListener {

    private static final long serialVersionUID = 1810339757295266082L;
    private VectModel vectModel;
    private StatusbarModel statusbarModel;
    private StateHistoryModel<Region> regionsHistoryModel;
    private List<Vector> vectors;
    private Point selectionRectStart;
    private Point selectionRectCurrent;
    private Point mouseCurrentPoint;
    private EventHandlers handlers = new EventHandlers();
    private VectModelHandler vectModelHandler = new VectModelHandler();

    private void removeHandlers() {
        removeComponentListener(handlers);
        removeMouseListener(handlers);
        removeMouseMotionListener(handlers);
    }

    private void addHandlers() {
        addComponentListener(handlers);
        addMouseListener(handlers);
        addMouseMotionListener(handlers);
    }

    class VectModelHandler implements VectListener {

        @Override
        public void arrowModeChanged() {
            repaint();
        }

        @Override
        public void fieldModeChanged() {
            updateSize();
            computeVectors();
            revalidate();
        }

        @Override
        public void gridDrawnChanged() {
            setGridDrawn(vectModel.isGridDrawn());
            repaint();
        }

        @Override
        public void chessModeChanged() {
            computeVectors();
            repaint();
        }

        @Override
        public void modelChanged() {
        }

        @Override
        public void regionChanged() {
            updateSize(); // needed!
            computeVectors();
            repaint();
        }

        @Override
        public void lengthMultChanged() {
            computeVectors();
            repaint();
        }

        @Override
        public void gridChanged() {
            setGrid(vectModel.getGrid());
            computeVectors();
            repaint();
        }

        @Override
        public void gridColorChanged() {
            setGridColor(vectModel.getGridColor());
            repaint();
        }

        @Override
        public void colorsChanged() {
            computeVectors();
            repaint();
        }
    }

    class EventHandlers implements MouseListener, MouseMotionListener, ComponentListener {

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (mouseCurrentPoint != null) {
                mouseCurrentPoint = null;
                repaint();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Point p = e.getPoint();
            Point llp = getLeftLowerGridPoint();
            Point rup = getRightUpperGridPoint();
            int x = Math.max(llp.x, Math.min(rup.x, p.x));
            int y = Math.max(llp.y, Math.min(rup.y, p.y));

            p.setLocation(x, y);
            pointToCart(p);
            selectionRectStart = p;
            selectionRectCurrent = p;

            mouseCurrentPoint = null;
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point p = e.getPoint();
            Point llp = getLeftLowerGridPoint();
            Point rup = getRightUpperGridPoint();
            int x = Math.max(llp.x, Math.min(rup.x, p.x));
            int y = Math.max(llp.y, Math.min(rup.y, p.y));
            p.setLocation(x, y);
            pointToCart(p);

            double p1coord[] = translateCoordinates(selectionRectStart.x, selectionRectStart.y);
            double p2coord[] = translateCoordinates(p.x, p.y);

            selectionRectCurrent = null;
            selectionRectStart = null;

            Region newRegion = new Region(p1coord[0], p2coord[0], p1coord[1], p2coord[1]);

            // change region of the model
            vectModel.setRegion(newRegion);
            regionsHistoryModel.add(newRegion);
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point p = e.getPoint();
            Point llp = getLeftLowerGridPoint();
            Point rup = getRightUpperGridPoint();
            pointToCart(p);
            updateStatusbar(p);

            int x = Math.max(llp.x, Math.min(rup.x, p.x));
            int y = Math.max(llp.y, Math.min(rup.y, p.y));

            selectionRectCurrent = new Point(x, y);

            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();

            pointToCart(p);
            updateStatusbar(p);

            if (isInsideRegion(p)) {
                mouseCurrentPoint = p;
                repaint();
            } else {
                // repaint only once
                if (mouseCurrentPoint != null) {
                    mouseCurrentPoint = null;
                    repaint();
                }
            }
        }

        @Override
        public void componentResized(ComponentEvent e) {
            if (e.getSource() == VectView.this) {
                return;
            }
            updateSize();
            computeVectors();
            repaint();
        }
    }

    public VectView() {
    }

    private boolean isInsideRegion(Point p) {
        Point llp = getLeftLowerGridPoint();
        Point rup = getRightUpperGridPoint();
        return (p.x >= llp.x && p.x <= rup.x && p.y >= llp.y && p.y <= rup.y);
    }

    private void updateStatusbar(Point mPoint) {
        if (isInsideRegion(mPoint)) {
            statusbarModel.setInRegion(true);
        } else {
            statusbarModel.setInRegion(false);
            return;
        }

        double coord[] = translateCoordinates(
                mPoint.getX(), mPoint.getY());
        double x = coord[0],
                y = coord[1],
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
        VectView.this.setMaximumSize(newSize);

        revalidate();
        computeGridPoints();
    }

    public void setRegionsHistory(StateHistoryModel<Region> regionsHistory) {
        this.regionsHistoryModel = regionsHistory;
    }

    private void pointToCart(Point p) {
        p.setLocation(p.x, getHeight() - p.y);
    }

    private double[] translateCoordinates(double fromX, double fromY) {
        double res[] = new double[2];

        // 1. substract left lower point
        // 2. divide by panel width - 2 x_llp, panel - 2 y_llp
        // 3. multiply by region width, height
        // 4. add xs, ys (region starts anywhere)
        Point pll = this.getLeftLowerGridPoint();
        Region currentRegion = vectModel.getRegion();
        double pllx = pll.x,
                plly = pll.y,
                xs = currentRegion.xs,
                xe = currentRegion.xe,
                ys = currentRegion.ys,
                ye = currentRegion.ye,
                regionWidth = xe - xs,
                regionHeight = ye - ys,
                panelWidth = getWidth(),
                panelHeight = getHeight(),
                px = fromX, py = fromY,
                x1 = px - pllx,
                y1 = py - plly;

        double x2 = x1 / (panelWidth - 2 * pllx),
                y2 = y1 / (panelHeight - 2 * plly),
                x3 = x2 * regionWidth,
                y3 = y2 * regionHeight,
                x = x3 + xs,
                y = y3 + ys;


        res[0] = x;
        res[1] = y;

        return res;
    }

    private void computeVectors() {
        Grid grid = getGrid();
        if (grid == null) {
            return;
        }
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

        double coord[] = translateCoordinates(p.getX(), p.getY());

        double x = coord[0],
                y = coord[1];

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

        super.paintComponent(g);

        g.translate(0, this.getHeight());
        g.scale(1, -1);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        if (vectors != null && vectModel != null) {
            for (Vector v : vectors) {
                v.setFilled(!vectModel.isArrowPlain());
                v.draw(g);
            }
        }

        if (selectionRectStart != null && selectionRectCurrent != null) {
            int x = Math.min(selectionRectCurrent.x, selectionRectStart.x),
                    y = Math.min(selectionRectCurrent.y, selectionRectStart.y);
            int width = Math.abs(selectionRectCurrent.x - selectionRectStart.x),
                    height = Math.abs(selectionRectCurrent.y - selectionRectStart.y);

            g.drawRect(x, y, width, height);
        }

        if (mouseCurrentPoint != null) {
            Vector v = computeVector(mouseCurrentPoint);
            v.setFilled(!vectModel.isArrowPlain());
            v.draw(g);
        }
    }

    public StatusbarModel getStatusbarModel() {
        return statusbarModel;
    }

    public VectModel getVectModel() {
        return vectModel;
    }

    public void setStatusbarModel(StatusbarModel statusbarModel) {
        this.statusbarModel = statusbarModel;

        if (statusbarModel == null) {
            removeHandlers();
        } else if (vectModel != null) {
            addHandlers();
        }
    }

    public void setVectModel(VectModel vectModel) {
        this.vectModel = vectModel;
        if (vectModel != null) {
            setGrid(vectModel.getGrid());
            setGridColor(vectModel.getGridColor());
            setGridDrawn(vectModel.isGridDrawn());

            updateSize();
            computeVectors();

            if (statusbarModel != null) {
                addHandlers();
            }
        } else {
            setGridDrawn(false);
            removeHandlers();
        }
        repaint();
    }

    @Override
    public void regionChanged() {
        vectModelHandler.regionChanged();
    }

    @Override
    public void modelChanged() {
        vectModelHandler.modelChanged();
    }

    @Override
    public void lengthMultChanged() {
        vectModelHandler.lengthMultChanged();
    }

    @Override
    public void gridDrawnChanged() {
        vectModelHandler.gridDrawnChanged();
    }

    @Override
    public void gridColorChanged() {
        vectModelHandler.gridColorChanged();
    }

    @Override
    public void gridChanged() {
        vectModelHandler.gridChanged();
    }

    @Override
    public void fieldModeChanged() {
        vectModelHandler.fieldModeChanged();
    }

    @Override
    public void colorsChanged() {
        vectModelHandler.colorsChanged();
    }

    @Override
    public void chessModeChanged() {
        vectModelHandler.chessModeChanged();
    }

    @Override
    public void arrowModeChanged() {
        vectModelHandler.arrowModeChanged();
    }
}
