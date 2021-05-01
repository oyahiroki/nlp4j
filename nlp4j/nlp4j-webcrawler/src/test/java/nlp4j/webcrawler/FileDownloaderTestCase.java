package nlp4j.webcrawler;

import java.io.File;

import nlp4j.test.NLP4JTestCase;

public class FileDownloaderTestCase extends NLP4JTestCase {

	/**
	 * 
	 */
	public FileDownloaderTestCase() {
		super();
		target = FileDownloader.class;
	}

	public void testDownload001() throws Exception {

		String url = "https://nlp4j.org/data/test/sample-data-vga.bmp";
		File tempFile = File.createTempFile("nlp4j_temp", "bin");

		System.err.println(tempFile.getAbsolutePath());

		FileDownloader downloader = new FileDownloader();

		downloader.download(url, tempFile);

	}

}
