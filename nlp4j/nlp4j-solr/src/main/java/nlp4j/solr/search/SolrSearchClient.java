package nlp4j.solr.search;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.search.AbstractSearchClient;
import nlp4j.search.SearchClient;
import nlp4j.search.SearchClientBuilder;

/**
 * created_at 2021-04-10
 * 
 * @author Hiroki Oya
 */
public class SolrSearchClient extends AbstractSearchClient implements SearchClient, Closeable {

	private static final int CONNECTION_TIMEOUT_MILLIS = 10000;
	private static final int SOCKET_TIMEOUT_MILLIS = 60000;
	private String endPoint;

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
		JsonObject requestObject = gson.fromJson(json, JsonObject.class);
		return search(collection, requestObject);
	}

	HttpSolrClient solrClient;

	/**
	 * @param collection of Solr
	 * @param requestAz  in Azure Style
	 * @return result
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

		MapSolrParams solrQueryParams = convertRequestParams(requestAz);

		try {

			QueryResponse solrResponse = this.solrClient //
					.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException

			JsonObject responseAz = convertResponse(solrResponse);
			{
				responseAz.add("@requestbody", requestAz);
			}

			return responseAz;

		} catch (SolrServerException e) {
			throw new IOException(e);
		} catch (RemoteSolrException e) {
			throw new IOException(e);
		} catch (IOException e) {
			throw e;
		} finally {
		}
	}

	/**
	 * @param responseSolr Response
	 * @return response in Azure Format
	 */
	public JsonObject convertResponse(QueryResponse responseSolr) {

		JsonObject responseAz = new JsonObject();

		{
			Gson gson = new Gson();
			JsonObject solrResponseJson = gson.fromJson(responseSolr.jsonStr(), JsonObject.class);
			responseAz.add("@solrresponse", solrResponseJson);
		}

		final SolrDocumentList documents = responseSolr.getResults();

		{ // num found
			responseAz.addProperty("@odata.count", documents.getNumFound());
//				System.err.println("Found " + documents.getNumFound() + " documents");
		}

		{ // documents

			JsonArray value = new JsonArray();

			responseAz.add("value", value);

			for (SolrDocument document : documents) {

				JsonObject v = new JsonObject();

				value.add(v);

//					System.err.println(document);

				document.forEach(d -> {

					String k = d.getKey();
					Object o = d.getValue();

					if (o instanceof String) {
						v.addProperty(k, (String) o);
					} //
					else if (o instanceof Number) {
						v.addProperty(k, (Number) o);
					} //
					else if (o instanceof List) {
						JsonArray arr = new JsonArray();
						List list = (List) o;
						for (Object x : list) {
							if (x instanceof String) {
								arr.add((String) x);
							} //
							else if (x instanceof Number) {
								arr.add((Number) x);
							} //
						}
						v.add(k, arr);
					}

//						System.err.println("key=" + d.getKey());
//						System.err.println("value=" + d.getValue());
//						System.err.println("class=" + d.getValue().getClass().getName());
				});

//					document.keySet().forEach(k -> {
//						System.err.println("fieldValue=" + document.getFieldValue(k));
//					});

//					System.err.println(document.getFieldValues("word_noun_ss"));

//					final String id = (String) document.getFirstValue("id");
//					final String name = (String) document.getFirstValue("name");

//					System.err.println("id: " + id + "; name: " + name);
			}
		}

		{
			JsonObject searchFacets = new JsonObject();

			responseAz.add("@search.facets", searchFacets);

			List<FacetField> facets = responseSolr.getFacetFields();

			if (facets != null) {
				for (FacetField f : facets) {

					String facetName = f.getName();

					JsonArray facetArray = new JsonArray();

					searchFacets.add(facetName, facetArray);

//						System.err.println(f.getName() + "," + f.getValueCount());

					for (Count cnt : f.getValues()) {

						JsonObject facetValue = new JsonObject();
						facetValue.addProperty("count", cnt.getCount());
						facetValue.addProperty("value", cnt.getName());
						facetArray.add(facetValue);

//							System.err.println(cnt.getName() + "," + cnt.getCount());
					}
				}

			}

		}

		return responseAz;
	}

	/**
	 * Convert Azure Search Format Request to Solr Format Request
	 * 
	 * @param requestAzureSearch Azure Style Request
	 * @return request Parameter for Solr
	 */
	public MapSolrParams convertRequestParams(JsonObject requestAzureSearch
//			, final Map<String, String> requestParamsSolr
	) {

		final Map<String, String> requestParamsSolr = new HashMap<>();

		// search -> q
		{
			String search = requestAzureSearch.get("search").getAsString();
			if (search != null) {
				requestParamsSolr.put("q", search);
			}
		}
		// facets
		if (requestAzureSearch.get("facets") != null && requestAzureSearch.get("facets").isJsonNull() == false) {

			JsonArray facets = requestAzureSearch.get("facets").getAsJsonArray();

			if (facets != null && facets.size() > 0) {
				requestParamsSolr.put("facet", "on");
			}

			ArrayList<String> ff = new ArrayList<>();
			if (facets != null && facets.size() > 0) {
				for (int n = 0; n < facets.size(); n++) {
					// word_noun,count:100
					String facet = facets.get(n).getAsString();
					String facetName = null;
					String[] facetParams = facet.split(",");
					for (int i = 0; i < facetParams.length; i++) {
						String p = facetParams[i];
						if (i == 0) {
							ff.add(p);
							facetName = p;
						} else {
							if (p.contains(":")) {
								String key = p.split(":")[0];
								String value = p.split(":")[1];
								if ("count".equals(key)) {
//									queryParamMap.put("f.word_noun_ss.facet.limit", "100");
									requestParamsSolr.put("f." + facetName + ".facet.limit", value);
									requestParamsSolr.put("f." + facetName + ".facet.mincount", "1");
								}
							} else {

							}
						}
					}
				}
			}
			requestParamsSolr.put("facet.field", StringUtils.join(ff, ","));
		}

		// top -> rows
		{
			String top = requestAzureSearch.get("top").getAsString();
			if (top != null) {
				requestParamsSolr.put("rows", top);
			}
		}

//		requestParamsSolr.put("fl", "id,word_noun_ss");
//		requestParamsSolr.put("sort", "id asc");
//		requestParamsSolr.put("start", "0");

		return new MapSolrParams(requestParamsSolr);
	}

	@Override
	public void close() throws IOException {
		if (this.solrClient != null) {
			this.solrClient.close();
		}

	}

}
