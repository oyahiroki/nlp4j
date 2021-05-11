package nlp4j.pattern;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.test.NLP4JTestCase;
import nlp4j.util.KeywordUtil;

@SuppressWarnings("javadoc")
public class PatternMatcherEnTestCase extends NLP4JTestCase {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public PatternMatcherEnTestCase() {
		target = PatternMatcher.class;
	}

	public void testMatchEn001() throws Exception {

		description = "English Extract SVO";

		// 今日はいい天気です。
		List<KeywordWithDependency> kwds1 = KeywordUtil
				.fromXml(new File("src/test/resources/nlp4j.pattern/keyword-en-001.xml"));

		KeywordWithDependency kwd = kwds1.get(0);

		// ADJ ... NOUN
		File xml = new File("src/test/resources/nlp4j.pattern/pattern-test-en-001-svo.xml");

		int expectedKeywordSize = 1;
		String expectedLex = "I ... eat ... sushi";

		List<Pattern> patterns = PatternReader.readFromXml(xml);

		List<Keyword> kwds = new ArrayList<Keyword>();

		for (Pattern pattern : patterns) {
			List<Keyword> kwds0 = PatternMatcher.match(kwd, pattern); // <-- Test Target
			if (kwds0 != null) {
				kwds.addAll(kwds0);
			}
		}

		logger.info("--- extracted Keywords");

		for (Keyword kw : kwds) {
			System.err.println("facet=" + kw.getFacet() + ",lex=" + kw.getLex() + ",begin=" + kw.getBegin() + ",end="
					+ kw.getEnd());
		}

		assertEquals(expectedKeywordSize, kwds.size());
		assertEquals(expectedLex, kwds.get(0).getLex());

	}

}
