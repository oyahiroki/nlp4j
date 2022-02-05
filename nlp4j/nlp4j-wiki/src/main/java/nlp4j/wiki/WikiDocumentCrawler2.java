package nlp4j.wiki;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.crawler.AbstractCrawler;
import nlp4j.crawler.Crawler;

/**
 * <pre>
 * WikiIndex文書の遅延読み込みを行う。
 * Lazy loading Crawler for Wiki Index Files.
 * </pre>
 * 
 * <pre>
 * Properties:
 * wikidumpfile: File path of wiki dump.
 * wikiindexfile: File path of wiki index.
 * 
 * created_at 2021-08-19
 * </pre>
 * 
 * <pre>
 * Document
 *     "item": (String) Wiki Document Title
 *     "wikitetxt": (String) Wiki Markdown format text
 *     "wikiplaintext": (String) Wiki Plain text
 *     "wikihtml": (String) Wiki HTML format text
 * </pre>
 * 
 * @author Hiroki Oya
 * @see nlp4j.wiki.WikiIndexDocument
 */
public class WikiDocumentCrawler2 extends AbstractCrawler implements Crawler {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	
	File wikidumpfile = null;
	File wikiindexfile = null;
	private List<String> entries;

	/**
	 * <pre>
	 * Document
	 *     "item": (String) Wiki Document Title
	 *     "wikitetxt": (String) Wiki Markdown format text
	 *     "wikiplaintext": (String) Wiki Plain text
	 *     "wikihtml": (String) Wiki HTML format text
	 * </pre>
	 * 
	 * @return List of nlp4j.wiki.WikiIndexDocument
	 * @see nlp4j.wiki.WikiIndexDocument
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
				
				logger.info(doc.getAttributeAsString("item"));

				if (this.entries == null) {
					docs.add(doc);
				} else {
					if (this.entries.contains(item.getTitle())) {
						docs.add(doc);
					}
				}
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
			else if (key.equals("entries")) {
				String[] ss = value.split(",");
				this.entries = Arrays.asList(ss);
			} else {

			}
		}
	}

}
