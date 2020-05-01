package nlp4j.annotator;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.KeywordAnnotator;

/**
 * COTOHA API が正規形にアスタリスクを返すのを修正する
 * 
 * @author Hiroki Oya
 * @since 1.3
 */
public class KeywordFixAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, KeywordAnnotator {

	@Override
	public void annotate(Document doc) throws Exception {
		for (Keyword kwd : doc.getKeywords()) {
			if (kwd.getLex().equals("*")) {
				kwd.setLex(kwd.getStr());
			}
			if (kwd.getLex().equals("(")) {
				kwd.setFacet("記号");
			}
			if (kwd.getLex().equals(")")) {
				kwd.setFacet("記号");
			}
			if (kwd.getLex().equals("~")) {
				kwd.setFacet("記号");
			}

		}
	}

}
