package nlp4j.webcrawler;

import nlp4j.crawler.AbstractCrawler;
import nlp4j.crawler.Crawler;

/**
 * <pre>
 * Abstract crawler for Web Data crawling
 * </pre>
 * 
 * created on 2021-02-09
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public abstract class AbstractWebCrawler extends AbstractCrawler implements Crawler {
	/**
	 * Initial Constructor
	 */
	public AbstractWebCrawler() {
		super();
	}

}
