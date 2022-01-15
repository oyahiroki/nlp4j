package batch;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;
import nlp4j.wiki.WikiDocumentAnnotator;
import nlp4j.wiki.WikiDocumentCrawler2;
import nlp4j.wiki.WikiIndex;
import nlp4j.wiki.WikiIndexReader;

public class PrintIndexTitleItems2 {

	static public void main(String[] args) throws Exception {

		String wikidumpfile_name = "/usr/local/data/wiki/" //
				+ "jawiktionary-20210401-pages-articles-multistream.xml.bz2";
		String wikiindexfile_name = "/usr/local/data/wiki/" //
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

		List<String> itemList = new ArrayList<>();

		for (Document doc : docs) {

			ann1.annotate(doc);

//			System.err.println(doc);
//			System.err.println(DocumentUtil.toPrettyJsonString(doc));
//			ann1.annotate(doc);
//			System.err.println(DocumentUtil.toPrettyJsonString(doc));

			String item = doc.getAttributeAsString("item");
			String text = doc.getAttributeAsString("text");

			if (text == null) {
				continue;
			}

			{ // remove new line chars
				text = text.replace("\n", " ");
			}

			{ // shorten text
				int length = 32;
				if (text.length() > length) {
					text = text.substring(0, length) + "...";
				}
			}

			if (item.startsWith("MediaWiki:") || item.startsWith("テンプレート:")) {
				continue;
			}

//			System.err.println(item + "," + text);

			itemList.add(item);

			if (count > 100) {
//				break;
				System.err.println(count);
			}

//			System.err.println("---");
//			System.err.println(doc.getAttributeAsString("item"));
//			System.err.println("---");
//			System.err.println(doc.getAttributeAsString("wikitext"));
//			System.err.println("---");
//			System.err.println(doc.getAttributeAsString("wikiplaintext"));
//			System.err.println("---");
//			System.err.println(doc.getAttributeAsString("wikihtml"));
			count++;
		}

		File outFile = new File("files/wiktionary_items.txt");

		itemList.sort(Comparator.naturalOrder());

		for (String item : itemList) {
			FileUtils.write(outFile, item + "\n", "UTF-8", true);
		}

	}

}
