package nlp4j.webcrawler.mlit;

import java.io.File;
import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.JsonLineSeparatedCrawler;
import nlp4j.importer.CsvOutImporter;

public class Main {

	public static void main(String[] args) throws Exception {

		String inFileName = System.getenv("USERPROFILE") + "/git/nlp4j-data/data-mlit/"
				+ "mlit_carinfo-all_20230401_json.txt";
		File inFile = new File(inFileName);

		String outDir = System.getenv("USERPROFILE") + "/git/nlp4j-data/data-mlit/";
		String fileName2 = "mlit_carinfo-all_20230401_utf8.csv";

		{ // CONVERT TO CSV FILE
			JsonLineSeparatedCrawler crawler = new JsonLineSeparatedCrawler();
			crawler.setProperty("file", inFile.getAbsolutePath());

			List<Document> docs = crawler.crawlDocuments();

			CsvOutImporter importer = new CsvOutImporter();
			File tempFile = new File(outDir, fileName2);
			if (tempFile.exists()) {
				System.err.println("Already exists: " + tempFile.getAbsolutePath());
				return;
			}
			{
				importer.setProperty("file", tempFile.getAbsolutePath());
				importer.setProperty("encoding", "UTF-8");
				importer.importDocuments(docs);
				importer.commit();
				importer.close();
			}
		} // END_OF( CONVERT TO CSV FILE)
	}

}
