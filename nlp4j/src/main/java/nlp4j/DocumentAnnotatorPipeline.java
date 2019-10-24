package nlp4j;

/**
 * ドキュメントアノテーターのパイプライン処理。<br>
 * Pipeline Process of Document Annotation.
 * 
 * @author Hiroki Oya
 * @since 1.0
 */
public interface DocumentAnnotatorPipeline extends DocumentAnnotator {

	void add(DocumentAnnotator annotator) throws Exception;

}
