package nlp4j.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

/**
 * @author Hiroki Oya
 * @since 1.1
 *
 */
public class DocumentUtilTestCase extends TestCase {

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

		String expected = "{\"keywords\":[{\"facet\":\"word.NN\",\"lex\":\"test\",\"str\":\"TEST\",\"count\":-1,\"begin\":-1,\"end\":-1,\"correlation\":0.0,\"sequence\":-1}],\"field1\":\"TEST\",\"text\":\"This is test.\"}";

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

		String expected = "{\n" + //
				"  \"field1\": \"TEST\",\n" + //
				"  \"field2\": 100,\n" + //
				"  \"date\": \"2020-04-01T00:00:00+0900\",\n" + //
				"  \"text\": \"This is test.\",\n" + //
				"  \"keywords\": [\n" + //
				"    {\n" + //
				"      \"facet\": \"word.NN\",\n" + //
				"      \"lex\": \"test\",\n" + //
				"      \"str\": \"TEST\",\n" + //
				"      \"count\": -1,\n" + //
				"      \"begin\": -1,\n" + //
				"      \"end\": -1,\n" + //
				"      \"correlation\": 0.0,\n" + //
				"      \"sequence\": -1\n" + //
				"    }\n" + //
				"  ]\n" + //
				"}"; //

		System.err.println(JsonUtils.prettyPrint(json).length());
		System.err.println(expected.length());
		assertEquals(expected, JsonUtils.prettyPrint(json));

	}

	/**
	 * @since 1.3
	 */
	public void testToJsonOjbectForIndex001() {
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
		{
			doc.putAttribute("field1", "TEST");
			doc.putAttribute("field2", 100);
			doc.putAttribute("date", new Date());
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
