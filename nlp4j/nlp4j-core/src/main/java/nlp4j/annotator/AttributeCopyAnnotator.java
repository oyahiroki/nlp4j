package nlp4j.annotator;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;

/**
 * 属性をコピーする
 * 
 * @author Hiroki Oya
 *
 */
public class AttributeCopyAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	String from = null;
	String to = null;

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);

		if (key.equals("from")) {
			this.from = value;
		} else if (key.equals("to")) {
			this.to = value;
		}
	}

	@Override
	public void annotate(Document doc) throws Exception {

		if (this.from != null) {
			Object v = doc.getAttribute(this.from);
			if (v != null && this.to != null) {
				doc.putAttribute(this.to, v);
			}
		}

	}

}
