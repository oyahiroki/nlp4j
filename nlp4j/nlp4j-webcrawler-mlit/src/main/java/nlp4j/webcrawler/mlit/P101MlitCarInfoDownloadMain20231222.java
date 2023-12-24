package nlp4j.webcrawler.mlit;

import java.io.File;

public class P101MlitCarInfoDownloadMain20231222 {

	public static void main(String[] args) throws Exception {
		String fromDate = "2023/04/01";
		String toDate = "2023/04/02";
		File outFile = File.createTempFile("nlp4j-mlit-", "_json.txt");
		outFile.delete();
		System.err.println(outFile);
		String[] params = { fromDate, toDate, outFile.getAbsolutePath() };

		P101MlitCarInfoDownloadMain.main(params);
		System.err.println(outFile);

	}

}
