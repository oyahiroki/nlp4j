package nlp4j.stanford.examples;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.stanford.StanfordPosAnnotator;

public class StanfordPosAnnotatorExample {

	public static void main(String[] args) throws Exception {
		String text = "I eat sushi with chopsticks.";
		DocumentAnnotator ann = new StanfordPosAnnotator();
		ann.setProperty("target", "text");
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		ann.annotate(doc);
		for (Keyword kwd : doc.getKeywords()) {
			int begin = kwd.getBegin();
			int end = kwd.getEnd();
			String pos = kwd.getFacet();
			String txt = kwd.getStr();
			String lemma = kwd.getLex();
			System.err.println(String.format("%d,%d,%s,%s,%s", begin, end, pos, txt, lemma));
		}
	}
}
