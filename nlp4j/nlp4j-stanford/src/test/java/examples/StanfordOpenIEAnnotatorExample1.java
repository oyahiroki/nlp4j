package examples;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.stanford.StanfordOpenIEAnnotator;

public class StanfordOpenIEAnnotatorExample1 {

	public static void main(String[] args) throws Exception {

		StanfordOpenIEAnnotator ann = new StanfordOpenIEAnnotator();
		ann.setProperty("target", "text");

		Document doc = new DefaultDocument();
		doc.putAttribute("text", //
				"Mount Fuji, located on the island of Honshu, " //
						+ "is the highest mountain in Japan. ");

		ann.annotate(doc);
		doc.getKeywords().forEach(kwd -> System.out.println(kwd.getFacet() + "," + kwd.getLex()));

	}

}

// Expected Output
// pattern.oie.triple,mount fuji , is highest mountain in , japan
// pattern.oie.triple,mount fuji , is mountain in , japan
// pattern.oie.triple,mount fuji , is , mountain
// pattern.oie.triple,mount fuji , is , highest mountain
// pattern.oie.triple,mount fuji , located on , island honshu
// pattern.oie.triple,highest mountain , is in , japan
// pattern.oie.triple,mount fuji , located on , island
// pattern.oie.clause,Mount Fuji located on the island of Honshu is the highest mountain in Japan
// pattern.oie.clause,Mount Fuji located on the island of Honshu

