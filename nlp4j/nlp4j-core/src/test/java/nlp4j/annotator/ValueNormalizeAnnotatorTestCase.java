package nlp4j.annotator;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.DocumentUtil;

public class ValueNormalizeAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument001() throws Exception {
		Document doc = new DefaultDocument();

		doc.putAttribute("key1", "ＡＢＣ　Ⅰ・Ⅱ・Ⅲ");

		List<String> list = new ArrayList<String>();
		list.add("Ａ");
		list.add("Ｂ");
		list.add("Ｃ");

		doc.putAttribute("key2", list);

		String[] arr = { "Ａ", "Ｂ", "Ｃ" };

		doc.putAttribute("key3", arr);

		System.err.println(DocumentUtil.toJsonPrettyString(doc));

		ValueNormalizeAnnotator ann = new ValueNormalizeAnnotator();

		ann.annotate(doc);

		System.err.println(DocumentUtil.toJsonPrettyString(doc));

	}

}
