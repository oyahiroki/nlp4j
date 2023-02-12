package nlp4j.wiki;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;

public class WikiDocumentCrawlerTestCase extends TestCase {

	Class target = WikiDocumentCrawler.class;

	public void testCrawlDocument001() throws Exception {

		String dir = "C:/usr/local/wiki/jawiktionary/20230101/";
		String wikidumpfile_name = dir + "jawiktionary-20230101-pages-articles-multistream.xml.bz2";
		String wikiindexfile_name = dir + "jawiktionary-20230101-pages-articles-multistream-index.txt.bz2";
		String entries = "学校,病院,医者,いぬ,ねこ";

		WikiDocumentCrawler crawler = new WikiDocumentCrawler();
		{
			crawler.setProperty("wikidumpfile", wikidumpfile_name);
			crawler.setProperty("wikiindexfile", wikiindexfile_name);
			crawler.setProperty("entries", entries);
		}

		List<Document> docs = crawler.crawlDocuments();

		for (Document doc : docs) {
//			System.err.println(doc);
			System.err.println("---");
			System.err.println(doc.getAttribute("item"));
			System.err.println("---");
			System.err.println(doc.getAttribute("wikitext"));
			System.err.println("---");
			System.err.println(doc.getAttribute("wikiplaintext"));
			System.err.println("---");
			System.err.println(doc.getAttribute("wikihtml"));
		}
	}

}
