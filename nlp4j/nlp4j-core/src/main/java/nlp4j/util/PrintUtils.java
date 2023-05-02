package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hiroki Oya
 * @since 1.3.7.8
 *
 */
public class PrintUtils {

	/**
	 * Print first n lines
	 * 
	 * @param n        number of lines
	 * @param textFile Plain text file
	 * @param skip     number of skip line
	 * @throws IOException on IO Error
	 */
	static public void headText(int n, File textFile, int skip) throws IOException {
		try (BufferedReader br = FileUtils.openTextFileAsBufferedReader(textFile);) {
			String line;
			int count = 0;
			if (n == 0) {
				return;
			}
			while ((line = br.readLine()) != null) {
				skip--;
				if (skip >= 0) {
					continue;
				}
				count++;
				if (count > n) {
					return;
				}
				System.err.println(line);
			}
		}
	}

	/**
	 * Print first n lines
	 * 
	 * @param n            number of lines
	 * @param textFileName
	 * @param skip         number of skip line
	 * @throws IOException on IO Error
	 */
	static public void headText(int n, String textFileName, int skip) throws IOException {
		headText(n, new File(textFileName), skip);
	}

	/**
	 * Print first n lines
	 * 
	 * @param n            number of lines
	 * @param textFileName
	 * @throws IOException on IO Error
	 */
	static public void headText(int n, String textFileName) throws IOException {
		headText(n, new File(textFileName), 0);
	}

	/**
	 * Print first n Json line
	 * 
	 * @param n        number of lines
	 * @param jsonFile Json Line separated Text File
	 * @param skip     number of skip line
	 * @throws IOException on IO Error
	 */
	static public void headJson(int n, File jsonFile, int skip) throws IOException {

		try (BufferedReader br = FileUtils.openTextFileAsBufferedReader(jsonFile);) {
			String line;
			int count = 0;
			if (n == 0) {
				return;
			}
			while ((line = br.readLine()) != null) {
				skip--;
				if (skip >= 0) {
					continue;
				}
				count++;
				if (count > n) {
					return;
				}
				System.err.println(JsonUtils.prettyPrint(line));
			}
		}
	}

	/**
	 * @param n        number of lines
	 * @param fileName Json Line separated Text File Name
	 * @param skip     number of skip line
	 * @throws IOException on IO Error
	 */
	static public void headJson(int n, String fileName, int skip) throws IOException {
		File jsonFile = new File(fileName);
		headJson(n, jsonFile, skip);
	}

	/**
	 * @param n        number of lines
	 * @param fileName Json Line separated Text File Name
	 * @throws IOException on IO Error
	 */
	static public void headJson(int n, String fileName) throws IOException {
		headJson(n, fileName, 0);
	}

	static public void grepJson(String regex, String fileName) throws IOException {
		int max = -1;
		grepJson(regex, fileName, max);

	}

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static public void grepJson(String regex, String fileName, int max) throws IOException {

		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);

		try (BufferedReader br = FileUtils.openTextFileAsBufferedReader(fileName);) {
			String line;
			int lineCount = 0;
			int count = 0;
			while ((line = br.readLine()) != null) {
				lineCount++;
				if (logger.isDebugEnabled()) {
					if (lineCount % 1000 == 0) {
						logger.debug("lineCount: " + lineCount);
					}
				}
				Matcher matcher = pattern.matcher(line);

				if (matcher.find()) {
					System.out.println(JsonUtils.prettyPrint(line));
					count++;
					if (max < 0 == false && count >= max) {
						return;
					}
				}
			}
		}

	}

}
