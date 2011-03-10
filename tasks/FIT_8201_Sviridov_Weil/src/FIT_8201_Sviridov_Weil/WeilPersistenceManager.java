package FIT_8201_Sviridov_Weil;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class for loading polylines from file/saving them to file
 * 
 * File format: width height // in pixels k // number of subject polygon
 * contours; k = 1|2 first -- outer { number of points in contour x_i y_i // in
 * cartesian coordinates } t // number of clip polygon contours; t = 1 { number
 * of points in contour x_i y_i // in cartesian coordinates }
 * 
 * @author alstein
 * 
 */
public class WeilPersistenceManager {

	/**
	 * Saves model to file
	 * 
	 * @param file
	 *            file to write to
	 * @param ps
	 *            object encapsulating data to save
	 * @throws IOException
	 *             thrown if IO operation fails
	 */
	public static void saveToFile(File file, WeilSettings ps)
			throws IOException {

		FileWriter fw = new FileWriter(file);

		fw.append(Integer.toString(ps.getMaxX() + WeilSettings.MODEL_PADDING));
		fw.append(" ");
		fw.append(Integer.toString(ps.getMaxY() + WeilSettings.MODEL_PADDING));
		fw.append("\r\n");

		if (!ps.getHolePolygon().isEmpty()) {
			fw.append(Integer.toString(2));
		} else {
			fw.append(Integer.toString(1));
		}

		fw.append("\r\n");

		fw.append(ps.getSubjectPolygon().toString());

		if (!ps.getHolePolygon().isEmpty()) {
			fw.append(ps.getHolePolygon().toString());
		}

		fw.append(Integer.toString(1));

		fw.append("\r\n");

		fw.append(ps.getClipPolygon().toString());
		fw.close();
	}

	/**
	 * Loads model from file
	 * 
	 * @param file
	 *            file to load from
	 * @param ps
	 *            object encapsulating data to load
	 * @throws IOException
	 *             thrown if IO operation fails
	 */
	public static void loadFromFile(File file, WeilSettings ps)
			throws IOException {
		FileInputStream fstream = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String strs[];

		int width;
		int height;
		int subject_contours_count;
		int clip_contours_count;

		Polygon subject = null;
		Polygon hole = null;
		Polygon clip = null;

		strs = LineParseUtils.nextNormalizedLine(br).split(" ");

		width = Integer.parseInt(strs[0]);
		height = Integer.parseInt(strs[1]);

		if (width < 0 || height < 0) {
			throw new IllegalArgumentException();
		}

		subject_contours_count = Integer.parseInt(LineParseUtils
				.nextNormalizedLine(br));
		if (subject_contours_count != 1 && subject_contours_count != 2) {
			throw new IllegalArgumentException();
		}

		subject = new Polygon(Polygon.COUNTERCLOCKWISE_ORIENTATION,
				WeilSettings.DEFAULT_SUBJECT_COLOR,
				WeilSettings.DEFAULT_SUBJECT_THICKNESS);
		// subject -- always
		loadPoints(subject, br);

		// hole -- if needed
		if (subject_contours_count == 2) {
			hole = new Polygon(Polygon.CLOCKWISE_ORIENTATION,
					WeilSettings.DEFAULT_SUBJECT_COLOR,
					WeilSettings.DEFAULT_SUBJECT_THICKNESS);
			loadPoints(hole, br);
		}

		clip_contours_count = Integer.parseInt(LineParseUtils
				.nextNormalizedLine(br));
		if (clip_contours_count != 1) {
			throw new IllegalArgumentException();
		}
		clip = new Polygon(Polygon.COUNTERCLOCKWISE_ORIENTATION,
				WeilSettings.DEFAULT_CLIP_COLOR,
				WeilSettings.DEFAULT_CLIP_THICKNESS);
		loadPoints(clip, br);

		ps.setPreferredSize(new Dimension(width, height));
		ps.setSubjectPolygon(subject);
		ps.setHolePolygon(hole);
		ps.setClipPolygon(clip);

		ps.modelLoaded();
	}

	/**
	 * Loads Polygon model from file
	 * 
	 * @param p
	 *            polygon to save points to
	 * @param br
	 *            file source
	 * @throws IOException
	 *             in case if IO failure
	 */
	private static void loadPoints(Polygon p, BufferedReader br)
			throws IOException {
		String strs[];
		int points_count;
		int x, y;

		points_count = Integer.parseInt(LineParseUtils.nextNormalizedLine(br));
		if (points_count < 0) {
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < points_count; ++i) {
			strs = LineParseUtils.nextNormalizedLine(br).split(" ");
			x = Integer.parseInt(strs[0]);
			y = Integer.parseInt(strs[1]);
			p.addPoint(new Point2D.Double(x, y));
		}
	}
}
