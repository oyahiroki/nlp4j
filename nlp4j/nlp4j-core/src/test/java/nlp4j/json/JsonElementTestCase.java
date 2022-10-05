package nlp4j.json;

import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.util.JsonUtils;

public class JsonElementTestCase extends TestCase {

	public void testToJsonObject001() throws Exception {
		TestObject te = new TestObject();
		te.setValue("ABC");
		System.err.println(JsonUtils.prettyPrint(te.toJsonObject()));
	}

	public void testFromJson001() throws Exception {

		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("value", "XYZ");

		TestObject te = (new TestObject()).fromJson(jsonObj);

		System.err.println(te.toString());

	}

}
