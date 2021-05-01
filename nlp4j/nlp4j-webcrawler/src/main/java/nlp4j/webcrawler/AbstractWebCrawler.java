package nlp4j.webcrawler;

import nlp4j.crawler.AbstractCrawler;
import nlp4j.crawler.Crawler;

/**
 * Abstract crawler for Web Data crawling
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 * @created_at 2021-02-09
 */
public abstract class AbstractWebCrawler extends AbstractCrawler implements Crawler {
	/**
	 * Initial Constructor
	 */
	public AbstractWebCrawler() {
		super();
	}

}
