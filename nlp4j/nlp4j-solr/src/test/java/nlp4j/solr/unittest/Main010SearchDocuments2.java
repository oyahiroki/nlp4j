package nlp4j.solr.unittest;

import java.io.IOException;

import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import nlp4j.solr.search.SolrSearchClient;

public class Main010SearchDocuments2 {

	public static void main(String[] args) throws Exception {
		String endPoint = "http://localhost:8983/solr/";
		String indexName = "unittest";
		String json = "{" //
				+ "search:'*:*'," //
				+ "facets:['field1_s,count:10']," //
				+ "top:10," //
				+ "skip:1" // TEST
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

}
