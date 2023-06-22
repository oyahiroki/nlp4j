package nlp4j.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Hiroki Oya
 * @since 1.3.7.8
 */
public class IOUtils {

	/**
	 * Short name of {@link IOUtils#printWriter(String) }
	 * 
	 * @param fileName of print writer
	 * @return
	 * @throws IOException
	 * @since 1.3.7.9
	 */
	static public PrintWriter pw(String fileName) throws IOException {
		return printWriter(fileName);
	}

	/**
	 * @param fileName
	 * @return PrintWriter, append=true, charset=UTF-8, autoFlush=true text file
	 * @throws IOException
	 * @since 1.3.7.8
	 */
	static public PrintWriter printWriter(String fileName) throws IOException {
		File file = new File(fileName);
		boolean append = true;
		Charset charset = StandardCharsets.UTF_8;
		boolean autoflush = true;
		return printWriter(file, append, charset, autoflush);
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 * @since 1.3.7.9
	 */
	static public PrintWriter printWriter(File file) throws IOException {
		boolean append = true;
		Charset charset = StandardCharsets.UTF_8;
		boolean autoflush = true;
		return printWriter(file, append, charset, autoflush);
	}

	/**
	 * @param file
	 * @param append
	 * @param charset
	 * @param autoflush
	 * @return
	 * @throws IOException
	 * @since 1.3.7.9
	 */
	static public PrintWriter printWriter(File file, boolean append, Charset charset, boolean autoflush)
			throws IOException {
		return new PrintWriter( //
				new OutputStreamWriter( //
						new FileOutputStream( //
								file, append),
						charset), //
				autoflush); //
	}

}
