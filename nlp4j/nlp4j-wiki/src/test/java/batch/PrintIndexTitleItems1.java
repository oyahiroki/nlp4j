package batch;

import java.io.File;
import java.util.List;

import nlp4j.wiki.WikiIndex;
import nlp4j.wiki.WikiIndexReader;

public class PrintIndexTitleItems1 {

	static public void main(String[] args) throws Exception {
		String indexFileName = "src/test/resources/nlp4j.wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";

		File indexFile = new File(indexFileName);
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
		List<String> titles = wikiIndex.getWikiItemTitles();

		System.err.println(titles.size());

		for (int n = 0; n < titles.size(); n++) {
			System.err.println(String.format("%09d", n) + "\t" + titles.get(n));
		}

	}

}
