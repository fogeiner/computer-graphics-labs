package FIT_8201_Sviridov_Lines;

import java.io.BufferedReader;
import java.io.IOException;

public class LineParseUtils {
	public static String nextNormalizedLine(BufferedReader br)
			throws IOException {
		while (true) {
			String str = br.readLine();
			if (str == null)
				return null;
			StringBuffer sb = new StringBuffer();

			str = str.trim();

			if (str.length() == 0)
				continue;

			int IN = 0;
			int OUT = 1;
			int state = IN;

			for (int i = 0; i < str.length(); ++i) {

				if (str.substring(i).startsWith("//"))
					break;

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
