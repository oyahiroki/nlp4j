package nlp4j.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.impl.DefaultDocument;

/**
 * @author Hiroki Oya
 * @since 1.3.7.12
 */
public class AnnotatorBuilderTestCase extends TestCase {

	/**
	 * @throws Exception
	 * @since 1.3.7.12
	 */
	public void test001() throws Exception {

		DocumentAnnotator ann = (new AnnotatorBuilder(DebugAnnotator.class)).p("xxx", "yyy").build();

		System.err.println(ann);
		{
			Document doc = new DefaultDocument();
			doc.putAttribute("text", "This is test.");

			ann.annotate(doc);
		}
		{
			Document doc = (new DocumentBuilder()).text("This is test.").build();
			ann.annotate(doc);
		}

	}

}
