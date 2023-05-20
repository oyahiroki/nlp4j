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
	 * @param fileName
	 * @return PrintWriter, append=true, charset=UTF-8, autoFlush=true text file
	 * @throws IOException
	 */
	static public PrintWriter printWriter(String fileName) throws IOException {
		Charset charset = StandardCharsets.UTF_8;
		boolean append = true;
		boolean autoflush = true;
		return new PrintWriter( //
				new OutputStreamWriter( //
						new FileOutputStream( //
								new File(fileName), append),
						charset), //
				autoflush); //
	}

}
