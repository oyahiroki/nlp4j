package nlp4j.stanford;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultDocument;

public class StanfordPosDependencyAnnotatorSample2 {

	public static void main(String[] args) throws Exception {

		Document doc = new DefaultDocument();
		doc.putAttribute("text", "We are trying to understand the difference.");

		StanfordPosDependencyAnnotator ann = new StanfordPosDependencyAnnotator();
		ann.setProperty("target", "text");

		ann.annotate(doc);

		for (Keyword kwd : doc.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				KeywordWithDependency kd = (KeywordWithDependency) kwd;
				System.err.println(kd.toStringAsXml());
			}
		}

	}

}
