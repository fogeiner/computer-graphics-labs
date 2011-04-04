package FIT_8201_Sviridov_Flt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Class encapsulates filters
 * 
 * @author alstein
 */
public class Filters {

	/**
	 * Makes grayscale image
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
	public static BufferedImage getGreyscaleImage(BufferedImage o) {
		int width = o.getWidth(), height = o.getHeight();
		int data[] = o.getRGB(0, 0, width, height, null, 0, width);

		BufferedImage n = new BufferedImage(width, height, o.getType());
		int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);
		for (int h = 0; h < height; ++h) {
			for (int w = 0; w < width; ++w) {
				int rgb = data[h * width + w];
				int RGB[] = { (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF };

				int R = RGB[0], G = RGB[1], B = RGB[2];

				int y = (int) ((0.299 * R + 0.587 * G + 0.114 * B) + 0.5);

				n_data[h * width + w] = 256 * (256 * y + y) + y;
			}
		}

		n.setRGB(0, 0, width, height, n_data, 0, width);
		return n;
	}

	/**
	 * Makes negative image
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
	public static BufferedImage getNegativeImage(BufferedImage o) {
		int width = o.getWidth(), height = o.getHeight();
		int data[] = o.getRGB(0, 0, width, height, null, 0, width);

		BufferedImage n = new BufferedImage(width, height, o.getType());
		int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

		for (int h = 0; h < height; ++h) {
			for (int w = 0; w < width; ++w) {
				int rgb = data[h * width + w];
				int RGB[] = { (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF };
				int R = 255 - RGB[0], G = 255 - RGB[1], B = 255 - RGB[2];
				n_data[h * width + w] = 256 * 256 * R + 256 * G + B;
			}
		}
		n.setRGB(0, 0, width, height, n_data, 0, width);
		return n;
	}

	/**
	 * Makes gamma corrected image
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
	public static BufferedImage getGammaCorrectedImage(BufferedImage o,
			double gamma) {
		int width = o.getWidth(), height = o.getHeight();
		int data[] = o.getRGB(0, 0, width, height, null, 0, width);

		BufferedImage n = new BufferedImage(width, height, o.getType());
		int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

		for (int h = 0; h < height; ++h) {
			for (int w = 0; w < width; ++w) {
				int rgb = data[h * width + w];
				int RGB[] = { (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF };
				for (int k = 0; k < 3; ++k) {
					RGB[k] = (int) (Math
							.min(255,
									Math.max(0, (int) (Math.pow(RGB[k] / 255.0,
											gamma) * 255 + 0.5))));
				}
				n_data[h * width + w] = 256 * 256 * RGB[0] + 256 * RGB[1]
						+ RGB[2];
			}
		}
		n.setRGB(0, 0, width, height, n_data, 0, width);
		return n;
	}

	private static double saturation(double v) {
		if (v < 0) {
			return 0;
		}

		if (v > 1) {
			return 1;
		}

		return v;
	}

	/**
	 * Makes brightness corrected image
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
	public static BufferedImage getContrastCorrectedImage(BufferedImage o,
			double gamma) {
		int width = o.getWidth(), height = o.getHeight();
		int data[] = o.getRGB(0, 0, width, height, null, 0, width);

		BufferedImage n = new BufferedImage(width, height, o.getType());
		int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

		for (int h = 0; h < height; ++h) {
			for (int w = 0; w < width; ++w) {
				int rgb = data[h * width + w];
				int RGB[] = { (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF };
				for (int k = 0; k < 3; ++k) {
					RGB[k] = (int) (Math.min(255, Math.max(0,
							(int) (saturation((RGB[k] / 255.0 - 0.5) * gamma
									+ 0.5) * 255 + 0.5))));
				}
				n_data[h * width + w] = 256 * 256 * RGB[0] + 256 * RGB[1]
						+ RGB[2];
			}
		}
		n.setRGB(0, 0, width, height, n_data, 0, width);
		return n;
	}

	/**
	 * Makes brightness corrected image
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
	public static BufferedImage getBrightnessCorrectedImage(BufferedImage o,
			double gamma) {
		int width = o.getWidth(), height = o.getHeight();
		int data[] = o.getRGB(0, 0, width, height, null, 0, width);

		BufferedImage n = new BufferedImage(width, height, o.getType());
		int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

		for (int h = 0; h < height; ++h) {
			for (int w = 0; w < width; ++w) {
				int rgb = data[h * width + w];
				int RGB[] = { (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF };
				for (int k = 0; k < 3; ++k) {
					RGB[k] = (int) (Math.min(
							255,
							Math.max(0, (int) (saturation(RGB[k] / 255.0
									+ gamma) * 255 + 0.5))));
				}
				n_data[h * width + w] = 256 * 256 * RGB[0] + 256 * RGB[1]
						+ RGB[2];
			}
		}
		n.setRGB(0, 0, width, height, n_data, 0, width);
		return n;
	}

	/**
	 * Makes blurred image
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
	public static BufferedImage getBlurImage(BufferedImage o) {
		double m[][] = new double[][] {
				new double[] { 1.0 / 74, 2.0 / 74, 3.0 / 74, 2.0 / 74, 1.0 / 74 },
				new double[] { 2.0 / 74, 4.0 / 74, 5.0 / 74, 4.0 / 74, 2.0 / 74 },
				new double[] { 3.0 / 74, 5.0 / 74, 6.0 / 74, 5.0 / 74, 3.0 / 74 },
				new double[] { 2.0 / 74, 4.0 / 74, 5.0 / 74, 4.0 / 74, 2.0 / 74 },
				new double[] { 1.0 / 74, 2.0 / 74, 3.0 / 74, 2.0 / 74, 1.0 / 74 } };
		return Filters.applyConvolutionMatrix(o, m, 0, 0, 0);
	}

	/**
	 * Makes embosses image
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
	public static BufferedImage getEmbossImage(BufferedImage o) {
		double m[][] = new double[][] { new double[] { 0, 1, 0 },
				new double[] { -1, 0, 1 }, new double[] { 0, -1, 0 } };
		return Filters.applyConvolutionMatrix(o, m, 128, 128, 128);
	}

	/**
	 * Makes Sobel edge detection to image
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
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
				double g11 = data[h * width + w] & 0xFF, g12 = data[(h + 1)
						* width + w] & 0xFF, g13 = data[(h + 2) * width + w] & 0xFF, g21 = data[h
						* width + (w + 1)] & 0xFF, g23 = data[(h + 2) * width
						+ (w + 1)] & 0xFF, g31 = data[h * width + (w + 2)] & 0xFF, g32 = data[(h + 1)
						* width + (w + 2)] & 0xFF, g33 = data[(h + 2) * width
						+ (w + 2)] & 0xFF;

				double Sx = (g13 + 2 * g23 + g33) - (g11 + 2 * g21 + g31);
				double Sy = (g31 + 2 * g32 + g33) - (g11 + 2 * g12 + g13);

				double S = Math.abs(Sx) + Math.abs(Sy);

				if (S > threshold) {
					n_data[h * width + w] = black_rgb;
				} else {
					n_data[h * width + w] = white_rgb;
				}
			}
		}
		n.setRGB(0, 0, width, height, n_data, 0, width);
		return n;
	}

	/**
	 * Makes Roberts edge detection to image
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
	public static BufferedImage getRobertsImage(BufferedImage o,
			double threshold) {
		int width = o.getWidth(), height = o.getHeight();
		BufferedImage n = new BufferedImage(width, height, o.getType());
		int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);
		BufferedImage gs = Filters.getGreyscaleImage(o);
		int data[] = gs.getRGB(0, 0, width, height, null, 0, width);

		int white_rgb = Color.white.getRGB();
		int black_rgb = Color.black.getRGB();

		for (int h = 0; h < height - 1; ++h) {
			for (int w = 0; w < width - 1; ++w) {

				double g11 = data[h * width + w] & 0xFF, g12 = data[(h + 1)
						* width + w] & 0xFF, g21 = data[h * width + (w + 1)] & 0xFF, g22 = data[(h + 1)
						* width + (w + 1)] & 0xFF;

				double R = Math.abs(g11 - g22) + Math.abs(g12 - g21);
				if (R > threshold) {
					n_data[h * width + w] = black_rgb;
				} else {
					n_data[h * width + w] = white_rgb;
				}
			}
		}

		n.setRGB(0, 0, width, height, n_data, 0, width);
		return n;
	}

	/**
	 * Makes sharpen image with 3x3 matrix
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
	public static BufferedImage getSharpen3Image(BufferedImage o) {
		double m[][] = new double[][] { new double[] { 0, -1, 0 },
				new double[] { -1, 5, -1 }, new double[] { 0, -1, 0 } };
		return Filters.applyConvolutionMatrix(o, m, 0, 0, 0);
	}

	/**
	 * Makes sharpen image with 5x5 matrix
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
	public static BufferedImage getSharpen5Image(BufferedImage o) {
		double m[][] = new double[][] { new double[] { 0, 0, 0, 0, 0 },
				new double[] { 0, 0, -1, 0, 0 },
				new double[] { 0, -1, 5, -1, 0 },
				new double[] { 0, 0, -1, 0, 0 }, new double[] { 0, 0, 0, 0, 0 } };
		return Filters.applyConvolutionMatrix(o, m, 0, 0, 0);
	}

	/**
	 * Makes double scaled image
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
	public static BufferedImage getDoubleScaleImage(BufferedImage o) {
		int o_width = o.getWidth(), o_height = o.getHeight();
		int center_height, center_width;
		center_width = o_width / 2 + (o_width / 2 % 2 == 0 ? 0 : 1);
		center_height = o_height / 2 + (o_height / 2 % 2 == 0 ? 0 : 1);
		BufferedImage c = o.getSubimage(o_width / 4, o_height / 4,
				center_width, center_height);
		BufferedImage n = new BufferedImage(2 * center_width,
				2 * center_height, BufferedImage.TYPE_INT_RGB);
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
				int RGB11[] = { (rgb11 >> 16) & 0xFF, (rgb11 >> 8) & 0xFF,
						rgb11 & 0xFF };
				int RGB12[] = { (rgb12 >> 16) & 0xFF, (rgb12 >> 8) & 0xFF,
						rgb12 & 0xFF };
				int RGB21[] = { (rgb21 >> 16) & 0xFF, (rgb21 >> 8) & 0xFF,
						rgb21 & 0xFF };
				int RGB22[] = { (rgb22 >> 16) & 0xFF, (rgb22 >> 8) & 0xFF,
						rgb22 & 0xFF };
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

	/**
	 * Makes color smoothed image
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
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

	/**
	 * Makes ordered dithered image
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
	public static BufferedImage getOrderedDitherImage(BufferedImage o) {
		int width = o.getWidth(), height = o.getHeight();
		int data[] = o.getRGB(0, 0, width, height, null, 0, width);

		BufferedImage n = new BufferedImage(width, height, o.getType());
		int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

		int m[][] = {
				{ 0, 128, 32, 160, 8, 136, 40, 168, 2, 130, 34, 162, 10, 138,
						42, 170 },
				{ 192, 64, 224, 96, 200, 72, 232, 104, 194, 66, 226, 98, 202,
						74, 234, 106 },
				{ 48, 176, 16, 144, 56, 184, 24, 152, 50, 178, 18, 146, 58,
						186, 26, 154 },
				{ 240, 112, 208, 80, 248, 120, 216, 88, 242, 114, 210, 82, 250,
						122, 218, 90 },
				{ 12, 140, 44, 172, 4, 132, 36, 164, 14, 142, 46, 174, 6, 134,
						38, 166 },
				{ 204, 76, 236, 108, 196, 68, 228, 100, 206, 78, 238, 110, 198,
						70, 230, 102 },
				{ 60, 188, 28, 156, 52, 180, 20, 148, 62, 190, 30, 158, 54,
						182, 22, 150 },
				{ 252, 124, 220, 92, 244, 116, 212, 84, 254, 126, 222, 94, 246,
						118, 214, 86 },
				{ 3, 131, 35, 163, 11, 139, 43, 171, 1, 129, 33, 161, 9, 137,
						41, 169 },
				{ 195, 67, 227, 99, 203, 75, 235, 107, 193, 65, 225, 97, 201,
						73, 233, 105 },
				{ 51, 179, 19, 147, 59, 187, 27, 155, 49, 177, 17, 145, 57,
						185, 25, 153 },
				{ 243, 115, 211, 83, 251, 123, 219, 91, 241, 113, 209, 81, 249,
						121, 217, 89 },
				{ 15, 143, 47, 175, 7, 135, 39, 167, 13, 141, 45, 173, 5, 133,
						37, 165 },
				{ 207, 79, 239, 111, 199, 71, 231, 103, 205, 77, 237, 109, 197,
						69, 229, 101 },
				{ 63, 191, 31, 159, 55, 183, 23, 151, 61, 189, 29, 157, 53,
						181, 21, 149 },
				{ 255, 127, 223, 95, 247, 119, 215, 87, 253, 125, 221, 93, 245,
						117, 213, 85 } };

		for (int h = 0; h < height; ++h) {
			for (int w = 0; w < width; ++w) {
				int rgb = data[h * width + w];

				int RGB[] = { (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF };
				for (int k = 0; k < RGB.length; ++k) {
					if (RGB[k] <= m[w % m[0].length][h % m.length]) {
						RGB[k] = 0;
					} else {
						RGB[k] = 255;
					}
				}
				rgb = RGB[2] + RGB[1] * 256 + RGB[0] * 256 * 256;
				n_data[h * width + w] = rgb;
			}
		}

		n.setRGB(0, 0, width, height, n_data, 0, width);
		return n;
	}

	/**
	 * Generates gradations
	 * 
	 * @param n
	 *            number
	 * @param count
	 *            number of gradations
	 * @return gradations
	 */
	public static double[] generateGradations(double n, int count) {
		if (count < 0) {
			throw new IllegalArgumentException("count cannot be negative");
		}

		double a[] = new double[count];
		if (count == 0) {
			return a;
		} else if (count == 1) {
			a[0] = n;
		} else {
			a[0] = 0;
			a[count - 1] = n;

			for (int k = 1; k < count - 1; ++k) {
				a[k] = n / (count - 1) * k;
			}
		}
		return a;
	}

