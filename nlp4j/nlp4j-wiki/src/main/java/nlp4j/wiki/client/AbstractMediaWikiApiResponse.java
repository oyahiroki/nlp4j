package nlp4j.wiki.client;

import com.google.gson.JsonObject;

public abstract class AbstractMediaWikiApiResponse implements MediaWikiApiResponse {

	private JsonObject jo;

	public AbstractMediaWikiApiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AbstractMediaWikiApiResponse(JsonObject jo) {
		super();
		this.jo = jo;
	}

	public String text() {
		return getText();
	}

}
