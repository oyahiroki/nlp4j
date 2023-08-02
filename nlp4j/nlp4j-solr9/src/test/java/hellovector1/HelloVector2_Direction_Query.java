package hellovector1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MultiMapSolrParams;

public class HelloVector2_Direction_Query {

	public static void main(String[] args) throws Exception {

		String endPoint = "http://localhost:8983/solr/";
		String collection = "hellovector_1_direction";

		try (Http2SolrClient solrClient = new Http2SolrClient.Builder(endPoint) //
				.withConnectionTimeout(10 * 1000, TimeUnit.MILLISECONDS)//
				.build();) {

			{
				final Map<String, String[]> requestParamsSolr = new HashMap<>();
				requestParamsSolr.put("q", new String[] { "{!knn f=vector topK=4}[" //
						+ "0.866, 0.5" // 右 30度
						+ "]" });
				requestParamsSolr.put("fl", new String[] { "id score text_txt_ja" });
				MultiMapSolrParams solrQueryParams = new MultiMapSolrParams(requestParamsSolr);
				QueryResponse solrResponse = solrClient //
						.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException
				System.err.println(solrResponse.jsonStr());

			}
			{
				final Map<String, String[]> requestParamsSolr = new HashMap<>();
				requestParamsSolr.put("q", new String[] { "{!knn f=vector topK=4}[" //
						+ "0.5, 0.866" //
						+ "]" });
				requestParamsSolr.put("fl", new String[] { "id score text_txt_ja" });
				MultiMapSolrParams solrQueryParams = new MultiMapSolrParams(requestParamsSolr);
				QueryResponse solrResponse = solrClient //
						.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException
				System.err.println(solrResponse.jsonStr());

			}
			{
				final Map<String, String[]> requestParamsSolr = new HashMap<>();
				requestParamsSolr.put("q", new String[] { "{!knn f=vector topK=4}[" //
						+ "1.0, 1.0" //
						+ "]" });
				requestParamsSolr.put("fl", new String[] { "id score text_txt_ja" });
				MultiMapSolrParams solrQueryParams = new MultiMapSolrParams(requestParamsSolr);
				QueryResponse solrResponse = solrClient //
						.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException
				System.err.println(solrResponse.jsonStr());

			}
			{
				final Map<String, String[]> requestParamsSolr = new HashMap<>();
				requestParamsSolr.put("q", new String[] { "{!knn f=vector topK=4}[" //
						+ "-1.1, -1.0" //
						+ "]" });
				requestParamsSolr.put("fl", new String[] { "id score text_txt_ja" });
				MultiMapSolrParams solrQueryParams = new MultiMapSolrParams(requestParamsSolr);
				QueryResponse solrResponse = solrClient //
						.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException
				System.err.println(solrResponse.jsonStr());

			}

		}

	}

}
