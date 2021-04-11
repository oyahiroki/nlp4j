package nlp4j.solr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;

public class SearchMain {

	public static void main(String... args) throws Exception {
		final SolrClient client = getSolrClient();

		String facet = "word_noun_ss";
		String word = "日本";
		String q = facet + ":" + word;

		final Map<String, String> queryParamMap = new HashMap<>();
		queryParamMap.put("q", q);
		queryParamMap.put("facet.field", "word_noun_ss");
		queryParamMap.put("f.word_noun_ss.facet.limit", "100");
		queryParamMap.put("rows", "1");
		queryParamMap.put("facet", "on");
//		queryParamMap.put("fl", "id,word_noun_ss");
//		queryParamMap.put("sort", "id asc");
//		queryParamMap.put("start", "0");

		MapSolrParams queryParams = new MapSolrParams(queryParamMap);

		final QueryResponse response = client.query("sandbox", queryParams, METHOD.POST);
		{
		}
		{
			List<FacetField> facets = response.getFacetFields();
			for (FacetField f : facets) {
				System.err.println(f.getName() + "," + f.getValueCount());
				for (Count cnt : f.getValues()) {
					System.err.println(cnt.getName() + "," + cnt.getCount());
				}
			}

		}
		final SolrDocumentList documents = response.getResults();

		print("Found " + documents.getNumFound() + " documents");
		for (SolrDocument document : documents) {
			System.err.println(document);
			System.err.println(document.getFieldValues("word_noun_ss"));
			final String id = (String) document.getFirstValue("id");
			final String name = (String) document.getFirstValue("name");

			print("id: " + id + "; name: " + name);
		}
	}

	private static void print(String string) {
		System.err.println(string);

	}

	private static SolrClient getSolrClient() {
		
		final String solrUrl = "http://localhost:8983/solr";
		return new HttpSolrClient.Builder(solrUrl) //
				.withConnectionTimeout(10000)//
				.withSocketTimeout(60000)//
				.build();
	}

}
