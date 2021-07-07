package nlp4j.wiki;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hiroki Oya
 * @created_at 2021-06-25
 */
public class WikiDumpReader {

	private static final String ENCODING = "utf-8";

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	WikiIndex wikiIndex;
	RandomAccessFile randomfile1;

	/**
	 * @param dumpFile  Wiki dump file (bz2)
	 * @param indexFile Wiki index file (bz2)
	 * @throws IOException on Error File Not found
	 */
	public WikiDumpReader(File dumpFile, File indexFile) throws IOException {
		if (dumpFile.exists() == false) {
			throw new FileNotFoundException("Dump File Not Found: " + dumpFile.getAbsolutePath());
		}
		if (indexFile.exists() == false) {
			throw new FileNotFoundException("Index File Not Found: " + indexFile.getAbsolutePath());
		}
		this.wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
		randomfile1 = new RandomAccessFile(dumpFile, "r");
	}

	/**
	 * @param itemString Dictionary Entry
	 * @return Wiki Item (Null on Not found)
	 * @throws IOException on Error
	 */
	public WikiPage getItem(String itemString) throws IOException {
		WikiIndexItem item = wikiIndex.getItem(itemString);
		if (item == null) {
			logger.debug("Not found in index:" + itemString);
			return null;
		}

		long p1 = item.getBlockNum();
		int size = (int) item.getSize();

		byte[] b = new byte[size];

		randomfile1.seek(p1);

		int r = randomfile1.read(b);

		logger.debug("File read in bytes: " + r);

//		System.err.println(r);
//
//		System.err.println("length: " + b.length);
//		System.err.println("begin: " + Integer.toHexString(b[0]));
//		System.err.println("char: " + (char) b[0]);
//		System.err.println("char: " + (char) b[1]);
//		System.err.println("char: " + (char) b[2]);
//		System.err.println("char: " + (char) b[3]);
//		System.err.println("char: " + (char) b[4]);
//		System.err.println("char: " + (char) b[5]);
//		System.err.println("char: " + (char) b[6]);
//		System.err.println("end: " + Integer.toHexString(b[b.length - 1]));
//
//		System.err.println("OK");

		ByteArrayInputStream bis = new ByteArrayInputStream(b);
		BZip2CompressorInputStream bz2cis = new BZip2CompressorInputStream(bis);
		String xml = "<mediawiki>" //
				+ org.apache.commons.io.IOUtils.toString(bz2cis, ENCODING) //
				+ "</mediawiki>";

		logger.debug(xml);

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			// XML Handler
			MediawikiXmlHandler handler = new MediawikiXmlHandler();

			InputStream bais = new ByteArrayInputStream(xml.getBytes(ENCODING));

			saxParser.parse(bais, handler);

			HashMap<String, WikiPage> pages = handler.getPages();

//			System.err.println(pages.get(itemString).getTitle());
//			System.err.println(pages.get(itemString).getText());

			WikiPage page = pages.get("" + item.getItemID());

			return page;

		} catch (Exception e) {
			throw new IOException(e);
		}

	}

	/**
	 * Close Wiki Dump file
	 */
	public void close() {
		if (this.randomfile1 != null) {
			try {
				this.randomfile1.close();
			} catch (IOException e) {
			}
		}
	}

}
