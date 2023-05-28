package nlp4j.http;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class FileDownloader {

	static public void download(String url) throws IOException {
		try {
			new URL(url);
		} catch (MalformedURLException e) {
			throw new IOException("Invalid URL: " + url);
		}
		String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());

		if (fileName == null || fileName.isEmpty()) {
			throw new IOException("file name is empty");
		}

		File outFile = new File(fileName);

		boolean overwrite = false;

		download(url, outFile, overwrite);
	}

	@SuppressWarnings("deprecation")
	static public void download(String url, File outFile, boolean overwrite) throws IOException {

		if (overwrite == false && outFile.exists()) {
			throw new IOException("File already exists: " + outFile.getAbsolutePath());
		}

		try ( //
				final CloseableHttpClient client = HttpClients.createDefault();
				final CloseableHttpResponse response = client.execute(new HttpGet(url))) {
			final int status = response.getCode();
			if (status >= 200 && status < 300) {
				final HttpEntity entity = response.getEntity();
				Files.write( //
						outFile.toPath(), //
						entity == null ? new byte[0] : EntityUtils.toByteArray(entity));
				System.err.println("Downloaded: " + outFile.getAbsolutePath());
			} //
			else {
				throw new IOException("Unexpected response status: " + status);
			}
		}

	}

}
