package nlp4j.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * created on 2022-07-23
 * 
 * @author Hiroki Oya
 * @since 1.3.7
 */
public class FilePrinter implements Closeable {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	File outFile = null;

	StringBuilder buff = new StringBuilder();
	int buffCount = 0;
	int buffMax = 100;

	public FilePrinter() throws IOException {
		super();
		this.outFile = File.createTempFile("temp", ".txt");
		logger.info("outFile: " + this.outFile.getAbsolutePath());
	}

	public FilePrinter(File outFile) throws IOException {
		this.outFile = outFile;
		if (this.outFile.exists() && this.outFile.canWrite() == false) {
			throw new IOException("can not write: " + this.outFile.getAbsolutePath());
		}
	}

	public FilePrinter(File outFile, int buffSize) throws IOException {
		this.outFile = outFile;
		if (this.outFile.exists() && this.outFile.canWrite() == false) {
			throw new IOException("can not write: " + this.outFile.getAbsolutePath());
		}
		this.buffMax = buffSize;
	}

	public FilePrinter(int buffSize) throws IOException {
		this.outFile = File.createTempFile("temp", ".txt");
		logger.info("outFile: " + this.outFile.getAbsolutePath());
		this.buffMax = buffSize;
	}

	public FilePrinter(String dir, String prefix, String postfix) throws IOException {
		this.outFile = File.createTempFile(prefix, postfix, new File(dir));
		if (this.outFile.exists() && this.outFile.canWrite() == false) {
			throw new IOException("can not write: " + this.outFile.getAbsolutePath());
		}
	}

	public void close() throws IOException {
		writeData();
	}

	public File getOutFile() {
		return outFile;
	}

	public void println(String data) throws IOException {
		buff.append(data + "\n");
		buffCount++;
		if (buffCount >= buffMax) {
			writeData();
		}
	}

	private void writeData() throws IOException {
		FileUtils.write(outFile, buff, "UTF-8", true);
		buff = new StringBuilder();
		buffCount = 0;
	}

}
