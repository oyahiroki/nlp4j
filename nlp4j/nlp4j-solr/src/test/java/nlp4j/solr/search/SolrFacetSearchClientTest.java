package nlp4j.solr.search;

import java.io.IOException;
import java.util.List;

import nlp4j.Keyword;

public class SolrFacetSearchClientTest {
	public static void check(String item) throws IOException {
		String endPoint = "http://localhost:8983/solr/";
		String indexName1 = "cb20220501b";
		String facet = "wikilink_all_ss";

		String search = facet + ":" + item;

		SolrFacetSearchClient c = new SolrFacetSearchClient(endPoint, indexName1, facet, 1000000L);
		{
			System.err.println("全文書数: " + c.getCountDocAll());
		}
		{
			System.err.println("全語彙の中で「" + item + "」の順位: " + c.getRank(item));
		}
		{
			System.err.println("「" + item + "」を含む文書の数: " + c.getCountAll(item));
		}

//		{
//			List<String> kwds = c.getTopKeywords();
//			for (int n = 0; n < kwds.size(); n++) {
//				String kwd = kwds.get(n);
//				System.err.println("" + (n + 1) + ": " + kwd + ", " + c.getCountAll(kwd));
//			}
//		}

		{
			List<Keyword> kwds = c.facetSearch(search, 100);
			for (Keyword k : kwds) {
				System.err.println(k.getLex() + "," + k.getCorrelation());
			}
		}

		System.err.println("---");
		{
			List<Keyword> kwds = c.facetSearch(search, 100, "(\\d+年|\\d+年代|\\d+月\\d+日)");
			for (Keyword k : kwds) {
				System.err.println(k.getLex() + "," + k.getCorrelation());
			}
		}
	}
}
