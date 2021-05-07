package nlp4j.pattern;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultKeywordWithDependency;
import nlp4j.test.NLP4JTestCase;
import nlp4j.util.KeywordUtil;

@SuppressWarnings("javadoc")
public class PatternMatcherTestCase extends NLP4JTestCase {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public PatternMatcherTestCase() {
		target = PatternMatcher.class;
	}

	public void testMatch001() throws Exception {

		description = "";

		// 今日はいい天気です。
		List<KeywordWithDependency> kwds1 = KeywordUtil
				.fromXml(new File("src/test/resources/nlp4j.pattern/keyword-000.xml"));

		KeywordWithDependency kwd = kwds1.get(0);

		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-005.xml");
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

	}

	public void testMatch002() throws Exception {

		description = "";

		List<KeywordWithDependency> kwds1 = KeywordUtil
				.fromXml(new File("src/test/resources/nlp4j.pattern/keyword-000.xml"));
		KeywordWithDependency kwd = kwds1.get(0);

		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-002.xml");
		List<Pattern> patterns = PatternReader.readFromXml(xml);

		List<Keyword> kwds = new ArrayList<Keyword>();

		for (Pattern pattern : patterns) {
			List<Keyword> kwds0 = PatternMatcher.match(kwd, pattern);
			if (kwds0 != null) {
				kwds.addAll(kwds0);
			}
		}

		// <?xml version="1.0" encoding="UTF-8"?>
		// <patterns lang="ja">
		// <!-- example: いい ... 天気 -->
		// <pattern facet="pattern.sample" value="{0.lex} ... {1.lex}">
		// <w id="1" upos="NOUN" lex="天気">
		// <w id="0" upos="ADJ" />
		// </w>
		// </pattern>
		// </patterns>

		logger.info("--- extracted Keywords");
		for (Keyword kw : kwds) {
			System.err.println("facet=" + kw.getFacet() + ",lex=" + kw.getLex() + ",begin=" + kw.getBegin() + ",end="
					+ kw.getEnd());
		}

	}

	public void testMatch003() throws Exception {
		description = "";

		// 今日はいい天気です。
		List<KeywordWithDependency> kwds1 = KeywordUtil
				.fromXml(new File("src/test/resources/nlp4j.pattern/keyword-000.xml"));
		KeywordWithDependency kwd = kwds1.get(0);

		// System.err.println(kwd.toStringAsXml());

		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-003.xml");
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

	}

	public void testMatch004() throws Exception {

		description = "";

		// ショックアブソーバーからオイルが漏れた
		List<KeywordWithDependency> kwds1 = KeywordUtil
				.fromXml(new File("src/test/resources/nlp4j.pattern/keyword-001.xml"));

		KeywordWithDependency kwd = kwds1.get(0);

		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-004.xml");
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

	}

}
