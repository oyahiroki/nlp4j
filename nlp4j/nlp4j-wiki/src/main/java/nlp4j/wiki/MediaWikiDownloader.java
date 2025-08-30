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

	static public class Builder {
		private String version;
		private String outdir;
		private String language;
		private String media;

		public Builder() {

		}

		public Builder version(String version) {
			this.version = version;
			return this;
		}

		public Builder outdir(String outdir) {
			this.outdir = outdir;
			return this;
		}

		public Builder language(String language) {
			this.language = language;
			return this;
		}

		public Builder media(MediaWikiDownloader.media media) {
			this.media = media.name();
			return this;
		}

		public Builder media(String media) {
			this.media = media;
			return this;
		}

		public MediaWikiDownloader build() {
			MediaWikiDownloader dl = new MediaWikiDownloader();
			dl.setProperty("version", this.version);
			if (this.outdir != null) {
				dl.setProperty("outdir", this.outdir);
			}
			dl.setProperty("language", this.language);
			dl.setProperty("media", this.media);
			return dl;
		}
	}

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

	static public enum media {
		/**
		 * Wikipedia
		 */
		wiki,
		/**
		 * Wiktionary
		 */
		wiktionary
	}

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
			String filenameMd5 = String.format(MD5_FILENAME, this.language, this.media, this.version);
			String outfilenameMd5 = this.outdir + "/" + filenameMd5;
			md5File = new File(outfilenameMd5);
			String url = getURL_md5();
			System.out.println("Download: " + url);
			System.out.println("File: " + outfilenameMd5);
			try {
				downloader.download(url, outfilenameMd5);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return null;
			}
		}
		{ // DOWNLOAD INDEX FILE
			String filenameIndex = String.format(WIKTIONARY_INDEX_FILENAME_BASE, this.language, this.media,
					this.version);
			String outfilenameIndex = this.outdir + "/" + filenameIndex;
			indexFile = new File(outfilenameIndex);
			String url = getURL_dumpIndex();
			System.out.println("Download: " + url);
			System.out.println("File: " + outfilenameIndex);
			try {
				downloader.download(url, outfilenameIndex);
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
			String filenameDump = String.format(WIKTIONARY_DUMP_FILENAME_BASE, this.language, this.media, this.version);
			String outfilenameDump = this.outdir + "/" + filenameDump;
			dumpFile = new File(outfilenameDump);
			String url = getURL_dumpData();
			System.out.println("Download: " + url);
			System.out.println("File: " + outfilenameDump);
			try {
				downloader.download(url, outfilenameDump);
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

	public String getURL_dumpData() {
		return getDumpURL(this.language, this.media, this.version);
	}

	static public String getDumpFileName(String language, String media, String version) {
		return String.format(WIKTIONARY_DUMP_FILENAME_BASE, language, media, version);
	}

	static public String getDumpURL(String language, String media, String version) {
		String fileName = getDumpFileName(language, media, version);
		String url = String.format(WIKIMEDIA_URL_BASE, language, media, version, fileName);
		return url;
	}

	public String getURL_dumpIndex() {
		String filename = String.format(WIKTIONARY_INDEX_FILENAME_BASE, this.language, this.media, this.version);
		String url = String.format(WIKIMEDIA_URL_BASE, this.language, this.media, this.version, filename);
		return url;
	}

	public String getURL_md5() {
		String filename = String.format(MD5_FILENAME, this.language, this.media, this.version);
		String url = String.format(WIKIMEDIA_URL_BASE, this.language, this.media, this.version, filename);
		return url;
	}

	/**
	 * @param key   (version|outdir|language|media)
	 * @param value (version like 20200401) (outdir like /usr/local/data/wiki)
	 *              (language like ja) (media (wiki|wiktionary)
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

	public void printURL() {
		System.out.println("MD5  : " + this.getURL_md5());
		System.out.println("INDEX: " + this.getURL_dumpIndex());
		System.out.println("DATA : " + this.getURL_dumpData());
	}

}
