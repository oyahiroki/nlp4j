package nlp4j.webcrawler.mlit;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.test.NLP4JTestCase;
import nlp4j.util.DocumentUtil;

public class MlitCarInfoCrawlerTestCase extends NLP4JTestCase {

	static private final Logger logger //
			= LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public MlitCarInfoCrawlerTestCase() {
		target = MlitCarInfoCrawler.class;
	}

	public void testGetAccessKey001() throws Exception {
		
		boolean skiptest = true;
		if(skiptest) {
			return;
		}

		MlitCarInfoCrawler crawler = new MlitCarInfoCrawler();
		String accessKey = crawler.getAccessKey();
		logger.info("accessKey=" + accessKey);
		assertNotNull(accessKey);

	}

	public void testCrawlDocuments001() throws Exception {

		boolean skiptest = true;
		if(skiptest) {
			return;
		}
		
		MlitCarInfoCrawler crawler = new MlitCarInfoCrawler();

		crawler.setProperty("from_date", "2020/01/01");
		crawler.setProperty("to_date", "2020/01/02");
		crawler.setProperty("debug", "true");

		List<Document> docs = crawler.crawlDocuments();

		System.err.println(DocumentUtil.toJsonPrettyString(docs));
	}

}
