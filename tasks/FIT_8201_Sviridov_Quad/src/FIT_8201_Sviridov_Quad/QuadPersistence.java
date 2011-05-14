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
import java.io.IOException;
import java.io.Reader;

/**
 *
Строки файла:
1) Rb Gb Bb – цвет фона в формате (0-255)
2) gamma – если 1.0, то без гамма-коррекции, иначе это значение гаммы
3) ntree – глубина дерева трассировки. Здесь значение 1. Если другое, то считать = 1
4) Ar Ag Ab – рассеянный свет, числа в формате [0,1], например, (0.2, 0.3, 0.0). Обычно равные - (0.3, 0.3, 0.3)
5) nl – число источников света. Достаточно 1. Если больше, а ваша прог не умеет, то надо взять один, а остальные просто пропустить при чтении файла
6) Далее nl строк типа "Lxi Lyi Lzi Ri Gi Bi" с координатами и интенсивностями (от 0 до 1 по цветам). Они описывают точечные источники света.
7) N – число примитивов. Здесь ставить число примитивов: сумма числа трг и сфер.
8) Далее идут строки с описаниями примитивов.

Могут быть следующие примитивы
1. Треугольник
1) TRG
2) POINT1x POINT1y POINT1z // координаты одной из вершин
3) POINT2x POINT2y POINT2z // координаты второй
4) POINT3x POINT3y POINT3z // координаты последней вершины
5) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2

2. Квадрик // оставляем для желающих. Остальные должны просто пропустить при чтении файла. Какие - берете сами по вкусу. НЕ ОБЯЗАТЕЛЬНО.
Задание в файле
1) QDR
2) A E H B C F D G I J // коэффициенты
3) POINT1x POINT1y POINT1z // габаритный бокс
4) POINT2x POINT2y POINT2z // габаритный бокс
5) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2 // для внешней стороны
6) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2 // для внутренней стороны
Т.е. берется и учитывается только та часть квадрика, которая находится внутри
габаритного бокса. Сначала строится квадрик, затем вся сцена группируется . Если это
замкнутая фигура, то габаритный бокс выбирается так, чтобы он обрезал ее часть, видны и
внешняя и внутренняя поверхности

3. Сфера
Задание в файле
1) SPH
2) POINT1x POINT1y POINT1z // координаты центра
3) r // радиус сферы
4) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2 // цвет для внешней стороны

 * @author alstein
 */
public class QuadPersistence {

    public static Model loadFromFile(File file) throws FileNotFoundException, IOException {
        Reader reader = new FileReader(file);
        BufferedReader bufferedRedeader = new BufferedReader(reader);
        String str = null;
        String strs[] = null;
        double ds[] = null;
        int is[] = null;

        Model model = new Model();

        // 1) Rb Gb Bb – цвет фона в формате (0-255)
        strs = LineParseUtils.nextNormalizedLine(bufferedRedeader).split(" ");
        is = strsToInts(strs, 3);
        Color bgColor = new Color(is[0], is[1], is[2]);

        model.setBackgroundColor(bgColor);

        // 2) gamma – если 1.0, то без гамма-коррекции, иначе это значение гаммы
        str = LineParseUtils.nextNormalizedLine(bufferedRedeader);
        double gamma = Double.parseDouble(str);

        model.setGamma(gamma);

        // 3) ntree – глубина дерева трассировки. Здесь значение 1. Если другое, то считать = 1
        str = LineParseUtils.nextNormalizedLine(bufferedRedeader);
        int ntree = Integer.parseInt(str);
        if (ntree != 1) {
            ntree = 1;
        }

        model.setNtree(ntree);

        // 4) Ar Ag Ab – рассеянный свет, числа в формате [0,1], например, (0.2, 0.3, 0.0). Обычно равные - (0.3, 0.3, 0.3)
        strs = LineParseUtils.nextNormalizedLine(bufferedRedeader).split(" ");
        ds = strsToDoubles(strs, 3);
        Coefficient3D ambient = new Coefficient3D(ds[0], ds[1], ds[2]);

        model.setAmbient(ambient);

        // 5) nl – число источников света. Достаточно 1. Если больше, а ваша прог не умеет, то надо взять один, а остальные просто пропустить при чтении файла
        // 6) Далее nl строк типа "Lxi Lyi Lzi Ri Gi Bi" с координатами и интенсивностями (от 0 до 1 по цветам). Они описывают точечные источники света.
        str = LineParseUtils.nextNormalizedLine(bufferedRedeader);
        int lightSourcesCount = Integer.parseInt(str);
        for (int i = 0; i < lightSourcesCount; ++i) {
            // coordinates color
            strs = LineParseUtils.nextNormalizedLine(bufferedRedeader).split(" ");
            ds = strsToDoubles(strs, 6);
            Vertex origin = new Vertex(ds[0], ds[1], ds[2]);
            Coefficient3D color = new Coefficient3D(ds[3], ds[4], ds[5]);

            Light light = new Light(origin, color);
            model.addLight(light);
        }

        // 7) N – число примитивов. Здесь ставить число примитивов: сумма числа трг и сфер.
        str = LineParseUtils.nextNormalizedLine(bufferedRedeader);
        int objectsCount = Integer.parseInt(str);

        // 8) Далее идут строки с описаниями примитивов.

        for (int i = 0; i < objectsCount; ++i) {
            Renderable renderable = parseRenderable(bufferedRedeader);
            if (renderable != null) {
                model.addRenderable(renderable);
            }
        }

        model.finishModel();

        return model;
    }

