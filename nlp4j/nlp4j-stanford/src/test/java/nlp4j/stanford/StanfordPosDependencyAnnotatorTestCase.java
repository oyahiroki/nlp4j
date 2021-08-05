package nlp4j.stanford;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultDocument;

public class StanfordPosDependencyAnnotatorTestCase extends TestCase {

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		System.err.println("--setup");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		System.err.println("--teardown");
	}

	public void test001() throws Exception {
		String text = "I eat sushi with chopsticks.";
		StanfordPosDependencyAnnotator ann = new StanfordPosDependencyAnnotator();
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		ann.setProperty("target", "text");
		ann.annotate(doc);
		for (Keyword kwd : doc.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				KeywordWithDependency kd = (KeywordWithDependency) kwd;
				System.err.println(kd.toStringAsXml());
			}
		}
	}

	public void test002() throws Exception {
		String text = "I believe that he will come.";
		StanfordPosDependencyAnnotator ann = new StanfordPosDependencyAnnotator();
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
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
