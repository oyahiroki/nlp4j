package nlp4j.deeplearnig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;

public class Main1 {

	/**
	 * 動的に形態素解析処理を行う
	 * 
	 * @throws Exception 例外発生時
	 */
	public static void main(String[] args) throws Exception {

//		String fileName = "src/test/resources/file/neko_short_utf8.txt";
		String fileName = "src/test/resources/file/neko.txt";

		File inFile = new File(fileName);

		System.err.println("debug: inFile: " + inFile.getAbsolutePath());

		List<String> lines = FileUtils.readLines(inFile, "UTF-8");

		ArrayList<Document> docs = new ArrayList<Document>();

		for (String line : lines) {
			if (line.length() > 8) {
				Document doc = new DefaultDocument();
				doc.setText(line.replace("\u3000", " ").trim());
				docs.add(doc);
			} else {

			}
		}

		for (Document doc : docs) {
			System.err.println(doc);
		}

		Word2VecIndexer indexer = new Word2VecIndexer();
		indexer.setProperty("outfile", "file/model-wordvectors.txt");

		indexer.addDocuments(docs);

		indexer.commit();

	}

}
