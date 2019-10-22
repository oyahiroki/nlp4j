package nlp4j;

import java.util.List;

/**
 * ドキュメントのインデックスです。<br/>
 * Index of Documents.
 * 
 * @author Hiroki Oya
 * @version 1.0
 */
public interface Index {

	/**
	 * インデックスにドキュメントを追加します。<br/>
	 * Add an document to Index.
	 * 
	 * @param doc ドキュメント
	 * @since 1.0
	 */
	void addDocument(Document doc);

	/**
	 * インデックスに複数のドキュメントを追加します。<br/>
	 * Add documents to Index.
	 * 
	 * @param docs 複数のドキュメント
	 * @since 1.0
	 */
	void addDocuments(List<Document> docs);

	/**
	 * インデックスのキーワードを返します。<br/>
	 * Return keywords of index.
	 * 
	 * @return キーワード
	 * @since 1.0
	 */
	List<Keyword> getKeywords();

	/**
	 * ファセットを指定してインデックスのキーワードを返します。<br/>
	 * Return facet keywords of index.
	 * 
	 * @param facet ファセット
	 * @return キーワード
	 * @since 1.0
	 */
	List<Keyword> getKeywords(String facet);

	/**
	 * ファセットと条件を指定してインデックスのキーワードを返します。<br/>
	 * Return facet keywords with a condition query.
	 * 
	 * @param facet     ファセットID
	 * @param condition 条件
	 * @return キーワード
	 * @since 1.0
	 */
	List<Keyword> getKeywords(String facet, String condition);

}