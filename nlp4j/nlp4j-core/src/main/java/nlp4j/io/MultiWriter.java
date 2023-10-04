package nlp4j.io;

import java.io.IOException;
import java.io.Writer;

/**
 * Created on: 2023-10-04
 * 
 * @author Hiroki Oya
 *
 */
public class MultiWriter extends Writer {

	private Writer[] writers;

	public MultiWriter(Writer... writers) {
		this.writers = writers;
	}

	@Override
	public void write(int c) throws IOException {
		for (Writer w : writers) {
			w.write(c);
		}
	}

	@Override
	public void write(char[] cbuf) throws IOException {
		for (Writer w : writers) {
			w.write(cbuf);
		}
	}

	@Override
	public void write(String str) throws IOException {
		for (Writer w : writers) {
			w.write(str);
		}
	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		for (Writer w : writers) {
			w.write(str, off, len);
		}
	}

	@Override
	public Writer append(CharSequence csq) throws IOException {
		for (Writer w : writers) {
			w.append(csq);
		}
		return this;
	}

	@Override
	public Writer append(CharSequence csq, int start, int end) throws IOException {
		for (Writer w : writers) {
			w.append(csq, start, end);
		}
		return this;
	}

	@Override
	public Writer append(char c) throws IOException {
		for (Writer w : writers) {
			w.append(c);
		}
		return this;
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		for (Writer w : writers) {
			w.write(cbuf, off, len);
		}

	}

	@Override
	public void flush() throws IOException {
		for (Writer w : writers) {
			w.flush();
		}

	}

	@Override
	public void close() throws IOException {
		for (Writer w : writers) {
			w.flush();
			w.close();
		}
	}

}
