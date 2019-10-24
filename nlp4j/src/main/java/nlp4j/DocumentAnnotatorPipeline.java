package nlp4j;

/**
 * ドキュメントアノテーターのパイプライン処理。<br>
 * Pipeline Process of Document Annotation.
 * 
 * @author Hiroki Oya
 * @since 1.0
 */
public interface DocumentAnnotatorPipeline extends DocumentAnnotator {

	/**
	 * アノテーターを追加します。<br>
	 * Add an annotator.
	 * 
	 * @param annotator アノテーター
	 * @throws Exception 例外発生時
	 */
	void add(DocumentAnnotator annotator) throws Exception;

}
