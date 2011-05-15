package FIT_8201_Sviridov_Quad;

import FIT_8201_Sviridov_Quad.primitives.Renderable;
import FIT_8201_Sviridov_Quad.primitives.Sphere;
import FIT_8201_Sviridov_Quad.primitives.Triangle;
import FIT_8201_Sviridov_Quad.utils.LineParseUtils;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Class for model persistence
 * 
 * @author alstein
 */
public class QuadPersistence {

	/**
	 * Returns model read from file
	 * 
	 * @param file
	 *            file
	 * @return model
	 * @throws FileNotFoundException
	 *             if file not found
	 * @throws IOException
	 *             if I/O occurs
	 */
	public static Model loadFromFile(File file) throws FileNotFoundException,
			IOException {
		Reader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String str = null;
		String strs[] = null;
		double ds[] = null;
		int is[] = null;

		Model model = new Model();

		// 1) Rb Gb Bb � ���� ���� � ������� (0-255)
		strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
		is = strsToInts(strs, 3);
		Color bgColor = new Color(is[0], is[1], is[2]);

		model.setBackgroundColor(bgColor);

		// 2) gamma � ���� 1.0, �� ��� �����-���������, ����� ��� �������� �����
		str = LineParseUtils.nextNormalizedLine(bufferedReader);
		double gamma = Double.parseDouble(str);

		model.setGamma(gamma);

		// 3) ntree � ������� ������ �����������. ����� �������� 1. ���� ������,
		// �� ������� = 1
		str = LineParseUtils.nextNormalizedLine(bufferedReader);
		int ntree = Integer.parseInt(str);
		if (ntree != 1) {
			ntree = 1;
		}

		model.setNtree(ntree);

		// 4) Ar Ag Ab � ���������� ����, ����� � ������� [0,1], ��������, (0.2,
		// 0.3, 0.0). ������ ������ - (0.3, 0.3, 0.3)
		strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
		ds = strsToDoubles(strs, 3);
		Coefficient3D ambient = new Coefficient3D(ds[0], ds[1], ds[2]);

		model.setAmbient(ambient);

		// 5) nl � ����� ���������� �����. ���������� 1. ���� ������, � ����
		// ���� �� �����, �� ���� ����� ����, � ��������� ������ ���������� ���
		// ������ �����
		// 6) ����� nl ����� ���� "Lxi Lyi Lzi Ri Gi Bi" � ������������ �
		// ��������������� (�� 0 �� 1 �� ������). ��� ��������� ��������
		// ��������� �����.
		str = LineParseUtils.nextNormalizedLine(bufferedReader);
		int lightSourcesCount = Integer.parseInt(str);
		for (int i = 0; i < lightSourcesCount; ++i) {
			// coordinates color
			strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
			ds = strsToDoubles(strs, 6);
			Vertex origin = new Vertex(ds[0], ds[1], ds[2]);
			Coefficient3D color = new Coefficient3D(ds[3], ds[4], ds[5]);

			Light light = new Light(origin, color);
			model.addLight(light);
		}

		// 7) N � ����� ����������. ����� ������� ����� ����������: ����� �����
		// ��� � ����.
		str = LineParseUtils.nextNormalizedLine(bufferedReader);
		int objectsCount = Integer.parseInt(str);

		// 8) ����� ���� ������ � ���������� ����������.

		for (int i = 0; i < objectsCount; ++i) {
			Renderable renderable = parseRenderable(bufferedReader);
			if (renderable != null) {
				model.addRenderable(renderable);
			}
		}

		model.finishModel();

		bufferedReader.close();
		return model;
	}

	/**
	 * Reads Renderable from file
	 * 
	 * @param bufferedReader
	 *            source
	 * @return Renderable
	 * @throws IOException
	 *             if I/O occurs
	 */
	private static Renderable parseRenderable(BufferedReader bufferedReader)
			throws IOException {
		String strs[] = null;
		String str = null;
		double ds[] = null;
		String obj = LineParseUtils.nextNormalizedLine(bufferedReader);
		// ����� ���� ��������� ���������
		Renderable renderable = null;
		// 1. �����������
		// 1) TRG
		if (obj.equals("TRG")) {
			// 2) POINT1x POINT1y POINT1z // ���������� ����� �� ������
			// 3) POINT2x POINT2y POINT2z // ���������� ������
			// 4) POINT3x POINT3y POINT3z // ���������� ��������� �������
			Vertex v[] = new Vertex[3];
			for (int i = 0; i < 3; ++i) {
				strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(
						" ");
				ds = strsToDoubles(strs, 3);
				Vertex vertex = new Vertex(ds[0], ds[1], ds[2]);
				v[i] = vertex;
			}
			// 5) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2
			ColorModel colorModel = parseColorModel(bufferedReader);

			renderable = new Triangle(v[0], v[1], v[2], colorModel);

			// 2. ������� // ��������� ��� ��������. ��������� ������ ������
			// ���������� ��� ������ �����.
			// ����� - ������ ���� �� �����. �� �����������.
			// 1) QDR
		} else if (obj.equals("QDR")) {
			// wtf!?
			// 2) A E H B C F D G I J // ������������
			strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
			// 3) POINT1x POINT1y POINT1z // ���������� ����
			strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
			// 4) POINT2x POINT2y POINT2z // ���������� ����
			strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
			// 5) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2 // ���
			// ������� �������
			strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
			// 6) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2 // ���
			// ���������� �������
			parseColorModel(bufferedReader);
			// �.�. ������� � ����������� ������ �� ����� ��������, �������
			// ��������� ������
			// ����������� �����. ������� �������� �������, ����� ��� �����
			// ������������ . ���� ���
			// ��������� ������, �� ���������� ���� ���������� ���, ����� ��
			// ������� �� �����, ����� �
			// ������� � ���������� �����������

		} else if (obj.equals("SPH")) {
			// 3. �����
			// ������� � �����
			// 1) SPH
			strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
			ds = strsToDoubles(strs, 3);
			Vertex v = new Vertex(ds[0], ds[1], ds[2]);
			// 2) POINT1x POINT1y POINT1z // ���������� ������
			// 3) r // ������ �����
			str = LineParseUtils.nextNormalizedLine(bufferedReader);
			double radius = Double.parseDouble(str);
			// 4) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2 // ���� ���
			// ������� �������
			ColorModel colorModel = parseColorModel(bufferedReader);

			renderable = new Sphere(v, radius, colorModel);
		} else {
			throw new IllegalArgumentException("Unknown object " + str);
		}

		return renderable;
	}

