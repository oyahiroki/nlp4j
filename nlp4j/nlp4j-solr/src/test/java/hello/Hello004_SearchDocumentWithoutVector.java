package hello;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MultiMapSolrParams;

public class Hello004_SearchDocumentWithoutVector {

	public static void main(String[] args) throws Exception {

		String endPoint = "http://localhost:8983/solr/";
		String collection = "sandbox";

		try (Http2SolrClient solrClient = new Http2SolrClient.Builder(endPoint) //
				.withConnectionTimeout(10 * 1000, TimeUnit.MILLISECONDS)//
				.build();) {

			final Map<String, String[]> requestParamsSolr = new HashMap<>();

			requestParamsSolr.put("q", new String[] { "text_txt_ja:今日" });
			requestParamsSolr.put("fl", new String[] { "text_txt_ja" });

			MultiMapSolrParams solrQueryParams = new MultiMapSolrParams(requestParamsSolr);

			QueryResponse solrResponse = solrClient //
					.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException

			System.err.println(solrResponse.jsonStr());

		}

	}

}
