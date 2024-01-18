package nlp4j.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * Created on: 2023-10-04
 * 
 * @author Hiroki Oya
 *
 */
public class NoCloseWriter extends Writer {

	private Writer w;

	public NoCloseWriter(Writer w) {
		this.w = w;
	}

	public NoCloseWriter(PrintStream ps) {
		boolean autoFlush = true;
//		this.w = new PrintWriter(ps, autoFlush, StandardCharsets.UTF_8); // since jdk10
		this.w = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ps, StandardCharsets.UTF_8)), autoFlush);
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		w.write(cbuf, off, len);
	}

	@Override
	public void flush() throws IOException {
		w.flush();
	}

	@Override
	public void close() throws IOException {
		// do nothing

	}

}
