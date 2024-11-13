package nlp4j.llm.embeddings;

import nlp4j.Document;
import nlp4j.DocumentBuilder;

public class EmbeddingAnnotatorMain {

	static public void main(String[] args) throws Exception {

		int max = 1000;

		EmbeddingAnnotator ann = new EmbeddingAnnotator();
		ann.setProperty("target", "text");

		long time1 = System.currentTimeMillis();

		for (int n = 0; n < max; n++) {
			Document doc = (new DocumentBuilder()).text("今日はいい天気です " + n).build();
			ann.annotate(doc);
//			System.err.println(doc.getAttributeAsListNumbers("vector").size());
		}
		long time2 = System.currentTimeMillis();

		System.err.println("time: " + (time2 - time1) + ", docs: " + max + ",average: "
				+ ((double) (time2 - time1) / (double) max));

	}

}
