package nlp4j.annotator;

import java.util.ArrayList;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.KeywordAnnotator;
import nlp4j.impl.DefaultKeyword;

/**
 * 「名詞の名詞」を「word_nn_no_nn」キーワードとして抽出します。
 * 
 * @author Hiroki Oya
 *
 */
public class Nokku34Annotator extends AbstractDocumentAnnotator implements DocumentAnnotator, KeywordAnnotator {

	@Override
	public void annotate(Document doc) throws Exception {

		ArrayList<Keyword> newkwds = new ArrayList<>();

		Keyword meishi_a = null;
		Keyword no = null;

		for (Keyword kwd : doc.getKeywords()) {
			if (meishi_a == null && kwd.getFacet().equals("名詞")) {
				meishi_a = kwd;
			} //
			else if (meishi_a != null && no == null && kwd.getLex().equals("の")) {
				no = kwd;
			} //
			else if (meishi_a != null && no != null && kwd.getFacet().equals("名詞")) {

				Keyword kw = new DefaultKeyword();
				kwd.setLex(meishi_a.getLex() + no.getLex() + kwd.getLex());
				kwd.setFacet("word_nn_no_nn");
				kwd.setBegin(meishi_a.getBegin());
				kwd.setEnd(kwd.getEnd());
				kwd.setStr(meishi_a.getStr() + no.getStr() + kwd.getStr());
				kwd.setReading(meishi_a.getReading() + no.getReading() + kwd.getReading());
				newkwds.add(kw);

				meishi_a = null;
				no = null;
			} //
			else {
				meishi_a = null;
				no = null;
			}

		}

		doc.addKeywords(newkwds);

	}

}
