package nlp4j;

import junit.framework.TestCase;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.DocumentUtil;

/**
 * @author Hiroki Oya
 * @created_at 2021-11-16
 */
public class DocumentBuilderTestCase extends TestCase {

	public void testDocumentBuilder001() {
		Document doc = new DocumentBuilder().put("id", "DOC001").put("item1", 1).put("text", "This is test.").create();
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
	}

	public void testDocumentBuilder002() {
		Document doc = new DocumentBuilder(DefaultDocument.class).put("id", "DOC001").put("item1", 1)
				.put("text", "This is test.").create();
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
	}

	public void testDocumentBuilder003() {
		Document doc = new DocumentBuilder().v("id", "DOC001").v("item1", 1).v("text", "This is test.").c();
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
	}

}
