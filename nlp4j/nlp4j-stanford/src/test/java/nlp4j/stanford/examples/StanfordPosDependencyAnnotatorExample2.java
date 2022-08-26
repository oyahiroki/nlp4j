package nlp4j.stanford.examples;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultDocument;
import nlp4j.stanford.StanfordPosDependencyAnnotator;

public class StanfordPosDependencyAnnotatorExample2 {

	public static void main(String[] args) throws Exception {

		Document doc = new DefaultDocument();
		doc.putAttribute("text", "I eat sushi with chopsticks.");

		StanfordPosDependencyAnnotator ann = new StanfordPosDependencyAnnotator();
		ann.setProperty("target", "text");

		ann.annotate(doc);

		for (Keyword kwd : doc.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				KeywordWithDependency kd = (KeywordWithDependency) kwd;
				// Print dependency as a XML
				System.err.println(kd.toStringAsXml());
				print(kd);
			}
		}

	}

	private static void print(KeywordWithDependency kd) {
		kd.getChildren().forEach(kwd -> {
			System.err.println(kd.getLex() + " -> (" + kwd.getRelation() + ") " + kwd.getLex());
			print(kwd);
		});

	}

}
