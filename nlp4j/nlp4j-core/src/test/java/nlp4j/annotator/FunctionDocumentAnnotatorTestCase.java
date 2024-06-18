package nlp4j.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;

public class FunctionDocumentAnnotatorTestCase extends TestCase {

	/**
	 * @throws Exception
	 * @since 1.3.7.13
	 */
	public void testAnnotateDocument001() throws Exception {
		Document doc = new DefaultDocument();
		doc.putAttribute("item1", "TEST");

		System.err.println(doc);

		FunctionDocumentAnnotator ann = new FunctionDocumentAnnotator();
		ann.put("item1", value -> {
			return ((String) value).toLowerCase();
		});

		ann.annotate(doc);

		System.err.println(doc);

	}

}
