package nlp4j.pattern;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import junit.framework.TestCase;

public class PatternReaderTestCase extends TestCase {

	public void testReadFromXmlFile() throws Exception {
		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-001.xml");
		List<Pattern> patterns = PatternReader.readFromXml(xml);
		System.err.println(patterns != null);
		System.err.println(patterns.size());
	}

	public void testReadFromXmlInputStream() throws Exception {
		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-001.xml");
		List<Pattern> patterns = PatternReader.readFromXml(new FileInputStream(xml));
		System.err.println(patterns != null);
		System.err.println(patterns.size());
	}

}
