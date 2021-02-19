package nlp4j.stanford;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.annotator.AttributeTypeConverter;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.DocumentUtil;

public class StanfordPosAnnotatorTestCase extends TestCase {

	Class target = StanfordPosAnnotator.class;

	public void testAnnotateDocument() throws Exception {

		StanfordPosAnnotator ann = new StanfordPosAnnotator();
		ann.setProperty("target", "text");

		Document doc = new DefaultDocument();
		doc.putAttribute("text", "This is test.");

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		ann.annotate(doc);

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

	}

}
