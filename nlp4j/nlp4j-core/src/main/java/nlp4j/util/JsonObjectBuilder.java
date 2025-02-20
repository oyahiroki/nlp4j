package nlp4j.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created on: 2025-2-21
 * 
 * @since 1.3.7.16
 */
public class JsonObjectBuilder {

	private JsonObject jo;

	public JsonObjectBuilder() {
		this.jo = new JsonObject();
	}

	/**
	 * @param property
	 * @param value
	 * @return
	 */
	public JsonObjectBuilder add(String property, JsonElement value) {
		this.jo.add(property, value);
		return this;
	}

	/**
	 * @param property
	 * @param value
	 * @return
	 */
	public JsonObjectBuilder add(String property, Boolean value) {
		this.jo.addProperty(property, value);
		return this;
	}

	/**
	 * @param property
	 * @param value
	 * @return
	 */
	public JsonObjectBuilder add(String property, Character value) {
		this.jo.addProperty(property, value);
		return this;
	}

	/**
	 * @param property
	 * @param value
	 * @return
	 */
	public JsonObjectBuilder add(String property, Number value) {
		this.jo.addProperty(property, value);
		return this;
	}

	/**
	 * @param property
	 * @param value
	 * @return
	 */
	public JsonObjectBuilder add(String property, String value) {
		this.jo.addProperty(property, value);
		return this;
	}

	/**
	 * @return
	 */
	public JsonObject build() {
		return this.jo;
	}

}
