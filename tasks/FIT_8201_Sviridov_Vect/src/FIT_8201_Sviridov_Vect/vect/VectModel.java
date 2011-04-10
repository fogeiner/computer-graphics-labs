package FIT_8201_Sviridov_Vect.vect;

import FIT_8201_Sviridov_Vect.utils.Grid;
import FIT_8201_Sviridov_Vect.utils.Region;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class represents Vector App model
 * 
 * @author admin
 */
public class VectModel {

    private Region region;
    private double vectLengthMult;
    private Grid grid;
    private List<Color> colors;
    private List<Double> values;
    private Color gridColor;
    private Color fieldColor;
    private boolean fieldColorful;
    private double minVectorLength;
    private double maxVectorLength;
    private boolean gridDrawn;
    private boolean arrowPlain;
    private boolean chessMode;
    private final List<VectListener> listeners;

    /**
     * Computes fx
     * @param x x
     * @param y y
     * @return fx
     */
    public double fx(double x, double y) {
        return Math.sin(x + y);
    }

    /**
     * Computes fy
     * @param x x
     * @param y x
     * @return fy
     */
    public double fy(double x, double y) {
        return Math.sin(x - y);
    }

    /**
     * Constructor with all available parameters Its dangerous to leave model
     * uninitialized due to listeners
     *
     * @param region
     *            initial region
     * @param lengthMult
     *            initial vector coefficient value
     * @param grid
     *            initial grid
     * @param colors
     *            initial colors set
     * @param gridColor
     *            initial grid color
     * @param fieldColorful
     *            initial field mode value
     * @param gridDrawn
     *            initial grid drawn value
     * @param arrowPlain
     *            initial arrow mode
     * @param chessMode
     *            initial chess mode
     */
    public VectModel(Region region, double lengthMult, Grid grid,
            List<Color> colors, Color gridColor, Color fieldColor, boolean fieldColorful,
            boolean gridDrawn, boolean arrowPlain, boolean chessMode) {
        this.listeners = new LinkedList<VectListener>();
        this.region = region;
        this.vectLengthMult = lengthMult;
        this.grid = grid;
        this.colors = colors;
        this.gridColor = gridColor;
        this.fieldColor = fieldColor;
        this.fieldColorful = fieldColorful;
        this.gridDrawn = gridDrawn;
        this.arrowPlain = arrowPlain;
        this.chessMode = chessMode;

        computeValues();
    }

    /**
     * Computes max and min values of vectors on given grid and region
     */
    private void computeVectorMaxMinLength() {

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        double width = region.width();
        double height = region.height();

        for (int w = 1; w < grid.W + 1; ++w) {
            double x = region.xs + width / grid.W * w;
            for (int h = 1; h < grid.H + 1; ++h) {
                double y = region.ys + height / grid.H * h;

                double length = vectorLength(x, y);
                if (length > max) {
                    max = length;
                }
                if (length < min) {
                    min = length;
                }
            }
        }

        maxVectorLength = max;
        minVectorLength = min;

    }

    /**
     * Computes new values based on colors number, grid and function
     */
    private void computeValues() {
        // iterate through all cells and compute
        // vector length; find min and max and
        // make a list of uniform values
        if (grid == null || colors == null) {
            return;
        }

        computeVectorMaxMinLength();

        int colorsCount = colors.size();
        double range = maxVectorLength - minVectorLength;
        double step = range / colorsCount;

        if (values != null) {
            values.clear();
        } else {
            values = new ArrayList<Double>(colorsCount - 1);
        }

        for (int k = 1; k < colorsCount; ++k) {
            values.add(new Double(minVectorLength + step * k));
        }
        Collections.reverse(values);
    }

    /**
     * Computes vector (fx,fy) length
     * @param x x
     * @param y y
     * @return length
     */
    private double vectorLength(double x, double y) {
        double fx = fx(x, y), fy = fy(x, y), res = Math.hypot(fx, fy);
        return res;
    }

    /**
     * Calls modelChanged() of all listeners
     */
    public void notifyListeners() {
        for (VectListener vectListener : listeners) {
            vectListener.modelChanged();
        }
    }

    /**
     * Adds new listener
     * @param vectListener new listener
     */
    public void addVectListener(VectListener vectListener) {
        listeners.add(vectListener);
    }

    /**
     * Removes listener
     * @param vectListener listener
     */
    public void removeVectListener(VectListener vectListener) {
        listeners.remove(vectListener);
    }

    /**
     * Getter for colors
     * @return model colors
     */
    public List<Color> getColors() {
        return colors;
    }

