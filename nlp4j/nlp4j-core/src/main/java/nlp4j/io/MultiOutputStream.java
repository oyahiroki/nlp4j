package nlp4j.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created on: 2023-10-04
 * 
 * @author Hiroki Oya
 *
 */
public class MultiOutputStream extends OutputStream {

	private OutputStream[] outputStreams;

	public MultiOutputStream(OutputStream... outputStreams) {
		this.outputStreams = outputStreams;
	}

	@Override
	public void write(int b) throws IOException {
		for (OutputStream out : outputStreams) {
			out.write(b);
		}
	}

	@Override
	public void close() throws IOException {
		for (OutputStream out : outputStreams) {
			out.close();
		}
	}

	@Override
	public void flush() throws IOException {
		for (OutputStream out : outputStreams) {
			out.flush();
		}
	}

}
