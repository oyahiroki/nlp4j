package nlp4j.wiki;

import java.io.File;

import nlp4j.test.NLP4JTestCase;

/**
 * @author Hiroki Oya
 * @created_at 2021-06-25
 */
public class WikiIndexReaderTestCase extends NLP4JTestCase {

	/**
	 * 
	 */
	public WikiIndexReaderTestCase() {
		target = WikiIndexReader.class;
	}

	/**
	 * @throws Exception on Error
	 */
	public void testReadIndexFile_File001() throws Exception {
		String indexFileName = "src/test/resources/nlp4j.wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
		String itemString = "ヨーロッパ";

		File indexFile = new File(indexFileName);
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile);
		WikiIndexItem item = wikiIndex.getItem(itemString);

		System.err.println(item);
	}

	// マリア・デル・カルメン・マルティネス＝ボルディウ

	/**
	 * @throws Exception on Error
	 */
	public void testReadIndexFile_File002() throws Exception {
		description = "Test for last item";
		String indexFileName = "src/test/resources/nlp4j.wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
		String itemString = "preferences";

		File indexFile = new File(indexFileName);
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile);
		WikiIndexItem item = wikiIndex.getItem(itemString);

		System.err.println(item);
	}
}
