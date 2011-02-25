package FIT_8201_Sviridov_Lines;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Class for loading polylines from file/saving them to file
 * @author admin
 *
 */
public class PolylinePersistenceManager {
	public static void saveToFile(File file, PolylineSettings ps) throws IOException{

		FileWriter fw = new FileWriter(file);

		List<Polyline> polylines = ps.getPolylines();
		int size = polylines.size();

		fw.write(size + "\r\n");
		for (int i = 0; i < size; ++i) {
			fw.write(polylines.get(i).toString());
		}
		fw.close();

		

	}
	
	public static void loadFromFile(File file, PolylineSettings ps) throws IOException{
		FileInputStream fstream = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		int polylines_count = Integer.parseInt(LineParseUtils
				.nextNormalizedLine(br));

		if (polylines_count < 0)
			throw new IllegalArgumentException();

		for (int i = 0; i < polylines_count; ++i) {
			int points_count = Integer.parseInt(LineParseUtils
					.nextNormalizedLine(br));

			if (points_count < 0)
				throw new IllegalArgumentException();

			int type = Integer.parseInt(LineParseUtils
					.nextNormalizedLine(br));

			if (type != PolylineSettings.CONTINIOUS && type != PolylineSettings.DASHED
					&& type != PolylineSettings.DOTTED_DASHED)
				throw new IllegalArgumentException();

			int thickness = Integer.parseInt(LineParseUtils
					.nextNormalizedLine(br));

			if (thickness < 1)
				throw new IllegalArgumentException();

			String str = LineParseUtils.nextNormalizedLine(br);
			String rgb[] = str.split(" ");

			Color color = new Color(Integer.parseInt(rgb[0]),
					Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));

			Polyline polyline = new Polyline(type, thickness, PolylineSettings.DEFAULT_CIRCLE_RADIUS, color);

			for (int j = 0; j < points_count; ++j) {
				str = LineParseUtils.nextNormalizedLine(br);

				String coord[] = str.split(" ");

				polyline.addPoint(new Point(Integer.parseInt(coord[0]),
						Integer.parseInt(coord[1])));
			}

			ps.addPolyline(polyline);
		}
	}
}
