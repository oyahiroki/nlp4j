package nlp4j.kuromoji.examples;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.krmj.annotator.KuromojiAnnotator;

public class KuromojiAnnotatorExample0 {
	public static void main(String[] args) throws Exception {
		Document doc = new DefaultDocument();
		doc.putAttribute("text", "私は学校に行きました。");
		KuromojiAnnotator annotator = new KuromojiAnnotator();
		annotator.setProperty("target", "text");
		annotator.annotate(doc); // throws Exception
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}
	}
}
