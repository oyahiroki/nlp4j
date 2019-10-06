package nlp4j;

import java.util.List;

/**
 * ドキュメントインポーターの抽象クラス。Abstract Document Imporeter
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public abstract class AbstractDocumentImporter implements DocumentImporter {

	@Override
	public void importDocuments(List<Document> docs) throws Exception {
		for (Document doc : docs) {
			importDocument(doc);
		}

	}

}
