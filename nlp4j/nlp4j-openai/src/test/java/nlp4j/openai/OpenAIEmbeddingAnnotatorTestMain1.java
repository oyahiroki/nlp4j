package nlp4j.openai;

import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.annotator.AnnotatorBuilder;

public class OpenAIEmbeddingAnnotatorTestMain1 {

	public static void main(String[] args) throws Exception {
		String text = "今日はいい天気です。";

		Document doc = (new DocumentBuilder()).text(text).build();

		OpenAIEmbeddingAnnotator ann = (new AnnotatorBuilder<OpenAIEmbeddingAnnotator>(new OpenAIEmbeddingAnnotator()))
				.build();

		ann.annotate(doc);

	}

}
