package nlp4j.wiki;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.test.TestUtils;
import nlp4j.util.DocumentUtil;

public class WikiDocumentCrawler2TestCase extends TestCase {

	static {
		TestUtils.setLevelDebug();
	}

	Class target = WikiDocumentCrawler2.class;

	String dir = "/usr/local/wiki/jawiktionary/20230101/";

	String wikidumpfile_name = dir + "jawiktionary-20230101-pages-articles-multistream.xml.bz2";
	String wikiindexfile_name = dir + "jawiktionary-20230101-pages-articles-multistream-index.txt.bz2";

	public void testCrawlDocuments001() throws Exception {

//		String entries = "学校,病院,医者,いぬ,ねこ";

		System.err.println((new File(wikidumpfile_name)).exists());
		System.err.println((new File(wikiindexfile_name)).exists());

		WikiDocumentCrawler2 crawler = new WikiDocumentCrawler2();
		{
			crawler.setProperty("wikidumpfile", wikidumpfile_name);
			crawler.setProperty("wikiindexfile", wikiindexfile_name);
//			crawler.setProperty("entries", entries);
		}

		WikiDocumentAnnotator ann1 = new WikiDocumentAnnotator();
		{
			String paths = //
					"=={{ja}}==/==={{noun}}===" //
							+ ",=={{ja}}==/===固有名詞===" //
							+ ",=={{ja}}==/===慣用句===" //
							+ ",=={{ja}}==/==={{adj}}===" //
							+ ",=={{ja}}==/===和語の漢字表記===" //
							+ ",=={{ja}}==/==={{adverb}}===" //
							+ ",=={{ja}}==/==={{adjectivenoun}}===" //
							+ "";
			ann1.setProperty("paths", paths);
		}

		List<Document> docs = crawler.crawlDocuments();

		assertNotNull(docs);
		assertNotNull(docs.get(0));

		System.err.println(docs.get(0) instanceof nlp4j.wiki.WikiIndexDocument);

		System.err.println(docs.size());

//		Document doc = docs.get(6157);

		int count = 0;

		for (Document doc : docs) {
//			System.err.println(doc);
			System.err.println(DocumentUtil.toPrettyJsonString(doc));
			ann1.annotate(doc);
			System.err.println(DocumentUtil.toPrettyJsonString(doc));

			if (count > 100) {
				break;
			}

//			System.err.println("---");
//			System.err.println(doc.getAttributeAsString("item"));
//			System.err.println("---");
			System.err.println(doc.getAttributeAsString("wikitext"));
//			System.err.println("---");
//			System.err.println(doc.getAttributeAsString("wikiplaintext"));
//			System.err.println("---");
//			System.err.println(doc.getAttributeAsString("wikihtml"));
			count++;
		}

	}

	public void testCrawlDocuments002() throws Exception {

		String entries = "履修,日本";

		WikiDocumentCrawler2 crawler = new WikiDocumentCrawler2();
		{
			crawler.setProperty("wikidumpfile", wikidumpfile_name);
			crawler.setProperty("wikiindexfile", wikiindexfile_name);
			crawler.setProperty("entries", entries);
		}

		WikiDocumentAnnotator ann1 = new WikiDocumentAnnotator();
		{
			String paths = //
					"=={{ja}}==/==={{noun}}===" //
							+ ",=={{ja}}==/===固有名詞===" //
							+ "";
			ann1.setProperty("paths", paths);
		}

		List<Document> docs = crawler.crawlDocuments();

		assertTrue(docs.size() > 0);

		System.err.println(docs.get(0) instanceof nlp4j.wiki.WikiIndexDocument);

		System.err.println(docs.size());

		// Document doc = docs.get(6157);

		int count = 0;

		for (Document doc : docs) {
			// System.err.println(doc);
			System.err.println(DocumentUtil.toPrettyJsonString(doc));
			ann1.annotate(doc);
			System.err.println(DocumentUtil.toPrettyJsonString(doc));

			if (count > 100) {
				break;
			}

			// System.err.println("---");
			// System.err.println(doc.getAttributeAsString("item"));
			// System.err.println("---");
			System.err.println(doc.getAttributeAsString("wikitext"));
			// System.err.println("---");
			// System.err.println(doc.getAttributeAsString("wikiplaintext"));
			// System.err.println("---");
			// System.err.println(doc.getAttributeAsString("wikihtml"));
			count++;
		}

	}

