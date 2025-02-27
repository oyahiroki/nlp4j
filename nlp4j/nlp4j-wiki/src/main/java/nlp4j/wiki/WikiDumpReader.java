package nlp4j.wiki;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.invoke.MethodHandles;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import nlp4j.util.FileUtils;

/**
 * <pre>
 * </pre>
 * 
 * created on 2021-06-25
 * 
 * @author Hiroki Oya
 */
public class WikiDumpReader implements AutoCloseable {

	private static final String ENCODING_UTF8 = "utf-8";

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private static final String TAG1_MEDIAWIKI = "<mediawiki>";

	private static final String TAG1_PAGE = "<page>";

	private static final String TAG1_TITLE = "<title>";

	private static final String TAG2_MEDIAWIKI = "</mediawiki>";

	private static final String TAG2_PAGE = "</page>";

	private static final String TAG2_TITLE = "</title>";

	File dumpFile;
	RandomAccessFile randomfile1;
	WikiIndex wikiIndex;

//	private WikiPageHandler wikiPageHander;

	private long time1 = System.currentTimeMillis();

	/**
	 * 読み込んだページ（複数ページを同時にファイルから読む必要があるため、キャッシュとして残る場合がある）
	 */
	private Map<String, WikiPage> pages = null;

	/**
	 * キャッシュから見つかった数
	 */
	private int countCacheFound = 0;

	/**
	 * @param dumpFile Wiki dump file (bz2)
	 * @throws IOException on Error File Not found
	 */
	public WikiDumpReader(File dumpFile) throws IOException {
		init0(dumpFile);
	}

	public WikiDumpReader(String dumpFileName) throws IOException {
		File dumpFile = new File(dumpFileName);
		init0(dumpFile);
	}

	private void init0(File dumpFile) throws IOException {
		if (dumpFile.exists() == false) {
			FileUtils.checExists(dumpFile);
			throw new FileNotFoundException("Dump File Not Found: " + dumpFile.getAbsolutePath());
		}
		this.dumpFile = dumpFile;
		this.randomfile1 = new RandomAccessFile(dumpFile, "r");
	}

	/**
	 * @param dumpFile  Wiki dump file (bz2)
	 * @param indexFile Wiki index file (bz2)
	 * @throws IOException on Error File Not found
	 */
	public WikiDumpReader(File dumpFile, File indexFile) throws IOException {
		init1(dumpFile, indexFile);
	}

	/**
	 * @param dumpFileName:  path of Wiki dump file (bz2)
	 * @param indexFileName: path of Wiki index file (bz2)
	 * @throws IOException on Error File Not found
	 */
	public WikiDumpReader(String dumpFileName, String indexFileName) throws IOException {
		File dumpFile = new File(dumpFileName);
		File indexFile = new File(indexFileName);
		init1(dumpFile, indexFile);
	}

	private void init1(File dumpFile, File indexFile) throws IOException {
		if (dumpFile.exists() == false) {
			throw new FileNotFoundException("Dump File Not Found: " + dumpFile.getAbsolutePath());
		}
		if (indexFile.exists() == false) {
			throw new FileNotFoundException("Index File Not Found: " + indexFile.getAbsolutePath());
		}
		// READ INDEX FILE
		this.wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
		this.dumpFile = dumpFile;
		// RANDOM READ DUMP FILE
		this.randomfile1 = new RandomAccessFile(dumpFile, "r");
	}

	/**
	 * java.lang.AutoCloseable: Close Wiki Dump file
	 */
	public void close() {

		logger.info("countCacheFound: " + countCacheFound);
		long time2 = System.currentTimeMillis();
		logger.info("time: " + (time2 - time1));

		if (this.randomfile1 != null) {
			try {
				this.randomfile1.close();
				logger.info("File closed.");
			} catch (IOException e) {
				logger.error(e.getMessage());
				logger.error(e);
			}
		}
	}

