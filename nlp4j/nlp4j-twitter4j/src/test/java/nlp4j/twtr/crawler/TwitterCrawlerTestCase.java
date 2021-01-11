package nlp4j.twtr.crawler;

import java.util.Properties;

import junit.framework.TestCase;

/**
 * @author Hiroki Oya
 * @since 20201224
 *
 */
public class TwitterCrawlerTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target = TwitterCrawler.class;

	static String[] keys = { //
			"oauth.consumerKey", //
			"oauth.consumerSecret", //
			"oauth.accessToken", //
			"oauth.accessTokenSecret" //
	};

	Properties props = new Properties();

	/**
	 * 
	 */
	public TwitterCrawlerTestCase() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public TwitterCrawlerTestCase(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setUp() throws Exception {

		super.setUp();

		System.err.println("target=" + this.target);

		System.err.println(super.getName());

		for (String key : keys) {
			props.setProperty(key, System.getProperty(key));
		}
	}

	public void testSetPropertyStringString() {

	}

	public void testCrawlDocuments() {

		System.err.println(props);

		TwitterCrawler crawler = new TwitterCrawler();
		crawler.setProperties(props);

	}

}
