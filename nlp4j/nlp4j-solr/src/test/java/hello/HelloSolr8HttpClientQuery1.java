package hello;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MultiMapSolrParams;

public class HelloSolr8HttpClientQuery1 {

	public static void main(String[] args) throws Exception {

		String endPoint = "http://localhost:8983/solr/";
		String collection = "sandbox";

		HttpSolrClient solrClient = new HttpSolrClient.Builder(endPoint) //
				.withConnectionTimeout(10 * 1000)//
				.withSocketTimeout(60 * 1000)//
				.build();

		final Map<String, String[]> requestParamsSolr = new HashMap<>();

		requestParamsSolr.put("q", new String[] { "*:*" });

		MultiMapSolrParams solrQueryParams = new MultiMapSolrParams(requestParamsSolr);

		QueryResponse solrResponse = solrClient //
				.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException

		System.err.println(solrResponse.jsonStr());

	}

}
