package nlp4j.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;

/**
 * @author Hiroki Oya
 * @since 1.3
 */
public class ParseIntAnnotatorTestCase extends TestCase {

	/**
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument001() throws Exception {

		Document doc = new DefaultDocument();
		doc.putAttribute("test", "10,000 km");

		ParseIntAnnotator ann = new ParseIntAnnotator();
		ann.setProperty("target", "test");
		ann.annotate(doc);

		System.err.println(doc);

	}

}
