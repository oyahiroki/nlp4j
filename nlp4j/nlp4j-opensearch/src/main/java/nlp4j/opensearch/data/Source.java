package nlp4j.opensearch.data;

import java.util.Map;

import com.google.gson.JsonObject;

public class Source {

	private JsonObject attributes;

	public JsonObject getAttributes() {
		return attributes;
	}

	public void setAttributes(JsonObject attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "Source [attributes=" + attributes + "]";
	}

//	private Map<String, Object> attributes;
//
//	public Map<String, Object> getAttributes() {
//		return attributes;
//	}
//
//	public void setAttributes(Map<String, Object> attributes) {
//		this.attributes = attributes;
//	}
//
//	@Override
//	public String toString() {
//		return "Source [attributes=" + attributes + "]";
//	}
}
