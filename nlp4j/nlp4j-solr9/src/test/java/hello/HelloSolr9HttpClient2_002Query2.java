package hello;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MultiMapSolrParams;

/**
 * Example of facet search
 *
 */
public class HelloSolr9HttpClient2_002Query2 {

	public static void main(String[] args) throws Exception {

		String endPoint = "http://localhost:8983/solr/";
		String collection = "gettingstarted";

		Http2SolrClient solrClient = new Http2SolrClient.Builder(endPoint) //
				.build();

		final Map<String, String[]> requestParamsSolr = new HashMap<>();

		requestParamsSolr.put("q", new String[] { "*:*" });
		requestParamsSolr.put("q.op", new String[] { "OR" });
		requestParamsSolr.put("rows", new String[] { "0" });
		requestParamsSolr.put("facet", new String[] { "true" });
		requestParamsSolr.put("facet.field", new String[] { "your_field_ss" });
		requestParamsSolr.put("facet.mincount", new String[] { "1" });
		requestParamsSolr.put("facet.limit", new String[] { "10" });

		MultiMapSolrParams solrQueryParams = new MultiMapSolrParams(requestParamsSolr);

		System.err.println(solrQueryParams.toQueryString());

		QueryResponse solrResponse = solrClient //
				.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException

		System.err.println(solrResponse.jsonStr());

		for (FacetField fl : solrResponse.getFacetFields()) {
			System.err.println(fl.getName());
			for (org.apache.solr.client.solrj.response.FacetField.Count cnt : fl.getValues()) {
				System.err.println(cnt.getName() + "," + cnt.getCount());
			}
		}
	}

}
