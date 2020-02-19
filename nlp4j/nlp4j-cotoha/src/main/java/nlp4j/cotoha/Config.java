package nlp4j.cotoha;

/**
 * Configurations for COTOHA Service
 * 
 * @author Hiroki Oya
 * @since 1.0.0.0
 */
public class Config {

	/**
	 * URL for access token
	 */
	static public String COTOHA_URL_ACCESSTOKEN = "https://api.ce-cotoha.com/v1/oauth/accesstokens";

	/**
	 * client_id
	 */
	static public String COTOHA_CLIENT_ID = null;

	/**
	 * client_secret
	 */
	static public String COTOHA_CLIENT_SECRET = null;

	/**
	 * api_base_url
	 */
	static public String API_BASE_URL = "https://api.ce-cotoha.com/api/dev";

	static {
		// https://api.ce-cotoha.com/contents/mypage/index.html
		COTOHA_CLIENT_ID = System.getProperty("COTOHA_CLIENT_ID", "VtGKQ9GQxUOkGGvhAHSSoATKNAjQ7Ocn");
		COTOHA_CLIENT_SECRET = System.getProperty("COTOHA_CLIENT_SECRET", "uNRzcGVTG1rBTGeq");
		COTOHA_URL_ACCESSTOKEN = System.getProperty("COTOHA_URL_ACCESSTOKEN",
				"https://api.ce-cotoha.com/v1/oauth/accesstokens");
	}

}
