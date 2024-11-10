package nlp4j;

import junit.framework.TestCase;
import nlp4j.annotator.DebugAnnotator;
import nlp4j.util.DocumentUtil;

public class DocumentAnnotatorBuilderTestCase extends TestCase {

	public void testBuild001() throws Exception {
		Document doc = (new DocumentBuilder()).text("これはテストです").build();
		DocumentAnnotator ann = (new DocumentAnnotatorBuilder<>(DebugAnnotator.class)).set("target", "text").build();
		ann.annotate(doc);
		doc.getKeywords().forEach(kwd -> {
			System.out.println(kwd.getBegin() + "," + kwd.getEnd() + "," + kwd.getFacet() + "," + kwd.getLex());
		});
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
	}

}
