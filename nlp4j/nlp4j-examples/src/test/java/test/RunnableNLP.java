package test;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.mecab.MecabAnnotator;

public class RunnableNLP implements Runnable {

	@Override
	public void run() {

		try {
			// 自然文のテキスト
			String text = "私は学校に行きました。";
			Document doc = new DefaultDocument();
			doc.putAttribute("text", text);
			MecabAnnotator annotator = new MecabAnnotator();
			annotator.setProperty("target", "text");
			annotator.annotate(doc); // throws Exception
			System.err.println("Finished : annotation");

			for (Keyword kwd : doc.getKeywords()) {
				System.err.println(kwd.getLex() + "," + kwd.getFacet() + "," + kwd.getUPos());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
