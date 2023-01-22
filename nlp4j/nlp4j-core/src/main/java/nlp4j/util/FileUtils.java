package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class FileUtils {

	static public long lineCount(File file) throws IOException {
		long lineCount = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
			while (br.readLine() != null) {
				lineCount++;
			}
		}
		return lineCount;
	}

	static public long lineCountGZipTextFile(File file) throws IOException {
		long lineCount = 0;
		try (BufferedReader br = GZIPFileUtils.openGZIPFileAsBufferedReader(file)) {
			while (br.readLine() != null) {
				lineCount++;
			}
		}
		return lineCount;
	}

	static public BufferedReader openTextFileAsBufferedReader(File gzipTextOrPlainTextFile) throws IOException {
		return openTextFileAsBufferedReader(gzipTextOrPlainTextFile, StandardCharsets.UTF_8);
	}

	static public BufferedReader openTextFileAsBufferedReader(File gzipTextOrPlainTextFile, Charset encoding)
			throws IOException {

		if (gzipTextOrPlainTextFile.getName().endsWith(".gz")) {
			return new BufferedReader(
					new InputStreamReader(new GZIPInputStream(new FileInputStream(gzipTextOrPlainTextFile)), encoding));

		} else {
			return new BufferedReader(new InputStreamReader((new FileInputStream(gzipTextOrPlainTextFile)), encoding));

		}

	}

}
