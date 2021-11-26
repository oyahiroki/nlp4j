package nlp4j.solr;

import org.apache.solr.common.params.MapSolrParams;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.solr.search.SolrSearchClient;

public class SolrSearchClientTestCase extends TestCase {

	public void testSearchStringString() {
	}

	public void testSearchStringJsonObject() {
	}

	public void testConvertRequestParams001() throws Exception {
		String endPoint = "http://localhost:8983/solr/";
		SolrSearchClient client = // 検索クライアントの初期化
				new SolrSearchClient.Builder(endPoint).build();
		JsonObject requestObject = new JsonObject();
		{
			String search = "item:学校";
			requestObject.addProperty("search", search);
			requestObject.addProperty("top", 1);
		}
		MapSolrParams params = client.convertRequestParams(requestObject);
		System.err.println(params);
	}

	public void testConvertRequestParams002() throws Exception {
		String endPoint = "http://localhost:8983/solr/";
		SolrSearchClient client = // 検索クライアントの初期化
				new SolrSearchClient.Builder(endPoint).build();
		JsonObject requestObject = new JsonObject();
		{
			String search = "item:学校";
			requestObject.addProperty("search", search);
			requestObject.addProperty("top", 1);
			JsonArray facets = new JsonArray();
			facets.add("item1");
			facets.add("item2");
			requestObject.add("facets", facets);
		}
		MapSolrParams params = client.convertRequestParams(requestObject);
		System.err.println(params);
	}

}
