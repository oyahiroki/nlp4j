package nlp4j.solr.search;

import java.io.Closeable;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MultiMapSolrParams;
import org.apache.solr.common.params.SolrParams;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.search.AbstractSearchClient;
import nlp4j.search.SearchClient;
import nlp4j.search.SearchClientBuilder;

/**
 * created_at 2021-04-10
 * 
 * @author Hiroki Oya
 */
public class SolrSearchClient extends AbstractSearchClient implements SearchClient, Closeable {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private static final int CONNECTION_TIMEOUT_MILLIS = 10000;
	private static final int SOCKET_TIMEOUT_MILLIS = 60000;
	private String endPoint;

	HttpSolrClient solrClient;

	private SolrSearchClient(String endPoint) {
		this.endPoint = endPoint;
	}

	/**
	 * <pre>
	 * Search Client Builder
	 * </pre>
	 * 
	 * created_at 2021-04-10
	 * 
	 * @author Hiroki Oya
	 */
	public static class Builder extends SearchClientBuilder<Builder> {
		private String endPoint;

		/**
		 * @param endPoint like 'http://localhost:8983/'
		 */
		public Builder(String endPoint) {
			this.endPoint = endPoint;
		}

		@Override
		public Builder getThis() {
			return this;
		}

		public SolrSearchClient build() {
			return new SolrSearchClient(this.endPoint);
		}
	}

	/**
	 * @param collection of Solr
	 * @param json       in Azure Style
	 * @return result
	 * @throws IOException on Error
	 */
	public JsonObject search(String collection, String json) throws IOException {
		Gson gson = new Gson();
		try {
			JsonObject requestObject = gson.fromJson(json, JsonObject.class);
			return search(collection, requestObject);
		} catch (JsonSyntaxException e) {
			logger.error("Syntax error: " + json);
			throw new IOException(e);
		}
	}

	/**
	 * created on: 2024-07-15
	 * 
	 * @param collection
	 * @param doc
	 * @return
	 * @throws IOException
	 */
	public JsonObject searchByVector(String collection, Document doc) throws IOException {

		String endPoint = "http://localhost:8983/solr/";
//			String collection = "sandbox";

		try (Http2SolrClient solrClient = new Http2SolrClient.Builder(endPoint) //
				.withConnectionTimeout(10 * 1000, TimeUnit.MILLISECONDS)//
				.build();) {

//				doc.get

			StringBuilder sb_vector = new StringBuilder();
			String field_vector_of_document = "vector";
			String field_vector_of_solr = "vector";
			{
				List<Number> nn = doc.getAttributeAsListNumbers(field_vector_of_document);
				for (Number n : nn) {
					if (sb_vector.length() > 0) {
						sb_vector.append(", ");
					}
					sb_vector.append(n.toString());
				}
			}

			final Map<String, String[]> requestParamsSolr = new HashMap<>();

			requestParamsSolr.put("q", new String[] { "{!knn " //
					+ "f=" + field_vector_of_solr + " " //
					+ "topK=" + "10 " //
//					+ "preFilter=field1_s:BBB "
					+ "}" + "[" //
					// + "1.0, 2.4, 3.5, 4.0"
					+ sb_vector.toString() //
					+ "]" + "" });
			requestParamsSolr.put("fl", new String[] { "id", "score", "field1_s", "word_ss" });

			MultiMapSolrParams solrQueryParams = new MultiMapSolrParams(requestParamsSolr);

			QueryResponse solrResponse = solrClient //
					.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException

//				System.err.println(solrResponse.jsonStr());

			JsonObject jo = (new Gson()).fromJson(solrResponse.jsonStr(), JsonObject.class);

			return jo;

		} catch (SolrServerException e) {
			throw new IOException(e);
		}

	}

	/**
	 * @param collection of Solr
	 * @param requestAz  in Azure Style
	 * @return result {'value':[{id:'123',text:'this is test'},{...},{...}]}
	 * @throws IOException on Error
	 */
	public JsonObject search(String collection, JsonObject requestAz) throws IOException {

		String baseSolrUrl = this.endPoint;

		if (this.solrClient == null) {
			this.solrClient = new HttpSolrClient.Builder(baseSolrUrl) //
					.withConnectionTimeout(CONNECTION_TIMEOUT_MILLIS)//
					.withSocketTimeout(SOCKET_TIMEOUT_MILLIS)//
					.build();
		}

		// Convert Azure Search Format Request to Solr Format Request
		SolrParams solrQueryParams = SolrRequestConverter.convertRequestParams(requestAz);

		logger.debug("query: " + solrQueryParams.toString());

		try {
			QueryResponse solrResponse = this.solrClient //
					.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException
			JsonObject responseAz = SolrResponseConverter.convertResponse(solrResponse);
			{
				responseAz.add("@requestbody", requestAz);
			}
			if (logger.isDebugEnabled()) {
				logger.debug(responseAz.toString());
			}
			return responseAz;
		} catch (SolrServerException | RemoteSolrException e) {

			// RETRY PROCESS
			{
				try {
					System.err.println("Exception Occured: " + e.getClass().getCanonicalName() + ", " + e.getMessage());
					System.err.println("WAITING 30SECONDS...");
					Thread.sleep(30 * 1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				System.err.println("RETRY...");
				try {
					QueryResponse solrResponse = this.solrClient //
							.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException
					JsonObject responseAz = SolrResponseConverter.convertResponse(solrResponse);
					{
						responseAz.add("@requestbody", requestAz);
					}
					if (logger.isDebugEnabled()) {
						logger.debug(responseAz.toString());
					}
					System.err.println("RETRY...SUCCEEDED");
					return responseAz;
				} catch (Exception e2) {

				}
			}
			System.err.println("RETRY...FAILED");

			throw new IOException(e);
		} finally {
		}
	}

	/**
	 * Performs a query to the Solr server
	 * 
	 * @param collection
	 * @param solrQueryParams
	 * @return
	 * @throws IOException
	 */
	public QueryResponse search(String collection, SolrParams solrQueryParams) throws IOException {
		try {
			return this.solrClient //
					.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException
		} catch (SolrServerException | RemoteSolrException e) {
			throw new IOException(e);
		} finally {

		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void close() throws IOException {
		if (this.solrClient != null) {
			this.solrClient.getHttpClient() //
					.getConnectionManager() //
					.closeIdleConnections(0, TimeUnit.MILLISECONDS);
			this.solrClient.close();
		}

	}

}
