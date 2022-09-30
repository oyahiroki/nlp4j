package nlp4j.webcrawler.mlit;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.JsonLineSeparatedCrawler;
import nlp4j.importer.CsvOutImporter;

/**
 * <pre>
 * JSONファイルを年別のCSVに変換する
 * created_at: 2022-09-30
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class P301MlitJsonToCsvConversionForYEAR {

	/**
	 * <pre>
	 * JSONファイルを年別のCSVに変換する
	 * args[0]: JSON FILE DIR
	 * args[1]: YEAR IN YYYY
	 * </pre>
	 * 
	 * @param args args[0] JSON FILE DIR
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.err.println("args[0]: JSON FILE DIR");
			System.err.println("args[1]: YEAR IN YYYY");
			return;
		}

//		String dir = "C:\\Users\\oyahi\\git\\nlp4j-data\\data-mlit";
		String dir = args[0];

//		String year = "2022";
		String year = args[1];

		File dirFile = new File(dir);

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (//
				name.startsWith("mlit_carinfo-" + year) //
						&& name.endsWith("_json.txt")) {
					return true;
				} else {
					return false;
				}
			}
		};

		System.err.println(System.getProperty("java.io.tmpdir"));

//		File outDir = new File(System.getProperty("java.io.tmpdir"));
		File outDir = new File("R:/");

		List<Document> docsAll = new ArrayList<>();

		for (File f : dirFile.listFiles(filter)) {
			System.err.println(f.getName());

			String fileName2 = f.getName().replace("_json.txt", "");

			System.err.println(fileName2);

			{
				JsonLineSeparatedCrawler crawler = new JsonLineSeparatedCrawler();
				crawler.setProperty("file", f.getAbsolutePath());
				List<Document> docs = crawler.crawlDocuments();
				docsAll.addAll(docs);

			}

		}

		CsvOutImporter importer = new CsvOutImporter();

		String outBaseName = "mlit_carinfo-" + year + "_all";

		File tempFile = new File(outDir, outBaseName + "_utf8.csv");

		if (tempFile.exists()) {
			System.err.println("Already exists: " + tempFile.getAbsolutePath());
			return;
		}

		{
			importer.setProperty("file", tempFile.getAbsolutePath());
			importer.setProperty("encoding", "UTF-8");
			importer.importDocuments(docsAll);
			importer.commit();
			importer.close();
		}

	}

}
