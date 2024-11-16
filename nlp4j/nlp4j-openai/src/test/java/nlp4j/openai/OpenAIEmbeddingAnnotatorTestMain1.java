package nlp4j.openai;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.annotator.AnnotatorBuilder;

public class OpenAIEmbeddingAnnotatorTestMain1 {

	public static void main(String[] args) throws Exception {
		String text = "今日はいい天気です。";

		Document doc = (new DocumentBuilder()).text(text).build();

		DocumentAnnotator ann = (new AnnotatorBuilder(OpenAIEmbeddingAnnotator.class)).build();

		ann.annotate(doc);

		long vector_size = doc.getAttributeAsListNumbers("vector").size();

		System.err.println("vector_size: " + vector_size);

	}

}
