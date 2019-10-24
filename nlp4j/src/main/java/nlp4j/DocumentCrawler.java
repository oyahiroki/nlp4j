package nlp4j;

import java.util.List;

/**
 * ドキュメントクローラー。<br>
 * Document Crawler.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public interface DocumentCrawler {

	/**
	 * プロパティを設定します。<br>
	 * Set property.
	 * 
	 * @param key   プロパティのキー
	 * @param value プロパティの値
	 */
	void setProperty(String key, String value);

	/**
	 * ドキュメントをクロールします。<br>
	 * Crawl documents.
	 * 
	 * @return ドキュメント
	 */
	List<Document> crawl();

}