	public void testCrawlDocuments003furigana() throws Exception {

		// String entries = "学校,病院,医者,いぬ,ねこ";

		WikiDocumentCrawler2 crawler = new WikiDocumentCrawler2();
		{
			crawler.setProperty("wikidumpfile", wikidumpfile_name);
			crawler.setProperty("wikiindexfile", wikiindexfile_name);
			// crawler.setProperty("entries", entries);
		}

		WikiDocumentAnnotator ann1 = new WikiDocumentAnnotator();
		{
			String paths = //
					"=={{ja}}==/==={{noun}}===" //
							+ ",=={{ja}}==/===固有名詞===" //
							+ ",=={{ja}}==/===慣用句===" //
							+ ",=={{ja}}==/==={{adj}}===" //
							+ ",=={{ja}}==/===和語の漢字表記===" //
							+ ",=={{ja}}==/==={{adverb}}===" //
							+ ",=={{ja}}==/==={{adjectivenoun}}===" //
							+ "";
			ann1.setProperty("paths", paths);
		}

		List<Document> docs = crawler.crawlDocuments();

//		System.err.println(docs.get(0) instanceof nlp4j.wiki.WikiIndexDocument);

//		System.err.println(docs.size());

		// Document doc = docs.get(6157);

		int count = 0;

		for (Document doc : docs) {
			// System.err.println(doc);
//			System.err.println(DocumentUtil.toPrettyJsonString(doc));
			ann1.annotate(doc);
//			System.err.println(DocumentUtil.toPrettyJsonString(doc));

			if (count > 100) {
				break;
			}

			// System.err.println("---");
			// System.err.println(doc.getAttributeAsString("item"));
			// System.err.println("---");
//			System.err.println(doc.getAttributeAsString("wikitext"));
			String wikitext = doc.getAttributeAsString("wikitext");
			if (wikitext.contains("{{ふりがな")) {
				System.err.println(wikitext);
				System.err.println("---");
				System.err.println(doc.getAttributeAsString("wikiplaintext"));
				System.err.println("---");
			}
			// System.err.println("---");
			// System.err.println(doc.getAttributeAsString("wikiplaintext"));
			// System.err.println("---");
			// System.err.println(doc.getAttributeAsString("wikihtml"));
			count++;
		}

	}

	public void testCrawlDocuments004Rel() throws Exception {

		String entries = "学校";

		WikiDocumentCrawler2 crawler = new WikiDocumentCrawler2();
		{
			crawler.setProperty("wikidumpfile", wikidumpfile_name);
			crawler.setProperty("wikiindexfile", wikiindexfile_name);
			crawler.setProperty("entries", entries);
		}

		WikiDocumentAnnotator ann1 = new WikiDocumentAnnotator();
		{
			String paths = //
					"=={{ja}}==/==={{noun}}===/===={{rel}}====";
			ann1.setProperty("paths", paths);
		}

		List<Document> docs = crawler.crawlDocuments();

		assertTrue(docs.size() > 0);

		System.err.println(docs.get(0) instanceof nlp4j.wiki.WikiIndexDocument);

		System.err.println(docs.size());

		// Document doc = docs.get(6157);

		int count = 0;

		for (Document doc : docs) {
			// System.err.println(doc);
			System.err.println(DocumentUtil.toPrettyJsonString(doc));
			ann1.annotate(doc);

			System.err.println("---");
			System.err.println(DocumentUtil.toPrettyJsonString(doc));

			if (count > 100) {
				break;
			}

			// System.err.println("---");
			// System.err.println(doc.getAttributeAsString("item"));
			// System.err.println("---");
//			System.err.println(doc.getAttributeAsString("wikitext"));
			// System.err.println("---");
			// System.err.println(doc.getAttributeAsString("wikiplaintext"));
			// System.err.println("---");
			// System.err.println(doc.getAttributeAsString("wikihtml"));
			count++;
		}

	}

}
