package nlp4j.wiki;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.crawler.Crawler;
import nlp4j.webcrawler.AbstractWebCrawler;
import nlp4j.webcrawler.FileDownloader;

public class WiktionaryJaDownloader extends AbstractWebCrawler implements Crawler {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static private final String filename1 = "jawiki-%s-pages-articles-multistream-index.txt.bz2";
	static private final String url1 = "https://dumps.wikimedia.org/jawiki/%s" + "/" + filename1;

	static private final String filename2 = "jawiki-%s-pages-articles-multistream.xml.bz2";
	static private final String url2 = "https://dumps.wikimedia.org/jawiki/%s" + "/" + filename2;

	private String version;
	private String outdir;

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

		System.err.println(System.getProperty("user.home"));

		FileDownloader downloader = new FileDownloader();

		{
			String url = String.format(url1, this.version, this.version);
			String filename = this.outdir + "/" + String.format(filename1, this.version);
			try {
				downloader.download(url, filename);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return null;
			}
		}
		{
			String url = String.format(url2, this.version, this.version);
			String filename = this.outdir + "/" + String.format(filename2, this.version);
			try {
				downloader.download(url, filename);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return null;
			}
		}

		return null;
	}

	@Override
	public void setProperty(String key, String value) {
		// TODO Auto-generated method stub
		super.setProperty(key, value);
		if ("version".equals(key)) {
			this.version = value;
		} //
		else if ("outdir".equals(key)) {
			this.outdir = value;
		}
	}

	static public void main(String[] args) throws Exception {
		WiktionaryJaDownloader wiktionaryDownloader = new WiktionaryJaDownloader();
		wiktionaryDownloader.setProperty("version", "20220201");
		wiktionaryDownloader.setProperty("outdir", "C:\\usr\\local\\data\\wiki");
		wiktionaryDownloader.crawlDocuments();
	}

}
