package nlp4j.impl;

import java.util.ArrayList;
import java.util.List;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentAnnotatorPipeline;

public class DefaultDocumentAnnotatorPipeline extends AbstractDocumentAnnotator implements DocumentAnnotatorPipeline {

	List<DocumentAnnotator> annotators = new ArrayList<>();

	@Override
	public void annotate(Document doc) throws Exception {
		for (DocumentAnnotator ann : annotators) {
			ann.annotate(doc);
		}
	}

	@Override
	public void add(DocumentAnnotator annotator) {
		annotators.add(annotator);
	}

}
