package nlp4j.annotator;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;

/**
 * 属性を削除する<br>
 * プロパティ<br>
 * target 処理対象<br>
 * 
 * @author Hiroki Oya
 * @since 1.3
 */
public class AttributeRemoveAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	String target = null;

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if (key.equals("target")) {
			this.target = value;
		}
	}

	@Override
	public void annotate(Document doc) throws Exception {

		if (this.target != null) {
			doc.remove(this.target);
		}

	}

}
