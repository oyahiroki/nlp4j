package nlp4j;

import java.util.ArrayList;

import nlp4j.crawler.AbstractCrawler;
import nlp4j.crawler.Crawler;
import nlp4j.impl.DefaultDocument;

/**
 * Crawler for Test
 * 
 * @author Hiroki Oya
 *
 */
public class CrawlerForTest extends AbstractCrawler implements Crawler {

	int id = 0;

	@Override
	public ArrayList<Document> crawlDocuments() {
		ArrayList<Document> docs = new ArrayList<>();

		for (int n = 0; n < 3; n++) {
			Document doc = new DefaultDocument();
			doc.putAttribute("text", "This is test");
			doc.putAttribute("id", id);
			id++;
			docs.add(doc);
		}

		return docs;
	}

}