	/**
	 * Finds closest number in arrays of gradations
	 * 
	 * @param n
	 *            number
	 * @param g
	 *            gradations
	 * @return closest number
	 */
	public static double findClosestInGradation(double n, double g[]) {
		double dif = Double.MAX_VALUE;
		double closest = Double.MAX_VALUE;
		for (double grad : g) {
			double new_dif = Math.abs(grad - n);
			if (new_dif < dif) {
				dif = new_dif;
				closest = grad;
			}
		}
		return closest;
	}

	/**
	 * Makes Floyd-Steinberg dithered image
	 * 
	 * @param o
	 *            image
	 * @return processed image
	 */
	public static BufferedImage getFloydSteinbergDitheredImage(BufferedImage o,
			int r_count, int g_count, int b_count) {
		int width = o.getWidth(), height = o.getHeight();
		int data[] = o.getRGB(0, 0, width, height, null, 0, width);

		BufferedImage n = new BufferedImage(width, height, o.getType());
		int n_data[] = n.getRGB(0, 0, width, height, null, 0, width);

		double d_data[][][] = new double[3][width][height];

		for (int h = 0; h < height; ++h) {
			for (int w = 0; w < width; ++w) {
				d_data[0][w][h] = ((data[h * width + w] >> 16) & 0xFF) / 255.0;
				d_data[1][w][h] = ((data[h * width + w] >> 8) & 0xFF) / 255.0;
				d_data[2][w][h] = ((data[h * width + w]) & 0xFF) / 255.0;
			}
		}

		double gradations[][] = { Filters.generateGradations(1.0, r_count),
				Filters.generateGradations(1.0, g_count),
				Filters.generateGradations(1.0, b_count) };

		for (int h = 0; h < height; ++h) {
			for (int w = 0; w < width; ++w) {
				double RGB[] = { d_data[0][w][h], d_data[1][w][h],
						d_data[2][w][h] };

				for (int k = 0; k < RGB.length; ++k) {

					double closest = Filters.findClosestInGradation(RGB[k],
							gradations[k]);
					double error = RGB[k] - closest;
					RGB[k] = closest;
					if (w + 1 < width) {
						d_data[k][w + 1][h] += (error * 7 / 16);
						if (h + 1 < height) {
							d_data[k][w + 1][h + 1] += (error * 1 / 16);
						}
					}
					if (h + 1 < height) {
						d_data[k][w][h + 1] += (error * 5 / 16);
						if (w - 1 > 0) {
							d_data[k][w - 1][h + 1] += (error * 3 / 16);
						}
					}

					RGB[k] = Math.min(Math.max(0.0, RGB[k]), 1.0);
				}
				int rgb = (int) (255 * RGB[2] + 0.5)
						+ (int) (255 * RGB[1] + 0.5) * 256
						+ (int) (255 * RGB[0] + 0.5) * 256 * 256;
				n_data[h * width + w] = rgb;
			}
		}
		n.setRGB(0, 0, width, height, n_data, 0, width);
		return n;
	}

	/**
	 * Applies convolution matrix to image
	 * 
	 * @param o
	 *            original image
	 * @param m
	 *            convolution matrix
	 * @param r_s
	 *            number to be added to R component
	 * @param g_s
	 *            number to be added to G component
	 * @param b_s
	 *            number to be added to B component
	 * @return processed image
	 */
	public static BufferedImage applyConvolutionMatrix(BufferedImage o,
			double m[][], double r_s, double g_s, double b_s) {
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

						int k = h + i - m_h;
						int l = w + j - m_w;

						int rgb = data[k * width + l];
						int RGB[] = { (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF,
								rgb & 0xFF };

						R += m[i][j] * RGB[0];
						G += m[i][j] * RGB[1];
						B += m[i][j] * RGB[2];
					}
				}
				R = Math.min(Math.max(0, R + r_s), 255);
				G = Math.min(Math.max(0, G + g_s), 255);
				B = Math.min(Math.max(0, B + b_s), 255);
				n_data[h * width + w] = 256 * 256 * ((int) (R + 0.5)) + 256
						* ((int) (G + 0.5)) + (int) (B + 0.5);
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
