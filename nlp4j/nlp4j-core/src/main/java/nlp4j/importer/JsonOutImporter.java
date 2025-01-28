package nlp4j.importer;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;

/**
 * Output JSON (JSON LINE) file
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class JsonOutImporter extends AbstractDocumentImporter {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	List<String> header = null;

	File outFile = null;

	/**
	 * <pre>
	 * Example:
	 *  key=file
	 *  file=/test_json.txt
	 * </pre>
	 * 
	 * @param key   file
	 * @param value filepath
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
		if (outFile != null) { // since 1.3.0.2
			DocumentUtil.writeAsLineSeparatedJson(doc, outFile);
		} //
		else { // since 1.3.0.2
			logger.warn("file parameter is not set.");
			System.err.println(DocumentUtil.toJsonPrettyString(doc));
		}
	}

}
