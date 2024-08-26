package nlp4j.opensearch;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.NlpServiceResponse;

public class SimpleOpenSearchClientTestCase extends TestCase {

	public void testSimpleOpenSearchClient() throws Exception {
		String password = System.getProperty("OPENSEARCH_PASSWORD");
		if (password == null) {
			System.err.println("password is not set.");
			return;
		}
		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient("https://localhost:9200", "admin", password,
				"myindex1");) {

			{
				String method = "_search";
				String jsonBody = "{\r\n" //
						+ "  \"query\" : {\r\n" //
						+ "    \"term\": { \"text_txt_ja\": \"天気\" }\r\n" //
						+ "  }\r\n" //
						+ "}";
				JsonObject jo = (new Gson()).fromJson(jsonBody, JsonObject.class);
				NlpServiceResponse res = client.get(method, jo);
				System.err.println(res.getOriginalResponseBody());
			}
			System.err.println("-----");
			{
				String field = "text_txt_ja";
				String value = "天気";
				NlpServiceResponse res = client.search(field, value);
				System.err.println(res.getOriginalResponseBody());
				{
					JsonObject jo = (new Gson()).fromJson(res.getOriginalResponseBody(), JsonObject.class);
					int c = jo.get("hits").getAsJsonObject().get("hits").getAsJsonArray().size();
					if (c == 0) {
						System.err.println("Not Found");
					} else {
						System.err.println("Found");
					}

				}
			}
			System.err.println("-----");
			{
				String method = "_search";
				String jsonBody = "{\r\n" //
						+ "  \"query\": {\r\n" //
						+ "    \"script_score\": {\r\n" //
						+ "      \"query\": {\r\n" //
						+ "        \"match_all\": {}\r\n" //
						+ "      },\r\n" //
						+ "      \"script\": {\r\n" //
						+ "        \"source\": \"cosineSimilarity(params.query_vector, doc['vector2']) + 1.0\",\r\n" //
						+ "        \"params\": {\r\n" //
						+ "          \"query_vector\": [0.0, 1.0]\r\n" //
						+ "        }\r\n" //
						+ "      }\r\n" //
						+ "    }\r\n" //
						+ "  }\r\n" //
						+ "}";
				JsonObject jo = (new Gson()).fromJson(jsonBody, JsonObject.class);
				NlpServiceResponse res = client.get(method, jo);
				System.err.println(res.getOriginalResponseBody());
			}
			System.err.println("-----");
			{
				String method = "_search";
//				String jsonBody = "{" //
//						+ "  \"query\": {" //
//						+ "    \"script_score\": {" //
//						+ "      \"query\": {" //
//						+ "        \"match_all\": {}" //
//						+ "      }," //
//						+ "      \"script\": {" //
//						+ "        \"source\": \"cosineSimilarity(params.query_vector, doc['vector2']) + 1.0\"," //
//						+ "        \"params\": {" //
//						+ "          \"query_vector\": [0.0, 1.0]" //
//						+ "        }" //
//						+ "      }" //
//						+ "    }" //
//						+ "  }" //
//						+ "}";
//				JsonObject jo = (new Gson()).fromJson(jsonBody, JsonObject.class);

				String vector_target = "vector2";
				float[] v = { 0.0f, 1.0f };

				JsonObject q = new JsonObject();
				{
					JsonObject query = new JsonObject();
					{
						JsonObject script_score = new JsonObject();
						{
							JsonObject _query = (new Gson()).fromJson("{'match_all':{}}", JsonObject.class);
							script_score.add("query", _query);
							query.add("script_score", script_score);
							////
							JsonObject script = new JsonObject();
							{
								script.addProperty("source",
										"cosineSimilarity(params.query_vector, doc['" + vector_target + "']) + 1.0");
								{
									JsonObject params = new JsonObject();
									{
										JsonArray query_vector = new JsonArray();
										{
											for (int n = 0; n < v.length; n++) {
												query_vector.add(v[n]);
											}
//											query_vector.add(0.0f);
//											query_vector.add(1.0f);
											params.add("query_vector", query_vector);
										}
										script.add("params", params);
									}
								}
								script_score.add("script", script);
							}
						}
					}
				}

				NlpServiceResponse res = client.get(method, q);
				System.err.println(res.getOriginalResponseBody());
			}

		}
	}

}
