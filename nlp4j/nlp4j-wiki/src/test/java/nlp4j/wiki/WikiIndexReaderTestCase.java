package nlp4j.wiki;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Hiroki Oya
 * @created_at 2021-06-25
 */
public class WikiIndexReaderTestCase extends TestCase {

	/**
	 * 
	 */
	public WikiIndexReaderTestCase() {
//		target = WikiIndexReader.class;
	}

	/**
	 * @throws Exception on Error
	 */
	public void testReadIndexFile_File001() throws Exception {
		String indexFileName = "src/test/resources/nlp4j.wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
		String itemString = "ヨーロッパ";

		File indexFile = new File(indexFileName);
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
		WikiIndexItem item = wikiIndex.getItem(itemString);

		System.err.println(item);
	}

	// マリア・デル・カルメン・マルティネス＝ボルディウ

	/**
	 * @throws Exception on Error
	 */
	public void testReadIndexFile_File002() throws Exception {
//		description = "Test for last item";
		String indexFileName = "src/test/resources/nlp4j.wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
		String itemString = "preferences";

		File indexFile = new File(indexFileName);
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
		WikiIndexItem item = wikiIndex.getItem(itemString);

		System.err.println(item);
	}

	public void testReadIndexFile_File003() throws Exception {
//		description = "Test for last item";
		String indexFileName = "src/test/resources/nlp4j.wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";

		File indexFile = new File(indexFileName);
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
		List<String> titles = wikiIndex.getWikiItemTitles();

		System.err.println(titles.size());

		int count1nonamespace = 0;
		int count2namespace = 0;

//		for(String title )
	}

	public void testReadIndexFile_File004() throws Exception {
//		description = "Test for last item";
		String indexFileName = "src/test/resources/nlp4j.wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";

		File indexFile = new File(indexFileName);
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
		List<WikiIndexItem> items = wikiIndex.getWikiIndexItems();

		System.err.println(items.size());

		int count1nonamespace = 0;
		int count2namespace = 0;

		for (WikiIndexItem item : items) {
			if (item.getNamespace() == null) {
				count1nonamespace++;
			} //
			else {
				System.err.println(item.getTitle());
				count2namespace++;
			}
		}

		System.err.println("count1nonamespace: " + count1nonamespace);
		System.err.println("count2namespace: " + count2namespace);
	}
}
