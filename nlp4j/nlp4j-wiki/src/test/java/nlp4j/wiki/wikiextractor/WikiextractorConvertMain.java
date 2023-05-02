package nlp4j.wiki.wikiextractor;

import java.io.File;
import java.io.IOException;

public class WikiextractorConvertMain {

	public static void main(String[] args) throws IOException {
		String fileName = "/usr/local/wiki/jawiki/20230101/wikiextractor/extract2/";
		String outFileName = "/usr/local/wiki/jawiki/20230101/wikiextractor/jawiki-20230101-wikiextractor.txt";

		File inDir = new File(fileName);
		File outFile = new File(outFileName);
		WikiextractorReader reader = new WikiextractorReader();
		reader.convert(inDir, outFile);
		System.err.println(outFile.getAbsolutePath());

	}

}