	/**
	 * @param itemString Dictionary Entry
	 * @return Wiki Item (Null on Not found)
	 * @throws IOException on Error
	 */
	public WikiPage getItem(String itemString) throws IOException {
		// GET INDEX INFO FROM INDEX FILE
		WikiIndexItem item = wikiIndex.getItem(itemString);

		// IF(INDEX NOT FOUND) THEN RETURN NULL
		if (item == null) {
			logger.debug("Not found in index:" + itemString);
			return null;
		}

		if (this.pages != null) {
			String itemId = "" + item.getItemID();
			if (this.pages.containsKey(itemId)) {
				logger.debug("Cache found!: " + item.getTitle());
				countCacheFound++;
				return this.pages.get(itemId);
			}
		}

		// READ INDEX INFO
		long p1 = item.getBlockNum();
		int size = (int) item.getSize();

		// IF(INDEX NOT FOUND) THEN RETURN NULL
		if (size < 0) {
			return null;
		}
		// BUFFER FOR DATA READ
		byte[] b = new byte[size];
		// SEEK FILE
		randomfile1.seek(p1);
		// READ FILE
		int r = randomfile1.read(b); // throws IOException

		logger.debug("File read in bytes: " + r);

		if (logger.isDebugEnabled()) {
			System.err.println(r);

			System.err.println("length: " + b.length);
			System.err.println("begin: " + Integer.toHexString(b[0]));
			System.err.println("char: " + (char) b[0]);
			System.err.println("char: " + (char) b[1]);
			System.err.println("char: " + (char) b[2]);
			System.err.println("char: " + (char) b[3]);
			System.err.println("char: " + (char) b[4]);
			System.err.println("char: " + (char) b[5]);
			System.err.println("char: " + (char) b[6]);
			System.err.println("end: " + Integer.toHexString(b[b.length - 1]));

			System.err.println("OK");
		}

		// READ AS BZ2
		BZip2CompressorInputStream bz2cis = new BZip2CompressorInputStream(new ByteArrayInputStream(b));

		String blockXml = TAG1_MEDIAWIKI //
				+ org.apache.commons.io.IOUtils.toString(bz2cis, ENCODING_UTF8) //
				+ TAG2_MEDIAWIKI;

//				System.err.println(blockXml);
//		System.err.println(blockXml);

		try {

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();

			// XML Handler for Media Wiki
//			MediawikiXmlHandler handler = new MediawikiXmlHandler();
			MediawikiXmlHandler2 handler = new MediawikiXmlHandler2();

			InputStream bais = new ByteArrayInputStream(blockXml.getBytes(ENCODING_UTF8));

// 2023/02/14 10:09:44.761 [main] INFO   nlp4j.wiki.WikiDumpReader       :93   File closed.
// Exception in thread "main" java.io.IOException: org.xml.sax.SAXParseException;
//			lineNumber: 4490; columnNumber: 17; 要素タイプ"timestamp"の後には、属性指定">"または"/>"が必要です。

			saxParser.parse(bais, handler); // throws org.xml.sax.SAXParseException

			pages = handler.getPages();

			logger.debug("pages.size:" + pages.size());

//			System.err.println(pages.get(itemString).getTitle());
//			System.err.println(pages.get(itemString).getText());

			WikiPage page = pages.get("" + item.getItemID());
			page.setXml(blockXml);

			{
				int idxTitle = blockXml.indexOf(TAG1_TITLE + itemString + TAG2_TITLE);
				if (idxTitle != -1) {
					int idx2 = blockXml.indexOf(TAG2_PAGE, idxTitle);
					if (idx2 != -1) {
						page.setXml(TAG1_PAGE + "\n" + blockXml.substring(idxTitle, idx2) + "\n" + TAG2_PAGE);
					}
				}
			}

			return page;

		} catch (org.xml.sax.SAXParseException e) {
			String msg = "itemString: " + itemString + ",XML: " + blockXml;
			throw new IOException(msg, e);
		} catch (Exception e) {
			String msg = "itemString: " + itemString + ",XML: " + blockXml;
			throw new IOException(msg, e);
		}

	}

