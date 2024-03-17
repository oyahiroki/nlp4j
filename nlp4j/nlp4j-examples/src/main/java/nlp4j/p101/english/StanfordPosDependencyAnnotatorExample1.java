package nlp4j.p101.english;

import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.pattern.UserPatternAnnotator;
import nlp4j.stanford.StanfordPosDependencyAnnotator;

@SuppressWarnings("javadoc")
public class StanfordPosDependencyAnnotatorExample1 {

	public static void main(String[] args) throws Exception {

		String text = "I eat sushi with chopsticks.";

		// Document
//		Document doc = new DefaultDocument();
//		doc.putAttribute("text", text);

		Document doc = new DocumentBuilder().text(text).create();

		System.err.println("Document: " + doc.getAttributeAsString("text"));

		{ // Annotation 1: nlp4j.stanford.StanfordPosDependencyAnnotator
			StanfordPosDependencyAnnotator ann = new StanfordPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}

		{ // Annotation 2: nlp4j.pattern.UserPatternAnnotator
			UserPatternAnnotator ann = new UserPatternAnnotator();
			ann.setProperty("file", "src/test/resources/nlp4j.pattern.examples/" //
					+ "pattern-en-5syntax_svo_simple.xml");
			ann.annotate(doc);
		}

		System.err.println("Extracted Keyword in XML:");
		for (Keyword kwd : doc.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
		}

		// Expected Result
		System.err.println("Extracted Pattern:");
		for (Keyword kwd : doc.getKeywords("pattern")) {
			System.err.println(kwd.getFacet() + " : " + kwd.getLex());
		}

	}
}
