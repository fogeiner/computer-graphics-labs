package FIT_8201_Sviridov_Vect.vect;

import FIT_8201_Sviridov_Vect.utils.Grid;
import FIT_8201_Sviridov_Vect.utils.Region;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author admin
 */
public class VectModel {

    public static final int BW_MODE = 0;
    public static final int COLOR_MODE = 1;
    public static final int PLAIN_MODE = 0;
    public static final int FILLED_MODE = 1;
    private Region region;
    private double lengthMult;
    private Grid grid;
    private List<Color> colors;
    private List<Double> values;
    private Color gridColor;
    private int fieldMode;
    private double maxVectorLength;
    private boolean gridDrawn;
    private int arrowMode;
    private final List<VectListener> listeners;
    private boolean notifyActive;

    public VectModel(Region region) {
        this.region = region;
        this.listeners = new LinkedList<VectListener>();
    }

    private void computeValues() {
        // iterate through all cells and compute
        // vector length; find min and max and 
        // make a list of uniform values
        if (grid == null || colors == null) {
            return;
        }
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

        int colorsCount = colors.size();
        double range = max - min;
        double step = range / colorsCount;

        if (values != null) {
            values.clear();
        } else {
            values = new ArrayList<Double>(colorsCount - 1);
        }

        for (int k = 1; k < colorsCount; ++k) {
            values.add(new Double(min + step * k));
        }
    }

    private double vectorLength(double x, double y) {
        double fx = fx(x, y),
                fy = fy(x, y),
                res = Math.sqrt(fx * fx + fy * fy);
        return res;
    }

    public void notifyListeners() {
        if (isNotifyActive()) {
            for (VectListener vectListener : listeners) {
                vectListener.modelChanged();
            }
        }
    }

    public void addVectListener(VectListener vectListener) {
        listeners.add(vectListener);
    }

    public void removeVectListener(VectListener vectListener) {
        listeners.remove(vectListener);
    }

    public double fx(double x, double y) {
        return 0.0;
    }

    public double fy(double x, double y) {
        return 0.0;
    }

    public List<Color> getColors() {
        return colors;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
        computeValues();
        if (isNotifyActive()) {
            for (VectListener vectListener : listeners) {
                vectListener.colorsChanged();
            }
        }
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
        computeValues();
        if (isNotifyActive()) {
            for (VectListener vectListener : listeners) {
                vectListener.regionChanged();
            }
        }
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
        if (isNotifyActive()) {
            for (VectListener vectListener : listeners) {
                vectListener.gridColorChanged();
            }
        }
    }

    public double getLengthMult() {
        return lengthMult;
    }

    public void setLengthMult(double lengthMult) {
        this.lengthMult = lengthMult;
        if (isNotifyActive()) {
            for (VectListener vectListener : listeners) {
                vectListener.lengthMultChanged();
            }
        }
    }

    public boolean isNotifyActive() {
        return notifyActive;
    }

    public void setNotifyActive(boolean notifyActive) {
        this.notifyActive = notifyActive;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
        computeValues();
        if (isNotifyActive()) {
            for (VectListener vectListener : listeners) {
                vectListener.gridChanged();
            }
        }

    }

    public List<Double> getValues() {
        return values;
    }

    public double getRatio() {
        return (region.xe - region.xs) / (region.ye - region.ys);
    }

    public Color getClosest(double value) {
        int valuesSize = values.size();
        for (int i = 0; i < valuesSize; ++i) {
            if (values.get(i) < value) {
                return colors.get(i);
            }
        }
        return colors.get(valuesSize);
    }

    public int getFieldMode() {
        return fieldMode;
    }

    public void setFieldMode(int fieldMode) {
        this.fieldMode = fieldMode;
        if (isNotifyActive()) {
            for (VectListener vectListener : listeners) {
                vectListener.fieldModeChanged();
            }
        }
    }

    public boolean isGridDrawn() {
        return gridDrawn;
    }

    public void setGridDrawn(boolean gridDrawn) {
        this.gridDrawn = gridDrawn;
        if (isNotifyActive()) {
            for (VectListener vectListener : listeners) {
                vectListener.gridDrawnChanged();
            }
        }
    }

    public double getMaxVectorLength() {
        return maxVectorLength;
    }

    public int getArrowMode() {
        return arrowMode;
    }

    public void setArrowMode(int arrowMode) {
        this.arrowMode = arrowMode;
        if (isNotifyActive()) {
            for (VectListener vectListener : listeners) {
                vectListener.arrowModeChanged();
            }
        }
    }
}
