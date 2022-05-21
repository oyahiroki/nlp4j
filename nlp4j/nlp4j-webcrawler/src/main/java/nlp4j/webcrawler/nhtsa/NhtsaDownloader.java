package nlp4j.webcrawler.nhtsa;

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

public class NhtsaDownloader extends AbstractWebCrawler implements Crawler {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static private final String url = "https://www-odi.nhtsa.dot.gov/downloads/folders/Complaints/" //
			+ "FLAT_CMPL.zip";

	private File fileName;

	/**
	 * Download a file from
	 * https://www-odi.nhtsa.dot.gov/downloads/folders/Complaints/FLAT_CMPL.zip
	 */
	public List<Document> crawlDocuments() {

		logger.info(fileName.getAbsolutePath());

		if (fileName == null) {
			logger.warn("File for output is invalid: fileName=" + fileName);
			return null;
		}

		FileDownloader downloader = new FileDownloader();

		try {
			downloader.download(url, fileName);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

		return null;
	}

	/**
	 * @param key (fileName for download)
	 */
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if ("fileName".equals(key)) {
			this.fileName = new File(value);
		}
	}

}
