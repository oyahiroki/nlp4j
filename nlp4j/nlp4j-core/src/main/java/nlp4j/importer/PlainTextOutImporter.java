package nlp4j.importer;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;

/**
 * Output Plain text file
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class PlainTextOutImporter extends AbstractDocumentImporter {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private File outFile = null;
	private String encoding = "UTF-8";
	private String target = "text";

	/**
	 * @param key   file | encoding
	 * @param value filepath | encoding name
	 */
	public void setProperty(String key, String value) {
		super.setProperty(key, value);

		if (key == null) {
			return;
		} else {
			// outfile
			if (key.equals("outfile")) {
				this.outFile = new File(value);
				logger.info("outfile: " + this.outFile.getAbsolutePath());
			} //
				// encoding
			else if (key.equals("encoding")) {
				this.encoding = value;
			} //
				// target
			else if (key.equals("target")) {
				this.target = value;
			}
		}
	}

	@Override
	public void importDocument(Document doc) throws IOException {

		String s = doc.getAttributeAsString(target);
		if (s == null) {
			return;
		} else {
			FileUtils.write(outFile, s + "\n", encoding, true);
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

}
