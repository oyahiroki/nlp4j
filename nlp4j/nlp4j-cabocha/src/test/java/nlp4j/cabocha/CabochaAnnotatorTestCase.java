package nlp4j.cabocha;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultDocument;
import nlp4j.test.NLP4JTestCase;

public class CabochaAnnotatorTestCase extends NLP4JTestCase {

	public CabochaAnnotatorTestCase() {
		super.target = CabochaAnnotator.class;
	}

	public void testAnnotateDocument001() throws Exception {
		Document doc = new DefaultDocument();
		String texts = "私は大学に歩いて行きました。";
		doc.putAttribute("text", texts);

		CabochaAnnotator ann = new CabochaAnnotator();
		ann.setProperty("tempDir", "R:/");
		ann.setProperty("encoding", "MS932");
		ann.setProperty("target", "text");

		ann.annotate(doc);

		for (Keyword kwd : doc.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
		}
	}

	public void testAnnotateDocument002() throws Exception {
		Document doc = new DefaultDocument();
		String texts = "私は急いで大学に歩いて行きました。車が道路を走る。風が強く吹く。";
		doc.putAttribute("text", texts);

		CabochaAnnotator ann = new CabochaAnnotator();
		ann.setProperty("tempDir", "R:/");
		ann.setProperty("encoding", "MS932");
		ann.setProperty("target", "text");

		ann.annotate(doc);

		for (Keyword kwd : doc.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println("[Extracted Keyword]");
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
		}
	}

}
