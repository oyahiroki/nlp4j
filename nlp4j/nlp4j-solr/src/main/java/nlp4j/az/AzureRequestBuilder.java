package nlp4j.az;

import com.google.gson.JsonObject;

public class AzureRequestBuilder implements Builder<JsonObject> {
	JsonObject requestObject = new JsonObject();
	{
		String search = "*:*";
		requestObject.addProperty("search", search);
		requestObject.addProperty("top", 0);
	}

	@Override
	public JsonObject build() {
		return requestObject;
	}

}
