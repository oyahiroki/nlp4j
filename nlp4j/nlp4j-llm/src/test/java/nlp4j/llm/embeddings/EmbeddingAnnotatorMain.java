package nlp4j.llm.embeddings;

import nlp4j.Document;
import nlp4j.DocumentBuilder;

public class EmbeddingAnnotatorMain {

	static public void main(String[] args) throws Exception {

		EmbeddingAnnotator ann = new EmbeddingAnnotator();
		ann.setProperty("target", "text");

		long time1 = System.currentTimeMillis();

		{
			Document doc = (new DocumentBuilder()).text("今日はいい天気です ").build();
			ann.annotate(doc);
			System.err.println(doc.toString());
			System.err.println(doc.getAttributeAsListNumbers("vector"));
			System.err.println(doc.getAttributeAsFloatArray("vector"));
		}
		long time2 = System.currentTimeMillis();

		System.err.println( //
				"time: " + (time2 - time1) + " ms");

	}

}
