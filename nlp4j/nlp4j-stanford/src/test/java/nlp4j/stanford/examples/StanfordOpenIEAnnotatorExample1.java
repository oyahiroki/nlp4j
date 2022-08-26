package nlp4j.stanford.examples;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.stanford.StanfordOpenIEAnnotator;

public class StanfordOpenIEAnnotatorExample1 {

	public static void main(String[] args) throws Exception {

		StanfordOpenIEAnnotator ann = new StanfordOpenIEAnnotator();

		Document doc = new DefaultDocument();
		doc.putAttribute("text", //
				"Mount Fuji, located on the island of Honshu, " //
						+ "is the highest mountain in Japan. ");

		ann.setProperty("target", "text");

		ann.annotate(doc);

		doc.getKeywords().forEach(kwd -> System.err.println(kwd.getLex()));

	}

}
