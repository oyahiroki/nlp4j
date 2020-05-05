package nlp4j.azure.search.searcher;

import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.InvalidPropertyException;
import nlp4j.azure.search.TestEnv;

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

}
