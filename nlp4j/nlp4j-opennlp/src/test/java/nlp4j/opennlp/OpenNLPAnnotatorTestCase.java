package nlp4j.opennlp;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;

public class OpenNLPAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument() throws Exception {

		// -------------------------
		// Prepare
		// -------------------------

		Document doc = new DefaultDocument("Dogs are running quickly.");

		OpenNLPAnnotator ann = new OpenNLPAnnotator();

		// -------------------------
		// Execute
		// -------------------------

		ann.annotate(doc);

		// -------------------------
		// Verify
		// -------------------------

		List<Keyword> keywords = doc.getKeywords();

		assertEquals(5, keywords.size());

		// Dogs -> dog / NOUN
		assertKeyword(keywords.get(0), "Dogs", "dog", "NOUN", "NOUN", 0, 4);

		// are -> be / AUX
		assertKeyword(keywords.get(1), "are", "be", "AUX", "AUX", 5, 8);

		// running -> run / VERB
		assertKeyword(keywords.get(2), "running", "run", "VERB", "VERB", 9, 16);

		// quickly -> quickly / ADV
		assertKeyword(keywords.get(3), "quickly", "quickly", "ADV", "ADV", 17, 24);

		// . -> . / PUNCT
		assertKeyword(keywords.get(4), ".", ".", "PUNCT", "PUNCT", 24, 25);
	}

	private void assertKeyword(Keyword kwd, String expectedStr, String expectedLex, String expectedUPos,
			String expectedFacet, int expectedBegin, int expectedEnd) {

		assertEquals(expectedStr, kwd.getStr());
		assertEquals(expectedLex, kwd.getLex());
		assertEquals(expectedUPos, kwd.getUPos());
		assertEquals(expectedFacet, kwd.getFacet());
		assertEquals(expectedBegin, kwd.getBegin());
		assertEquals(expectedEnd, kwd.getEnd());
	}
}