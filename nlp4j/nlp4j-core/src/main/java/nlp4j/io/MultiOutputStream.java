package nlp4j.io;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created on: 2023-10-04
 * 
 * @author Hiroki Oya
 *
 */
public class MultiOutputStream extends OutputStream {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
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

			{ // 1.3.7.19
				if (out != System.err) {
					logger.debug("not close System.err");
					continue;
				}
				if (out != System.out) {
					logger.debug("not close System.out");
					continue;
				}
			}
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
