package example202211;

import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.TextFileLineSeparatedCrawler;
import nlp4j.word2vec.Word2VecOutImporter;

public class HelloWord2VecJa2 {

	public static void main(String[] args) throws Exception {

		List<Document> docs = null;
		{
			String fileName = "files/example_ja/out/mlit_carinfo-202112_json_word_splitted.txt";
			TextFileLineSeparatedCrawler crawler = new TextFileLineSeparatedCrawler();
			crawler.setProperty("file", fileName);
			docs = crawler.crawlDocuments();
		}

		{
			Word2VecOutImporter out = new Word2VecOutImporter();
			out.setProperty("outfile", "files/example_ja/out/model.txt");
			out.importDocuments(docs);
			out.commit();
			out.close();
		}

	}

}
