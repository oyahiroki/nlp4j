package nlp4j.crawler;

import java.io.IOException;
import java.util.stream.Stream;

import nlp4j.Document;

/**
 * Created on: 2024-09-07
 */
public interface StreamDocumentCrawler {

	/**
	 * ファイルを順次読み込みながら、Document を Stream で返すメソッド
	 * 
	 * @return Stream of Document
	 */
	Stream<Document> streamDocuments() throws IOException;

}