package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.io.LimitedLineBufferedReader;

public class TextFileUtils {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * @param n
	 * @param textFile
	 * @throws IOException
	 * @since 1.3.7.9
	 */
	static public void head(int n, File textFile) throws IOException {
		head(n, textFile, new PrintWriter(System.out, true));
	}

	/**
	 * @param n
	 * @param textFile
	 * @param pw
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public void head(int n, File textFile, PrintWriter pw) throws IOException {
		if (n <= 0) {
			return;
		}
		try (BufferedReader br = nlp4j.util.FileUtils.openTextFileAsBufferedReader(textFile);) {
			String line;
			int count = 0;
			while ((line = br.readLine()) != null) {
				count++;
				pw.println(line);
				if (count >= n) {
					return;
				}
			} // END_OF(WHILE)
		} // END_OF(TRY)
	}

	/**
	 * @param plainTextFile Plain Text File (*.txt) (*.gz since 1.3.7.12)
	 * @return BufferedReader
	 * @throws IOException on IO Error
	 */
	static public BufferedReader openPlainTextFileAsBufferedReader(File plainTextFile) throws IOException {
		// IF(*.gz) THEN
		if (plainTextFile != null && plainTextFile.getName().endsWith(".gz")) {
			return GZIPFileUtils.openGZIPFileAsBufferedReader(plainTextFile);
		}
		// ELSE (*.txt is expected)
		else {
			return new BufferedReader(
					new InputStreamReader(new FileInputStream(plainTextFile), StandardCharsets.UTF_8));
		}
	}

	/**
	 * @param plainTextFile Plain Text File (*.txt)(*.gz since 1.3.7.12)
	 * @param maxLines
	 * @return BufferedReader
	 * @throws IOException on IO Error
	 */
	static public BufferedReader openPlainTextFileAsBufferedReader(File plainTextFile, int maxLines)
			throws IOException {
		// IF(*.gz) THEN
		if (plainTextFile != null && plainTextFile.getName().endsWith(".gz")) {
			return new LimitedLineBufferedReader(GZIPFileUtils.openGZIPFileAsInputStreamReader(plainTextFile),
					maxLines);
		}
		// ELSE (*.txt is expected)
		else {
			return new LimitedLineBufferedReader(
					new InputStreamReader(new FileInputStream(plainTextFile), StandardCharsets.UTF_8), maxLines);
		}

	}

	/**
	 * @param plainTextFile Plain Text File (*.txt)
	 * @param encoding      encoding of text file
	 * @return BufferedReader
	 * @return
	 * @throws IOException on IO Error
	 */
	static public BufferedReader openPlainTextFileAsBufferedReader(File plainTextFile, String encoding)
			throws IOException {
		return new BufferedReader(new InputStreamReader(new FileInputStream(plainTextFile), Charset.forName(encoding)));
	}

	/**
	 * @param textFileUrl
	 * @return
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public BufferedReader openPlainTextFileAsBufferedReader(URL textFileUrl) throws IOException {
		// IF(*.gz) THEN
		if (textFileUrl != null && textFileUrl.getFile().endsWith(".gz")) {
			return GZIPFileUtils.openGZIPFileAsBufferedReader(textFileUrl);
		}
		// ELSE (*.txt is expected)
		else {
			return new BufferedReader(new InputStreamReader(textFileUrl.openStream(), StandardCharsets.UTF_8));
		}
	}

	/**
	 * @param textFileUrl
	 * @param writeCache  ファイルをローカルのディスクに書き込む ローカルディスクのキャッシュを利用する
	 * @return
	 * @throws IOException
	 */
	static public BufferedReader openPlainTextFileAsBufferedReader(URL textFileUrl, boolean writeCache)
			throws IOException {
		// IF(キャッシュを利用する)
		if (writeCache == false) {
			return openPlainTextFileAsBufferedReader(textFileUrl);
		}
		// ELSE(キャッシュを利用する)
		else {
			File cacheDir = new File("/usr/local/nlp4j/cache");
			// ディレクトリがなければ作成する
			if (cacheDir.exists() == false) {
				cacheDir.mkdir();
			}
			// URL to FileName
			String fileName = FilenameUtils.toFileName(textFileUrl);
			File cacheFile = new File(cacheDir, fileName);
			// Cache Not Found
			if (cacheFile.exists() == false) {
				logger.info("download_from: " + textFileUrl);
				FileUtils.copyURLToFile(textFileUrl, cacheFile);
			}
			// Cache Found
			else {
				logger.info("read_from: " + cacheFile.getAbsolutePath());
			}
			return openPlainTextFileAsBufferedReader(cacheFile);
		}
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

	static public int wc_l(File textFile) throws IOException {
		int count = 0;
		try (BufferedReader br = nlp4j.util.FileUtils.openTextFileAsBufferedReader(textFile);) {
			while ((br.readLine()) != null) {
				count++;
			}
		}
		return count;
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
