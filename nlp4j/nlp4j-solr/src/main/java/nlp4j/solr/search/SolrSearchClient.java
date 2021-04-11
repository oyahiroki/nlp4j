package nlp4j.solr.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
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

public class SolrSearchClient extends AbstractSearchClient implements SearchClient {

	private String endPoint;

	private SolrSearchClient(String endPoint) {
		this.endPoint = endPoint;
	}

	public static class Builder extends SearchClientBuilder<Builder> {

		private String endPoint;

		/**
		 * @param endPoint like http://localhost:8983/solr
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

	/**
	 * @param collection of Solr
	 * @param request    in Azure Style
	 * @return result
	 * @throws IOException on Error
	 */
	public JsonObject search(String collection, JsonObject request) throws IOException {

		HttpSolrClient client = new HttpSolrClient.Builder(this.endPoint) //
				.withConnectionTimeout(10000)//
				.withSocketTimeout(60000)//
				.build();

//		String facet = "word_noun_ss";
//		String word = "日本";
//		String q = facet + ":" + word;

		final Map<String, String> queryParamMap = new HashMap<>();

		// search -> q
		{
			String search = request.get("search").getAsString();
			if (search != null) {
				queryParamMap.put("q", search);
			}
		}
		// facets
		if (request.get("facets") != null && request.get("facets").isJsonNull() == false) {

			JsonArray facets = request.get("facets").getAsJsonArray();

			if (facets != null && facets.size() > 0) {
				queryParamMap.put("facet", "on");
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
									queryParamMap.put("f." + facetName + ".facet.limit", value);
									queryParamMap.put("f." + facetName + ".facet.mincount", "" + 100);
								}
							} else {

							}
						}
					}
				}
			}
			queryParamMap.put("facet.field", StringUtils.join(ff, ","));
		}
		// top -> rows
		{
			String top = request.get("top").getAsString();
			if (top != null) {
				queryParamMap.put("rows", top);
			}
		}

//		queryParamMap.put("fl", "id,word_noun_ss");
//		queryParamMap.put("sort", "id asc");
//		queryParamMap.put("start", "0");

		MapSolrParams queryParams = new MapSolrParams(queryParamMap);

		QueryResponse response;

		try {

			response = client.query(collection, queryParams, METHOD.POST);

			JsonObject responseObject = new JsonObject();

			{
				responseObject.add("@requestbody", request);
			}

			final SolrDocumentList documents = response.getResults();

			{ // num found
				responseObject.addProperty("@odata.count", documents.getNumFound());
//				System.err.println("Found " + documents.getNumFound() + " documents");
			}

			{ // documents

				JsonArray value = new JsonArray();

				responseObject.add("value", value);

				for (SolrDocument document : documents) {

					JsonObject v = new JsonObject();

					value.add(v);

					System.err.println(document);

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

						System.err.println("key=" + d.getKey());
						System.err.println("value=" + d.getValue());
						System.err.println("class=" + d.getValue().getClass().getName());
					});

					document.keySet().forEach(k -> {
						System.err.println("fieldValue=" + document.getFieldValue(k));
					});

					System.err.println(document.getFieldValues("word_noun_ss"));
					final String id = (String) document.getFirstValue("id");
					final String name = (String) document.getFirstValue("name");

					System.err.println("id: " + id + "; name: " + name);
				}
			}

			{
				JsonObject searchFacets = new JsonObject();

				responseObject.add("@search.facets", searchFacets);

				List<FacetField> facets = response.getFacetFields();

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

			return responseObject;

		} catch (SolrServerException e) {
			throw new IOException(e);
		} catch (IOException e) {
			throw e;
		}
	}

}
