package nlp4j.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * PrintWriter that ignores output
 * 
 * @author Hiroki Oya
 */
public class DevNullPrintWriter extends PrintWriter {

	public DevNullPrintWriter(Writer out) {
		super(new DevNullOutputStream());
	}

	public DevNullPrintWriter(OutputStream out) {
		super(new DevNullOutputStream());
	}

	public DevNullPrintWriter(String fileName) throws FileNotFoundException {
		super(new DevNullOutputStream());
	}

	public DevNullPrintWriter(File file) throws FileNotFoundException {
		super(new DevNullOutputStream());
	}

	public DevNullPrintWriter(Writer out, boolean autoFlush) {
		super(new DevNullOutputStream());
	}

	public DevNullPrintWriter(OutputStream out, boolean autoFlush) {
		super(new DevNullOutputStream());
	}

	public DevNullPrintWriter(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
		super(new DevNullOutputStream());
	}

	public DevNullPrintWriter(String fileName, Charset charset) throws IOException {
		super(new DevNullOutputStream());
	}

	public DevNullPrintWriter(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
		super(new DevNullOutputStream());
	}

	public DevNullPrintWriter(File file, Charset charset) throws IOException {
		super(new DevNullOutputStream());
	}

	public DevNullPrintWriter(OutputStream out, boolean autoFlush, Charset charset) {
		super(new DevNullOutputStream());
	}

	@Override
	public void write(char[] buf, int off, int len) {
//		super.write(buf, off, len);
	}

}
