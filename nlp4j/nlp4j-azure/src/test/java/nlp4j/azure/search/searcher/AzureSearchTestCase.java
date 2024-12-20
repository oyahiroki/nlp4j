package nlp4j.azure.search.searcher;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.InvalidPropertyException;
import nlp4j.azure.search.TestEnv;
import nlp4j.util.JsonUtils;

public class AzureSearchTestCase extends TestCase {

	public void testSearch001() throws Exception {
		try {
			JsonObject requestObj = new JsonObject();
			{
				requestObj.addProperty("count", true);
				requestObj.addProperty("search", "*");
			}

			AzureSearch searcher = new AzureSearch();
			searcher.setProperty("service_name", TestEnv.SERVICE_NAME);
			searcher.setProperty("index_name", TestEnv.INDEX_NAME);
			searcher.setProperty("admin_key", TestEnv.ADMIN_KEY);

			JsonObject res = searcher.search(requestObj);

			assertEquals(200, res.get("@code").getAsInt());
		} catch (InvalidPropertyException e) {
			// pass
		}
	}

	public void testSearch002() throws Exception {
		try {
			String json = "{'count':true,'search':'*'}";

			AzureSearch searcher = new AzureSearch();
			searcher.setProperty("service_name", TestEnv.SERVICE_NAME);
			searcher.setProperty("index_name", TestEnv.INDEX_NAME);
			searcher.setProperty("admin_key", TestEnv.ADMIN_KEY);

			JsonObject res = searcher.search(json);

			assertEquals(200, res.get("@code").getAsInt());
		} catch (InvalidPropertyException e) {
			// pass
		}
	}

	public void testSearch003() throws Exception {
		try {
			String json = "{'count':true,'search':'*','top':0,'facets':['field1,count:100,sort:count']}";

			AzureSearch searcher = new AzureSearch();
			searcher.setProperty("service_name", TestEnv.SERVICE_NAME);
			searcher.setProperty("index_name", TestEnv.INDEX_NAME);
			searcher.setProperty("admin_key", TestEnv.ADMIN_KEY);

			JsonObject res = searcher.search(json);

			assertEquals(200, res.get("@code").getAsInt());
		} catch (InvalidPropertyException e) {
			// pass
		}
	}

	public void testSearch004() throws Exception {
		try {
			String json = "{'count':true,'search':'*','top':0,'facets':['date,interval:month']}";

			AzureSearch searcher = new AzureSearch();
			searcher.setProperty("service_name", TestEnv.SERVICE_NAME);
			searcher.setProperty("index_name", TestEnv.INDEX_NAME);
			searcher.setProperty("admin_key", TestEnv.ADMIN_KEY);

			JsonObject res = searcher.search(json);

			assertEquals(200, res.get("@code").getAsInt());
		} catch (InvalidPropertyException e) {
			// pass
		}
	}

	public void testDeviation001() throws Exception {
		String json1 = "{'count':true,'search':'*','top':0,'facets':['date,interval:month'],'queryType':'full','searchMode':'all'}";
		String json2 = "{'count':true,'search':'*','top':0,'facets':['date,interval:month','field1,count:100,sort:count'],'queryType':'full','searchMode':'all'}";

		AzureSearch searcher = new AzureSearch();
		if (TestEnv.SERVICE_NAME == null) {
			System.err.println("Skip this test");
			return;
		}
		searcher.setProperty("service_name", TestEnv.SERVICE_NAME);
		searcher.setProperty("index_name", TestEnv.INDEX_NAME);
		searcher.setProperty("admin_key", TestEnv.ADMIN_KEY);

		JsonObject res = searcher.searchDeviation(json1, json2);

		System.err.println(JsonUtils.prettyPrint(res));

//		assertEquals(200, res.get("@code").getAsInt());
	}

	public void testDeviation002() throws Exception {
		// '@@view':'deviations'
		// facets[0] -> 'date,interval:month'
		// facets[1] -> 'field1,count:100,sort:count'
		String json1 = "{" //
				+ "'@@view':'deviations',"//
				+ "'count':true,'search':'*','top':0,"//
				+ "'facets':['date,interval:month','field1,count:100,sort:value'],'queryType':'full','searchMode':'all'}";

		AzureSearch searcher = new AzureSearch();
		if (TestEnv.SERVICE_NAME == null) {
			System.err.println("Skip this test");
			return;
		}
		searcher.setProperty("service_name", TestEnv.SERVICE_NAME);
		searcher.setProperty("index_name", TestEnv.INDEX_NAME);
		searcher.setProperty("admin_key", TestEnv.ADMIN_KEY);

		JsonObject res = searcher.searchDeviation(json1);
		System.err.println("response---");
		System.err.println(JsonUtils.prettyPrint(res));

//		assertEquals(200, res.get("@code").getAsInt());
	}

}
