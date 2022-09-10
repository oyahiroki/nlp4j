package nlp4j.annotator;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;

/**
 * Normailze document value with Unicode NFKC
 * 
 * @author Hiroki Oya
 *
 */
public class ValueNormalizeAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	public ValueNormalizeAnnotator() {
	}

	@Override
	public void annotate(Document doc) throws Exception {
		// FOR_EACH(KEY)
		for (String key : doc.getAttributeKeys()) {
			Object o = doc.getAttribute(key);
			// STRING
			if (o instanceof String) {
				doc.putAttribute(key, nfkc((String) o));
			} //
				// LIST
			else if (o instanceof List) {
				@SuppressWarnings("rawtypes")
				List list = (List) o;
				List<Object> list2 = new ArrayList<>();
				for (Object oo : list) {
					if (oo instanceof String) {
						list2.add(nfkc((String) oo));
					} else {
						list2.add(oo);
					}
				}
				doc.putAttribute(key, list2);
			} //
				// STRING ARRAY
			else if (o instanceof String[]) {
				String[] ss = (String[]) o;
				for (int n = 0; n < ss.length; n++) {
					ss[n] = nfkc(ss[n]);
				}
				doc.putAttribute(key, ss);
			} //
				// ELSE
			else {
				// do nothing
			}
		} // END OF FOR_EACH(KEY)
	}

	private String nfkc(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFKC);
	}

}
