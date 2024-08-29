package hello.opensearch;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.NlpServiceResponse;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.opensearch.SimpleOpenSearchClient;
import nlp4j.util.JsonUtils;

public class Hello003_EmbeddingSearch {
	static private String password;

	static {
		password = System.getProperty("OPENSEARCH_PASSWORD");
		if (password == null) {
			System.err.println("password is not set.");

			password = System.getenv("OPENSEARCH_PASSWORD");

		}
	}

	static public void main(String[] args) throws Exception {

		String text = "私はクロックが好きです";
		float[] v = EmbeddingAnnotator.embedding(text);

		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient("https://localhost:9200", "admin", password,
				"myindex1");) {
			System.err.println("-----");

			System.err.println("-----");
			{

				String vector_field = "vector1024";
//				float[] v = { 0.0f, 1.0f };

				NlpServiceResponse res = client.vector_search(vector_field, v);
//				System.out.println(res.getOriginalResponseBody());
				JsonObject jo = res.getAsJsonObject();
				removeVector(jo);

				JsonArray hits = jo.get("hits").getAsJsonObject().get("hits").getAsJsonArray();
				for (int n = 0; n < hits.size(); n++) {
					JsonObject hit = hits.get(n).getAsJsonObject();
					System.err.println( //
							"score: " + hit.get("_score").getAsNumber() + " , " //
									+ "id: " + hit.get("_id").getAsString() + " , " //
									+ "text: " + hit.get("_source").getAsJsonObject().get("text_txt_ja").getAsString() //
					);
				}

			}
		}
	}

	private static void removeVector(JsonObject jo) {
		JsonArray hits = jo.get("hits").getAsJsonObject().get("hits").getAsJsonArray();
		for (int n = 0; n < hits.size(); n++) {
			JsonObject hit = hits.get(n).getAsJsonObject();

			hit.get("_source").getAsJsonObject().remove("vector1024");
			hit.get("_source").getAsJsonObject().addProperty("vector1024", "@@removed");
		}
		System.err.println(JsonUtils.prettyPrint(jo));

	}

}
