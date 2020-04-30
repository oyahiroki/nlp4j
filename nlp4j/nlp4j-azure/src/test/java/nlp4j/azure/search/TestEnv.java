package nlp4j.azure.search;

import com.azure.core.util.Configuration;

public class TestEnv {

	public static final String SERVICE_NAME = Configuration.getGlobalConfiguration()
			.get("AZURE_COGNITIVE_SEARCH_SERVICE_NAME");
	public static final String INDEX_NAME = Configuration.getGlobalConfiguration()
			.get("AZURE_COGNITIVE_SEARCH_INDEX_NAME");
	public static final String ADMIN_KEY = Configuration.getGlobalConfiguration()
			.get("AZURE_COGNITIVE_SEARCH_ADMIN_KEY");

}
