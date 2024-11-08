package nlp4j.webcrawler.caa;

import nlp4j.webcrawler.caa.v202302.CaaRecallDownloaderMain;

public class CaaRecallDownloaderMainTest {

	public static void main(String[] args) {

		String[] params = { "-dir", "C:/usr/local/nlp4j/temp", "-begin", "0", "-end", "10" };

		CaaRecallDownloaderMain.main(params);

	}

}
