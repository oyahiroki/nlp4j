package nlp4j;

import java.util.List;

/**
 * ドキュメントアノテーターの抽象クラス。 <br>
 * Abstract Class of Document Annotator.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public abstract class AbstractDocumentAnnotator implements DocumentAnnotator {

	@Override
	public void annotate(List<Document> docs) throws Exception {
		for (Document doc : docs) {
			annotate(doc);
		}

	}

}
