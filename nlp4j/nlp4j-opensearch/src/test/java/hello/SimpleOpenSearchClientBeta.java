package hello;

import java.io.IOException;
import java.util.Date;

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;
import nlp4j.util.DateUtils;

public class SimpleOpenSearchClientBeta {

	public void post() throws IOException {

	}

	public static void main(String[] args) throws Exception {

		String endPoint = "https://localhost:9200/";
		String index = "myindex1";

		try (HttpClient c = (new HttpClientBuilder()).user("admin").password("StrongPassword1234##").protocol("https")
				.hostname("localhost").port(9200).build();) {

			{ // Index Document
				String jsonBody = "{\"item1_s\":[\"aaa\",\"bbb\",\"ccc\"],\"text_txt_ja\":\"今日はいい天気です" + ""
						+ DateUtils.toISO8601(new Date()) + "\",\"vector2\":[0.5,0.5]}";
				String id = "1";
				String url = endPoint + index + "/_doc/" + id;
				System.err.println(url);
				NlpServiceResponse res = c.post(url, jsonBody);
				System.err.println(res.getOriginalResponseBody());
			}
			System.err.println("-----");
			{ // Analyze
				String jsonBody = "{\"text\":\"今日はいい天気です" + "" + DateUtils.toISO8601(new Date()) + "\"}";

				String url = endPoint + index + "/_analyze/";
				System.err.println(url);
				NlpServiceResponse res = c.get(url, jsonBody);
				System.err.println(res.getOriginalResponseBody());
			}
			System.err.println("-----");
			{ // Search
				String jsonBody = "{\r\n" //
						+ "  \"query\" : {\r\n" //
						+ "    \"term\": { \"text_txt_ja\": \"天\" }\r\n" //
						+ "  }\r\n" //
						+ "}";

				String url = endPoint + index + "/_search/";
				System.err.println(url);
				NlpServiceResponse res = c.get(url, jsonBody);
				System.err.println(res.getOriginalResponseBody());
			}
			System.err.println("-----");
			{ // Search2
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

				String url = endPoint + index + "/_search/";
				System.err.println(url);
				NlpServiceResponse res = c.get(url, jsonBody);
				System.err.println(res.getOriginalResponseBody());
			}
		}
	}

}
