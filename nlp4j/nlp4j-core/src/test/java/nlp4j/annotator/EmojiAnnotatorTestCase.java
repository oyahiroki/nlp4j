package nlp4j.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.DocumentUtil;

public class EmojiAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument001() throws Exception {
		String text = "😊";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);

		EmojiAnnotator ann = new EmojiAnnotator();
		ann.setProperty("target", "text");
		ann.annotate(doc);

		System.err.println(DocumentUtil.toJsonPrettyString(doc));
	}

	public void testAnnotateDocument002() throws Exception {
		String text = "This is test😊.";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);

		EmojiAnnotator ann = new EmojiAnnotator();
		ann.setProperty("target", "text");
		ann.annotate(doc);

		System.err.println(DocumentUtil.toJsonPrettyString(doc));
	}

}
