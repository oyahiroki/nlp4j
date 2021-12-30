package nlp4j.twtr.anntator;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;

/**
 * 
 * created_at 2021-08-01
 * 
 * @author Hiroki Oya
 * @since 1.1.0.0
 */
public class TwitterSourceAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	@Override
	public void annotate(Document doc) throws Exception {

		if (doc.getAttribute("source") != null) {
			String s1 = doc.getAttributeAsString("source");
			int idx1 = s1.indexOf(">");
			if (idx1 != -1) {
				int idx2 = s1.indexOf("<", idx1);
				if (idx2 != -1) {
					String s2 = s1.substring(idx1 + 1, idx2);
					doc.putAttribute("source", s2);
				}
			}
		}

	}

}
