package nlp4j.pattern.examples;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.cabocha.CabochaAnnotator;
import nlp4j.impl.DefaultDocument;
import nlp4j.pattern.UserPatternAnnotator;
import nlp4j.stanford.StanfordPosDependencyAnnotator;

@SuppressWarnings("javadoc")
public class UserPatternAnnotatorEnExample1 {

	@SuppressWarnings("javadoc")
	public static void main(String[] args) throws Exception {

		String text = "I eat sushi with chopsticks.";

		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);

		{
			StanfordPosDependencyAnnotator ann = new StanfordPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}

		{ // 係り受けパターン抽出
			String dir = "src/test/resources/nlp4j.pattern.examples/";
			UserPatternAnnotator ann = new UserPatternAnnotator();
			ann.setProperty("file", dir + "pattern-en-5syntax_sv.xml");
//			ann.setProperty("file", dir + "pattern-en-5syntax_svo.xml");
			ann.annotate(doc);
		}

		for (Keyword kwd : doc.getKeywords()) {
			// 係り受け
			if (kwd instanceof KeywordWithDependency) {
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			} else {
//				System.err.println(kwd.toString());
			}
		}

		// Expected Result
		for (Keyword kwd : doc.getKeywords("pattern")) {
			System.err.println(kwd.getFacet() + " : " + kwd.getLex());
		}

	}
}
