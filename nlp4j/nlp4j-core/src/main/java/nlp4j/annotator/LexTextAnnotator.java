package nlp4j.annotator;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;
import nlp4j.Keyword;

/**
 * 属性をコピーする<br>
 * プロパティ<br>
 * from コピー元<br>
 * to コピー先<br>
 * 
 * @author Hiroki Oya
 *
 */
public class LexTextAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
	}

	@Override
	public void annotate(Document doc) throws Exception {

		StringBuilder lexText = new StringBuilder();

		for (Keyword kwd : doc.getKeywords()) {
			if (kwd.getLex().equals("*") == false && kwd.getLex().equals(kwd.getStr()) == false) {
				if (lexText.length() > 0) {
					lexText.append(" ");
				}
				lexText.append(kwd.getLex());
			}
		}
		for (String target : super.targets) {
			doc.putAttribute(target, lexText.toString());
		}

	}

}
