package nlp4j.annotator;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;

/**
 * 属性を置換する
 * 
 * @author Hiroki Oya
 *
 */
public class AttributeReplaceAllAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	String regex = null;
	String target = null;
	String replacement = "";

	/**
	 * URLを抽出する正規表現
	 */
	static public final String REGEX_URL = "https?://[a-zA-Z0-9\\.:/\\+\\-\\#\\?\\=\\&\\;\\%\\~]+";

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if (key.equals("regex")) {
			this.regex = value;
		} else if (key.equals("target")) {
			this.target = value;
		} else if (key.equals("replacement")) {
			this.replacement = value;
		}
	}

	@Override
	public void annotate(Document doc) throws Exception {

		if (this.target != null) {
			Object v = doc.getAttribute(this.target);
			if (v != null && replacement != null) {
				doc.putAttribute(this.target, v.toString().replaceAll(regex, replacement));
			}
		}

	}

}
