package nlp4j;

import java.util.List;

/**
 * ドキュメントインポーター。Apache SolrやAzure Searchのようなコンポーネントを想定。Document importer for
 * document index like Apache Solr, Azure Search.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public interface DocumentImporter {

	void setProperty(String key, String value);

	void importDocuments(List<Document> docs) throws Exception;

	void importDocument(Document doc) throws Exception;

	void commit();

}
