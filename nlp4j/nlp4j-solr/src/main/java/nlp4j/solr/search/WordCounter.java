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

	private long countAll;

	HashMap<String, Long> wordCount = new HashMap<>();

	public long getCountAll() {
		return this.countAll;
	}

	public long getCount(String word) {
		if (wordCount.containsKey(word)) {
			return wordCount.get(word);
		} //
		else {
			String indexName = "sandbox";
			String json = "{" //
					+ "search:'word_noun_ss:" + word + "'," //
					+ "facets:null," //
					+ "top:0" //
					+ "}" //
			;

			try {

				JsonObject responseObject = client.search(indexName, json);

//				System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));

				long count = responseObject.get("@odata.count").getAsLong();

				this.wordCount.put(word, count);

				return count;

			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
	}

	public List<Keyword> getTopKeywords(int size) {

		ArrayList<Keyword> kwds = new ArrayList<>();

		String indexName = "sandbox";
		String json = "{" //
				+ "search:'*:*'," //
				+ "facets:['word_noun_ss,count:" + size + "']," //
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
					JsonArray ff = facets.get("word_noun_ss").getAsJsonArray();
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

	public WordCounter(SearchClient client) {

		this.client = client;

		String indexName = "sandbox";
		String json = "{" //
				+ "search:'*:*'," //
				+ "facets:['word_noun_ss,count:10000']," //
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
					JsonArray ff = facets.get("word_noun_ss").getAsJsonArray();
					if (ff != null) {
						for (int n = 0; n < ff.size(); n++) {
							JsonObject f = ff.get(n).getAsJsonObject();
							String s = f.get("value").getAsString();
							long c = f.get("count").getAsLong();
							this.wordCount.put(s, c);
						}
					}

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public double getIdf(String word) {

		System.err.println(word);

		double idf = Math.log(((this.countAll) / (getCount(word))) + 1);

		return idf;
	}

	public List<Keyword> getCooccurrence(String word, long facetCount) {

		List<Keyword> kwds = new ArrayList<>();

		String indexName = "sandbox";
		String json = "{" //
				+ "search:'word_noun_ss:" + word + "'," //
				+ "facets:['word_noun_ss,count:" + facetCount + "']," //
				+ "top:0" //
				+ "}" //
		;

		try {

			JsonObject responseObject = client.search(indexName, json);
			{
				JsonObject facets = responseObject.get("@search.facets").getAsJsonObject();
				{
					JsonArray ff = facets.get("word_noun_ss").getAsJsonArray();
					if (ff != null) {
						for (int n = 0; n < ff.size(); n++) {
							JsonObject f = ff.get(n).getAsJsonObject();
							String s = f.get("value").getAsString();
							long c = f.get("count").getAsLong();
							this.wordCount.put(s, c);

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

	public List<Keyword> getCooccurrenceSortedByIDF(String word, int facetCount) {

		List<Keyword> kwds = getCooccurrence(word, facetCount);

		kwds.forEach(k -> k.setCorrelation(getIdf(k.getLex())));

		Collections.sort(kwds, new Comparator<Keyword>() {

			@Override
			public int compare(Keyword o1, Keyword o2) {

				return (o2.getCorrelation() - o1.getCorrelation() < 0) ? -1 : 1;

			}
		});

		return kwds;
	}

}
