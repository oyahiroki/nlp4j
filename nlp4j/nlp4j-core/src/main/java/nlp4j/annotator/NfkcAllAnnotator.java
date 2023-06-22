package nlp4j.annotator;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;
import nlp4j.util.Normalizer;

/**
 * @author Hiroki Oya
 * @since 1.3.7.9
 *
 */
public class NfkcAllAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	@Override
	public void annotate(Document doc) throws Exception {

		for (String key : doc.getAttributeKeys()) {

			Object o = doc.getAttribute(key);

			if (o instanceof String) {
				String s = doc.getAttributeAsString(key);
				if (s != null && s.isEmpty() == false) {
					s = Normalizer.nfkc(s);
				}
				doc.putAttribute(key, s);
			}

		}

	}

}
