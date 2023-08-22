package nlp4j.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * CSV Writer
 * 
 * @author Hiroki Oya
 * @since 1.3.7.10
 */
public class CsvWriter implements Closeable {

	File outCsvFile;

	private CSVPrinter printer;

	private String encoding = "UTF-8";

	/**
	 * CSV Writer
	 * 
	 * @param outCsvFile
	 * @throws IOException
	 */
	public CsvWriter(File outCsvFile) throws IOException {
		super();
		this.outCsvFile = outCsvFile;
		boolean append = false;
		printer = new CSVPrinter( //
				new OutputStreamWriter(new FileOutputStream(outCsvFile, append), this.encoding), CSVFormat.EXCEL);
	}

	/**
	 * CSV Writer
	 * 
	 * @param outCsvFile
	 * @param append
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	public CsvWriter(File outCsvFile, boolean append) throws IOException {
		super();
		this.outCsvFile = outCsvFile;
		printer = new CSVPrinter( //
				new OutputStreamWriter(new FileOutputStream(outCsvFile, append), this.encoding), CSVFormat.EXCEL);
	}

	/**
	 * @param ss data of CSV
	 * @throws IOException
	 */
	public void write(List<String> ss) throws IOException {
		printer.printRecord(ss);
	}

	/**
	 * @param ss data of CSV
	 * @throws IOException
	 */
	public void write(Object... ss) throws IOException {
		printer.printRecord(ss);
	}

	@Override
	public void close() throws IOException {
		if (this.printer != null) {
			this.printer.flush();
			try {
				this.printer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
