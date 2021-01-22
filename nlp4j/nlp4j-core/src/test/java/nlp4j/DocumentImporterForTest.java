package nlp4j;

import java.io.IOException;

public class DocumentImporterForTest extends AbstractDocumentImporter {

	@Override
	public void importDocument(Document doc) throws IOException {
		System.err.println("import ... " + doc);
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
