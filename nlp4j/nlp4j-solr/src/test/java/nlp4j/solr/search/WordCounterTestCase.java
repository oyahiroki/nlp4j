package nlp4j.solr.search;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Keyword;

public class WordCounterTestCase extends TestCase {

	public void test001() throws Exception {
		String endPoint = "http://localhost:8983/solr/";
		String indexName = "wiki1";
		String facet = "word_ss";
		SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();
		WordCounter wc = new WordCounter(client, indexName, facet);
		System.err.println("全文書数:" + wc.getCountAll());
	}

	public void test002() throws Exception {
		String endPoint = "http://localhost:8983/solr/";
		String indexName = "wiki1";
		String facet = "word_ss";
		SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();
		WordCounter wc = new WordCounter(client, indexName, facet);
		System.err.println("日本:" + wc.getCount(facet, "日本"));
	}

	public void test003() throws Exception {
		String endPoint = "http://localhost:8983/solr/";
		String indexName = "wiki1";
		String facet = "word_ss";
		SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();
		WordCounter wc = new WordCounter(client, indexName, facet);
		System.err.println("東京オリンピック:" + wc.getCount(facet, "東京オリンピック"));
	}

	public void test501() throws Exception {
		String endPoint = "http://localhost:8983/solr/";
		String indexName = "wiki1";
		String facet = "word_ss";
		SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();
		WordCounter wc = new WordCounter(client, indexName, facet);
		System.err.println("日本xx:" + wc.getCount(facet, "日本xx"));
	}

	public void test101() throws Exception {
		String endPoint = "http://localhost:8983/solr/";
		String indexName = "wiki1";
		String facet = "word_ss";
		SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();
		WordCounter wc = new WordCounter(client, indexName, facet);
		System.err.println("idf(日本):" + wc.getIdf(facet, "日本"));
		System.err.println("idf(関東地方):" + wc.getIdf(facet, "関東地方"));
	}

	public void test102() throws Exception {
		String endPoint = "http://localhost:8983/solr/";
		String indexName = "wiki1";
		String facet = "word_ss";
		SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();
		WordCounter wc = new WordCounter(client, indexName, facet);

//		System.err.println(wc.getCooccurrence("", "日本", 300));

		wc.getCooccurrence(facet, "日本", 10).forEach(k -> {
			System.err.println(k.getLex() + " " + k.getCount() + " " + wc.getIdf(facet, k.getLex()));
		});
	}

	public void test103() throws Exception {
		String endPoint = "http://localhost:8983/solr/";
		String indexName = "wiki1";
		String facet = "word_ss";
		SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();
		WordCounter wc = new WordCounter(client, indexName, facet);
		System.err.println("医者:" + wc.getCount(facet, "医者"));

		System.err.println("---");

		{
			List<Keyword> kwds = wc.getCooccurrenceSortedByIDF(facet, "医者", 10000);
			kwds.forEach(k -> {
				System.err.println(k.getLex() + " " + k.getCount() + " " + k.getCorrelation());
			});
		}

	}

	public void test002b() throws Exception {
		String endPoint = "http://localhost:8983/solr/";
		String indexName = "wiki1";
		String facet = "word_ss";

		SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();

		WordCounter wc = new WordCounter(client, indexName, facet);

		List<Keyword> kwds = wc.getTopKeywords(facet, 100);
		kwds.stream().forEach(k -> System.err.println(k.getLex() + " " + k.getCount()));

	}

}
