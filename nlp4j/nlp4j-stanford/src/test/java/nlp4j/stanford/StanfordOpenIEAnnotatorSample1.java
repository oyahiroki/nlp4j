package nlp4j.stanford;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;

public class StanfordOpenIEAnnotatorSample1 {

	public static void main(String[] args) throws Exception {

		StanfordOpenIEAnnotator ann = new StanfordOpenIEAnnotator();

		Document doc = new DefaultDocument();
		doc.putAttribute("text", //
				"Mount Fuji, located on the island of Honshu, " //
						+ "is the highest mountain in Japan, standing 3,776.24 m. ");

		ann.setProperty("target", "text");

		ann.annotate(doc);

		System.err.println("pattern.oie.triple");

		for (Keyword kwd : doc.getKeywords("pattern.oie.triple")) {
			System.err.println(kwd.getFacet() + "," + kwd.getLex());
		}

	}

}
