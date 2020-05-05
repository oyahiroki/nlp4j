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

	/**
	 * 処理中AnnotatorのIndex
	 */
	private int annotatorIndexInProcess = 0;

	/**
	 * 処理中DocumentのIndex
	 */
	private int docIndexInProcess = 0;

	/**
	 * @return 処理中AnnotatorのIndex
	 * @since 1.3
	 */
	public int getAnnotatorIndexInProcess() {
		return annotatorIndexInProcess;
	}

	/**
	 * @return 処理中DocumentのIndex
	 * @since 1.3
	 */
	public int getDocIndexInProcess() {
		return docIndexInProcess;
	}

	@Override
	public void add(DocumentAnnotator annotator) {
		annotators.add(annotator);
	}

	@Override
	public void annotate(Document doc) throws Exception {
		for (int n = 0; n < annotators.size(); n++) {
			annotatorIndexInProcess = n;
			DocumentAnnotator ann = annotators.get(n);
			ann.annotate(doc);
			docIndexInProcess = 0;
		}
		annotatorIndexInProcess = 0;
	}

	@Override
	public void annotate(List<Document> docs) throws Exception {
		for (int n = 0; n < annotators.size(); n++) {
			annotatorIndexInProcess = n;
			DocumentAnnotator ann = annotators.get(n);
			for (int m = 0; m < docs.size(); m++) {
				docIndexInProcess = m;
				Document doc = docs.get(m);
				ann.annotate(doc);
			}
			docIndexInProcess = 0;
		}
		annotatorIndexInProcess = 0;
	}

	/**
	 * @return 登録されているアノテーターの数
	 * @since 1.3
	 */
	public int annotatorsSize() {
		if (annotators != null) {
			return annotators.size();
		} else {
			return 0;
		}
	}

}
