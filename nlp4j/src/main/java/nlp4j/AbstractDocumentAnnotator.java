package nlp4j;

import java.util.List;

/**
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
