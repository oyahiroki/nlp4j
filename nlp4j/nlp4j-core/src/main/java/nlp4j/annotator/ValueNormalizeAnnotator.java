/**
 * 
 */
package nlp4j.annotator;

import java.text.Normalizer;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;

/**
 * Normailze document value with Unicode NFKC
 * 
 * @author Hiroki Oya
 *
 */
public class ValueNormalizeAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	/**
	 * 
	 */
	public ValueNormalizeAnnotator() {
	}

	@Override
	public void annotate(Document doc) throws Exception {
		for (String key : doc.getAttributeKeys()) {
			String value = doc.getAttribute(key).toString();
			value = Normalizer.normalize(value, Normalizer.Form.NFKC);
			doc.putAttribute(key, value);
		}
	}

}
