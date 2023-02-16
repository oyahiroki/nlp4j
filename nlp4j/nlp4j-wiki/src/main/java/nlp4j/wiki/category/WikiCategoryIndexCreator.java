package nlp4j.wiki.category;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.node.Node;
import nlp4j.node.NodeUtils;
import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiIndex;
import nlp4j.wiki.WikiIndexItem;
import nlp4j.wiki.WikiIndexItemHandler;
import nlp4j.wiki.WikiIndexReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.util.MediaWikiTextUtils;

/**
 * created on 2023-02-14
 * 
 * @author Hiroki Oya
 *
 */
public class WikiCategoryIndexCreator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public void processCategoryFiles(String indexFileName, String dumpFileName, String outCategoryFileName,
			String outCategoryIndexFileName) throws IOException {
		long time1 = System.currentTimeMillis();
		logger.info("start");

		// Index File
		File indexFile = new File(indexFileName);
		System.err.println(indexFile.getAbsolutePath());
		// Dump File
		File dumpFile = new File(dumpFileName);
		System.err.println(dumpFile.getAbsolutePath());

		File outFile = new File(outCategoryFileName);
		File outFileIndex = new File(outCategoryIndexFileName);

		if (outFile.exists()) {
			throw new IOException("File already exists: " + outFile.getAbsolutePath());
		}
		if (outFileIndex.exists()) {
			throw new IOException("File already exists: " + outFileIndex.getAbsolutePath());
		}

		String categoryWikiTitlePrefix = "Category:";

		// カテゴリのID,Title のマップを作成する

		// TRY (WikiDumpReader, FileWriter)
		try ( //
				WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile); //
				FileWriter filewriter = new FileWriter(outFile, StandardCharsets.UTF_8, false); //
				FileWriter filewriterIndex = new FileWriter(outFileIndex, StandardCharsets.UTF_8, false); //
		) {
			WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile);
			int count = 0;
			List<WikiIndexItem> wikiIndexItems = wikiIndex.getWikiIndexItems();

			// FOR_EACH(WikiIndexItem)
			for (WikiIndexItem wikiIndexItem : wikiIndexItems) {

				count++;

				if (count % 1000 == 0) {
					System.err.println(count + " of " + wikiIndexItems.size() + " ("
							+ String.format("%.2f", (((double) count) / (double) wikiIndexItems.size()) * 100) + "%)");
				}

				String wikiIndexItemTitle = wikiIndexItem.getTitle();

				// IF(page title is category)
				if (wikiIndexItemTitle.startsWith(categoryWikiTitlePrefix)) {

					String title_short = wikiIndexItemTitle.substring(9);

					WikiPage categoryWikiPage = dumpReader.getItem(wikiIndexItemTitle);
					String pageId = categoryWikiPage.getId();

					filewriterIndex.write("" + pageId + ":" + title_short + "\n");

					String wikiText = categoryWikiPage.getText();
					List<String> categories = MediaWikiTextUtils.parseCategoryTags(wikiText);
					// IF(親カテゴリが存在) THEN
					if (categories.size() > 0) {
						for (String cate : categories) {
							if (logger.isDebugEnabled()) {
								logger.debug(title_short + " (子) -> (親) " + cate);
							}
							filewriter.write("" + pageId + ":" + title_short + "->" + cate + "\n");
						}
					}
					// ELSE(親カテゴリが存在しない)
					else {
						filewriter.write("" + pageId + ":" + title_short + "\n");
					}
				} // END_OF IF(page title is category)
			} // END_OF FOR_EACH(WikiIndexItem)
		} // END_OF TRY (WikiDumpReader, FileWriter)

		long time2 = System.currentTimeMillis();
		logger.info("Finished: " + (time2 - time1) + " ms");
	}

}
