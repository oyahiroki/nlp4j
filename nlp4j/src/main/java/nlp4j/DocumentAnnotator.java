package nlp4j;

import java.io.IOException;
import java.util.List;

/**
 * ドキュメントにメタ情報を付加するアノテーターです。<br/>
 * Document Annotator.
 * 
 * @author Hiroki Oya
 * @version 1.0
 * @since 1.0
 *
 */
public interface DocumentAnnotator {

	/**
	 * ドキュメントにアノテーションを付加します。<br/>
	 * Add an annotation to a document.
	 * 
	 * @param doc ドキュメント
	 * @throws IOException IO例外
	 * @since 1.0
	 */
	public void annotate(Document doc) throws Exception;

	/**
	 * 複数のドキュメントにアノテーションを付加します。<br/>
	 * Add an annotation to documents.
	 * 
	 * @param docs ドキュメント
	 * @throws IOException IO例外
	 * @since 1.0
	 */
	public void annotate(List<Document> docs) throws Exception;

}