package nlp4j;

import java.io.IOException;
import java.util.List;

/**
 * ドキュメントにメタ情報を付加するアノテーターです。
 * 
 * @author Hiroki Oya
 * @version 1.0
 * @since 1.0
 *
 */
public interface Annotator {

	/**
	 * ドキュメントにアノテーションを付加します。
	 * 
	 * @param doc ドキュメント
	 * @throws IOException IO例外
	 * @since 1.0
	 */
	public void annotate(Document doc) throws IOException;

	/**
	 * 複数のドキュメントにアノテーションを付加します。
	 * 
	 * @param docs ドキュメント
	 * @throws IOException IO例外
	 * @since 1.0
	 */
	public void annotate(List<Document> docs) throws IOException;

}
