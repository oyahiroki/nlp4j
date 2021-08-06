package nlp4j.cabocha;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultDocument;
import nlp4j.test.NLP4JTestCase;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-06
 */
public class CabochaAnnotatorTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target;

	@SuppressWarnings("javadoc")
	public CabochaAnnotatorTestCase() {
		target = CabochaAnnotator.class;
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

		int idx = 0;
		System.err.println("keyword size: " + doc.getKeywords().size());
		for (Keyword kwd : doc.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println("[Extracted Keyword] " + idx);
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
			idx++;
		}
	}

	public void testAnnotateDocument003() throws Exception {
		Document doc = new DefaultDocument();
		String texts = "私は急いで大学に歩いて行きました。\n車が道路を走る。\n風が強く吹く。\nhttps://www.xyz.com/test";
		doc.putAttribute("text", texts);

		CabochaAnnotator ann = new CabochaAnnotator();
		ann.setProperty("tempDir", "R:/");
		ann.setProperty("encoding", "MS932");
		ann.setProperty("target", "text");

		ann.annotate(doc);

		int idx = 0;
		System.err.println("keyword size: " + doc.getKeywords().size());
		for (Keyword kwd : doc.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println("[Extracted Keyword] " + idx);
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
			idx++;
		}
	}

	public void testAnnotateDocument004() throws Exception {
		Document doc = new DefaultDocument();
		String texts = "観葉植物が安い";
		doc.putAttribute("text", texts);

		CabochaAnnotator ann = new CabochaAnnotator();
		ann.setProperty("tempDir", "R:/");
		ann.setProperty("encoding", "MS932");
		ann.setProperty("target", "text");

		ann.annotate(doc);

		int idx = 0;
		System.err.println("keyword size: " + doc.getKeywords().size());
		for (Keyword kwd : doc.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println("[Extracted Keyword] " + idx);
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
			idx++;
		}
	}

}
