package nlp4j.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Lightweight JSON wrapper for Gson.
 *
 * Features: - Object / Array unified as JsonNode - null-safe access - easy DSL
 * traversal - suitable for OpenSearch-style JSON
 */
public class JsonNode {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final JsonElement element;

	// =========================================================
	// Constructor
	// =========================================================

	private JsonNode(JsonElement element) {
		this.element = element == null ? JsonNull.INSTANCE : element;
	}

	// =========================================================
	// Factory
	// =========================================================

	public JsonNode() {
		this.element = JsonNull.INSTANCE;
	}

	public static JsonNode parse(String json) {
		return new JsonNode(JsonParser.parseString(json));
	}

	public static JsonNode of(JsonElement element) {
		return new JsonNode(element);
	}

	public static JsonNode object() {
		return new JsonNode(new JsonObject());
	}

	public static JsonNode array() {
		return new JsonNode(new JsonArray());
	}

	// =========================================================
	// Type
	// =========================================================

	public boolean isObject() {
		return element.isJsonObject();
	}

	public boolean isArray() {
		return element.isJsonArray();
	}

	public boolean isPrimitive() {
		return element.isJsonPrimitive();
	}

	public boolean isNull() {
		return element == null || element.isJsonNull();
	}

	// =========================================================
	// Object Access
	// =========================================================

	public JsonNode get(String key) {

		if (!isObject()) {
			return new JsonNode(JsonNull.INSTANCE);
		}

		JsonObject obj = element.getAsJsonObject();

		JsonElement e = obj.get(key);

		return new JsonNode(e == null ? JsonNull.INSTANCE : e);
	}

	public boolean has(String key) {

		if (!isObject()) {
			return false;
		}

		return element.getAsJsonObject().has(key);
	}

	public Set<String> keys() {

		if (!isObject()) {
			return Collections.emptySet();
		}

		return element.getAsJsonObject().keySet();
	}

	// =========================================================
	// Array Access
	// =========================================================

	public JsonNode get(int index) {

		if (!isArray()) {
			return new JsonNode(JsonNull.INSTANCE);
		}

		JsonArray arr = element.getAsJsonArray();

		if (index < 0 || index >= arr.size()) {
			return new JsonNode(JsonNull.INSTANCE);
		}

		return new JsonNode(arr.get(index));
	}

	public int size() {

		if (isArray()) {
			return element.getAsJsonArray().size();
		}

		if (isObject()) {
			return element.getAsJsonObject().size();
		}

		return 0;
	}

	public List<JsonNode> asList() {

		if (!isArray()) {
			return Collections.emptyList();
		}

		List<JsonNode> list = new ArrayList<>();

		for (JsonElement e : element.getAsJsonArray()) {
			list.add(new JsonNode(e));
		}

		return list;
	}

	// =========================================================
	// Primitive Access
	// =========================================================

	public String asString() {

		if (!isPrimitive()) {
			return null;
		}

		return element.getAsString();
	}

	public String asString(String defaultValue) {

		String v = asString();

		return v == null ? defaultValue : v;
	}

	public int asInt() {

		if (!isPrimitive()) {
			return 0;
		}

		return element.getAsInt();
	}

	public int asInt(int defaultValue) {

		try {
			return asInt();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public long asLong(long defaultValue) {

		try {

			if (!isPrimitive()) {
				return defaultValue;
			}

			return element.getAsLong();

		} catch (Exception e) {
			return defaultValue;
		}
	}

	public double asDouble(double defaultValue) {
		try {
			if (!isPrimitive()) {
				return defaultValue;
			}
			return element.getAsDouble();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public boolean asBoolean(boolean defaultValue) {

		try {

			if (!isPrimitive()) {
				return defaultValue;
			}

			return element.getAsBoolean();

		} catch (Exception e) {
			return defaultValue;
		}
	}

	// =========================================================
	// Put (Object)
	// =========================================================

	public JsonNode put(String key, String value) {

		ensureObject();

		element.getAsJsonObject().addProperty(key, value);

		return this;
	}

	public JsonNode put(String key, Number value) {

		ensureObject();

		element.getAsJsonObject().addProperty(key, value);

		return this;
	}

	public JsonNode put(String key, Boolean value) {

		ensureObject();

		element.getAsJsonObject().addProperty(key, value);

		return this;
	}

	public JsonNode put(String key, JsonNode value) {

		ensureObject();

		element.getAsJsonObject().add(key, value.element);

		return this;
	}

	// =========================================================
	// Add (Array)
	// =========================================================

	public JsonNode add(String value) {

		ensureArray();

		element.getAsJsonArray().add(value);

		return this;
	}

	public JsonNode add(Number value) {

		ensureArray();

		element.getAsJsonArray().add(value);

		return this;
	}

	public JsonNode add(Boolean value) {

		ensureArray();

		element.getAsJsonArray().add(value);

		return this;
	}

	public JsonNode add(JsonNode value) {

		ensureArray();

		element.getAsJsonArray().add(value.element);

		return this;
	}

	// =========================================================
	// Helper
	// =========================================================

	private void ensureObject() {

		if (!isObject()) {
			throw new IllegalStateException("JsonNode is not object");
		}
	}

	private void ensureArray() {

		if (!isArray()) {
			throw new IllegalStateException("JsonNode is not array");
		}
	}

	public String firstKey() {

		if (!isObject()) {
			return null;
		}

		Iterator<String> it = keys().iterator();

		return it.hasNext() ? it.next() : null;
	}

	// =========================================================
	// Raw
	// =========================================================

	public JsonElement raw() {
		return element;
	}

	public JsonObject rawObject() {
		return element.getAsJsonObject();
	}

	public JsonArray rawArray() {
		return element.getAsJsonArray();
	}

	// =========================================================
	// Iterable
	// =========================================================

	public Iterator<JsonNode> iterator() {
		return asList().iterator();
	}

	// =========================================================
	// JSON
	// =========================================================

	public String toJson() {
		return GSON.toJson(element);
	}

	@Override
	public String toString() {
		return toJson();
	}

	public String[] keySet() {
		return this.element == null ? new String[0] : element.getAsJsonObject().keySet().toArray(new String[0]);
	}

	public JsonNode getAsJsonObject(String key) {
		return get(key);
	}

	public int getAsInt() {
		return asInt();
	}
}
