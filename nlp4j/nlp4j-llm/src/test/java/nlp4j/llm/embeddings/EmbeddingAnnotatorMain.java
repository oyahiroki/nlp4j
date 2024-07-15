package nlp4j.llm.embeddings;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;

public class EmbeddingAnnotatorMain {

	static public void main(String[] args) throws Exception {
	
		Document doc = (new DocumentBuilder()).text("今日はいい天気です").build();
	
		DocumentAnnotator ann = new EmbeddingAnnotator();
		ann.setProperty("target", "text");
	
		ann.annotate(doc);
	
		System.err.println(doc.getAttributeAsListNumbers("vector").size());
	
	}

}
