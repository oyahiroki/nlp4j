package nlp4j.webcrawler.nhtsa;

public class NhtsaDownloaderMainTest {

	public static void main(String[] args) throws Exception {
		String[] params = { "-dir", "C:/usr/local/nlp4j/collections/nhtsa/data/zip/" };
		NhtsaDownloaderMain.main(params);
	}

}
