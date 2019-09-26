package nlp4j;

import java.util.List;

public abstract class AbstractDocumentImporter implements DocumentImporter {

	@Override
	public void importDocuments(List<Document> docs) throws Exception {
		for (Document doc : docs) {
			importDocument(doc);
		}

	}

}
