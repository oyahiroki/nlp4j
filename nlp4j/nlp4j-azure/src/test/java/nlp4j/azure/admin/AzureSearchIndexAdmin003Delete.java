package nlp4j.azure.admin;

import java.io.IOException;

import nlp4j.azure.search.admin.AzureSearchIndexAdmin;

public class AzureSearchIndexAdmin003Delete {

	static public void main(String[] args) throws IOException {
		// AZURE_COGNITIVE_SEARCH_ADMIN_KEY
		String adminKey = Config.ADMIN_KEY;

		if (adminKey == null) {
			System.err.println("adminKey: null");
			return;
		}

		String serviceName = "nlp4j-search";
		String indexName = "search1";

		AzureSearchIndexAdmin admin = new AzureSearchIndexAdmin();

		admin.delete(adminKey, serviceName, indexName);
	}

}
