package nlp4j.annotator;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentBuilder;

/**
 * @author Hiroki Oya
 * @since 1.3.7.12
 */
public class DebugAnnotatorTestCase extends TestCase {

	/**
	 * @throws Exception
	 * @since 1.3.7.12
	 */
	public void testAnnotateListOfDocument001() throws Exception {

		Document doc = (new DocumentBuilder()).text("This is test").build();

		DebugAnnotator ann = new DebugAnnotator();
		ann.setProperty("wait", "1000");

		ann.annotate(doc);

	}

	public void testAnnotateListOfDocument002() throws Exception {

		List<Document> docs = new ArrayList<>();

		for (int n = 0; n < 10; n++) {
			Document doc = (new DocumentBuilder()).text("This is test " + n).build();
			docs.add(doc);
		}

		DebugAnnotator ann = new DebugAnnotator();
		ann.setProperty("wait", "1000");

		ann.annotate(docs);

	}

}
