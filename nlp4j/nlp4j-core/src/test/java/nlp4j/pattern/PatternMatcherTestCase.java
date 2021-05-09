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
public class PatternMatcherTestCase extends NLP4JTestCase {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public PatternMatcherTestCase() {
		target = PatternMatcher.class;
	}

	public void testMatchJa001() throws Exception {

		description = "Extract ADJ ... NOUN (1)";

		// 今日はいい天気です。
		List<KeywordWithDependency> kwds1 = KeywordUtil
				.fromXml(new File("src/test/resources/nlp4j.pattern/keyword-ja-000.xml"));

		KeywordWithDependency kwd = kwds1.get(0);

		// ADJ ... NOUN
		File xml = new File("src/test/resources/nlp4j.pattern/pattern-test-ja-001-adj_noun.xml");

		int expectedKeywordSize = 1;
		String expectedLex = "いい ... 天気";

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

	public void testMatchJa002() throws Exception {

		description = "Extract single Keywords with Pattern";

		// 今日はいい天気です。
		List<KeywordWithDependency> kwds1 = KeywordUtil
				.fromXml(new File("src/test/resources/nlp4j.pattern/keyword-ja-000.xml"));
		KeywordWithDependency kwd = kwds1.get(0);

		// 単語切り出し
		File xml = new File("src/test/resources/nlp4j.pattern/pattern-test-ja-002-just_a_word.xml");
		List<Pattern> patterns = PatternReader.readFromXml(xml);

		int expectedKeywordSize = 6;
		String[] expectedKeywordLex = { "。", "です", "天気", "は", "今日", "いい" };

		List<Keyword> kwds = new ArrayList<Keyword>();

		for (Pattern pattern : patterns) {
			List<Keyword> kwds0 = PatternMatcher.match(kwd, pattern);
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
		for (int n = 0; n < kwds.size(); n++) {
			assertEquals(expectedKeywordLex[n], kwds.get(n).getLex());
		}

	}

	public void testMatchJa003() throws Exception {

		description = "Extract NOUN ... ADP ... NOUN";

		int expectedKeywordSize = 1;
		String expectedLex = "今日 ... は ... 天気";

		// 今日はいい天気です。
		List<KeywordWithDependency> kwds1 = KeywordUtil
				.fromXml(new File("src/test/resources/nlp4j.pattern/keyword-ja-000.xml"));
		KeywordWithDependency kwd = kwds1.get(0);

		// NOUN ... ADP ... NOUN with REGEX
		File xml = new File("src/test/resources/nlp4j.pattern/pattern-test-ja-003-noun_adp_noun_regex.xml");
		List<Pattern> patterns = PatternReader.readFromXml(xml);

		List<Keyword> kwds = new ArrayList<Keyword>();

		for (Pattern pattern : patterns) {
			List<Keyword> kwds0 = PatternMatcher.match(kwd, pattern);
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

	public void testMatchJa004() throws Exception {

		description = "Extract NOUN .. ADP ... VERB";

		int expectedKeywordSize = 1;
		String expectedLex = "オイル　... が ... 漏れる";

		// ショックアブソーバーからオイルが漏れた
		List<KeywordWithDependency> kwds1 = KeywordUtil
				.fromXml(new File("src/test/resources/nlp4j.pattern/keyword-ja-001.xml"));

		KeywordWithDependency kwd = kwds1.get(0);

		File xml = new File("src/test/resources/nlp4j.pattern/pattern-test-ja-004-noun_adp_noun_verb.xml");
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

	public void testMatchEn001() throws Exception {

		description = "Extract ADJ ... NOUN (1)";

		// 今日はいい天気です。
		List<KeywordWithDependency> kwds1 = KeywordUtil
				.fromXml(new File("src/test/resources/nlp4j.pattern/keyword-en-001.xml"));

		KeywordWithDependency kwd = kwds1.get(0);

		System.err.println(kwd.toStringAsXml());

//		// SVO
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
