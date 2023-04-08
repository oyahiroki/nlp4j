package nlp4j.webcrawler.mlit;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.JsonLineSeparatedCrawler;
import nlp4j.importer.CsvOutImporter;
import nlp4j.util.DocumentUtil;

public class P201MlitJsonToCsvConversion {

	/**
	 * <pre>
	 * MLITのJSON形式のデータをCSVに変換する
	 * args[0]: JSONファイルのディレクトリ
	 * </pre>
	 * 
	 * @param args args[0]:JSON FILE DIR
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		if (args.length != 1) {
			System.err.println("args[0]: JSON FILE DIR");
			return;
		}

//		String dir = "C:\\Users\\oyahi\\git\\nlp4j-data\\data-mlit";
		String dir = args[0];

		File dirFile = new File(dir);

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith("_json.txt")) {
					return true;
				} else {
					return false;
				}
			}
		};

		System.err.println(System.getProperty("java.io.tmpdir"));

		File outDir = new File(System.getProperty("java.io.tmpdir"));
//		File outDir = new File("R:/");

		// FOR_EACH JSON FILE
		for (File f : dirFile.listFiles(filter)) {
			System.err.println(f.getName());
			String fileName2 = f.getName().replace("_json.txt", "");
			System.err.println(fileName2);
			{ // CONVERT TO CSV FILE
				JsonLineSeparatedCrawler crawler = new JsonLineSeparatedCrawler();
				crawler.setProperty("file", f.getAbsolutePath());
				List<Document> docs = crawler.crawlDocuments();
//				for (Document doc : docs) {
//					System.err.println(DocumentUtil.toPrettyJsonString(doc));
//				}
				CsvOutImporter importer = new CsvOutImporter();
				File tempFile = new File(outDir, fileName2 + "_utf8.csv");
				if (tempFile.exists()) {
					System.err.println("Already exists: " + tempFile.getAbsolutePath());
					continue;
				}
				{
					importer.setProperty("file", tempFile.getAbsolutePath());
					importer.setProperty("encoding", "UTF-8");
					importer.importDocuments(docs);
					importer.commit();
					importer.close();
				}
			} // END_OF( CONVERT TO CSV FILE)
		} // END OF (FOR_EACH JSON FILE)

	}

}
