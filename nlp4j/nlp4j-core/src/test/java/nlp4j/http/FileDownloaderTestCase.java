package nlp4j.http;

import java.io.File;

import junit.framework.TestCase;

public class FileDownloaderTestCase extends TestCase {

	public void testDownloadString() {
	}

	public void testDownloadStringFileBoolean() {
	}

	public void testDownloadStringFileBooleanDownloadProgressListener() throws Exception {
		File outFile = File.createTempFile("nlp4j-", ".txt");
		System.err.println(outFile.getAbsolutePath());
		FileDownloader.download( //
				"https://nlp4j.org/", //
				outFile, //
				true, //
				(read, total, percent) -> {
					if (percent >= 0) {
						System.out.printf("Progress: %.0f%%%n", percent); // 1%刻みで表示
					} //
					else {
						System.out.printf("Downloaded: %d bytes%n", read); // Content-Length不明時
					}
				});
	}

}
