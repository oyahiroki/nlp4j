package nlp4j.azure.search.searcher;

import com.azure.core.util.Configuration;
import com.google.gson.JsonObject;

import junit.framework.TestCase;

public class AzureSearchTest extends TestCase {

	private static final String ENDPOINT = Configuration.getGlobalConfiguration()
			.get("AZURE_COGNITIVE_SEARCH_ENDPOINT");
	private static final String ADMIN_KEY = Configuration.getGlobalConfiguration()
			.get("AZURE_COGNITIVE_SEARCH_ADMIN_KEY");

	public void testSearch001() throws Exception {

		JsonObject requestObj = new JsonObject();
		{
			requestObj.addProperty("count", true);
			requestObj.addProperty("search", "*");
		}

		AzureSearch searcher = new AzureSearch();
		searcher.setProperty("endpoint", ENDPOINT);
		searcher.setProperty("admin_key", ADMIN_KEY);

		searcher.search(requestObj);

	}

}
