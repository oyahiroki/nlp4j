package nlp4j.solr.search;

import org.apache.solr.common.params.SolrParams;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import junit.framework.TestCase;

/**
 * <pre>
 * Azure Query:
 * https://docs.microsoft.com/en-us/rest/api/searchservice/search-documents
 * https://docs.microsoft.com/ja-jp/rest/api/searchservice/search-documents
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class SolrRequestConverterTestCase extends TestCase {

	public void testConvertRequestParamsJsonObject001() {
		JsonObject requestObject = new JsonObject();
		{
			String search = "item:学校";
			requestObject.addProperty("search", search);
			requestObject.addProperty("top", 1);
		}
		SolrParams params = SolrRequestConverter.convertRequestParams(requestObject);
		System.err.println(params);
	}

	public void testConvertRequestParamsJsonObject002() {
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
		SolrParams params = SolrRequestConverter.convertRequestParams(requestObject);
		System.err.println(params);
	}

	public void testConvertRequestParamsJsonString001() {
		String json = "{" //
				+ "search:'*:*'," //
				+ "facets:['word_ss,count:10']," //
				+ "top:0" //
				+ "}" //
		;
		SolrParams params = SolrRequestConverter.convertRequestParams(json);
		System.err.println(params.toString());
		for (String p : params.toString().split("&")) {
			System.err.println(p);
		}

	}

	public void testConvertRequestParamsJsonString002() {
		String json = "{" //
				+ "search:'*:*'," //
				+ "facets:['word_ss,count:10','wikilink_ss,count:10']," //
				+ "top:0" //
				+ "}" //
		;
		SolrParams params = SolrRequestConverter.convertRequestParams(json);
		System.err.println("<params>");
		System.err.println(params.toString());
		System.err.println("</params>");
		System.err.println("<params>");
		for (String p : params.toString().split("&")) {
			System.err.println(p);
		}
		System.err.println("</params>");
	}
	public void testConvertRequestParamsJsonString003() {
		String json = "{" //
				+ "search:'*:*'," //
				+ "facets:['word_ss,count:10','wikilink_ss,count:10']," //
				+ "select:'id, field1, field2'," //
				+ "top:0" //
				+ "}" //
		;
		SolrParams params = SolrRequestConverter.convertRequestParams(json);
		System.err.println("<params>");
		System.err.println(params.toString());
		System.err.println("</params>");
		System.err.println("<params>");
		for (String p : params.toString().split("&")) {
			System.err.println(p);
		}
		System.err.println("</params>");
	}

}