	public Map<String, WikiPage> getItemsInSameBlock(String itemString) throws IOException {

		// GET INDEX INFO FROM INDEX FILE
		WikiIndexItem item = wikiIndex.getItem(itemString);

		// IF(INDEX NOT FOUND) THEN RETURN NULL
		if (item == null) {
			logger.debug("Not found in index:" + itemString);
			return null;
		}
		// READ INDEX INFO
		long p1 = item.getBlockNum();
		int size = (int) item.getSize();

		// IF(INDEX NOT FOUND) THEN RETURN NULL
		if (size < 0) {
			return null;
		}
		// BUFFER FOR DATA READ
		byte[] b = new byte[size];
		// SEEK FILE
		randomfile1.seek(p1);
		// READ FILE
		int r = randomfile1.read(b); // throws IOException

		logger.debug("File read in bytes: " + r);

		if (logger.isDebugEnabled()) {
			System.err.println(r);

			System.err.println("length: " + b.length);
			System.err.println("begin: " + Integer.toHexString(b[0]));
			System.err.println("char: " + (char) b[0]);
			System.err.println("char: " + (char) b[1]);
			System.err.println("char: " + (char) b[2]);
			System.err.println("char: " + (char) b[3]);
			System.err.println("char: " + (char) b[4]);
			System.err.println("char: " + (char) b[5]);
			System.err.println("char: " + (char) b[6]);
			System.err.println("end: " + Integer.toHexString(b[b.length - 1]));

			System.err.println("OK");
		}

		// READ AS BZ2
		BZip2CompressorInputStream bz2cis = new BZip2CompressorInputStream(new ByteArrayInputStream(b));

		String blockXml = TAG1_MEDIAWIKI //
				+ org.apache.commons.io.IOUtils.toString(bz2cis, ENCODING_UTF8) //
				+ TAG2_MEDIAWIKI;

//		System.err.println(blockXml);

		try {

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();

			// XML Handler for Media Wiki
//			MediawikiXmlHandler handler = new MediawikiXmlHandler();
			MediawikiXmlHandler2 handler = new MediawikiXmlHandler2();

			InputStream bais = new ByteArrayInputStream(blockXml.getBytes(ENCODING_UTF8));

			saxParser.parse(bais, handler);

			// ID -> WikiPage
			Map<String, WikiPage> pages = handler.getPages();

			for (WikiPage page : pages.values()) {

				String title = page.getTitle();

//				WikiPage page = pages.get("" + item.getItemID());

//				page.setXml(blockXml);

				{
					// <page>
					// <title>abc</title>
					// ...
					// </page>
					int idxTitle = blockXml.indexOf(TAG1_TITLE + title + TAG2_TITLE);
					if (idxTitle != -1) {
						// </page>
						int idx2 = blockXml.indexOf(TAG2_PAGE, idxTitle);
						if (idx2 != -1) {
							page.setXml(TAG1_PAGE + "\n" + blockXml.substring(idxTitle, idx2) + "\n" + TAG2_PAGE);
						}
					}
				}

			}

			return pages;

		} catch (Exception e) {
			throw new IOException(e);
		}

	}

	/**
	 * @return WikiIndex
	 */
	public WikiIndex getWikiIndex() {
		return wikiIndex;
	}

	/**
	 * <pre>
	 * Read all pages from Dump File
	 * すべてのページを読み込む
	 * </pre>
	 * 
	 * @param wikiPageHander Wiki Page Handler
	 * @throws IOException    on IO Error
	 * @throws BreakException on Process was broken <b>Normally</b>
	 */
	public void read(WikiPageHandler wikiPageHander) throws IOException, BreakException {

		try ( //
				FileInputStream fis = new FileInputStream(this.dumpFile); //
				// READ AS BZ2
				// decompressConcatenated
				// if true, decompress until the end of the input;
				// if false, stop after the first .bz2 stream andleave the input position to
				// point to the nextbyte after the .bz2 stream
				// MEMO: decompressConcatenated をtrueにしないとエラーになる 2022-06-11
				// 参考: https://github.com/lemire/IndexWikipedia/issues/4
				BZip2CompressorInputStream bz2cis = new BZip2CompressorInputStream(fis, true); //
		) {

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			{ // 2022-08-01
				// to prevent org.xml.sax.SAXParseException; lineNumber: 111758337;
				// columnNumber: 66; JAXP00010004:
				// エンティティの累積サイズ"50,000,001"は、"FEATURE_SECURE_PROCESSING"で設定された制限"50,000,000"を超えました。
				saxParserFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
			}

			SAXParser saxParser = saxParserFactory.newSAXParser();

			// XML Handler for Media Wiki
			MediawikiXmlHandler3 handler = new MediawikiXmlHandler3();

			if (wikiPageHander != null) {
				handler.setWikiPageHander(wikiPageHander);
			}

			// bz2cis: BZip2CompressorInputStream
			saxParser.parse(bz2cis, handler); // throws org.xml.sax.SAXParseException;

//			HashMap<String, WikiPage> pages = handler.getPages();

//			System.err.println(pages.get(itemString).getTitle());
//			System.err.println(pages.get(itemString).getText());

		} //

		catch (SAXException e) {
//			if (e.getCause() != null && e.getCause() instanceof BreakException) {
////				System.err.println("break;");
//			} else {
//				throw new IOException(e);
//			}
			if (e.getCause() != null && e.getCause() instanceof BreakException) {
				logger.info("break");
				throw (BreakException) e.getCause();
			}
			throw new IOException(e);
		} catch (Exception e) {
			throw new IOException(e);
		}

	}

//	public void setWikiPageHander(WikiPageHandler wikiPageHander) {
//		this.wikiPageHander = wikiPageHander;
//	}

}
