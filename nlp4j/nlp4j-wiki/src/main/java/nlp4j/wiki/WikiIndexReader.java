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
			CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);
			BufferedReader br = new BufferedReader(new InputStreamReader(input, encoding));

			String line;

			while ((line = br.readLine()) != null) {
				int n1 = line.indexOf(':');
				int n2 = line.indexOf(':', n1 + 1);
				long blockNum = Long.parseLong(line.substring(0, n1));
				long itemID = Long.parseLong(line.substring(n1 + 1, n2));
				String item = line.substring(n2 + 1);
				WikiIndexItem wikiIndexItem = new WikiIndexItem(blockNum, itemID, item);
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
