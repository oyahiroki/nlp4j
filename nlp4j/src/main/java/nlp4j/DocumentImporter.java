package nlp4j;

import java.util.List;

/**
 * ドキュメントインポーター。Apache SolrやAzure Searchのようなコンポーネントを想定。<br>
 * Document importer for document index like Apache Solr, Azure Search.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public interface DocumentImporter {

	/**
	 * プロパティをセットします。<br>
	 * Set Propery
	 * 
	 * @param key
	 * @param value
	 */
	void setProperty(String key, String value);

	/**
	 * ドキュメントをインポートします。<br>
	 * Import documents.
	 * 
	 * @param docs
	 * @throws Exception
	 */
	void importDocuments(List<Document> docs) throws Exception;

	/**
	 * ドキュメントをインポートします。<br>
	 * Import document.
	 * 
	 * @param doc
	 * @throws Exception
	 */
	void importDocument(Document doc) throws Exception;

	/**
	 * ドキュメントのインポートをコミットします。<br>
	 * Commit document import.
	 */
	void commit();

}
