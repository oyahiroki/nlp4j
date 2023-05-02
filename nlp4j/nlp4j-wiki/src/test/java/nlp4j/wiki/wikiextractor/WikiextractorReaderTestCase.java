package nlp4j.wiki.wikiextractor;

import java.io.File;

import junit.framework.TestCase;

public class WikiextractorReaderTestCase extends TestCase {

	public void testConvert001() throws Exception {
		String dirName = "src/test/resources/nlp4j.wiki.wikiextractor/wikiextract";
		File outFile = File.createTempFile("wikiextractor", ".txt");
		WikiextractorReader reader = new WikiextractorReader();
		reader.convert(new File(dirName), outFile);
		System.err.println(outFile.getAbsolutePath());
	}

	public void testConvert002() throws Exception {
		String dirName = "src/test/resources/nlp4j.wiki.wikiextractor/wikiextract/AA/wiki_00";
		File outFile = File.createTempFile("wikiextractor", ".txt");
		WikiextractorReader reader = new WikiextractorReader();
		reader.convert(new File(dirName), outFile);
		System.err.println(outFile.getAbsolutePath());
	}

}
