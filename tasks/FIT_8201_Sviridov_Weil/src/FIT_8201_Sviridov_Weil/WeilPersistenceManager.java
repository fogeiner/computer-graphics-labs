package FIT_8201_Sviridov_Weil;

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
 * 
 * @author admin
 * 
 */
public class WeilPersistenceManager {
	public static void saveToFile(File file, WeilSettings ps)
			throws IOException {

		FileWriter fw = new FileWriter(file);

		fw.close();

	}

	public static void loadFromFile(File file, WeilSettings ps)
			throws IOException {
		FileInputStream fstream = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		LineParseUtils.nextNormalizedLine(br);

		
		ps.modelLoaded();
	}
}
