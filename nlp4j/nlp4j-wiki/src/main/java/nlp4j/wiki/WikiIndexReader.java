package nlp4j.wiki;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <pre>
 * https://dumps.wikimedia.org/jawiki/
 * </pre>
 * 
 * @author Hiroki Oya
 * @created_at 2021-06-25
 */
public class WikiIndexReader {

	private static final String encoding = "UTF-8";
	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private WikiIndexReader() {
	}

	/**
	 * Read index file of wiki
	 * 
	 * <pre>
	 * Example of line
	 * 382308:2239:水素
	 * ^ blockNum
	 * 382308:2239:水素
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;^ idx1
	 * 382308:2239:水素
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;^ itemID
	 * 382308:2239:水素
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;^ idx2
	 * 382308:2239:水素
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;^ title
	 * </pre>
	 * 
	 * @param indexFile wiki index file
	 * @return wiki index
	 * @throws IOException on error
	 */
	static public WikiIndex readIndexFile(File indexFile) throws IOException {

		logger.debug("Reading: " + indexFile.getAbsolutePath());
		logger.debug("Reading File size: " + indexFile.length());

		WikiIndex wikiIndex = new WikiIndex((int) indexFile.length() / 10);
		wikiIndex.setDataLength(indexFile.length());

		long time1 = System.currentTimeMillis();

		// try-with-resources
		try (FileInputStream fis = new FileInputStream(indexFile)) {

			BufferedInputStream bis = new BufferedInputStream(fis);
			CompressorInputStream cis = new CompressorStreamFactory() //
					.createCompressorInputStream(bis);
			BufferedReader br = new BufferedReader(new InputStreamReader(cis, encoding));

			String line;

			// Example of line
			// 382308:2239:水素
			while ((line = br.readLine()) != null) {

				int idx1block = line.indexOf(':');
				int idx2id = line.indexOf(':', idx1block + 1);

				long blockNum = Long.parseLong(line.substring(0, idx1block));
				long itemID = Long.parseLong(line.substring(idx1block + 1, idx2id));

				String title = line.substring(idx2id + 1).trim();

				title = StringEscapeUtils.unescapeHtml4(title);

				WikiIndexItem wikiIndexItem = new WikiIndexItem(blockNum, itemID, title);
				wikiIndex.add(wikiIndexItem);

			}

		} catch (CompressorException e) {
			throw new IOException(e);
		}

		long time2 = System.currentTimeMillis();

		logger.debug("Read done: " + (time2 - time1));
		logger.debug("WikiIndex item size: " + wikiIndex.size());

		return wikiIndex;
	}
}