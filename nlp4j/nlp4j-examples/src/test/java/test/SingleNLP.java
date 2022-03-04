package test;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.mecab.MecabAnnotator;

public class SingleNLP {

	public static void main(String[] args) {

		long time1 = System.currentTimeMillis();

		for (int n = 0; n < 100; n++) {
			try {
				// 自然文のテキスト
				String text = "私は" + n + "回" + "学校に行きました。";
				Document doc = new DefaultDocument();
				doc.putAttribute("text", text);
				MecabAnnotator annotator = new MecabAnnotator();
				annotator.setProperty("target", "text");
				annotator.annotate(doc); // throws Exception
				System.err.println("Finished : annotation");

				for (Keyword kwd : doc.getKeywords()) {
//					System.err.println(kwd.getLex() + "," + kwd.getFacet() + "," + kwd.getUPos());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		long time2 = System.currentTimeMillis();

		System.err.println(time2 - time1);

	}

}
