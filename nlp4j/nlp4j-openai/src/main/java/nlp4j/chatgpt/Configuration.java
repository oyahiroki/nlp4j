package nlp4j.chatgpt;

public class Configuration {

	private String organization;
	private String apiKey;

	public Configuration(String organization, String apiKey) {
		super();
		this.organization = organization;
		this.apiKey = apiKey;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public String toString() {
		return "Configuration [organization=" + organization + ", apiKey=" + apiKey + "]";
	}

}
