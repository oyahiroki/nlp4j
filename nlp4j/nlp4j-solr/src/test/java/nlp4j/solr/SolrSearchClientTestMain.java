package nlp4j.solr;

import com.google.gson.JsonObject;

import nlp4j.solr.search.SolrSearchClient;
import nlp4j.util.JsonUtils;

public class SolrSearchClientTestMain {

	public static void main(String[] args) throws Exception {

		String endPoint = "http://localhost:8983/solr/";
		String indexName = "cb_wik_002";

		SolrSearchClient client = // 検索クライアントの初期化
				new SolrSearchClient.Builder(endPoint).build();

		JsonObject requestObject = new JsonObject();
		{
			String search = "item:学校";
			requestObject.addProperty("search", search);
			requestObject.addProperty("top", 1);
		}

		JsonObject response = client.search(indexName, requestObject);

		System.err.println(JsonUtils.prettyPrint(response));

		for (int i = 0; i < 10000; i++) {
			System.err.println(i);
			client.search(indexName, requestObject);
		}

		client.close();

	}

}
