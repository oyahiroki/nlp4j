package nlp4j.io;

import java.io.IOException;
import java.io.OutputStream;

public class DevNullOutputStream extends OutputStream {

	public DevNullOutputStream() {
	}

	@Override
	public void write(int b) throws IOException {
	}

	@Override
	public void write(byte[] b) {
	}

	@Override
	public void write(byte[] b, int off, int len) {
	}
}
