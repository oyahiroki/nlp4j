package nlp4j.solr.search;

import java.util.List;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * created_at: 2022-07-15
 * 
 * @author Hiroki Oya
 *
 */
public class SolrResponseConverter {

	/**
	 * @param responseSolr Response
	 * @return response in Azure Format
	 */
	static public JsonObject convertResponse(QueryResponse responseSolr) {

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
						@SuppressWarnings("rawtypes")
						List<Object> list = (List) o;
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

}
