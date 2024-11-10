package nlp4j.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;
import nlp4j.impl.DefaultKeywordWithDependency;

/**
 * @author Hiroki Oya
 * @since 1.1
 *
 */
public class DocumentUtilTestCase extends TestCase {

	public void testReadFromLineSeparatedJson001() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/DocumentUtilTestCase001.txt");
		List<Document> docs = DocumentUtil.readFromLineSeparatedJson(file);
		for (Document doc : docs) {
			System.err.println(DocumentUtil.toJsonPrettyString(doc));
		}
	}

	/**
	 * @since 1.3.7.13
	 */
	public void testToMapDocument001() {

		Document doc = (new DocumentBuilder()).put("aaa", "111").put("bbb", "222").put("ccc", "333").create();

		Map<String, Object> map = DocumentUtil.toMap(doc);
		map.forEach((key, value) -> {
			System.err.println("key=" + key + ",value=" + value);

		});
	}

	public void testParseFromJson001() throws Exception {
		String json = "{}";
		Document doc = DocumentUtil.parseFromJson(json);
		assertNotNull(doc);
		System.err.println(doc);
	}

	public void testParseFromJson002() throws Exception {
		// plain string
		String s = "This is test.";
		String json = "{'text':'" + s + "'}";
		Document doc = DocumentUtil.parseFromJson(json);
		assertNotNull(doc);
		System.err.println(doc);
		assertNotNull(doc.getAttribute("text"));
		System.err.println("text=" + doc.getAttribute("text"));
		assertEquals(s, doc.getAttribute("text"));
	}

	public void testParseFromJson003() throws Exception {
		// plain text and keywords
		String json = "{" //
				+ "'text':'This is test.',"//
				+ "keywords:[{'begin':0,'end':4,'facet':'noun','lex':'test','str':'test'}]"//
				+ "}";
		Document doc = DocumentUtil.parseFromJson(json);
		assertNotNull(doc);
		System.err.println(doc);
		assertNotNull(doc.getAttribute("text"));
		System.err.println("text=" + doc.getAttribute("text"));
		assertEquals(1, doc.getKeywords().size());
	}

	/**
	 * created_at: 2022-09-06
	 * 
	 * @throws Exception
	 */
	public void testParseFromJson004() throws Exception {
		// plain text and array
		String json = "{" //
				+ "'text':'This is test.',"//
				+ "'arr':['aaa','bbb','ccc']"//
				+ "}";
		Document doc = DocumentUtil.parseFromJson(json);
		assertNotNull(doc);
		System.err.println(doc);
		assertNotNull(doc.getAttribute("text"));
		assertEquals("This is test.", doc.getAttribute("text"));
		System.err.println("text=" + doc.getAttribute("text"));
		System.err.println(doc.getAttribute("arr"));
		System.err.println(doc.getAttribute("arr").getClass().getName());
		assertTrue(doc.getAttribute("arr") instanceof List);
	}

	/**
	 * created_at: 2022-09-06
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void testParseFromJson005() throws Exception {
		// plain text and array
		String json = "{" //
				+ "'text':'This is test.',"//
				+ "'arr':[1,2,3]"//
				+ "}";
		Document doc = DocumentUtil.parseFromJson(json);

		assertNotNull(doc);

		System.err.println(doc);

		{ // check test attribute
			assertNotNull(doc.getAttribute("text"));
			assertEquals("This is test.", doc.getAttribute("text"));
			System.err.println("text=" + doc.getAttribute("text"));
		}
		{ // check arr attribute
			{ // arr
				System.err.println(doc.getAttribute("arr"));
				System.err.println(doc.getAttribute("arr").getClass().getName());
				assertTrue(doc.getAttribute("arr") instanceof List);
			}
			{ // arr[0]
				System.err.println(((List) doc.getAttribute("arr")).get(0).getClass().getName());
				assertEquals(1.0d, ((List) doc.getAttribute("arr")).get(0));
			}
			{ // arr[0] - arr[2]
				List<Number> arr = doc.getAttributeAsListNumbers("arr");
				assertEquals(3, arr.size());
				assertEquals(1.0, arr.get(0));
				assertEquals(2.0, arr.get(1));
				assertEquals(3.0, arr.get(2));
			}

		}

	}

	public void testParseFromJson501() throws Exception {
		String json = "{'text':'invalid json syntax'}}}}}}}}}}";
		@SuppressWarnings("unused")
		Document doc;
		try {
			doc = DocumentUtil.parseFromJson(json);
			fail();
		} catch (Exception e) {
			System.err.println("success");
		}
	}

	/**
	 * @since 1.1
	 */
	public void testToJsonStringDocument001() {
		DefaultDocument doc = new DefaultDocument();
		{
			DefaultKeyword kwd = new DefaultKeyword();
			kwd.setFacet("word.NN");
			kwd.setStr("TEST");
			kwd.setLex("test");
			doc.addKeyword(kwd);
		}
		{
			doc.putAttribute("field1", "TEST");
			doc.setText("This is test.");
		}

		String json = DocumentUtil.toJsonString(doc);
		System.err.println(json);

		String expected = "{\"field1\":\"TEST\",\"text\":\"This is test.\",\"keywords\":[{\"facet\":\"word.NN\",\"lex\":\"test\",\"str\":\"TEST\",\"@classname\":\"nlp4j.impl.DefaultKeyword\"}]}";

		assertNotNull(json);

		assertEquals(expected, json);

	}

	/**
	 * @since 1.3
	 */
	public void testToJsonOjbect001() throws Exception {
		DefaultDocument doc = new DefaultDocument();
		{
			DefaultKeyword kwd = new DefaultKeyword();
			kwd.setFacet("word.NN");
			kwd.setStr("TEST");
			kwd.setLex("test");
			doc.addKeyword(kwd);
		}
		{
			doc.putAttribute("field1", "TEST");
			doc.putAttribute("field2", 100);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			doc.putAttribute("date", sdf.parse("2020-04-01"));
			doc.setText("This is test.");
		}

		JsonObject json = DocumentUtil.toJsonObject(doc);

		System.err.println(JsonUtils.prettyPrint(json));

		// 時刻がGMTに変換されることに注意
		// 2020-04-01T00:00:00+0900
		// 2020-03-31T15:00:00Z

		String expected = "{\r\n" + "  \"field1\": \"TEST\",\r\n" + "  \"field2\": 100,\r\n"
				+ "  \"date\": \"2020-03-31T15:00:00Z\",\r\n" + "  \"text\": \"This is test.\",\r\n"
				+ "  \"keywords\": [\r\n" + "    {\r\n" + "      \"facet\": \"word.NN\",\r\n"
				+ "      \"lex\": \"test\",\r\n" + "      \"str\": \"TEST\",\r\n" + "      \"count\": -1,\r\n"
				+ "      \"begin\": -1,\r\n" + "      \"end\": -1,\r\n" + "      \"correlation\": 0.0,\r\n"
				+ "      \"sequence\": -1,\r\n" + "      \"flag\": false\r\n" + "    }\r\n" + "  ]\r\n" + "}"; //

		System.err.println(JsonUtils.prettyPrint(json).length());
		System.err.println(expected.length());
		assertNotNull(JsonUtils.prettyPrint(json));

	}

	/**
	 * created at: 2022-05-14
	 */
	public void testToJsonOjbect002() {
		DefaultDocument doc = new DefaultDocument();
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb");
		}
		kwd1.addChild(kwd2);

		doc.addKeyword(kwd1);

		JsonObject json = DocumentUtil.toJsonObject(doc);

		System.err.println(JsonUtils.prettyPrint(json));

	}

	/**
	 * @since 1.3
	 */
	public void testToJsonOjbectForIndex001() throws Exception {
		DefaultDocument doc = new DefaultDocument();
		{
			DefaultKeyword kwd = new DefaultKeyword();
			kwd.setFacet("word.NN");
			kwd.setStr("TEST");
			kwd.setLex("test");
			doc.addKeyword(kwd);
		}
		{
			DefaultKeyword kwd = new DefaultKeyword();
			kwd.setFacet("word.NN");
			kwd.setStr("Vehicle");
			kwd.setLex("vehicle");
			doc.addKeyword(kwd);
		}
		{
			DefaultKeyword kwd = new DefaultKeyword();
			kwd.setFacet("word.VB");
			kwd.setStr("DO");
			kwd.setLex("do");
			doc.addKeyword(kwd);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse("2020-04-01");
		{
			doc.putAttribute("field1", "TEST");
			doc.putAttribute("field2", 100);
			doc.putAttribute("date", date);
			doc.setText("This is test.");
		}

		JsonObject json = DocumentUtil.toJsonObjectForIndex(doc);
		System.err.println(JsonUtils.prettyPrint(json));

	}

	/**
	 * @since 1.1
	 */
	public void testToDocumentString002() {
		String json = "{" + "\"keywords\":["
				+ "{\"facet\":\"word.NN\",\"lex\":\"test\",\"str\":\"TEST\",\"count\":-1,\"begin\":-1,\"end\":-1,\"correlation\":0.0,\"sequence\":-1}"
				+ "]," + "\"field1\":\"TEST\",\"text\":\"This is test.\"" + "}";

		Document doc = DocumentUtil.toDocument(json);
		System.err.println(doc);

		assertEquals("TEST", doc.getAttribute("field1"));
		assertEquals("This is test.", doc.getAttribute("text"));
		assertEquals("This is test.", doc.getText());
		assertEquals("test", doc.getKeywords().get(0).getLex());
	}

	public void testToDocumentString003() {
		String json = "{\"arr\":[\"a\",\"b\",\"c\"]}";

		Document doc = DocumentUtil.toDocument(json);
		System.err.println(DocumentUtil.toJsonPrettyString(doc));

		System.err.println(doc.getAttribute("arr"));

		assertEquals("[a, b, c]", doc.getAttribute("arr").toString());
	}

	/**
	 * @since 1.1
	 */
	public void testToXmlStringDocument001() {
		DefaultDocument doc = new DefaultDocument();
		{
			DefaultKeyword kwd = new DefaultKeyword();
			kwd.setFacet("word.NN");
			kwd.setStr("TEST");
			kwd.setLex("test");
			doc.addKeyword(kwd);
		}
		{
			doc.putAttribute("field1", "TEST");
			doc.setText("This is test.");
		}

		String xml = DocumentUtil.toXml(doc);
		System.err.println(XmlUtils.prettyFormatXml(xml));

	}

}
