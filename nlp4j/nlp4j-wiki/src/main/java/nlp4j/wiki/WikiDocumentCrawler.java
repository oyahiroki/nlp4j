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
 * @author Hiroki Oya
 * @created_at 2021-07-09
 */
public class WikiDocumentCrawler extends AbstractCrawler implements Crawler {

	File wikidumpfile = null;
	File wikiindexfile = null;

	String[] entries = null;

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
		if (this.entries == null) {
			return null;
		}

		ArrayList<Document> docs = new ArrayList<>();

		try {
			WikiDumpReader dumpReader = new WikiDumpReader(wikidumpfile, wikiindexfile);
			for (String entry : entries) {

				WikiPage page = dumpReader.getItem(entry);

				if (page == null) {

				}

				Document doc = new DefaultDocument();

				doc.putAttribute("item", entry);

//				System.err.println("<plaintext>");
//				System.err.println(page.getPlainText());
//				System.err.println("</plaintext>");

				doc.putAttribute("wikiplaintext", page.getPlainText());

//				System.err.println("<text>");
//				System.err.println(page.getText());
//				System.err.println("</text>");
				doc.putAttribute("wikitext", page.getText());

//				System.err.println("<html>");
//				System.err.println(page.getHtml());
//				System.err.println("</html>");
				doc.putAttribute("wikihtml", page.getHtml());

				docs.add(doc);

			}

			dumpReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return docs;
	}

	@Override
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
				this.entries = value.split(",");
			} //
			else {

			}

		}

	}

}