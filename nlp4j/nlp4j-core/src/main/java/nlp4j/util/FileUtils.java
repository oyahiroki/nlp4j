package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

}
