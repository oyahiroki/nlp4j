package nlp4j.pattern;

import java.io.File;
import java.util.List;

import nlp4j.KeywordWithDependency;
import nlp4j.test.NLP4JTestCase;

@SuppressWarnings("javadoc")
public class PatternTestCase extends NLP4JTestCase {

	public PatternTestCase() {
		target = Pattern.class;
	}

	public void testClone001() {
		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-001.xml");
		List<Pattern> patterns = PatternReader.readFromXml(xml);
		assertNotNull(patterns);
		assertEquals(1, patterns.size());

		Pattern pattern = patterns.get(0);
		Pattern patternCloned = pattern.clone();

		assertFalse(pattern == patternCloned);

		{
			KeywordWithDependency kw0 = pattern.getKeywordRule();
			System.err.println("--- kw0 ---");
			System.err.println(kw0.toStringAsXml());
			KeywordWithDependency kw1 = patternCloned.getKeywordRule();
			System.err.println("--- kw1 ---");
			System.err.println(kw1.toStringAsXml());
			assertFalse(kw0 == kw1);
		}

		// Deep Clone?
		{
			KeywordWithDependency kw0Child = pattern.getKeywordRule().getChildren().get(0);
			KeywordWithDependency kw1Child = patternCloned.getKeywordRule().getChildren().get(0);
			assertFalse(kw0Child == kw1Child);
		}

	}

	public void testGetFacet001() {
		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-001.xml");
		List<Pattern> patterns = PatternReader.readFromXml(xml);
		assertNotNull(patterns);
		assertEquals(1, patterns.size());
		Pattern pattern = patterns.get(0);

		String facet = pattern.getFacet();
		assertNotNull(facet);
		assertEquals("pattern.sample", facet);
	}

	public void testGetKeywordRule001() {
		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-001.xml");
		List<Pattern> patterns = PatternReader.readFromXml(xml);
		assertNotNull(patterns);
		assertEquals(1, patterns.size());

		Pattern pattern = patterns.get(0);
		KeywordWithDependency kw0 = pattern.getKeywordRule();

		assertNotNull(kw0);
		System.err.println("--- kw0 ---");
		System.err.println(kw0.toStringAsXml());
	}

	public void testGetLang001() {
		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-001.xml");
		List<Pattern> patterns = PatternReader.readFromXml(xml);
		Pattern pattern = patterns.get(0);
		String lang = pattern.getLang();
		assertNotNull(lang);
		assertEquals("ja", lang);
	}

	public void testGetValue001() {
		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-001.xml");
		List<Pattern> patterns = PatternReader.readFromXml(xml);
		Pattern pattern = patterns.get(0);
		String value = pattern.getValue();
		assertNotNull(value);
		assertEquals("{0.lex} ... {1.lex}", value);
		System.err.println(value);
	}

}
