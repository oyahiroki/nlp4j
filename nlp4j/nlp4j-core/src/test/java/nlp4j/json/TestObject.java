package nlp4j.json;

import com.google.gson.JsonObject;

public class TestObject implements JsonElement {

	String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("value", this.value);
		return jsonObj;
	}

	@Override
	public TestObject fromJson(JsonObject o) {

//		TestObject o2 = new TestObject();
//		if (o.get("value") != null) {
//			o2.value = o.get("value").getAsString();
//		}
//		return o2;

		if (o.get("value") != null) {
			this.value = o.get("value").getAsString();
		}
		return this;
	}

	@Override
	public String toString() {
		return "TestObject [value=" + value + "]";
	}

}
