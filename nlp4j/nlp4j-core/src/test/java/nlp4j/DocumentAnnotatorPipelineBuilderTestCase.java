package nlp4j;

import junit.framework.TestCase;
import nlp4j.annotator.DebugAnnotator;

public class DocumentAnnotatorPipelineBuilderTestCase extends TestCase {

	/**
	 * created-on: 2024-11-10
	 * 
	 * @throws Exception
	 * @since 1.3.7.15
	 */
	public void testBuild00() throws Exception {

		Document doc = (new DocumentBuilder()).text("test").build();

		DebugAnnotator ann1 = new DebugAnnotator();
		DebugAnnotator ann2 = new DebugAnnotator();

		DocumentAnnotator anns = (new DocumentAnnotatorPipelineBuilder()).add(ann1).add(ann2).build();

		anns.annotate(doc);

	}

}
