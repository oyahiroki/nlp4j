package nlp4j.webcrawler.caa;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.crawler.Crawler;
import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.util.HttpClient;
import nlp4j.webcrawler.AbstractWebCrawler;

public class CaaRecallDownloader extends AbstractWebCrawler implements Crawler {

	private static final int SLEEP_MS = 1000;

	static public final String endPoint = "https://www.recall.caa.go.jp/result/detail.php";

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static public String getUrl(int id) {
		String rcl = toUrlParam(id);
		String url = endPoint + "?rcl=" + rcl;
		return url;
	}

	static public String toOutFilename(int id) {
		return String.format("out_%011d.html", id);
	}

	static public String toUrlParam(int id) {
		return String.format("%011d", id);
	}

	private int begin = 0;

	private File dir = null;

	private int end = -1;

	@Override
	public List<Document> crawlDocuments() {

		boolean skipIfExist = true;

		for (int id = begin; id <= end; id++) {

			String fileName = toOutFilename(id);

			String dir2 = CaaFileUtil.getFolderName(id);

			File dir2File = new File(dir, dir2);

			File outFile = new File(dir2File, fileName);

			String url = getUrl(id);

			if (skipIfExist && outFile.exists() == true) {
				logger.info("Skip: " + outFile.getAbsolutePath());
				continue;
			}

			try {
				logger.info(String.format("sleep: %d ms", SLEEP_MS));
				Thread.sleep(SLEEP_MS);
			} //
			catch (InterruptedException e1) {
				throw new RuntimeException(e1);
			}

			HttpClient client = new HttpClient();

			try {

				logger.info("accessing: " + url);

				DefaultNlpServiceResponse res = client.get(url);

				logger.info("response: " + res.getResponseCode());

				if (res.getResponseCode() == 200) {
					String responseBody = res.getOriginalResponseBody();
					FileUtils.write(outFile, responseBody, "UTF-8", false);
				} //
				else {
					String responseBody = "";
					FileUtils.write(outFile, responseBody, "UTF-8", false);
				}

				logger.info("out: " + outFile.getAbsolutePath());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if ("dir".equals(key)) {
			File dir = new File(value);
			try {
				FileUtils.forceMkdir(dir);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.dir = dir;
		} //
		else if ("begin".equals(key)) {
			this.begin = Integer.parseInt(value);
		} //
		else if ("end".equals(key)) {
			this.end = Integer.parseInt(value);
		}
	}

}
