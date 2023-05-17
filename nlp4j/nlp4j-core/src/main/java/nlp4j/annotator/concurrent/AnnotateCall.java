package nlp4j.annotator.concurrent;

import java.util.concurrent.Callable;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;

public class AnnotateCall implements Callable<AnnotationResult> {

	private DocumentAnnotator ann;
	private Document doc;
	private int annotatorIndex = -1;

	public AnnotateCall(DocumentAnnotator ann, Document doc, int annotatorIndex) {
		super();
		this.ann = ann;
		this.doc = doc;
		this.annotatorIndex = annotatorIndex;
	}

	@Override
	public AnnotationResult call() throws Exception {

		try {
			if (this.ann != null && doc != null) {
				ann.annotate(doc); // throws Exception
			}
		} catch (Exception e) {
			AnnotationException ae = new AnnotationException();
			ae.initCause(e);
			ae.setAnnotatorIndex(annotatorIndex);
			throw ae;
		}

		AnnotationResult result = new AnnotationResult();
		result.setAnnotator(this.ann);
		result.setDocument(this.doc);
		result.setAnnotatorIndex(this.annotatorIndex);

		return result;
	}

}
