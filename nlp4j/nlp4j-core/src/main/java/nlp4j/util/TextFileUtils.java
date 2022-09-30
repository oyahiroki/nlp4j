package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class TextFileUtils {

	/**
	 * @param plainTextFile Plain Text File (*.txt)
	 * @return BufferedReader
	 * @throws IOException on IO Error
	 */
	static public BufferedReader openPlainTextFileAsBufferedReader(File plainTextFile) throws IOException {
		return new BufferedReader(new InputStreamReader(new FileInputStream(plainTextFile), StandardCharsets.UTF_8));
	}

	static public void sortLinesByValue(File inFile, File outFile) throws IOException {
		sortLinesByValue(inFile, "UTF-8", outFile, "UTF-8", false);
	}

	static public void sortLinesByValue(File inFile, String inFileEncoding, File outFile, String outFileEncoding,
			boolean append) throws IOException {
		List<String> lines = FileUtils.readLines(inFile, inFileEncoding);
		Collections.sort(lines);
		FileUtils.writeLines(outFile, outFileEncoding, lines, "\n", append);
	}

	/**
	 * Writes a data to a file creating the file if it does not exist.
	 * 
	 * @param outFile file the file to write
	 * @param data    the content to write
	 * @throws IOException - in case of an I/O error
	 */
	static public void write(File outFile, List<String> data) throws IOException {
		write(outFile, data, "UTF-8", false);
	}

	/**
	 * Writes a data to a file creating the file if it does not exist.
	 * 
	 * @param outFile  file the file to write
	 * @param data     the content to write
	 * @param encoding the name of the requested charset, null means platform
	 *                 default
	 * @param append   if true, then the data will be added to the end of the file
	 *                 rather than overwriting
	 * @throws IOException - in case of an I/O error
	 */
	static public void write(File outFile, List<String> data, String encoding, boolean append) throws IOException {
		FileUtils.write(outFile, String.join("\n", data), encoding, append);
	}

}
