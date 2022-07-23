package nlp4j.solr.search;

import java.io.IOException;

import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;

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
		try {
			JsonObject responseObject = client.search(indexName, json);

			System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));
		} catch (IOException e) {
			System.err.println("SKIP THIS TEST: " + e.getMessage());
		} catch (RemoteSolrException e) {
			System.err.println("SKIP THIS TEST: " + e.getMessage());
		}

	}

	public void testSearch101() throws Exception {
		String endPoint = "http://localhost:8983/solr";
		String indexName = "cb20220501";
		String json = "{" //
				+ "search:'*:*'," //
				+ "facets:['word_ss,count:10']," //
				+ "top:0" //
				+ "}" //
		;
		SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();
		try {
			JsonObject responseObject = client.search(indexName, json);

			System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));

			assertEquals(10, responseObject.get("@search.facets") //
					.getAsJsonObject().get("word_ss").getAsJsonArray().size());

		} catch (IOException e) {
			System.err.println("SKIP THIS TEST");
		} catch (@SuppressWarnings("deprecation") RemoteSolrException e) {
			System.err.println("SKIP THIS TEST");
		}

	}

	public void testSearch102() throws Exception {
		String endPoint = "http://localhost:8983/solr";
		String indexName = "cb20220501";
		String json = "{" //
				+ "search:'*:*'," //
				+ "facets:['word_ss,count:10','wikilink_ss,count:5']," //
				+ "top:0" //
				+ "}" //
		;
		SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();
		try {
			JsonObject responseObject = client.search(indexName, json);

			System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));

			assertEquals(10, responseObject.get("@search.facets") //
					.getAsJsonObject().get("word_ss").getAsJsonArray().size());
			assertEquals(5, responseObject.get("@search.facets") //
					.getAsJsonObject().get("wikilink_ss").getAsJsonArray().size());

		} catch (IOException e) {
			System.err.println("SKIP THIS TEST");
		} catch (@SuppressWarnings("deprecation") RemoteSolrException e) {
			System.err.println("SKIP THIS TEST");
		}

	}

}
