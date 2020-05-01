/**
 * 
 */
package nlp4j.annotator;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;
import nlp4j.Keyword;

/**
 * 日本語を分かち書きにするアノテーター<br>
 * 事前にキーワードをセットしておくこと
 * 
 * @author Hiroki Oya
 * @since 1.2.1.0
 *
 */
public class WakachiAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	/**
	 * default Constructor. set default target as 'text_wakachi'
	 */
	public WakachiAnnotator() {
		super();
		setProperty("target", "text_wakachi");
	}

	@Override
	public void annotate(Document doc) throws Exception {

		String target = super.prop.getProperty("target");

		StringBuilder text2 = new StringBuilder();

		int idx = 0;

		for (Keyword kwd : doc.getKeywords()) {

			if (idx < kwd.getBegin()) {
				continue;
			} //
			else {
				idx = kwd.getEnd();
			}

			if (text2.length() > 0) {
				text2.append(" ");
			}

			text2.append(kwd.getLex());
		}

		doc.putAttribute(target, text2.toString());

	}

}
