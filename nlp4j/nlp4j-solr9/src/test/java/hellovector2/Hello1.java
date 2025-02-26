package hellovector2;

import nlp4j.llm.embeddings.LlmClient;
import nlp4j.util.FloatUtils;

public class Hello1 {

	public static void main(String[] args) throws Exception {
		String text = "今日はとてもいい天気です。";
		String endPoint = "http://localhost:8888/";
		try (LlmClient nlp = new LlmClient(endPoint);) {
			float[] ff = nlp.embedding(text);
			System.err.println(FloatUtils.toString(ff));
		}

	}

}
