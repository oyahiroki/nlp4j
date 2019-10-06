package nlp4j;

import java.util.List;

/**
 * ドキュメントクローラー。Document Crawler.
 * 
 * @since 1.0
 * @author Hiroki Oya
 *
 */
public interface DocumentCrawler {

	void setProperty(String key, String value);

	List<Document> crawl();

}
