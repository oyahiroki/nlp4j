package lab;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiIndex;
import nlp4j.wiki.WikiIndexReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class Wikipedia020ReadAllPageXmlV2 {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * ダンプファイルの全件読み込み改良版
	 * 
	 * @param args
	 * @throws Exception
	 */
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

		List<String> otheruses = new ArrayList<String>();
//		StringBuilder otheruses = new StringBuilder();

		Set<String> used = new HashSet<String>();

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

					if (used.contains(title)) {
//						logger.info("SKIP: " + title);
						continue;
					}

					Map<String, WikiPage> pages = dumpReader.getItemsInSameBlock(title);

					for (WikiPage page : pages.values()) {

						String t = page.getTitle();
						used.add(t);
						System.err.println("title: " + t);
						String text = page.getText();

						if (text != null) {
							for (String s : text.split("\n")) {
								if (s.startsWith("{{Otheruses") //
										|| s.startsWith("{{otheruses")

								) {
									count++;

									String s2 = t + "\t" + s;

//									System.err.println(s2);

//									otheruses.add(s);
									otheruses.add(s2);
									break;
								}
							}
						} else {
							logger.warn("Text is null: " + title);
						}
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

		StringBuilder sb = new StringBuilder();

		for (String s : otheruses) {
			sb.append(s + "\n");
		}

		FileUtils.write(new File("src/test/java/lab/out.txt"), //
				sb.toString(), "UTF-8", false);

	}

}
