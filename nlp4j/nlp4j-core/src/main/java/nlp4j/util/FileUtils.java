package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

/**
 * @author Hiroki Oya
 *
 */
public class FileUtils {

	/**
	 * Returns line count of a text file
	 * 
	 * @param textFileUTF8 in UTF-8 text file
	 * @return
	 * @throws IOException
	 */
	static public long lineCount(File textFileUTF8) throws IOException {
		long lineCount = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(textFileUTF8, StandardCharsets.UTF_8))) {
			while (br.readLine() != null) {
				lineCount++;
			}
		}
		return lineCount;
	}

	/**
	 * Returns line count of a text file in GZip
	 * 
	 * @param fileGZipText
	 * @return
	 * @throws IOException
	 */
	static public long lineCountGZipTextFile(File fileGZipText) throws IOException {
		long lineCount = 0;
		try (BufferedReader br = GZIPFileUtils.openGZIPFileAsBufferedReader(fileGZipText)) {
			while (br.readLine() != null) {
				lineCount++;
			}
		}
		return lineCount;
	}

	/**
	 * Returns new PrintWriter of UTF-8
	 * 
	 * @param fileName *.gz | *.txt
	 * @return
	 * @throws IOException
	 * @since 1.3.7.6
	 */
	static public PrintWriter newPrintWriter(String fileName) throws IOException {
		return new PrintWriter(
				new OutputStreamWriter(new FileOutputStream(new File(fileName)), StandardCharsets.UTF_8));
	}

	/**
	 * @param gzipTextOrPlainTextFile
	 * @return
	 * @throws IOException
	 */
	static public BufferedReader openTextFileAsBufferedReader(File gzipTextOrPlainTextFile) throws IOException {
		return openTextFileAsBufferedReader(gzipTextOrPlainTextFile, StandardCharsets.UTF_8);
	}

	/**
	 * Returns BufferedReader of text file (plain or gzip)
	 * 
	 * @param gzipTextOrPlainTextFile plain text file OR gzip text file
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	static public BufferedReader openTextFileAsBufferedReader(File gzipTextOrPlainTextFile, Charset encoding)
			throws IOException {

		if (gzipTextOrPlainTextFile.getName().endsWith(".gz")) {
			return new BufferedReader(
					new InputStreamReader(new GZIPInputStream(new FileInputStream(gzipTextOrPlainTextFile)), encoding));

		} else {
			return new BufferedReader(new InputStreamReader((new FileInputStream(gzipTextOrPlainTextFile)), encoding));

		}

	}

	/**
	 * @param gzipTextOrPlainTextFile *.gz | *.txt
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	static public BufferedReader openTextFileAsBufferedReader(File gzipTextOrPlainTextFile, String encoding)
			throws IOException {
		return openTextFileAsBufferedReader(gzipTextOrPlainTextFile, Charset.forName(encoding));
	}

	/**
	 * @param gzipTextOrPlainTextFileName *.gz | *.txt
	 * @return
	 * @throws IOException
	 * @since 1.3.7.8
	 * 
	 */
	static public BufferedReader openTextFileAsBufferedReader(String gzipTextOrPlainTextFileName) throws IOException {
		return openTextFileAsBufferedReader(new File(gzipTextOrPlainTextFileName), StandardCharsets.UTF_8);
	}

}
