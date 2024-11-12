package nlp4j.opensearch;

import nlp4j.NlpServiceResponse;
import nlp4j.util.JsonUtils;

public class SimpleOpenSearchClient001_CatIndices {

	public static void main(String[] args) throws Exception {

		String endpoint = "https://localhost:9200";
		String user = "admin";
		String password = System.getProperty("OPENSEARCH_PASSWORD");
		String index = "myindex1";

		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient(endpoint, user, password, index);) {

			NlpServiceResponse res = client.catIndices();

			System.err.println(JsonUtils.prettyPrint(res.getAsJson()));

		}

	}

}
