package nlp4j.mecab.examples;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.mecab.MecabAnnotator;

/**
 * created_at 2021-07-18
 * 
 * @author Hiroki Oya
 */
public class MecabAnnotatorExample0 {

	public static void main(String[] args) throws Exception {
		// 自然文のテキスト
		String text = "私は学校に行きました。";
		Document doc = new DefaultDocument();
		{
			doc.putAttribute("text", text);
		}

		MecabAnnotator annotator = new MecabAnnotator();
		{
			annotator.setProperty("target", "text");
		}
		annotator.annotate(doc); // throws Exception

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}

		annotator.close();

	}

}
