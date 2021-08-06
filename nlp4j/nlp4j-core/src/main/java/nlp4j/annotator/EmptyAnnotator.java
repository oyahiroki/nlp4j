package nlp4j.annotator;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;

/**
 * @author Hiroki Oya
 * @since 1.3.2.1
 *
 */
public class EmptyAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	@Override
	public void annotate(Document doc) throws Exception {
	}

}
