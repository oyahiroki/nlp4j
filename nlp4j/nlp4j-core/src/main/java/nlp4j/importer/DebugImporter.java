package nlp4j.importer;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.DocumentImporter;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;

/**
 * Importer for Debugging
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class DebugImporter extends AbstractDocumentImporter implements DocumentImporter {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public DebugImporter() {
		super();
	}

	@Override
	public void importDocument(Document doc) throws IOException {
		logger.info("import ... " + doc.toString().replace("\n", "\\n"));
		System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc)));
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
