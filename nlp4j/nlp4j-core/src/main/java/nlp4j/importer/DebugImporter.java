package nlp4j.importer;

import java.io.IOException;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;

/**
 * Importer for Debugging
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class DebugImporter extends AbstractDocumentImporter {

	/**
	 * 
	 */
	public DebugImporter() {
		super();
	}

	@Override
	public void importDocument(Document doc) throws IOException {
		System.err.println("import ... " + doc.toString().replace("\n", "\\n"));
		System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc)));
	}

	@Override
	public void commit() throws IOException {
		System.err.println("commit");
	}

	@Override
	public void close() {
		System.err.println("close");
	}

}
