package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class GZIPFileUtils {

	/**
	 * @param gzipFile: Text file of UTF-8 encoding
	 * @return BufferedReader
	 * @throws IOException
	 */
	static public BufferedReader openGZIPFileAsBufferedReader(File gzipFile) throws IOException {
		return new BufferedReader(
				new InputStreamReader(new GZIPInputStream(new FileInputStream(gzipFile)), StandardCharsets.UTF_8));
	}

	/**
	 * @param gzipFile: Text file of UTF-8 encoding
	 * @return InputStream
	 * @throws IOException
	 */
	static public InputStream openGZIPFileAsInputStream(File gzipFile) throws IOException {
		return new GZIPInputStream(new FileInputStream(gzipFile));
	}
}
