package nlp4j.openai;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Messages {

	JsonArray messages = new JsonArray();

	public void add(String role, String content) {
		JsonObject message = new JsonObject();
		message.addProperty("role", role);
		message.addProperty("content", content);
		this.messages.add(message);
	}

	public JsonArray toJsonArray() {
		return this.messages;
	}

}
