package nlp4j.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

/**
 * @author Hiroki Oya
 * @since 1.3.7.8
 */
public class IOUtils {

	/**
	 * @param file
	 * @return PrintWriter (output=file, append=true, autoflush=true, charset=UTF-8)
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
	 * @param file      : directory created if not exists
	 * @param append
	 * @param charset
	 * @param autoflush
	 * @return
	 * @throws IOException
	 * @since 1.3.7.9
	 */
	static public PrintWriter printWriter(File file, boolean append, Charset charset, boolean autoflush)
			throws IOException {

		// file.isFile() はファイルが存在しない場合は false を返す

		// 2023-08-14
		if (file.getParentFile().exists() == false) {
			FileUtils.forceMkdirParent(file);
		}

		return new PrintWriter( //
				new OutputStreamWriter( //
						new FileOutputStream( //
								file, append),
						charset), //
				autoflush); //
	}

	/**
	 * @param fileName
	 * @return PrintWriter (output=file, append=true, charset=UTF-8, autoFlush=true)
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
	 * @return PrintWriter (output=file, append=true, autoflush=true, charset=UTF-8)
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public PrintWriter pw(File file) throws IOException {
		return printWriter(file);
	}

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
	 * @return System Error
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public PrintWriter pwSystemErr() throws IOException {
		return new PrintWriter(System.err, true);
	}

	/**
	 * @return System Out
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public PrintWriter pwSystemOut() throws IOException {
		return new PrintWriter(System.out, true);
	}

}
