package nlp4j.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;

/**
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class AttributeFilterAnnotatorTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target = AttributeFilterAnnotator.class;

	public void testSetProperty() {
		{
			AttributeFilterAnnotator ann = new AttributeFilterAnnotator();
			ann.setProperty("fields", "aaa,ccc");
		}
		{
			AttributeFilterAnnotator ann = new AttributeFilterAnnotator();
			ann.setProperty("fields", "");
		}
	}

	public void testAnnotateDocument() throws Exception {

		AttributeFilterAnnotator ann = new AttributeFilterAnnotator();
		ann.setProperty("fields", "aaa,ccc");

		Document doc = new DefaultDocument();
		doc.putAttribute("aaa", "111");
		doc.putAttribute("bbb", "222");
		doc.putAttribute("ccc", "333");

		ann.annotate(doc);

		assertEquals(doc.getAttribute("aaa"), "111");
		assertNull(doc.getAttribute("bbb"));
		assertEquals(doc.getAttribute("ccc"), "333");

	}

}
