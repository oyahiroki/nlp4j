package nlp4j.wiki;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

/**
 * <pre>
 * created_at 2021-06-25
 * </pre>
 * 
 * @author Hiroki Oya
 */
public class WikiIndexReaderTestCase extends TestCase {

	/**
	 * 
	 */
	public WikiIndexReaderTestCase() {
//		target = WikiIndexReader.class;
	}

	/**
	 * <pre>
	 * Read Wiki Index File
	 * </pre>
	 * 
	 * @throws Exception on Error
	 */
	public void testReadIndexFile_File001() throws Exception {
		String indexFileName = "src/test/resources/nlp4j.wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
		String itemString = "ヨーロッパ";

		File indexFile = new File(indexFileName);

		if (indexFile.exists() == false) {
			System.err.println("Not exists: " + indexFile.getAbsolutePath());
			return;
		}

		// Read Wiki Index File
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
		{
			// 見出しの数
			// titles: 296564
			System.err.println("titles: " + wikiIndex.getWikiItemTitles().size());
			// WikiIndex [wikiItemTitles=296564]
			System.err.println(wikiIndex);

//			for (String title : wikiIndex.getWikiItemTitles()) {
//				System.err.println(title);
//			}
		}

		WikiIndexItem item = wikiIndex.getItem(itemString);

		// WikiIndexItem [blockNum=1227884, itemID=7613, title=ヨーロッパ, namespace=null,
		// nextBlock=1263980]
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

	/**
	 * @throws Exception on Error
	 */
	public void testReadIndexFile_File003() throws Exception {
		String indexFileName = "src/test/resources/nlp4j.wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
		String itemString = "学校";

		File indexFile = new File(indexFileName);

		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException

		WikiIndexItem item = wikiIndex.getItem(itemString);

		System.err.println(item);
	}

	/**
	 * @throws Exception on Error
	 */
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
				// ELSE LIKE("カテゴリ:日本語 花札")
			else {
				System.err.println(item.getTitle());
				count2namespace++;
			}
		}

		System.err.println("count1nonamespace: " + count1nonamespace);
		System.err.println("count2namespace: " + count2namespace);
	}

	public void testReadIndexFile_File101() throws Exception {
//		description = "Test for last item";
		String indexFileName = "src/test/resources/nlp4j.wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";

		File indexFile = new File(indexFileName);
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
		List<String> titles = wikiIndex.getWikiItemTitles();

		System.err.println(titles.size());

		for (int n = 0; n < 10000; n++) {
			System.err.println(String.format("%09d", n) + "\t" + titles.get(n));
		}

		int count1nonamespace = 0;
		int count2namespace = 0;

//		for(String title )
	}
}