    /**
     * Setter for colors
     * @param colors new colors
     */
    public void setColors(List<Color> colors) {
        this.colors = colors;
        computeValues();

        for (VectListener vectListener : listeners) {
            vectListener.colorsChanged();
        }
    }

    /**
     * Getter for region
     * @return region
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Setter for region
     * @param region new region
     */
    public void setRegion(Region region) {
        this.region = region;

        computeVectorMaxMinLength();
        for (VectListener vectListener : listeners) {
            vectListener.regionChanged();
        }
    }

    /**
     * Getter for grid color
     * @return grid color
     */
    public Color getGridColor() {
        return gridColor;
    }

    /**
     * Setter for grid color
     * @param gridColor grid color
     */
    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;

        for (VectListener vectListener : listeners) {
            vectListener.gridColorChanged();

        }
    }

    /**
     * Getter for vector length multiplier
     * @return vector length multiplier
     */
    public double getVectLengthMult() {
        return vectLengthMult;
    }

    /**
     * Setter for vector length multiplier
     * @param lengthMult new vector length multiplier
     */
    public void setVectLengthMult(double lengthMult) {
        this.vectLengthMult = lengthMult;

        for (VectListener vectListener : listeners) {
            vectListener.lengthMultChanged();

        }
    }

    /**
     * Getter for grid
     * @return grid
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Setter for grid
     * @param grid new grid
     */
    public void setGrid(Grid grid) {
        this.grid = grid;
        computeValues();
        for (VectListener vectListener : listeners) {
            vectListener.gridChanged();
        }

    }

    /**
     * Getter for values
     * @return values
     */
    public List<Double> getValues() {
        return values;
    }

    /**
     * Computes region width/height ratio
     * @return aspect ration
     */
    public double getRatio() {
        return (region.xe - region.xs) / (region.ye - region.ys);
    }

    /**
     * Returns the color corresponding to the closest value in values and colors sets
     * @param value
     * @return closest color
     */
    public Color getClosest(double value) {
        int valuesSize = values.size();

        for (int i = 0; i < valuesSize; ++i) {
            if (values.get(i) < value) {
                return colors.get(i);
            }
        }
        return colors.get(valuesSize);
    }

    /**
     * Getter for field mode
     * @return true if field is colorful, false if it's black and white
     */
    public boolean isFieldColorful() {
        return fieldColorful;
    }

    /**
     * Setter for field mode
     * @param fieldColor true for colorful field, false for black and white
     */
    public void setFieldColorful(boolean fieldColor) {
        this.fieldColorful = fieldColor;

        for (VectListener vectListener : listeners) {
            vectListener.fieldModeChanged();

        }
    }

    /**
     * Getter for is grid drawn
     * @return true if grid is drawn, false otherwise
     */
    public boolean isGridDrawn() {
        return gridDrawn;
    }

    /**
     * Setter for is grid drawn
     * @param gridDrawn true for grid to be drawn, false otherwise
     */
    public void setGridDrawn(boolean gridDrawn) {
        this.gridDrawn = gridDrawn;

        for (VectListener vectListener : listeners) {
            vectListener.gridDrawnChanged();
        }
    }

    /**
     * Returns maximum length value of (fx,fy) vector
     * in grid points
     * @return max vector length
     */
    public double getMaxVectorLength() {
        return maxVectorLength;
    }

    /**
     * Getter for arrow mode
     * @return true if arrows are plain, false otherwise
     */
    public boolean isArrowPlain() {
        return arrowPlain;
    }

    /**
     * Setter for arrow mode
     * @param arrowPlain true for arrows to be plain, false otherwise
     */
    public void setArrowPlain(boolean arrowPlain) {
        this.arrowPlain = arrowPlain;

        for (VectListener vectListener : listeners) {
            vectListener.arrowModeChanged();
        }
    }

    /**
     * Getter for chess mode
     * @return true if chess mode is on, false otherwise
     */
    public boolean isChessMode() {
        return chessMode;
    }

    /**
     * Setter for chess mode
     * @param chessMode true for chess mode to turn on, false otherwise
     */
    public void setChessMode(boolean chessMode) {
        this.chessMode = chessMode;
        for (VectListener vectListener : listeners) {
            vectListener.chessModeChanged();
        }
    }

    public Color getFieldColor() {
        return fieldColor;
    }

    public void setFieldColor(Color fieldColor) {
        this.fieldColor = fieldColor;
        for (VectListener vectListener : listeners) {
            vectListener.fieldColorChanged();
        }

    }

    /**
     * Remove all listeners
     */
    public void clearListeners() {
        listeners.clear();
    }
}
