package nlp4j.importer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;

/**
 * Output CSV file
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class CsvOutImporter extends AbstractDocumentImporter {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	List<String> header = null;

	File outFile = null;

	CSVPrinter printer;

	String encoding = "UTF-8";

	/**
	 * @param key   file | encoding
	 * @param value filepath | encoding name
	 */
	public void setProperty(String key, String value) {
		super.setProperty(key, value);

		if (key != null && key.equals("file")) {
			this.outFile = new File(value);
		} else if (key != null && key.equals("encoding")) {
			this.encoding = value;
		}
	}

	@Override
	public void importDocument(Document doc) throws IOException {

		if (header == null) {
			header = doc.getAttributeKeys();
		}

		if (this.printer == null) {
			printer = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(outFile), this.encoding),
					CSVFormat.EXCEL.withHeader(this.header.toArray(new String[0])));
		}

		ArrayList<String> values = new ArrayList<>();
		for (String h : header) {
			values.add(doc.getAttributeAsString(h));
		}

		printer.printRecord(values);

	}

	@Override
	public void commit() throws IOException {
		logger.info("commit");
		if (this.printer != null) {
			this.printer.flush();
		}
	}

	@Override
	public void close() {
		logger.info("close");
		if (this.printer != null) {
			try {
				this.printer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
