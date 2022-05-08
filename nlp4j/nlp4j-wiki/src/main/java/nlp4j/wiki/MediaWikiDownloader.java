package nlp4j.wiki;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.crawler.Crawler;
import nlp4j.webcrawler.AbstractWebCrawler;
import nlp4j.webcrawler.FileDownloader;
import nlp4j.wiki.util.MediaWikiMD5Util;

/**
 * @author Hiroki Oya
 *
 */
public class MediaWikiDownloader extends AbstractWebCrawler implements Crawler {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * {language}{media}-{version}-md5sums.txt
	 */
	static public final String MD5_FILENAME //
			= "%s" // language
					+ "%s" // media
					+ "-" //
					+ "%s"// version
					+ "-md5sums.txt" //
	;

	/**
	 * https://dumps.wikimedia.org/{language}{media}/{version}/{filename}
	 */
	static public final String WIKIMEDIA_URL_BASE //
			= "https://dumps.wikimedia.org/" //
					+ "%s" // language
					+ "%s" // media
					+ "/" //
					+ "%s" // version
					+ "/" //
					+ "%s" // filename
	;

	/**
	 * {language}{media}-{version}-pages-articles-multistream.xml.bz2
	 */
	static public final String WIKTIONARY_DUMP_FILENAME_BASE //
			= "%s" // language
					+ "%s" // media
					+ "-" //
					+ "%s" // version
					+ "-pages-articles-multistream.xml.bz2" //
	;

	/**
	 * {language}{media}-{version}-pages-articles-multistream-index.txt.bz2
	 */
	static public final String WIKTIONARY_INDEX_FILENAME_BASE //
			= "%s" // language
					+ "%s" // media
					+ "-" //
					+ "%s" // version
					+ "-pages-articles-multistream-index.txt.bz2" //
	;

	private String language = "en";

	private String media = "wiktionary";

	private String outdir = null;
	private String version = null;

	/**
	 * <pre>
	 * 1. DOWNLOAD MD5 FILE
	 * 2. DOWNLOAD INDEX FILE
	 * 3. CHECK INDEX MD5
	 * 4. DOWNLOAD DUMP FILE
	 * 5. CHECK DUMP MD5
	 * </pre>
	 */
	public List<Document> crawlDocuments() {

		if (this.version == null) {
			logger.info("property is not set: version");
			return null;
		}
		if (this.outdir == null) {
			logger.info("property is not set: outdir");
			return null;
		}

//		System.err.println(System.getProperty("user.home")); // -> C:\Users\oyahi

		File md5File = null;
		File indexFile = null;
		File dumpFile = null;

		FileDownloader downloader = new FileDownloader();

		{ // DOWNLOAD MD5 FILE
			String filename = String.format(MD5_FILENAME, this.language, this.media, this.version);
			String outfilename = this.outdir + "/" + filename;
			md5File = new File(outfilename);
			String url = String.format(WIKIMEDIA_URL_BASE, this.language, this.media, this.version, filename);
			try {
				downloader.download(url, outfilename);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return null;
			}
		}
		{ // DOWNLOAD INDEX FILE
			String filename = String.format(WIKTIONARY_INDEX_FILENAME_BASE, this.language, this.media, this.version);
			String outfilename = this.outdir + "/" + filename;
			indexFile = new File(outfilename);
			String url = String.format(WIKIMEDIA_URL_BASE, this.language, this.media, this.version, filename);
			try {
				downloader.download(url, outfilename);
				// CHECK INDEX MD5
				logger.info("Checking MD5: " + indexFile.getAbsolutePath());
				boolean md5check = MediaWikiMD5Util.checkMD5(md5File, indexFile);
				if (md5check == false) {
					throw new IOException("Invalid MD5: " + indexFile.getAbsolutePath());
				}
				logger.info("Checking MD5: done");
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return null;
			}

		}
		{ // DOWNLOAD DUMP FILE
			String filename = String.format(WIKTIONARY_DUMP_FILENAME_BASE, this.language, this.media, this.version);
			String outfilename = this.outdir + "/" + filename;
			dumpFile = new File(outfilename);
			String url = String.format(WIKIMEDIA_URL_BASE, this.language, this.media, this.version, filename);
			try {
				downloader.download(url, outfilename);
				logger.info("Checking MD5: " + dumpFile.getAbsolutePath());
				// CHECK DUMP MD5
				boolean md5check = MediaWikiMD5Util.checkMD5(md5File, dumpFile);
				if (md5check == false) {
					throw new IOException("Invalid MD5: " + dumpFile.getAbsolutePath());
				}
				logger.info("Checking MD5: done");
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return null;
			}
		}

		return null;
	}

	/**
	 * @param key   (version|outdir|language|media)
	 * @param value (version like 20200401) (outdir like /usr/local/data/wiki)
	 *              (language like ja) (media (wikipedia|wiki)
	 */
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if ("version".equals(key)) {
			this.version = value;
		} //
		else if ("outdir".equals(key)) {
			this.outdir = value;
		} //
		else if ("language".equals(key)) {
			this.language = value;
		} //
		else if ("media".equals(key)) {

			if (value == null || (value.equals("wiktionary") == false && value.equals("wiki") == false)) {
				throw new RuntimeException("Invalid value is set: key=" + key + ",value=" + value);
			}

			if (value.equals("wikipedia")) {
				this.media = "wiki";
			} else {
				this.media = value;
			}

		}
	}

}
