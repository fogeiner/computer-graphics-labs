package FIT_8201_Sviridov_Cam;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Utility class with method of line normalization (trim, get rid of C-style
 * one-line comments, empty lines)
 * 
 * @author alstein
 */
public class LineParseUtils {

	/**
	 * Reads line from BufferedReader, trims, gets rid of C-style one-line
	 * comments and empty lines
	 * 
	 * @param br
	 *            BufferedReader
	 * @return String with next normalized line
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public static String nextNormalizedLine(BufferedReader br)
			throws IOException {
		while (true) {
			String str = br.readLine();
			if (str == null) {
				return null;
			}
			StringBuilder sb = new StringBuilder();

			str = str.trim();

			if (str.length() == 0) {
				continue;
			}

			int IN = 0;
			int OUT = 1;
			int state = IN;

			for (int i = 0; i < str.length(); ++i) {

				if (str.substring(i).startsWith("//")) {
					break;
				}

				Character c = str.charAt(i);

				if (Character.isWhitespace(c)) {
					if (state == IN) {
						state = OUT;
						sb.append(" ");
					} else {
						continue;
					}
				} else {
					if (state == IN) {
						sb.append(c);
					} else {
						state = IN;
						sb.append(c);
					}
				}
			}

			str = sb.toString().trim();

			if (str.length() > 0) {
				return str;
			}
		}
	}
}
