package nlp4j.azure.search.admin;

import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.InvalidPropertyException;
import nlp4j.azure.search.TestEnv;

/**
 * @author Hiroki Oya
 * @since 1.3
 */
public class AzureSearchDocumentAdminTestCase extends TestCase {

	/**
	 * @throws Exception 例外発生時
	 */
	public void testSearchDelete001() throws Exception {
		try {
			AzureSearchDocumentAdmin admin = new AzureSearchDocumentAdmin();
			admin.setProperty("service_name", TestEnv.SERVICE_NAME);
			admin.setProperty("index_name", TestEnv.INDEX_NAME);
			admin.setProperty("admin_key", TestEnv.ADMIN_KEY);

			JsonObject requestObj = AzureSearchDocumentAdmin.createDefaultSearchRequest();

			admin.searchDelete(requestObj);
		} catch (InvalidPropertyException e) {
			// pass
		}
	}

}
