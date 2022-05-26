package lab;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiIndex;
import nlp4j.wiki.WikiIndexReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class Wikipedia020ReadAllPageXml {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) throws Exception {

//		String itemString = "三密";

		String dir = "R:/usr/local/data/wiki/20220501";
		String lang = "ja";
		String media = "wiki";
		String version = "20220501";

		File indexFile = MediaWikiFileUtils.getIndexFile(dir, lang, media, version);
		File dumpFile = MediaWikiFileUtils.getDumpFile(dir, lang, media, version);

		System.err.println(indexFile);

		// WikiPedia のインデックスが読めるかどうかテスト

		{
		}

		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile);

		int count = 0;

//		List<String> otheruses = new ArrayList<String>();
		StringBuilder otheruses = new StringBuilder();

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);

		) {
			{

				List<String> titles = wikiIndex.getWikiItemTitles();
				int countAll = titles.size();

				// FOR EACH(WikiItemTitles) THEN print info
				for (String title : titles) {

					count++;
					logger.info("processing: " + count + " of " + countAll);

					if (title == null) {
						logger.warn("Title is null");
						continue;
					}

					WikiPage page = dumpReader.getItem(title);
					String text = page.getText();

					if (text != null) {
						for (String s : text.split("\n")) {
							if (s.startsWith("{{otheruses")) {
								count++;
								System.err.println(s);
//								otheruses.add(s);
								otheruses.append(s + "\n");
								break;
							}
						}
					} else {
						logger.warn("Text is null: " + title);
					}

//					if (count > 10) {
//						break;
//					}

				} // END OF FOR EACH
//				System.err.println("<text>");
//				System.err.println(page.getTimestamp());
//				System.err.println(page.getText()); // (1) Wiki 形式
//				System.err.println("</text>");
//				System.err.println(page.getXml() != null);
//				System.err.println(page.getXml());

			}
		}

		FileUtils.write(new File("src/test/java/lab/out.txt"), //
				otheruses.toString(), "UTF-8", false);

	}

}
