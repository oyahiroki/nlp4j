package nlp4j.wiki;

import java.io.File;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class WikiAbstractReaderTestCase extends TestCase {

	public void testRead001() throws Exception {

		File abstFile = MediaWikiFileUtils.getAbstarctFile("/usr/local/data/wiki/20220501/", "ja", "wiki", "20220501");
//		System.err.println(abstFile.exists());

		if (abstFile.exists() == false) {
			System.err.println("SKIP THIS TEST: " + abstFile.getAbsolutePath());
			return;
		}

		DocumentHandler handler = new DocumentHandler() {
			int count = 0;

			@Override
			public void read(Document document) throws BreakException {
				String abs = document.getAttributeAsString("abstract");
				if (abs != null && abs.length() > 32) {
					abs = abs.substring(0, 32);
				}
				System.err.println(count + " " + document.getAttribute("title") + ":" + abs);
				count++;
				if (count > 10000) {

					System.err.println("keys: " + document.getAttributeKeys());

					throw new BreakException();
				}
			}
		};

		WikiAbstractReader reader = new WikiAbstractReader(abstFile);
		reader.setHandler(handler);
		reader.read();

	}

}
