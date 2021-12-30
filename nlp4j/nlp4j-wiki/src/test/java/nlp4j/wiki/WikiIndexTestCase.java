package nlp4j.wiki;

import java.io.File;

import junit.framework.TestCase;

/**
 * <pre>
 * created_at 2021-06-25
 * </pre>
 * 
 * @author Hiroki Oya
 */
public class WikiIndexTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target;

	/**
	 * 
	 */
	public WikiIndexTestCase() {
		target = WikiIndex.class;
	}

	/**
	 * Test hasItem() method
	 * 
	 * @throws Exception on error
	 */
	public void testHasItem001() throws Exception {
		String indexFileName = "src/test/resources/nlp4j.wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
		String itemString = "ヨーロッパ";

		File indexFile = new File(indexFileName);
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile);
		boolean b = wikiIndex.hasItem(itemString);
		assertTrue(b);
	}

	/**
	 * Test getItem() method
	 * 
	 * @throws Exception on error
	 */
	public void testGetItem002() throws Exception {
		String indexFileName = "src/test/resources/nlp4j.wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
		String itemString = "ヨーロッパ";

		File indexFile = new File(indexFileName);
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile);
		WikiIndexItem item = wikiIndex.getItem(itemString);
		assertNotNull(item);
	}

}
