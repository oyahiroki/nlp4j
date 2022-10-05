package nlp4j.solr.search;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;
import nlp4j.solr.search.SolrSearchClient;

public class SolrFacetSearchClient {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	// Solr URL
	private String endPoint;
	// Solr Index
	private String indexName;

	private String facet;

	SolrSearchClient client;
	// 文書全件
	long countAll0 = -1;

	private final String SEARCH_ALL = "*:*";

	private final long COUNT0;
	// 文書全件の上位カウントキーワード
	Map<String, Long> mapKeywordCount0 = new HashMap<String, Long>();

	// 文書全件の上位カウントキーワード順位
	Map<String, Integer> mapKeywordRank = new HashedMap<String, Integer>();
	List<String> topKeywords = new ArrayList<String>();

	public SolrFacetSearchClient(String endPoint, String indexName, String facet) throws IOException {
		super();
		this.endPoint = endPoint;
		this.indexName = indexName;
		this.facet = facet;
		COUNT0 = 10000L;
		init();
	}

	public SolrFacetSearchClient(String endPoint, String indexName, String facet, long l) throws IOException {
		super();
		this.endPoint = endPoint;
		this.indexName = indexName;
		this.facet = facet;
		COUNT0 = l;
		init();
	}

	public List<Keyword> facetSearch(String search, int facetcount) throws IOException {
		return facetSearch(search, facetcount, null);
	}

	public List<Keyword> facetSearch(String search, int facetcount, String filterRegex) throws IOException {
		long countAll1 = -1;
		{
//			String json1 = "{'count':true" //
//					+ ",'search':'" + search + "'" //
//					+ ",'top':0" //
//					+ ",'facets':['" + facet + ",count:" + facetcount * 2 + "']" //
//					+ ",'queryType':'full'" //
//					+ ",'searchMode':'all'}";
//			JsonObject requestObject1 = (new Gson()).fromJson(json1, JsonObject.class);

			JsonObject requestObject1 = new JsonObject();
			requestObject1.addProperty("count", true);
			requestObject1.addProperty("search", search);
			requestObject1.addProperty("top", 0);
			{
				JsonArray facets = new JsonArray();
				facets.add("" + facet + ",count:" + facetcount * 2);
				requestObject1.add("facets", facets);
			}
			requestObject1.addProperty("queryType", "full");
			requestObject1.addProperty("searchMode", "all");

			JsonObject response1 = client.search(indexName, requestObject1);
//			System.err.println(JsonUtils.prettyPrint(response1));
			Map<String, Long> mapKeywordCount1 = new HashedMap<String, Long>();
			{
				countAll1 = response1.get("@odata.count").getAsLong();
			}
			{
				JsonArray arr = response1.get("@search.facets").getAsJsonObject().get(facet).getAsJsonArray();
				for (int n = 0; n < arr.size(); n++) {
					JsonObject o = arr.get(n).getAsJsonObject();
					String value = o.get("value").getAsString();
					long count = o.get("count").getAsLong();
					mapKeywordCount1.put(value, count);
				}
			}

			List<Keyword> kwds = new ArrayList<>();

			for (String key : mapKeywordCount1.keySet()) {
				Long count0 = mapKeywordCount0.get(key);
				Long count1 = mapKeywordCount1.get(key);
				if (count0 == null) {
					count0 = 1L;
				}
				if (count1 == null) {
					count1 = 1L;
				}
				double d = ((double) count1 / (double) countAll1) / ((double) count0 / (double) countAll0);
//				System.err.println(key + " -> " + count1 + " , " + count0 + ", " + d);

				Keyword kwd = new DefaultKeyword();
				kwd.setCorrelation(d);
				kwd.setLex(key);
				kwd.setFacet(facet);
				kwd.setCount(count1);

				if (filterRegex == null || kwd.getLex().matches(filterRegex) == false) {
					kwds.add(kwd);
				}

				if (kwds.size() >= facetcount) {
					break;
				}

			}

			kwds = kwds.stream() //
					.sorted(Comparator.comparing(Keyword::getCorrelation, Comparator.reverseOrder())) //
					.collect(Collectors.toList());

//			for (Keyword k : kwds) {
//				System.err.println(k.getLex() + "," + k.getCorrelation());
//			}

			return kwds;
		}

	}

	public long getCountAll(String lex) {
		Long c = this.mapKeywordCount0.get(lex);
		if (c == null) {
			return -1L;
		} else {
			return c;
		}
	}

	public long getCountDocAll() {
		return countAll0;
	}

	public String getFacet() {
		return facet;
	}

	public int getRank(String lex) {

		Integer count = this.mapKeywordRank.get(lex);
		if (count == null) {
			return -1;
		} else {
			return count;
		}
	}

	public List<String> getTopKeywords() {
		return topKeywords;
	}

	private void init() throws IOException {
		// Initialize Solr Client
		client = new SolrSearchClient.Builder(endPoint).build();
		{ // FETCH COUNT OF KEYWORD
			long time1 = System.currentTimeMillis();
			logger.info("RETREIVING WORDS: " + this.COUNT0);
			String json0 = "{'count':true" //
					+ ",'search':'" + SEARCH_ALL + "'" //
					+ ",'top':0" //
					+ ",'facets':['" + facet + ",count:" + COUNT0 + "']" //
					+ ",'queryType':'full'" //
					+ ",'searchMode':'all'}";
			JsonObject requestObject0 = (new Gson()).fromJson(json0, JsonObject.class);
			JsonObject response0 = client.search(indexName, requestObject0);
//			System.err.println(JsonUtils.prettyPrint(response0));
			{ // COUNT OF ALL DOCUMENTS
				countAll0 = response0.get("@odata.count").getAsLong();
			}
			{ // COUNT OF KEYWORDS
				JsonArray arr = response0.get("@search.facets").getAsJsonObject().get(facet).getAsJsonArray();
				for (int n = 0; n < arr.size(); n++) {
					JsonObject o = arr.get(n).getAsJsonObject();
					String value = o.get("value").getAsString();
					long count = o.get("count").getAsLong();
					mapKeywordCount0.put(value, count);
					mapKeywordRank.put(value, (n + 1));
					topKeywords.add(value);
				}
			}
//			for (String key : mapKeywordCount0.keySet()) {
//				System.err.println(key + " -> " + mapKeywordCount0.get(key));
//			}
			long time2 = System.currentTimeMillis();
//			System.err.println("time: " + (time2 - time1));
			logger.info("RETREIVING WORDS DONE: " + this.COUNT0);
			logger.info("time: " + (time2 - time1));
		} // END(FETCH COUNT OF KEYWORD)
	}

}
