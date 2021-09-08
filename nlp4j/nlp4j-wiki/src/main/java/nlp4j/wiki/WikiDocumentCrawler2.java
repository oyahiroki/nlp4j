package nlp4j.wiki;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.AbstractCrawler;
import nlp4j.crawler.Crawler;
import nlp4j.impl.DefaultDocument;

/**
 * 文書の遅延読み込みを行う。Lazy loading Crawler for Wiki Index Files.
 * 
 * <pre>
 * Properties:
 * wikidumpfile: File path of wiki dump.
 * wikiindexfile: File path of wiki index.
 * 
 * </pre>
 * 
 * @author Hiroki Oya
 * @created_at 2021-08-19
 */
public class WikiDocumentCrawler2 extends AbstractCrawler implements Crawler {

	File wikidumpfile = null;
	File wikiindexfile = null;

	/**
	 * Document : "item","wikitetxt","wikiplaintext","wikihtml"
	 * 
	 * @return List of Document
	 */
	@Override
	public List<Document> crawlDocuments() {

		if (this.wikidumpfile == null || this.wikidumpfile.exists() == false) {
			return null;
		}

		if (this.wikiindexfile == null || this.wikiindexfile.exists() == false) {
			return null;
		}

		ArrayList<Document> docs = new ArrayList<>();

		try {

			WikiDumpReader dumpReader = new WikiDumpReader(wikidumpfile, wikiindexfile);

			WikiIndex wikiIndex = WikiIndexReader.readIndexFile(wikiindexfile); // throws IOException

			List<WikiIndexItem> items = wikiIndex.getWikiIndexItems();

			for (WikiIndexItem item : items) {

				WikiIndexDocument doc = new WikiIndexDocument();
				{
					doc.setDumpReader(dumpReader);
					doc.putAttribute("item", item.getTitle());
				}

				docs.add(doc);

			}

//			dumpReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return docs;
	}

	/**
	 * <pre>
	 * wikidumpfile: Dump file of wiki
	 * wikiindexfile: Index file of wiki
	 * </pre>
	 * 
	 * @param key   : key of properties
	 * @param value : value of properties
	 */
	public void setProperty(String key, String value) {
		super.setProperty(key, value);

		if (key == null) {
			return;
		} //
		else {
			if (key.equals("wikidumpfile")) {
				this.wikidumpfile = new File(value);
			} //
			else if (key.equals("wikiindexfile")) {
				this.wikiindexfile = new File(value);
			} //
			else {

			}

		}

	}

}
