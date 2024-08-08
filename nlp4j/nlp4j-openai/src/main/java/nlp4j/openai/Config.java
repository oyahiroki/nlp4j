package nlp4j.openai;

public class Config {

//	private String organization;
//	private String apiKey;

	static private String apiKey = System.getProperty("openai.api_key", null);
	static private String organization = System.getProperty("openai.organization", null);

	public Config() {
		super();
	}

	public Config(String organization, String apiKey) {
		super();
		Config.organization = organization;
		Config.apiKey = apiKey;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		Config.organization = organization;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		Config.apiKey = apiKey;
	}

	@Override
	public String toString() {
		return "Configuration [organization=" + organization + ", apiKey=" + apiKey + "]";
	}

}
