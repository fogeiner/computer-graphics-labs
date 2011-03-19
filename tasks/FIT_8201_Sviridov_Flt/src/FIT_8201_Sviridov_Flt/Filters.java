package FIT_8201_Sviridov_Flt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author alstein
 */
public class Filters {

    public static BufferedImage getGreyscaleImage(BufferedImage o) {
        int width = o.getWidth(), height = o.getHeight();
        int data[] = o.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);
        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < width; ++w) {
                //  int rgb = o.getRGB(j, i);
                int rgb = data[h * width + w];
                int RGB[] = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};

                double R = RGB[0] / 255.0, G = RGB[1] / 255.0, B = RGB[2] / 255.0;

                int y = (int) (((double) 0.299 * R + 0.587 * G + 0.114 * B) * 255 + 0.5);

                n_data[h * width + w] = 256 * (256 * y + y) + y;
            }
        }

        n.setRGB(0, 0, width, height, n_data, 0, width);
        return n;
    }

    public static BufferedImage getNegativeImage(BufferedImage o) {
        int width = o.getWidth(), height = o.getHeight();
        int data[] = o.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < width; ++w) {
                int rgb = data[h * width + w];
                int RGB[] = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};
                int R = 255 - RGB[0], G = 255 - RGB[1], B = 255 - RGB[2];
                n_data[h * width + w] = 256 * 256 * R + 256 * G + B;
            }
        }
        n.setRGB(0, 0, width, height, n_data, 0, width);
        return n;
    }

    public static BufferedImage getBlurImage(BufferedImage o) {
        double m[][] = new double[][]{
            new double[]{1.0 / 74, 2.0 / 74, 3.0 / 74, 2.0 / 74, 1.0 / 74},
            new double[]{2.0 / 74, 4.0 / 74, 5.0 / 74, 4.0 / 74, 2.0 / 74},
            new double[]{3.0 / 74, 5.0 / 74, 6.0 / 74, 5.0 / 74, 3.0 / 74},
            new double[]{2.0 / 74, 4.0 / 74, 5.0 / 74, 4.0 / 74, 2.0 / 74},
            new double[]{1.0 / 74, 2.0 / 74, 3.0 / 74, 2.0 / 74, 1.0 / 74}};
        return Filters.applyConvolutionMatrix(o, m, 0, 0, 0);
    }

    public static BufferedImage getEmbossImage(BufferedImage o) {
        double m[][] = new double[][]{new double[]{0, 1, 0}, new double[]{-1, 0, 1}, new double[]{0, -1, 0}};
        return Filters.applyConvolutionMatrix(o, m, 128, 128, 128);
    }

    public static BufferedImage getSobelImage(BufferedImage o, double threshold) {
        int width = o.getWidth(), height = o.getHeight();

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage gs = Filters.getGreyscaleImage(o);
        int data[] = gs.getRGB(0, 0, width, height, null, 0, width);

        int white_rgb = Color.white.getRGB();
        int black_rgb = Color.black.getRGB();
        for (int h = 0; h < height - 2; ++h) {
            for (int w = 0; w < width - 2; ++w) {
                double g11 = data[h * width + w] & 0xFF,
                        g12 = data[(h + 1) * width + w] & 0xFF,
                        g13 = data[(h + 2) * width + w] & 0xFF,
                        g21 = data[h * width + (w + 1)] & 0xFF,
                        g22 = data[(h + 1) * width + (w + 1)] & 0xFF,
                        g23 = data[(h + 2) * width + (w + 1)] & 0xFF,
                        g31 = data[h * width + (w + 2)] & 0xFF,
                        g32 = data[(h + 1) * width + (w + 2)] & 0xFF,
                        g33 = data[(h + 2) * width + (w + 2)] & 0xFF;

                double Sx = (g13 + 2 * g23 + g33) - (g11 + 2 * g21 + g31);
                double Sy = (g31 + 2 * g32 + g33) - (g11 + 2 * g12 + g13);

                double S = Math.abs(Sx) + Math.abs(Sy);

                if (S > threshold) {
                    n_data[h * width + w] = white_rgb;
                } else {
                    n_data[h * width + w] = black_rgb;
                }
            }
        }
        n.setRGB(0, 0, width, height, n_data, 0, width);
        return n;
    }

    public static BufferedImage getRobertsImage(BufferedImage o, double threshold) {
        int width = o.getWidth(), height = o.getHeight();
        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);
        BufferedImage gs = Filters.getGreyscaleImage(o);
        int data[] = gs.getRGB(0, 0, width, height, null, 0, width);

        int white_rgb = Color.white.getRGB();
        int black_rgb = Color.black.getRGB();

        for (int h = 0; h < height - 1; ++h) {
            for (int w = 0; w < width - 1; ++w) {

                double g11 = data[h * width + w] & 0xFF,
                        g12 = data[(h + 1) * width + w] & 0xFF,
                        g21 = data[h * width + (w + 1)] & 0xFF,
                        g22 = data[(h + 1) * width + (w + 1)] & 0xFF;

                double R = Math.abs(g11 - g22) + Math.abs(g12 - g21);
                if (R > threshold) {
                    n_data[h * width + w] = white_rgb;
                } else {
                    n_data[h * width + w] = black_rgb;
                }
            }
        }

        n.setRGB(0, 0, width, height, n_data, 0, width);
        return n;
    }

    public static BufferedImage getSharpen3Image(BufferedImage o) {
        double m[][] = new double[][]{new double[]{0, -1, 0}, new double[]{-1, 5, -1}, new double[]{0, -1, 0}};
        return Filters.applyConvolutionMatrix(o, m, 0, 0, 0);
    }

    public static BufferedImage getSharpen5Image(BufferedImage o) {
        double m[][] = new double[][]{
            new double[]{0, 0, 0, 0, 0},
            new double[]{0, 0, -1, 0, 0},
            new double[]{0, -1, 5, -1, 0},
            new double[]{0, 0, -1, 0, 0},
            new double[]{0, 0, 0, 0, 0}};
        return Filters.applyConvolutionMatrix(o, m, 0, 0, 0);
    }

    public static BufferedImage getDoubleScaleImage(BufferedImage o) {
       int o_width = o.getWidth(), o_height = o.getHeight();
        int center_height, center_width;
        center_width = o_width / 2 + (o_width / 2 % 2 == 0 ? 0 : 1);
        center_height = o_height / 2 + (o_height / 2 % 2 == 0 ? 0 : 1);
        BufferedImage c = o.getSubimage(o_width / 4, o_height / 4, center_width, center_height);
        BufferedImage n = new BufferedImage(2 * center_width, 2 * center_height, BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < center_height; ++h) {
            for (int w = 0; w < center_width; ++w) {
                int h_offset = 2 * h;
                int w_offset = 2 * w;
                int rgb;
                int rgb11 = c.getRGB(w, h);
                int rgb12;
                if (w != center_width - 1) {
                    rgb12 = c.getRGB(w + 1, h);
                } else {
                    rgb12 = c.getRGB(w, h);
                }
                int rgb21;
                if (h != center_height - 1) {
                    rgb21 = c.getRGB(w, h + 1);
                } else {
                    rgb21 = c.getRGB(w, h);
                }
                int rgb22;
                if (h != center_height - 1 && w != center_width - 1) {
                    rgb22 = c.getRGB(w + 1, h + 1);
                } else if (h != center_height - 1) {
                    rgb22 = c.getRGB(w, h + 1);
                } else if (w != center_width - 1) {
                    rgb22 = c.getRGB(w + 1, h);
                } else {
                    rgb22 = c.getRGB(w, h);
                }
                int RGB11[] = {(rgb11 >> 16) & 0xFF, (rgb11 >> 8) & 0xFF, rgb11 & 0xFF};
                int RGB12[] = {(rgb12 >> 16) & 0xFF, (rgb12 >> 8) & 0xFF, rgb12 & 0xFF};
                int RGB21[] = {(rgb21 >> 16) & 0xFF, (rgb21 >> 8) & 0xFF, rgb21 & 0xFF};
                int RGB22[] = {(rgb22 >> 16) & 0xFF, (rgb22 >> 8) & 0xFF, rgb22 & 0xFF};
                int AR[] = new int[3];
                int AL[] = new int[3];
                int ARL[] = new int[3];
                for (int k = 0; k < 3; ++k) {
                    AR[k] = (int) ((double) (RGB11[k] + RGB12[k]) / 2 + 0.5);
                    AL[k] = (int) ((double) (RGB11[k] + RGB21[k]) / 2 + 0.5);
                    ARL[k] = (int) ((double) (RGB11[k] + RGB12[k] + RGB21[k] + RGB22[k]) / 4 + 0.5);
                }
                n.setRGB(w_offset, h_offset, rgb11);
                rgb = AR[2] + AR[1] * 256 + AR[0] * 256 * 256;
                n.setRGB(w_offset + 1, h_offset, rgb);
                rgb = AL[2] + AL[1] * 256 + AL[0] * 256 * 256;
                n.setRGB(w_offset, h_offset + 1, rgb);
                rgb = ARL[2] + ARL[1] * 256 + ARL[0] * 256 * 256;
                n.setRGB(w_offset + 1, h_offset + 1, rgb);
            }
        }
        return n;
    }

    public static BufferedImage getColorSmoothedImage(BufferedImage o, int size) {
        int width = o.getWidth(), height = o.getHeight();
        int data[] = o.getRGB(0, 0, width, height, null, 0, width);
        int m = size / 2;

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

        int[] rs = new int[size * size];
        int[] gs = new int[size * size];
        int[] bs = new int[size * size];
        int middle = rs.length / 2;
        for (int h = m; h < height - m; ++h) {
            for (int w = m; w < width - m; ++w) {
                for (int i = 0; i < size; ++i) {
                    for (int j = 0; j < size; ++j) {
                        int rgb = data[(h + i - m) * width + (w + j - m)];
                        rs[i * size + j] = (rgb >> 16) & 0xFF;
                        gs[i * size + j] = (rgb >> 8) & 0xFF;
                        bs[i * size + j] = rgb & 0xFF;
                    }
                }

                Arrays.sort(rs);
                Arrays.sort(gs);
                Arrays.sort(bs);

                int R = rs[middle], G = gs[middle], B = bs[middle];
                n_data[h * width + w] = B + 256 * G + 256 * 256 * R;
            }
        }
        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < m; ++w) {
                n_data[h * width + w] = data[h * width + w];
            }
            for (int w = width - m; w < width; ++w) {
                n_data[h * width + w] = data[h * width + w];
            }
        }
        for (int w = 0; w < width; ++w) {
            for (int h = 0; h < m; ++h) {
                n_data[h * width + w] = data[h * width + w];
            }
            for (int h = height - m; h < height; ++h) {
                n_data[h * width + w] = data[h * width + w];
            }
        }
        n.setRGB(0, 0, width, height, n_data, 0, width);
        return n;
    }

    public static int[][] generateBayerMatrix(int n) {
        int dim = 1 << n;
        int m[][] = new int[dim][dim];
        for (int i = 0; i < dim; ++i) {
            m[i] = new int[dim];
        }
        for (int y = 0; y < dim; ++y) {
            for (int x = 0; x < dim; ++x) {
                int v = 0, mask = n - 1, yc = y, xc = x ^ y;
                for (int bit = 0; bit < 2 * n; --mask) {
                    v |= ((yc >> mask) & 1) << bit++;
                    v |= ((xc >> mask) & 1) << bit++;
                }
                m[x][y] = v;
            }
        }
        return m;
    }

    public static BufferedImage getOrderedDitherImage(BufferedImage o) {
        int width = o.getWidth(), height = o.getHeight();
        int data[] = o.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

        int m[][] = generateBayerMatrix(4);

        for (int h = 0; h < height; h += m.length) {
            for (int w = 0; w < width; w += m[0].length) {
                for (int i = 0; i < m.length && h + i < height; ++i) {
                    for (int j = 0; j < m[0].length && w + j < width; ++j) {
                        int rgb = data[(h + i) * width + (w + j)];

                        int RGB[] = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};
                        for (int k = 0; k < RGB.length; ++k) {
                            if (RGB[k] <= m[i][j]) {
                                RGB[k] = 0;
                            } else {
                                RGB[k] = 255;
                            }
                        }
                        rgb = RGB[2] + RGB[1] * 256 + RGB[0] * 256 * 256;
                        n_data[(h + i) * width + (w + j)] = rgb;
                    }
                }
            }
        }

        n.setRGB(0, 0, width, height, n_data, 0, width);
        return n;
    }

    public static int[] generateGradations(int n, int count) {
        int a[] = new int[count];
        if (count == 1) {
            a[0] = n / 2;
        } else {
            double step = n / (count - 1);
            for (int i = 0; i < count; ++i) {
                // +0.5 is NOT needed
                a[i] = (int) (i * step);
            }
        }
        return a;
    }

    public static int findClosestInGradation(int n, int g[]) {
        int dif = Integer.MAX_VALUE;
        int closest = Integer.MAX_VALUE;
        for (int grad : g) {
            int new_dif = Math.abs(grad - n);
            if (new_dif < dif) {
                dif = new_dif;
                closest = grad;
            }
        }
        return closest;
    }

    public static BufferedImage getFloydSteinbergDitheredImage(BufferedImage o, int r_count, int g_count, int b_count) {
        int width = o.getWidth(), height = o.getHeight();
        int data[] = o.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

        double errors[][][] = new double[][][]{
            new double[width][height],
            new double[width][height],
            new double[width][height]
        };
        int gradations[][] = {
            Filters.generateGradations(255, r_count),
            Filters.generateGradations(255, g_count),
            Filters.generateGradations(255, b_count)
        };
        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < width; ++w) {
                int rgb = data[h * width + w];
                int RGB[] = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};
                for (int k = 0; k < 3; ++k) {
                    RGB[k] += (int) (errors[k][w][h] + 0.5);
                    int closest = Filters.findClosestInGradation(RGB[k], gradations[k]);
                    int error = RGB[k] - closest;
                    RGB[k] = closest;
                    if (w + 1 < width) {
                        errors[k][w + 1][h] = ((double) 7 / 16 * error);
                        if (h + 1 < height) {
                            errors[k][w + 1][h + 1] = ((double) 1 / 16 * error);
                        }
                    }
                    if (h + 1 < height) {
                        errors[k][w][h + 1] = ((double) 5 / 16 * error);
                        if (w - 1 > 0) {
                            errors[k][w - 1][h + 1] = ((double) 3 / 16 * error);
                        }
                    }
                }
                rgb = RGB[2] + RGB[1] * 256 + RGB[0] * 256 * 256;
                n_data[h * width + w] = rgb;
            }
        }
        n.setRGB(0, 0, width, height, n_data, 0, width);
        return n;
    }

    public static BufferedImage applyConvolutionMatrix(BufferedImage o, double m[][], double r_s, double g_s, double b_s) {
        int width = o.getWidth(), height = o.getHeight();
        int data[] = o.getRGB(0, 0, width, height, null, 0, width);

        BufferedImage n = new BufferedImage(width, height, o.getType());
        int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

        int m_h = m.length / 2;
        int m_w = m[0].length / 2;
        for (int h = m_h; h < height - m_h; ++h) {
            for (int w = m_w; w < width - m_w; ++w) {
                double R = 0, G = 0, B = 0;
                for (int i = 0; i < m.length; ++i) {
                    for (int j = 0; j < m[0].length; ++j) {
                        double r = 0, g = 0, b = 0;
                        int k = h + i - m_h;
                        int l = w + j - m_w;

                        int rgb = data[k * width + l];
                        int RGB[] = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};

                        R += m[i][j] * RGB[0];
                        G += m[i][j] * RGB[1];
                        B += m[i][j] * RGB[2];
                    }
                }
                R = Math.min(Math.max(0, R + r_s), 255);
                G = Math.min(Math.max(0, G + g_s), 255);
                B = Math.min(Math.max(0, B + b_s), 255);
                n_data[h * width + w] = 256 * 256 * ((int) (R + 0.5)) + 256 * ((int) (G + 0.5)) + (int) (B + 0.5);
            }
        }
        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < m_w; ++w) {
                n_data[h * width + w] = data[h * width + w];
            }
            for (int w = width - m_w; w < width; ++w) {
                n_data[h * width + w] = data[h * width + w];
            }
        }
        for (int w = 0; w < width; ++w) {
            for (int h = 0; h < m_h; ++h) {
                n_data[h * width + w] = data[h * width + w];
            }
            for (int h = height - m_h; h < height; ++h) {
                n_data[h * width + w] = data[h * width + w];
            }
        }

        n.setRGB(0, 0, width, height, n_data, 0, width);
        return n;
    }
}