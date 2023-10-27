package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class GZIPFileUtils {

	/**
	 * @param gzipFileURL
	 * @return
	 * @throws IOException
	 * @since 1.3.7.12
	 * 
	 */
	static public BufferedReader openGZIPFileAsBufferedReader(URL gzipFileURL) throws IOException {
		GZIPInputStream gzipStream = new GZIPInputStream(gzipFileURL.openStream());
		InputStreamReader isr = new InputStreamReader(gzipStream, "UTF-8");
		return new BufferedReader(isr);
	}

	/**
	 * @param gzipFile: Text file of UTF-8 encoding
	 * @return BufferedReader
	 * @throws IOException
	 */
	static public BufferedReader openGZIPFileAsBufferedReader(File gzipFile) throws IOException {
		return new BufferedReader(openGZIPFileAsInputStreamReader(gzipFile));
	}

	/**
	 * @param gzipFile: Text file of UTF-8 encoding
	 * @return InputStream
	 * @throws IOException
	 */
	static public InputStream openGZIPFileAsInputStream(File gzipFile) throws IOException {
		return new GZIPInputStream(new FileInputStream(gzipFile));
	}

	/**
	 * @param gzipFile
	 * @return
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public InputStreamReader openGZIPFileAsInputStreamReader(File gzipFile) throws IOException {
		return new InputStreamReader(openGZIPFileAsInputStream(gzipFile), StandardCharsets.UTF_8);
	}

}
