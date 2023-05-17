package nlp4j.annotator.concurrent;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;

public class AnnotationResult {

	private DocumentAnnotator annotator;
	private Document document;
	private int annotatorIndex;
	public DocumentAnnotator getAnnotator() {
		return annotator;
	}
	public void setAnnotator(DocumentAnnotator annotator) {
		this.annotator = annotator;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public int getAnnotatorIndex() {
		return annotatorIndex;
	}
	public void setAnnotatorIndex(int annotatorIndex) {
		this.annotatorIndex = annotatorIndex;
	}

}
