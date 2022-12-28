package nlp4j.dictionary;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.util.DocumentUtil;

/**
 * @author Hiroki Oya
 * @since 1.3.7.5
 */
public class UserDictionaryStringMatchAnnotatorTestCase extends TestCase {

	public void testSetProperty101() {
		UserDictionaryStringMatchAnnotator ann = new UserDictionaryStringMatchAnnotator();
		ann.setProperty("files", "src/test/resources/nlp4j.dictionary/userdic_utf8_tsv.txt");
		System.err.println(ann.toString());
	}

	public void testAnnotateDocument101() throws Exception {
		Document doc = (new DocumentBuilder()).put("text", "私は日本アイ・ビー・エム株式会社に勤務しています．").build();
		UserDictionaryStringMatchAnnotator ann = new UserDictionaryStringMatchAnnotator();
		ann.setProperty("files", "src/test/resources/nlp4j.dictionary/userdic_utf8_tsv.txt");
		ann.setProperty("target", "text");
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		ann.annotate(doc);
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		assertTrue(doc.getKeywords().size() > 0);
	}

	public void testAnnotateDocument102() throws Exception {
		Document doc = (new DocumentBuilder()).put("text", "私は日本アイビーエム株式会社に勤務しています．").build();
		UserDictionaryStringMatchAnnotator ann = new UserDictionaryStringMatchAnnotator();
		ann.setProperty("files", "src/test/resources/nlp4j.dictionary/userdic_utf8_tsv.txt");
		ann.setProperty("target", "text");
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		ann.annotate(doc);
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		assertTrue(doc.getKeywords().size() > 0);
	}

	public void testAnnotateDocument103() throws Exception {
		Document doc = (new DocumentBuilder()).put("text", "私は日本IBM株式会社に勤務しています．").build();
		UserDictionaryStringMatchAnnotator ann = new UserDictionaryStringMatchAnnotator();
		ann.setProperty("files", "src/test/resources/nlp4j.dictionary/userdic_utf8_tsv.txt");
		ann.setProperty("target", "text");
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		ann.annotate(doc);
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		assertTrue(doc.getKeywords().size() > 0);
	}

}
