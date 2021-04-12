package nlp4j.solr.search;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Keyword;

public class WordCounterTestCase extends TestCase {

	public void test001() throws Exception {

		String endPoint = "http://localhost:8983/solr";

		SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();

		WordCounter wc = new WordCounter(client);

		System.err.println("全文書数:" + wc.getCountAll());

		System.err.println("日本:" + wc.getCount("日本"));

		System.err.println("日本xx:" + wc.getCount("日本xx"));

		System.err.println("東京オリンピック:" + wc.getCount("東京オリンピック"));

		System.err.println("idf(日本):" + wc.getIdf("日本"));

		System.err.println("idf(関東地方):" + wc.getIdf("関東地方"));

//		wc.getCooccurrence("日本", 300).forEach(k -> {
//			System.err.println(k.getLex() + " " + k.getCount() + " " + wc.getIdf(k.getLex()));
//		});

		System.err.println("医者:" + wc.getCount("医者"));

		System.err.println("---");

		{
			List<Keyword> kwds = wc.getCooccurrenceSortedByIDF("医者", 10000);
			kwds.forEach(k -> {
				System.err.println(k.getLex() + " " + k.getCount() + " " + k.getCorrelation());
			});
		}

	}

	public void test002() throws Exception {
		String endPoint = "http://localhost:8983/solr";

		SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();

		WordCounter wc = new WordCounter(client);

		List<Keyword> kwds = wc.getTopKeywords(100);
		kwds.stream().forEach(k -> System.err.println(k.getLex() + " " + k.getCount()));

	}

}
