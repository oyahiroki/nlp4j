package nlp4j.webcrawler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.util.HttpClient;

/**
 * Data downloader
 * 
 * @author Hiroki Oya
 * @created_at 2021-04-30
 * @since 1.3.1.0
 */
public class FileDownloader {

	private static final int DOWNLOAD_BUFF_SIZE = 1024;
	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * @param url     for fetch data
	 * @param outFile outputFile
	 * @throws IOException on HTTP Response code not 200
	 */
	public void download(String url, File outFile) throws IOException {

		if (outFile.canWrite() == false) {
			logger.warn("Already exists: " + outFile.getAbsolutePath());
			throw new IOException("Already exists: " + outFile.getAbsolutePath());
		}

		HttpClient client = new HttpClient();

		logger.info("accessing: " + url);

		try (InputStream is = client.getInputStream(url, null)) {

			// INPUT
			BufferedInputStream bis = new BufferedInputStream(is);
			// OUTPUT
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));

			int readSize;
			int totalSize = 0;
			long contentLength = client.getContentLength();

			byte[] buff = new byte[DOWNLOAD_BUFF_SIZE];

			int pctInt0 = 0;

			while ((readSize = bis.read(buff)) != -1) {
				bos.write(buff, 0, readSize);
				totalSize += readSize;
				if (contentLength != -1) {
					double pct = Math.floor(((double) totalSize / (double) contentLength) * 100 * 100) / 100;
					int pctInt = (int) pct;
					if (pctInt != pctInt0) {
						logger.info("Download status (%): " + pct);
						System.err.println("Download status (%): " + pct);
						pctInt0 = pctInt;
					}
				} //
				else {
					logger.info("Download status (Bytes): " + totalSize);
				}
			}

			bis.close();
			bos.close();

		}

	}

}
