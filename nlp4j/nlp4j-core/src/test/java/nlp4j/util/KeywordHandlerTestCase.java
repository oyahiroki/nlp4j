package nlp4j.util;

import java.io.FileInputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nlp4j.KeywordWithDependency;
import nlp4j.test.NLP4JTestCase;

@SuppressWarnings("javadoc")
public class KeywordHandlerTestCase extends NLP4JTestCase {

	public KeywordHandlerTestCase() {
		super();
		target = KeywordHandler.class;
	}

	public void test001() throws Exception {

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

		SAXParser saxParser = saxParserFactory.newSAXParser();

		KeywordHandler handler = new KeywordHandler();

		saxParser.parse(new FileInputStream("src/test/resources/nlp4j.pattern/keyword-001.xml"), handler);

		List<KeywordWithDependency> kwds = handler.getKeywords();

		for (KeywordWithDependency kwd : kwds) {
			System.err.println(kwd.toStringAsXml());
		}

	}

}