	/**
	 * Reads ColorModel
	 * @param bufferedReader source
	 * @return ColorModel
	 * @throws IOException if I/O error occurs
	 */
	private static ColorModel parseColorModel(BufferedReader bufferedReader)
			throws IOException {
		String strs[] = null;
		double ds[] = null;
		// 5) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2
		strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
		ds = strsToDoubles(strs, 13);

		Coefficient3D ambientCoefficient = new Coefficient3D(ds[0], ds[1],
				ds[2]);
		Coefficient3D diffuseCoefficient = new Coefficient3D(ds[3], ds[4],
				ds[5]);
		Coefficient3D specularCoefficient = new Coefficient3D(ds[6], ds[7],
				ds[8]);
		double power = ds[9];
		double transparencyCoefficient = ds[10];
		double Refraction1 = ds[11];
		double Refraction2 = ds[12];
		ColorModel colorModel = new ColorModel(ambientCoefficient,
				diffuseCoefficient, specularCoefficient, power,
				transparencyCoefficient, Refraction1, Refraction2);
		return colorModel;
	}

	/**
	 * Parses string array to array of ints
	 * @param strs strings
	 * @param count count
	 * @return array of ints
	 */
	private static int[] strsToInts(String strs[], int count) {
		int els[] = new int[count];
		for (int i = 0; i < count; i++) {
			els[i] = Integer.parseInt(strs[i]);
		}
		return els;
	}

	/**
	 * Parses string array to array of dobules
	 * @param strs strings
	 * @param count count
	 * @return array of doubles
	 */
	private static double[] strsToDoubles(String strs[], int count) {
		double els[] = new double[count];
		for (int i = 0; i < count; i++) {
			els[i] = Double.parseDouble(strs[i]);
		}
		return els;
	}

	/**
	 * Appends int to file
	 * @param fw file writer
	 * @param num number
	 * @throws IOException if I/O error occurs
	 */
	private static void appendInteger(FileWriter fw, int num)
			throws IOException {
		fw.append(Integer.toString(num));
	}

	/**
	 * Appends double to file
	 * @param fw file writer
	 * @param num number
	 * @throws IOException if I/O error occurs
	 */
	private static void appendDouble(FileWriter fw, double num)
			throws IOException {
		fw.append(Double.toString(num));
	}

	/**
	 * Appends \r\n to file
	 * @param fw file writer
	 * @throws IOException if I/O error occurs
	 */
	private static void appendNewLine(FileWriter fw) throws IOException {
		fw.append("\r\n");
	}

	/**
	 * Saves model to file
	 * @param file file
	 * @param model model
	 * @throws IOException if I/O error occurs
	 */
	public static void saveToFile(File file, Model model) throws IOException {
		FileWriter fw = new FileWriter(file);
		// 1) Rb Gb Bb � ���� ���� � ������� (0-255)
		Color backgroundColor = model.getBackgroundColor();
		appendInteger(fw, backgroundColor.getRed());
		fw.append(' ');
		appendInteger(fw, backgroundColor.getGreen());
		fw.append(' ');
		appendInteger(fw, backgroundColor.getBlue());
		appendNewLine(fw);
		// 2) gamma � ���� 1.0, �� ��� �����-���������, ����� ��� �������� �����
		appendDouble(fw, model.getGamma());
		appendNewLine(fw);
		// 3) ntree � ������� ������ �����������. ����� �������� 1. ���� ������,
		// �� ������� = 1
		appendInteger(fw, model.getNtree());
		appendNewLine(fw);
		// 4) Ar Ag Ab � ���������� ����, ����� � ������� [0,1], ��������, (0.2,
		// 0.3, 0.0). ������ ������ - (0.3, 0.3, 0.3)
		fw.append(model.getAmbient().toString());
		appendNewLine(fw);
		// 5) nl � ����� ���������� �����. ���������� 1. ���� ������, � ����
		// ���� �� �����, �� ���� ����� ����, � ��������� ������ ���������� ���
		// ������ �����
		List<Light> lights = model.getLights();
		int lightsCount = lights.size();
		appendInteger(fw, lightsCount);
		appendNewLine(fw);
		// 6) ����� nl ����� ���� "Lxi Lyi Lzi Ri Gi Bi" � ������������ �
		// ��������������� (�� 0 �� 1 �� ������). ��� ��������� ��������
		// ��������� �����.
		for (Light light : lights) {
			fw.append(light.toString());
			appendNewLine(fw);
		}
		// 7) N � ����� ����������. ����� ������� ����� ����������: ����� �����
		// ��� � ����.
		List<Renderable> renderables = model.getRenderables();
		int renderablesCount = renderables.size();
		appendInteger(fw, renderablesCount);
		appendNewLine(fw);

		// 8) ����� ���� ������ � ���������� ����������.
		for (Renderable renderable : renderables) {
			fw.append(renderable.toString());
			appendNewLine(fw);
		}

		fw.close();
	}
}
