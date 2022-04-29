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

public class MediaWikiDownloader extends AbstractWebCrawler implements Crawler {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private String language = "en";

	private String media = "wiktionary";

	static private final String WIKTIONARY_INDEX_FILENAME_BASE //
			= "%s" // language
					+ "%s" // media
					+ "-" //
					+ "%s" // version
					+ "-pages-articles-multistream-index.txt.bz2";

	static private final String WIKIMEDIA_URL_BASE //
			= "https://dumps.wikimedia.org/" //
					+ "%s" // language
					+ "%s" // media
					+ "/" //
					+ "%s" // version
					+ "/" //
					+ "%s" // filename
	;
	static private final String WIKTIONARY_DUMP_FILENAME_BASE //
			= "%s" // language
					+ "%s" // media
					+ "-" //
					+ "%s" // version
					+ "-pages-articles-multistream.xml.bz2";

	static private final String MD5_FILENAME //
			= "%s" // language
					+ "%s" // media
					+ "-" //
					+ "%s"// version
					+ "-md5sums.txt";

	private String version = null;
	private String outdir = null;

	@Override
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

		{
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
		{
			String filename = String.format(WIKTIONARY_INDEX_FILENAME_BASE, this.language, this.media, this.version);
			String outfilename = this.outdir + "/" + filename;
			indexFile = new File(outfilename);
			String url = String.format(WIKIMEDIA_URL_BASE, this.language, this.media, this.version, filename);
			try {
				downloader.download(url, outfilename);
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
		{
			String filename = String.format(WIKTIONARY_DUMP_FILENAME_BASE, this.language, this.media, this.version);
			String outfilename = this.outdir + "/" + filename;
			dumpFile = new File(outfilename);
			String url = String.format(WIKIMEDIA_URL_BASE, this.language, this.media, this.version, filename);
			try {
				downloader.download(url, outfilename);
				logger.info("Checking MD5: " + dumpFile.getAbsolutePath());
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

			if (value.equals("wikipedia")) {
				this.media = "wiki";
			} else {
				this.media = value;
			}

		}
	}

}
