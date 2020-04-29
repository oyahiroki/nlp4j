package nlp4j.azure.admin;

import java.io.IOException;

import nlp4j.azure.search.admin.AzureSearchIndexAdmin;
import nlp4j.azure.search.admin.Config;

public class AzureSearchIndexAdmin002Get {

	static public void main(String[] args) throws IOException {

		// AZURE_COGNITIVE_SEARCH_ADMIN_KEY
		String adminKey = Config.ADMIN_KEY;

		if (adminKey == null) {
			System.err.println("adminKey: null");
			return;
		}

		String serviceName = "nlp4j-search";
		String indexName = "search1";
		String outFileName = "src/test/resources/collections/search1/index.json";

		AzureSearchIndexAdmin admin = new AzureSearchIndexAdmin();

		admin.get(adminKey, serviceName, indexName, outFileName);

	}

}
