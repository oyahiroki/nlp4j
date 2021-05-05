package nlp4j.pattern;

import java.io.FileInputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nlp4j.test.NLP4JTestCase;

public class UserPatternHandlerTestCase extends NLP4JTestCase {

	public UserPatternHandlerTestCase() {
		super();
		target = UserPatternHandler.class;
	}

	public void test001() throws Exception {

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

		SAXParser saxParser = saxParserFactory.newSAXParser();

		UserPatternHandler handler = new UserPatternHandler();

		saxParser.parse(new FileInputStream("src/test/resources/nlp4j.pattern/pattern-sample-ja.xml"), handler);

		List<Pattern> patterns = handler.getPatterns();

		for (Pattern p : patterns) {
			System.err.println(p.toString());
		}

	}

}
