package nlp4j.annotator;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;

public class TimeWaitAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	@Override
	public void annotate(Document doc) throws Exception {
		Thread.sleep(1000);
		doc.putAttribute("x", "1");
	}

}
