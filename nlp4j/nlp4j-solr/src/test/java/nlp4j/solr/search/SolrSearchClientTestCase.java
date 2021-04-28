package nlp4j.solr.search;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import junit.framework.TestCase;

public class SolrSearchClientTestCase extends TestCase {

	public void testSearch001() throws Exception {

		String endPoint = "http://localhost:8983/";
		String indexName = "sandbox";
		String json = "{" //
				+ "search:'*:*'," //
				+ "facets:['word_noun_ss,count:10']," //
				+ "top:1" //
				+ "}" //
		;

		SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();

		JsonObject responseObject = client.search(indexName, json);

		System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));

	}

}
