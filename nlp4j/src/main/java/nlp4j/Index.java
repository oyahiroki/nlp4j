package nlp4j;

import java.util.List;

/**
 * ドキュメントのインデックスです。
 * 
 * @author Hiroki Oya
 * @version 1.0
 */
public interface Index {

	/**
	 * インデックスにドキュメントを追加します。
	 * 
	 * @param doc ドキュメント
	 * @since 1.0
	 */
	void addDocument(Document doc);

	/**
	 * インデックスに複数のドキュメントを追加します。
	 * 
	 * @param docs 複数のドキュメント
	 * @since 1.0
	 */
	void addDocuments(List<Document> docs);

	/**
	 * インデックスのキーワードを返します。
	 * 
	 * @return キーワード
	 * @since 1.0
	 */
	List<Keyword> getKeywords();

	/**
	 * ファセットを指定してインデックスのキーワードを返します。
	 * 
	 * @param facet ファセット
	 * @return キーワード
	 * @since 1.0
	 */
	List<Keyword> getKeywords(String facet);

	/**
	 * ファセットと条件を指定してインデックスのキーワードを返します。
	 * 
	 * @param facet     ファセットID
	 * @param condition 条件
	 * @return キーワード
	 * @since 1.0
	 */
	List<Keyword> getKeywords(String facet, String condition);

}