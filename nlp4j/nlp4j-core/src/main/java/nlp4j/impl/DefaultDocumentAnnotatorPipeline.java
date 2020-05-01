package nlp4j.impl;

import java.util.ArrayList;
import java.util.List;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentAnnotatorPipeline;

/**
 * ドキュメントに対してアノテーター処理を連続的に処理するパイプラインのクラスです。<br>
 * Document Annotator pipeline class.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public class DefaultDocumentAnnotatorPipeline extends AbstractDocumentAnnotator implements DocumentAnnotatorPipeline {

	List<DocumentAnnotator> annotators = new ArrayList<>();

	@Override
	public void annotate(Document doc) throws Exception {
		for (DocumentAnnotator ann : annotators) {
			ann.annotate(doc);
		}
	}

	@Override
	public void annotate(List<Document> docs) throws Exception {
		for (DocumentAnnotator ann : annotators) {
			for (Document doc : docs) {
				ann.annotate(doc);
			}
		}
	}

	@Override
	public void add(DocumentAnnotator annotator) {
		annotators.add(annotator);
	}

}
