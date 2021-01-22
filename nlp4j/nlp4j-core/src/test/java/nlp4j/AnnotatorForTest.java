package nlp4j;

public class AnnotatorForTest extends AbstractDocumentAnnotator {

	int n = 0;

	@Override
	public void annotate(Document doc) throws Exception {

		doc.putAttribute("test", "hello");
		doc.putAttribute("test_num", n);
		n++;

	}

}
