package nlp4j.webcrawler.mlit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;
import nlp4j.util.DocumentsUtils;

/**
 * 国土交通省「自動車のリコール・不具合情報」をダウンロードする<br>
 * http://carinf.mlit.go.jp/jidosha/carinf/opn/index.html<br>
 */
public class P101MlitCarInfoDownloadMain {

	private static void checkArgs(String[] args) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		sdf.parse(args[0]);
		sdf.parse(args[1]);

		File file = new File(args[2]);

		if (file.isDirectory() == true) {
			throw new Exception("Is not file: " + args[2]);
		}
		if (file.exists() == true) {
			throw new Exception("Already exists: " + args[2]);
		}
		if (file.getParentFile().canWrite() == false) {
			throw new Exception("Can't write: " + args[2]);
		}
	}

	/**
	 * <pre>
	 * 国土交通省「自動車のリコール・不具合情報」をダウンロードする
	 * http://carinf.mlit.go.jp/jidosha/carinf/opn/index.html<br>
	 * Parameters Example:
	 * args[0] Start Date: 2022/08/01
	 * args[1] End Date  : 2022/08/31
	 * args[2] Out file  : /usr/data/mlit/mlit_carinfo-202208_json.txt
	 * </pre>
	 * 
	 * @param args Start Date, End Date, Out File
	 * @throws Exception ON ERROR
	 */
	public static void main(String[] args) throws Exception {

		if (args == null || args.length != 3) {
			printHowToUse();
			return;
		}

		checkArgs(args);

		String from_date = args[0].trim();
		String to_date = args[1].trim();
		String output_filename = args[2].trim();

		MlitCarInfoCrawler crawler = new MlitCarInfoCrawler();
		{
			String accessKey = crawler.getAccessKey();
			crawler.setProperty("accessKey", accessKey);
			crawler.setProperty("from_date", from_date);
			crawler.setProperty("to_date", to_date);
			// crawler.setProperty("debug", "true");
		}

		List<Document> docs = crawler.crawlDocuments();

		for (Document doc : docs) {
			System.err.println(DocumentUtil.toJsonString(doc));
		}

		File outFile = new File(output_filename);

		if (output_filename.toLowerCase().endsWith(".csv")) {
			DocumentsUtils.printAsCsv(docs, outFile);
		} //
		else {
			DocumentUtil.writeAsLineSeparatedJson(docs, outFile);
		}

	}

	private static void printHowToUse() {
		System.err.println(
				P101MlitCarInfoDownloadMain.class.getCanonicalName() + " {from_date} {to_date} {output_filename}");
		System.err.println("Example: " + P101MlitCarInfoDownloadMain.class.getCanonicalName() //
				+ " 2020/01/01" //
				+ " 2020/03/31" //
				+ " /usr/local/nlp4j/data/mlit/mlit_carinfo-20200101-20200331_json.txt" //
		);
	}

}
