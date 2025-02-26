package nlp4j.llm.embeddings;

import java.io.IOException;

import org.apache.hc.client5.http.HttpHostConnectException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;
import nlp4j.util.JsonObjectBuilder;

public class SemanticSearchClient {

	static public double cosineSimilarity(String text1, String text2) throws IOException {

		JsonObject jsonObj = (new JsonObjectBuilder()) //
				.add("text1", text1) //
				.add("text2", text2) //
				.build();

		// client.post throws IOException
		try ( // Http client
				HttpClient client = (new HttpClientBuilder()).build(); //
		) {
			// throws IOException
			NlpServiceResponse res = client.post("http://localhost:8888/semanticsearch", jsonObj.toString());

			JsonObject jo = (new Gson()).fromJson(res.getOriginalResponseBody(), JsonObject.class);

//			System.err.println(res.getOriginalResponseBody());

			double d = jo.get("cosine_similarity").getAsDouble();
//			System.err.println(d);

			return d;

//			Document doc = parser.parseResponse(res.getOriginalResponseBody());

		} catch (IOException e) {
			if (e instanceof HttpHostConnectException) {
				throw new IOException("Server is down or not started", e);
			} else {
				throw e;
			}
		}
	}

	public static void main(String[] args) throws IOException {

		System.out.println(SemanticSearchClient.cosineSimilarity("これはテストです", "これは試験です"));
		System.out.println(SemanticSearchClient.cosineSimilarity("これはテストです", "これはTESTです"));
		System.out.println(SemanticSearchClient.cosineSimilarity("これはテストです", "これは東京です"));

	}

}
