package nlp4j.webcrawler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.http.HttpClientBuilder;

/**
 * <pre>
 * Data downloader
 * </pre>
 * 
 * created on 2021-04-30
 * 
 * @author Hiroki Oya
 * @since 1.0.0.0
 */
public class FileDownloader {

	private static final int DOWNLOAD_BUFF_SIZE = 1024 * 1024 * 10;
	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * @param url     to fetch data
	 * @param outFile outputFile
	 * @throws IOException on HTTP Response code not 200
	 */
	public void download(String url, File outFile) throws IOException {
		download(url, outFile, false);
	}

	/**
	 * @param url       to fetch data
	 * @param outFile   outputFile
	 * @param overwrite file
	 * @throws IOException on HTTP Response code not 200
	 */
	public void download(String url, File outFile, boolean overwrite) throws IOException {

		// OUTPUT
		if (outFile.getParentFile().exists() == false) {
			File parentDir = FileUtils.createParentDirectories(outFile);
			if (parentDir != null) {
				logger.info("Created: " + parentDir.getAbsolutePath());
			}
		}

		// IF(overwirte == FALSE) THEN
		if (overwrite == false) {
			if (outFile.exists() == true) {
				logger.info("Already exists: " + outFile.getAbsolutePath());
//				throw new IOException("Already exists: " + outFile.getAbsolutePath());
				return;
			}
		} //
			// ELSE(overwirte == TRUE)
		else {
			if (outFile.exists() == true) {
				logger.info("Already exists: " + outFile.getAbsolutePath());
				outFile.delete();
				logger.info("Deleted: " + outFile.getAbsolutePath());
			}

		}

		logger.info("accessing: " + url);

		try ( //
				nlp4j.http.HttpClient client = (new HttpClientBuilder()).build(); //
				InputStream is = client.getInputStream(url, null); //
				// INPUT
				BufferedInputStream bis = new BufferedInputStream(is); //
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile)); //
		) {

			// OUTPUT
			if (outFile.getParentFile().exists() == false) {
				File parentDir = FileUtils.createParentDirectories(outFile);
				if (parentDir != null) {
					logger.info("Created: " + parentDir.getAbsolutePath());
				}
			}

			int readSize;
			long totalSize = 0; // fixed int -> long 1.1.1.0
			long contentLength = client.getContentLength();

			byte[] buff = new byte[DOWNLOAD_BUFF_SIZE];

			int pctInt0 = 0;

			long totalSize0 = 0; // fixed int -> long 1.1.1.0

			int diff = 1024 * 1024;

			while ((readSize = bis.read(buff)) != -1) {

				bos.write(buff, 0, readSize);
				totalSize += readSize;

				if (contentLength != -1) {
					double pct = Math.floor(((double) totalSize / (double) contentLength) * 100 * 100) / 100;
					int pctInt = (int) pct;
					if (pctInt != pctInt0) {
						logger.info("Download status (%): " + pct);
//						System.err.println("Download status (%): " + pct);
						pctInt0 = pctInt;
					}
				} //
				else {
					if ((totalSize - totalSize0) > diff) {
						totalSize0 = totalSize;
						logger.info("Download status (Bytes): " + String.format("%,d", totalSize));
					}
				}
			}

//			bis.close();
			bos.flush();
//			bos.close();

		}

	}

	/**
	 * @param url      to fetch data
	 * @param fileName output file
	 * @throws IOException on HTTP Response code not 200
	 */
	public void download(String url, String fileName) throws IOException {
		download(url, new File(fileName), false);
	}

	/**
	 * @param url       to fetch data
	 * @param fileName  output file
	 * @param overwrite file
	 * @throws IOException on HTTP Response code not 200
	 */
	public void download(String url, String fileName, boolean overwrite) throws IOException {
		download(url, new File(fileName), overwrite);
	}

}
