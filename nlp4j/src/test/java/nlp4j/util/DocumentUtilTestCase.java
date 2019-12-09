package nlp4j.util;

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
