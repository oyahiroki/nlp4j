package nlp4j.azure.admin;

import java.io.File;
import java.io.IOException;

public class AzureSearchIndexAdmin001Post {

	public static void main(String[] args) throws IOException {
		// AZURE_COGNITIVE_SEARCH_ADMIN_KEY
		String adminKey = Config.ADMIN_KEY;

		if (adminKey == null) {
			System.err.println("adminKey: null");
			return;
		}

		String serviceName = "nlp4j-search";
		String indexName = "search1";
		String outFileName = "src/test/resources/collections/search1/index.json";
		File inFile = new File(outFileName);

		AzureSearchIndexAdmin admin = new AzureSearchIndexAdmin();

		admin.post(adminKey, serviceName, inFile);
	}

}
