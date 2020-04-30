package nlp4j.azure.admin;

import com.azure.core.util.Configuration;

/**
 * Config for Azure
 * 
 * @author Hiroki Oya
 *
 */
public class Config {

	/**
	 * ENDPOINT set as AZURE_COGNITIVE_SEARCH_ENDPOINT
	 */
	public static final String ENDPOINT = Configuration //
			.getGlobalConfiguration() //
			.get("AZURE_COGNITIVE_SEARCH_ENDPOINT");

	/**
	 * ADMIN_KEY set as AZURE_COGNITIVE_SEARCH_ADMIN_KEY
	 */
	public static final String ADMIN_KEY = Configuration //
			.getGlobalConfiguration() //
			.get("AZURE_COGNITIVE_SEARCH_ADMIN_KEY");

}
