package nlp4j.json;

import junit.framework.TestCase;

public class JsonNodeTestCase extends TestCase {

	public void testJsonNode() {
		
	}

	public void testParse() {
		
	}

	public void testOf001() {
		JsonNode jo = JsonNode.parse("{'key1':'value1'}");
		assertNotNull(jo);
		System.err.println(jo.toString());
	}

	public void testOf002() {
		JsonNode jo = JsonNode.parse("[1,2,3]");
		assertNotNull(jo);
		System.err.println(jo.toString());
		System.err.println(jo.get(0));
	}

	public void testObject() {
		
	}

	public void testArray() {
		
	}

	public void testIsObject() {
		
	}

	public void testIsArray() {
		
	}

	public void testIsPrimitive() {
		
	}

	public void testIsNull() {
		
	}

	public void testGetString() {
		
	}

	public void testHas() {
		
	}

	public void testKeys() {
		
	}

	public void testGetInt() {
		
	}

	public void testSize() {
		
	}

	public void testAsList() {
		
	}

	public void testAsString() {
		
	}

	public void testAsStringString() {
		
	}

	public void testAsInt() {
		
	}

	public void testAsIntInt() {
		
	}

	public void testAsLong() {
		
	}

	public void testAsDouble() {
		
	}

	public void testAsBoolean() {
		
	}

	public void testPutStringString() {
		
	}

	public void testPutStringNumber() {
		
	}

	public void testPutStringBoolean() {
		
	}

	public void testPutStringJsonNode() {
		
	}

	public void testAddString() {
		
	}

	public void testAddNumber() {
		
	}

	public void testAddBoolean() {
		
	}

	public void testAddJsonNode() {
		
	}

	public void testFirstKey() {
		
	}

	public void testRaw() {
		
	}

	public void testRawObject() {
		
	}

	public void testRawArray() {
		
	}

	public void testIterator() {
		
	}

	public void testToJson() {
		
	}

	public void testToString() {
		
	}

	public void testKeySet() {
		
	}

	public void testGetAsJsonObject() {
		
	}

}
