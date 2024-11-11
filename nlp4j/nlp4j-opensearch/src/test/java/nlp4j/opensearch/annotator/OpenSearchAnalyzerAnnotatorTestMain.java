package nlp4j.opensearch.annotator;

import nlp4j.Document;
import nlp4j.DocumentBuilder;

public class OpenSearchAnalyzerAnnotatorTestMain {

	public static void main(String[] args) throws Exception {
		Document doc = (new DocumentBuilder()).text("今日はいい天気です").build();

		OpenSearchAnalyzerAnnotator ann = new OpenSearchAnalyzerAnnotator();
		ann.setProperty("index", "myindex1");
		ann.setProperty("target", "text");

		ann.annotate(doc);

		doc.getKeywords().stream().forEach(kwd -> {
			System.err.println(kwd);
		});

	}

}
