package nlp4j.opensearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.Keyword;
import nlp4j.KeywordBuilder;
import nlp4j.NlpServiceResponse;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.opensearch.importer.OpenSearchDocumentImporter;
import nlp4j.util.DateUtils;
import nlp4j.util.DocumentUtil;
import nlp4j.util.DoubleUtils;

public class SimpleOpenSearchClientTestCase extends TestCase {

	static private String password;

	static {
		password = System.getProperty("OPENSEARCH_PASSWORD");
		if (password == null) {
			System.err.println("password is not set.");

			password = System.getenv("OPENSEARCH_PASSWORD");

		}
	}

	public void testSearch000() throws Exception {
		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient("https://localhost:9200", "admin", password,
				"myindex1");) {
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
	}

	public void testSearch001() throws Exception {
		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient("https://localhost:9200", "admin", password,
				"myindex1");) {
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
	}

	public void vectorSearch000() throws Exception {
		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient("https://localhost:9200", "admin", password,
				"myindex1");) {
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
		}
	}

	public void vectorSearch001() throws Exception {

		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient("https://localhost:9200", "admin", password,
				"myindex1");) {
			System.err.println("-----");

			System.err.println("-----");
			{
				String method = "_search";

				String vector_field = "vector2";
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
										"cosineSimilarity(params.query_vector, doc['" + vector_field + "']) + 1.0");
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

	public void vectorSearch002() throws Exception {

		String text = "私は時計が好きです";
		float[] v = EmbeddingAnnotator.embedding(text);

		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient("https://localhost:9200", "admin", password,
				"myindex1");) {
			System.err.println("-----");

			System.err.println("-----");
			{

				String vector_field = "vector1024";
//				float[] v = { 0.0f, 1.0f };

				NlpServiceResponse res = client.vector_search(vector_field, v);
				System.out.println(res.getOriginalResponseBody());
				JsonObject jo = res.getAsJsonObject();

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

	public void testAnalyze001() throws Exception {
		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient("https://localhost:9200", "admin", password,
				"myindex1");) {
			String text = "今日は走って学校に行きました。";

			NlpServiceResponse res = client.analyze(text);
			System.err.println(res.getOriginalResponseBody());
			JsonObject jo = res.getAsJsonObject();
			JsonArray tokens = jo.getAsJsonArray("tokens").getAsJsonArray();
			for (int n = 0; n < tokens.size(); n++) {
				JsonObject token = tokens.get(n).getAsJsonObject();
				int start = token.get("start_offset").getAsInt();
				int end = token.get("end_offset").getAsInt();
				String t = token.get("token").getAsString();
				String t2 = text.substring(start, end);
				String type = token.get("type").getAsString();
				Keyword kwd = (new KeywordBuilder()).begin(start).end(end).facet(type).str(t2).lex(t).build();
				System.err.println(kwd);
			}
		}

	}

	public void testPostDocument001() throws Exception {
		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient("https://localhost:9200", "admin", password,
				"myindex1");) {
			System.err.println("-----");
			boolean debug = true;
			{
				JsonObject doc = new JsonObject();
				{
					{
						String id = "doc_" + DateUtils.get_yyyyMMdd_HHmmss();
						doc.addProperty("id", id);
					}
					{
						JsonArray items = new JsonArray();
						items.add("aaa");
						items.add("bbb");
						items.add("ccc");
						doc.add("items", items);
					}
					{
						String text_txt_ja = "今日はいい天気です";
						doc.addProperty("text_txt_ja", text_txt_ja);
					}
					{
						float[] vector2 = { 0.5f, 0.5f };
						JsonArray v = new JsonArray();
						for (int n = 0; n < vector2.length; n++) {
							v.add(vector2[n]);
						}
						doc.add("vector2", v);
					}
				}
				NlpServiceResponse res = client.postDoc(doc);
				System.err.println(res.getOriginalResponseBody());
			}
		}
	}

	public void testImport001() throws Exception {

		String text = "私は時計が好きです。";

		List<Document> docs = new ArrayList<>();
		{
			{
				Document doc = new DefaultDocument();
				{
					doc.putAttribute("id", "doc_" + DateUtils.get_yyyyMMdd_HHmmss());
					doc.putAttribute("text", text);
				}
				docs.add(doc);
			}

			{ // Vector
				DocumentAnnotator ann = new EmbeddingAnnotator();
				ann.setProperty("target", "text");
				ann.setProperty("vector_name", "vector1024");
				ann.setProperty("endPoint", "http://localhost:8888/");
				ann.annotate(docs); // vector size is 1024
			}

//			System.err.println(DocumentUtil.toJsonPrettyString(docs.get(0)));

		}
		{
			try (OpenSearchDocumentImporter imt = new OpenSearchDocumentImporter();) {
				imt.importDocuments(docs);

			}
		}
	}

	public void testSimpleOpenSearchClient() throws Exception {

		boolean debug = true;

		if (debug) {
			return;
		}

		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient("https://localhost:9200", "admin", password,
				"myindex1");) {
			System.err.println("-----");
			{
				JsonObject doc = new JsonObject();
				{
					{
						String id = "doc_" + DateUtils.get_yyyyMMdd_HHmmss();
						doc.addProperty("id", id);
					}
					{
						JsonArray items = new JsonArray();
						items.add("aaa");
						items.add("bbb");
						items.add("ccc");
						doc.add("items", items);
					}
					{
						String text_txt_ja = "今日はいい天気です";
						doc.addProperty("text_txt_ja", text_txt_ja);
					}
					{
						float[] vector2 = { 0.5f, 0.5f };
						JsonArray v = new JsonArray();
						for (int n = 0; n < vector2.length; n++) {
							v.add(vector2[n]);
						}
						doc.add("vector2", v);
					}
				}
				NlpServiceResponse res = client.postDoc(doc);
				System.err.println(res.getOriginalResponseBody());
			}

			System.err.println("-----");
			{
				String method = "_search";

				String vector_field = "vector2";
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
										"cosineSimilarity(params.query_vector, doc['" + vector_field + "']) + 1.0");
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
