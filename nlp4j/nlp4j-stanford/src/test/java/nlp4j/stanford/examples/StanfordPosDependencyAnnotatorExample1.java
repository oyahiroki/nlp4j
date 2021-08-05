package nlp4j.stanford.examples;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultDocument;
import nlp4j.stanford.StanfordPosDependencyAnnotator;

@SuppressWarnings("javadoc")
public class StanfordPosDependencyAnnotatorExample1 {

	public static void main(String[] args) throws Exception {

		StanfordPosDependencyAnnotator ann = new StanfordPosDependencyAnnotator();

		Document doc = new DefaultDocument();
		doc.putAttribute("text", "I eat sushi with chopsticks. I eat sushi with tuna.");

		ann.setProperty("target", "text");

		ann.annotate(doc);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
			if (kwd instanceof KeywordWithDependency) {
				KeywordWithDependency kd = (KeywordWithDependency) kwd;
				System.err.println(kd.toStringAsXml());
			}
		}

	}

}
