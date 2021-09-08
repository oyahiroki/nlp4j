package nlp4j.solr.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;
import nlp4j.search.SearchClient;

public class WordCounter {

	SearchClient client;

	/**
	 * 全文書数
	 */
	private long countAll;

	String indexName = "sandbox";

	HashMap<String, HashMap<String, Long>> facetWordCount = new HashMap<>();

//	HashMap<String, Long> wordCount = new HashMap<>();

	public WordCounter(SearchClient client, String indexName, String facets) {

		this.client = client;
		this.indexName = indexName;

		JsonObject initialFacetSearchQueryObject = new JsonObject();

//		String[] initialFacets = "word_noun_ss".split(",");
		String[] initialFacets = facets.split(",");

		long initialFaetCount = 10000;
		{
			initialFacetSearchQueryObject.addProperty("search", "*:*");
			JsonArray queryFacets = new JsonArray();
			for (String initialFacet : initialFacets) {
				queryFacets.add(initialFacet + ",count:" + initialFaetCount);
			}
			initialFacetSearchQueryObject.add("facets", queryFacets);
			initialFacetSearchQueryObject.addProperty("top", 0);
		}

//		String initialFacetSearchQuery = "{search:'*:*',facets:['word_noun_ss,count:10000'],top:0}";
		String initialFacetSearchQuery = initialFacetSearchQueryObject.toString();

		try {

			JsonObject responseObject = client.search(indexName, initialFacetSearchQuery);

//			System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));

			// 全文書数
			this.countAll = responseObject.get("@odata.count").getAsLong();

			{
				JsonObject resFacets = responseObject.get("@search.facets").getAsJsonObject();

				for (String initialFacet : initialFacets) {

					JsonArray ff = resFacets.get(initialFacet).getAsJsonArray();

					HashMap<String, Long> wordCount = new HashMap<>();

					// put
					facetWordCount.put(initialFacet, wordCount);

					if (ff != null) {
						for (int n = 0; n < ff.size(); n++) {
							JsonObject f = ff.get(n).getAsJsonObject();
							String s = f.get("value").getAsString();
							long c = f.get("count").getAsLong();
							wordCount.put(s, c);
						}
					}

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public List<Keyword> getCooccurrence(String facet, String word, long facetCount) {

		List<Keyword> kwds = new ArrayList<>();

		String json = "{" //
				+ "search:'" + facet + ":" + word + "'," //
				+ "facets:['" + facet + ",count:" + facetCount + "']," //
				+ "top:0" //
				+ "}" //
		;

		try {

			JsonObject responseObject = client.search(indexName, json);
			{
				JsonObject facets = responseObject.get("@search.facets").getAsJsonObject();
				{
					JsonArray ff = facets.get(facet).getAsJsonArray();

					if (this.facetWordCount.get(facet) == null) {
						this.facetWordCount.put(facet, new HashMap<>());
					}

					HashMap<String, Long> wordCount = this.facetWordCount.get(facet);

					if (ff != null) {
						for (int n = 0; n < ff.size(); n++) {
							JsonObject f = ff.get(n).getAsJsonObject();
							String s = f.get("value").getAsString();
							long c = f.get("count").getAsLong();

							wordCount.put(s, c);

							Keyword kwd = new DefaultKeyword();
							kwd.setLex(s);
							kwd.setCount(c);
							kwds.add(kwd);
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return kwds;

	}

	public List<Keyword> getCooccurrenceSortedByIDF(String facet, String word, int facetCount) {

		List<Keyword> kwds = getCooccurrence(facet, word, facetCount);

		kwds.forEach(k -> k.setCorrelation(getIdf(facet, k.getLex())));

		Collections.sort(kwds, new Comparator<Keyword>() {

			@Override
			public int compare(Keyword o1, Keyword o2) {

				return (o2.getCorrelation() - o1.getCorrelation() < 0) ? -1 : 1;

			}
		});

		return kwds;
	}

	public long getCount(String facet, String word) {

		if (this.facetWordCount.containsKey(facet) && this.facetWordCount.get(facet).containsKey(word)) {
			return this.facetWordCount.get(facet).get(word);
		} //
		else {
			String json = "{" //
					+ "search:'" + facet + ":" + word + "'," //
					+ "facets:null," //
					+ "top:0" //
					+ "}" //
			;

			HashMap<String, Long> wordCount = this.facetWordCount.get(facet);

			if (wordCount == null) {
				wordCount = new HashMap<String, Long>();
				this.facetWordCount.put(facet, wordCount);
			}

			try {

				JsonObject responseObject = client.search(indexName, json);

//				System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));

				long count = responseObject.get("@odata.count").getAsLong();

				wordCount.put(word, count);

				return count;

			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
	}

	public long getCountAll() {
		return this.countAll;
	}

	public double getIdf(String facet, String word) {

//		System.err.println(word);

		long count = getCount(facet, word);

//		if (count == 0) {
//			return 0.0;
//		}

//		System.err.println("word=" + word + ",count=" + count);

		double idf = Math.log(((this.countAll) / (count + 1)));

//		System.err.println(idf);

		return idf;
	}

	public List<Keyword> getTopKeywords(String facet, int size) {

		ArrayList<Keyword> kwds = new ArrayList<>();

		String json = "{" //
				+ "search:'*:*'," //
				+ "facets:['" + facet + ",count:" + size + "']," //
				+ "top:0" //
				+ "}" //
		;

		try {

			JsonObject responseObject = client.search(indexName, json);

//			System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));

			this.countAll = responseObject.get("@odata.count").getAsLong();

			{
				JsonObject facets = responseObject.get("@search.facets").getAsJsonObject();
				{
					JsonArray ff = facets.get(facet).getAsJsonArray();
					if (ff != null) {
						for (int n = 0; n < ff.size(); n++) {
							JsonObject f = ff.get(n).getAsJsonObject();
							String s = f.get("value").getAsString();
							long c = f.get("count").getAsLong();
							Keyword kwd = new DefaultKeyword();
							kwd.setLex(s);
							kwd.setCount(c);
							kwds.add(kwd);
						}
					}

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return kwds;

	}

}
