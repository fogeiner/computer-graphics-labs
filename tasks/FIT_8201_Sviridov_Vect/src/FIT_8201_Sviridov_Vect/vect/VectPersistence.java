/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FIT_8201_Sviridov_Vect.vect;

import FIT_8201_Sviridov_Vect.utils.Grid;
import FIT_8201_Sviridov_Vect.utils.LineParseUtils;
import FIT_8201_Sviridov_Vect.utils.Region;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author admin
 */
public class VectPersistence {

    static public void saveToFile(VectModel vectModel, File file) throws IOException {
        FileWriter fw = new FileWriter(file);
        Region region = vectModel.getRegion();
        Grid grid = vectModel.getGrid();
        double a = region.xs,
                b = region.xe,
                c = region.ys,
                d = region.ye;

        double c0 = vectModel.getLengthMult();
        List<Color> colors = vectModel.getColors();
        Color gridColor = vectModel.getGridColor();
        int n = colors.size();

        StringBuilder sb = new StringBuilder();
        sb.append(Double.toString(a));
        sb.append(" ");
        sb.append(Double.toString(b));
        sb.append(" ");
        sb.append(Double.toString(c));
        sb.append(" ");
        sb.append(Double.toString(d));
        sb.append("\r\n");
        sb.append(Double.toString(c0));
        sb.append("\r\n");
        sb.append(Integer.toString(n - 1));
        sb.append("\r\n");
        Collections.reverse(colors);
        for (Color color : colors) {
            sb.append(colorToString(color));
            sb.append("\r\n");
        }
        Collections.reverse(colors);
        sb.append(Integer.toString(grid.W));
        sb.append(" ");
        sb.append(Integer.toString(grid.H));
        sb.append("\r\n");
        sb.append(colorToString(gridColor));
        sb.append("\r\n");
        fw.append(sb.toString());
        fw.close();
    }

    public static String colorToString(Color c) {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(c.getRed()));
        sb.append(" ");
        sb.append(Integer.toString(c.getGreen()));
        sb.append(" ");
        sb.append(Integer.toString(c.getBlue()));
        return sb.toString();
    }

    public static Color stringToColor(String string) {
        String[] rgb = string.split(" ");
        int r = Integer.parseInt(rgb[0]),
                g = Integer.parseInt(rgb[1]),
                b = Integer.parseInt(rgb[2]);
        return new Color(r, g, b);
    }

    static public VectModel loadFromFile(File file) throws FileNotFoundException, IOException {
        FileInputStream fstream = new FileInputStream(file);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        double a, b, c, d;
        double c0;
        int n;
        List<Color> colors = new ArrayList<Color>();
        int M, N;
        Color gridColor;

        String string;
        String strs[];
        strs = LineParseUtils.nextNormalizedLine(br).split(" ");
        a = Double.parseDouble(strs[0]);
        b = Double.parseDouble(strs[1]);
        if (a >= b) {
            throw new IllegalArgumentException("x start (a) must be less than x end (b)");
        }
        c = Double.parseDouble(strs[2]);
        d = Double.parseDouble(strs[3]);
        if (c >= d) {
            throw new IllegalArgumentException("y start (c) must be less than y end (d)");
        }

        string = LineParseUtils.nextNormalizedLine(br);
        c0 = Double.parseDouble(string);

        if (c0 < 0.5 || c0 > 3.0) {
            throw new IllegalArgumentException("Vector mult coeff (C0) must be in [0.3, 3.0]");
        }

        string = LineParseUtils.nextNormalizedLine(br);
        n = Integer.parseInt(string);

        if (n < 4 || n > 20) {
            throw new IllegalArgumentException("n must be in {4, ..., 20}");
        }

        for (int i = 0; i < n + 1; ++i) {
            string = LineParseUtils.nextNormalizedLine(br);
            colors.add(stringToColor(string));
        }

        Collections.reverse(colors);
        strs = LineParseUtils.nextNormalizedLine(br).split(" ");
        M = Integer.parseInt(strs[0]);
        N = Integer.parseInt(strs[1]);

        if (M < 4 || N < 4 || M > 50 || N > 50) {
            throw new IllegalArgumentException("M and N must be in {4, ..., 50}");
        }

        string = LineParseUtils.nextNormalizedLine(br);
        gridColor = stringToColor(string);

        VectModel vectModel = new VectModel(new Region(a, b, c, d), c0, new Grid(M, N), colors, gridColor, true, true, true, false);
        return vectModel;
    }
}
