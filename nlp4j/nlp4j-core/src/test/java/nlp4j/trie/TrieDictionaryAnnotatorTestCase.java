package nlp4j.trie;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentBuilder;

public class TrieDictionaryAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument001() throws Exception {

		Document doc = (new DocumentBuilder()).text("ABCDEA").build();

		System.err.println(doc.toString());

		TrieDictionaryAnnotator ann = new TrieDictionaryAnnotator();
		{
			ann.setProperty("target", "text");
			ann.setProperty("add1", "A");
			ann.setProperty("add1", "ABC");
			ann.setProperty("add1", "BCD");
			ann.setProperty("add1", "XXX");
		}

		ann.annotate(doc);

		System.err.println(doc.toString());

	}

}
