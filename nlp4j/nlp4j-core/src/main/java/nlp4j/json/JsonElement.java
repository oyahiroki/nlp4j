package nlp4j.json;

import com.google.gson.JsonObject;

public interface JsonElement {

	public JsonObject toJsonObject();

	public JsonElement fromJson(JsonObject o);

}
