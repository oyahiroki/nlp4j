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
import nlp4j.util.DocumentUtil;

/**
 * Output CSV file
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class JsonOutImporter extends AbstractDocumentImporter {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	List<String> header = null;

	File outFile = null;

	final String encoding = "UTF-8";

	/**
	 * @param key   file | encoding
	 * @param value filepath | encoding name
	 */
	public void setProperty(String key, String value) {
		super.setProperty(key, value);

		if (key != null && key.equals("file")) {
			this.outFile = new File(value);
		}
	}

	@Override
	public void commit() throws IOException {
		logger.info("commit");
	}

	@Override
	public void close() {
		logger.info("close");
	}

	@Override
	public void importDocument(Document doc) throws IOException {
		DocumentUtil.writeAsLineSeparatedJson(doc, outFile);
	}

}
