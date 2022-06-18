package nlp4j.wiki;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.util.DocumentUtil;

public class WikiDocumentCrawler2TestCase extends TestCase {

	Class target = WikiDocumentCrawler2.class;

	public void test001() throws Exception {

		String wikidumpfile_name = "C:/usr/local/data/wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream.xml.bz2";
		String wikiindexfile_name = "C:/usr/local/data/wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
//		String entries = "学校,病院,医者,いぬ,ねこ";

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

	public void test002() throws Exception {

		String wikidumpfile_name = "C:/usr/local/data/wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream.xml.bz2";
		String wikiindexfile_name = "C:/usr/local/data/wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
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

	public void test003furigana() throws Exception {

		String wikidumpfile_name = "C:/usr/local/data/wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream.xml.bz2";
		String wikiindexfile_name = "C:/usr/local/data/wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
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

}