    private static Renderable parseRenderable(BufferedReader bufferedReader) throws IOException {
        String strs[] = null;
        String str = null;
        double ds[] = null;
        String obj = LineParseUtils.nextNormalizedLine(bufferedReader);
        // Могут быть следующие примитивы
        Renderable renderable = null;
        // 1. Треугольник
        // 1) TRG
        if (obj.equals("TRG")) {
            // 2) POINT1x POINT1y POINT1z // координаты одной из вершин
            // 3) POINT2x POINT2y POINT2z // координаты второй
            // 4) POINT3x POINT3y POINT3z // координаты последней вершины
            Vertex v[] = new Vertex[3];
            for (int i = 0; i < 3; ++i) {
                strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
                ds = strsToDoubles(strs, 3);
                Vertex vertex = new Vertex(ds[0], ds[1], ds[2]);
                v[i] = vertex;
            }
            // 5) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2
            ColorModel colorModel = parseColorModel(bufferedReader);

            renderable = new Triangle(v[0], v[1], v[2], colorModel);

            // 2. Квадрик // оставляем для желающих. Остальные должны просто пропустить при чтении файла.
            // Какие - берете сами по вкусу. НЕ ОБЯЗАТЕЛЬНО.
            // 1) QDR
        } else if (obj.equals("QDR")) {
            // wtf!?
            // 2) A E H B C F D G I J // коэффициенты
            strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
            // 3) POINT1x POINT1y POINT1z // габаритный бокс
            strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
            // 4) POINT2x POINT2y POINT2z // габаритный бокс
            strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
            // 5) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2 // для внешней стороны
            strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
            // 6) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2 // для внутренней стороны
            ColorModel colorModel = parseColorModel(bufferedReader);
            // Т.е. берется и учитывается только та часть квадрика, которая находится внутри
            // габаритного бокса. Сначала строится квадрик, затем вся сцена группируется . Если это
            // замкнутая фигура, то габаритный бокс выбирается так, чтобы он обрезал ее часть, видны и
            // внешняя и внутренняя поверхности

        } else if (obj.equals("SPH")) {
            // 3. Сфера
            // Задание в файле
            // 1) SPH
            strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
            ds = strsToDoubles(strs, 3);
            Vertex v = new Vertex(ds[0], ds[1], ds[2]);
            // 2) POINT1x POINT1y POINT1z // координаты центра
            // 3) r // радиус сферы
            str = LineParseUtils.nextNormalizedLine(bufferedReader);
            double radius = Double.parseDouble(str);
            // 4) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2 // цвет для внешней стороны
            ColorModel colorModel = parseColorModel(bufferedReader);

            renderable = new Sphere(v, radius, colorModel);
        } else {
            throw new IllegalArgumentException("Unknown object " + str);
        }

        return renderable;
    }

    private static ColorModel parseColorModel(BufferedReader bufferedReader) throws IOException {
        String strs[] = null;
        double ds[] = null;
// 5) Kar Kag Kab Kdr Kdg Kdb Ksr Ksg Ksb Power Kt N1 N2
        strs = LineParseUtils.nextNormalizedLine(bufferedReader).split(" ");
        ds = strsToDoubles(strs, 13);

        Coefficient3D ambientCoefficient = new Coefficient3D(ds[0], ds[1], ds[2]);
        Coefficient3D diffuseCoefficient = new Coefficient3D(ds[3], ds[4], ds[5]);
        Coefficient3D specularCoefficient = new Coefficient3D(ds[6], ds[7], ds[8]);
        double power = ds[9];
        double transparencyCoefficient = ds[10];
        double Refraction1 = ds[11];
        double Refraction2 = ds[12];
        ColorModel colorModel = new ColorModel(ambientCoefficient, diffuseCoefficient, specularCoefficient,
                power, transparencyCoefficient,
                Refraction1, Refraction2);
        return colorModel;
    }

    private static int[] strsToInts(String strs[], int count) {
        int els[] = new int[count];
        for (int i = 0; i < count; i++) {
            els[i] = Integer.parseInt(strs[i]);
        }
        return els;
    }

    private static double[] strsToDoubles(String strs[], int count) {
        double els[] = new double[count];
        for (int i = 0; i < count; i++) {
            els[i] = Double.parseDouble(strs[i]);
        }
        return els;
    }

    public static void saveToFile(File file, Model sceneModel) {
    }
}
