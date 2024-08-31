package nlp4j.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

public class TempFileUtils {

	static public Pair<PrintWriter, File> printWriter() throws IOException {
		File tempFile = File.createTempFile("nlp4j_temp", ".txt");
		PrintWriter pw = IOUtils.printWriter(tempFile.getAbsolutePath());
		Pair<PrintWriter, File> p = Pair.of(pw, tempFile);
		return p;
	}

	/**
	 * @param s
	 * @return
	 * @throws IOException
	 * @since 1.3.7.14
	 */
	static public File print(String s) throws IOException {
		File tempFile = File.createTempFile("nlp4j_temp", ".txt");
		FileUtils.write(tempFile, s, "UTF-8");
		return tempFile;
	}

}
