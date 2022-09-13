package nlp4j.solr.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.params.MultiMapSolrParams;
import org.apache.solr.common.params.SolrParams;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * created_at: 2022-07-15
 * 
 * @author Hiroki Oya
 *
 */
public class SolrRequestConverter {

	/**
	 * Convert Azure Search Format Request to Solr Format Request
	 * 
	 * @param requestAzureSearch Azure Style Request
	 * @return request Parameter for Solr
	 */
	static public SolrParams convertRequestParams(String requestAzureSearch) {
		return convertRequestParams((new Gson()).fromJson(requestAzureSearch, JsonObject.class));
	}

	/**
	 * Convert Azure Search Format Request to Solr Format Request
	 * 
	 * @param requestAzureSearch Azure Style Request
	 * @return request Parameter for Solr
	 */
	static public SolrParams convertRequestParams(JsonObject requestAzureSearch) {

		final Map<String, String[]> requestParamsSolr = new HashMap<>();

		// search -> q
		{
			String search = requestAzureSearch.get("search").getAsString();
			if (search != null) {
				String[] pp = { search };
				requestParamsSolr.put("q", pp);
			}
		}
		// facets
		if (requestAzureSearch.get("facets") != null //
				&& requestAzureSearch.get("facets").isJsonNull() == false) {

			JsonArray facetsArr = requestAzureSearch.get("facets").getAsJsonArray();

//			if (facetsArr != null && facetsArr.size() > 0) {
//				requestParamsSolr.put("facet", "on");
//			}

			ArrayList<String> ff = new ArrayList<>();

			// IF(facets パラメータが存在する)THEN
			// IF(facets parameter exists)THEN
			if (facetsArr != null && facetsArr.size() > 0) {

				String[] pp = { "on" };
				requestParamsSolr.put("facet", pp);

				for (int n = 0; n < facetsArr.size(); n++) {
					// facet is like: "word_noun,count:100"
					String facet = facetsArr.get(n).getAsString();
					String facetName = null;
					String[] facetParams = facet.split(",");
					for (int i = 0; i < facetParams.length; i++) {
						String p = facetParams[i];
						if (i == 0) {
							ff.add(p);
							facetName = p;
						} //
							//
						else {
							// カウント指定「count:100」に対応
							if (p.contains(":")) {
								String key = p.split(":")[0];
								String value = p.split(":")[1];
								if ("count".equals(key)) {
//									queryParamMap.put("f.word_noun_ss.facet.limit", "100");
									{
										String[] pp1 = { value };
										requestParamsSolr.put("f." + facetName + ".facet.limit", pp1);
									}
									{
										String[] pp2 = { "1" };
										requestParamsSolr.put("f." + facetName + ".facet.mincount", pp2);
									}
								}
							} else {
								// noop
							}
						}
					}
				}
			}
			requestParamsSolr.put("facet.field", ff.toArray(new String[0]));
		}

		// top -> rows
		{
			String top = (requestAzureSearch.get("top") != null) ? requestAzureSearch.get("top").getAsString() : "10";
			if (top != null) {
				String[] pp = { top };
				requestParamsSolr.put("rows", pp);
			}
		}
		// skip -> start
		{
			String skip = (requestAzureSearch.get("skip") != null) ? requestAzureSearch.get("skip").getAsString() : "0";
			if (skip != null) {
				String[] pp = { skip };
				requestParamsSolr.put("start", pp);
			}
		}

		// 2022-09-10
		// select -> fl
		{
			String select = (requestAzureSearch.get("select") != null) ? requestAzureSearch.get("select").getAsString()
					: null;
			if (select != null) {
				List<String> fl = new ArrayList<>();
				String[] ss = select.split(",");
				for (String s : ss) {
					s = s.trim();
					fl.add(s);
				}
				requestParamsSolr.put("fl", fl.toArray(new String[0]));
			}
		}

//		requestParamsSolr.put("fl", "id,word_noun_ss");
//		requestParamsSolr.put("sort", "id asc");
//		requestParamsSolr.put("start", "0");

//		return new MapSolrParams(requestParamsSolr);
		return new MultiMapSolrParams(requestParamsSolr);

	}

}
