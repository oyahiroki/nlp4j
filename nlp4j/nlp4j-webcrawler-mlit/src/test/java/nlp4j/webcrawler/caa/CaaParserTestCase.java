package nlp4j.webcrawler.caa;

import java.io.File;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import nlp4j.Document;
import nlp4j.test.NLP4JTestCase;
import nlp4j.util.DocumentUtil;

public class CaaParserTestCase extends NLP4JTestCase {

	public CaaParserTestCase() {
		super();
		target = CaaParser.class;
	}

	public void testParse001() throws Exception {
		CaaParser parser = new CaaParser();
		String fileName = "src/test/resources/nlp4j.webcrawler.caa/out_00000001438.html";
		String htmlData = FileUtils.readFileToString(new File(fileName), "UTF-8");
		htmlData = htmlData.trim();
		Document doc = parser.parse(htmlData);
		System.err.println(DocumentUtil.toPrettyJsonString(doc));
	}

	public void testParse002() throws Exception {
		CaaParser parser = new CaaParser();
		String fileName = "src/test/resources/nlp4j.webcrawler.caa/out_00000001882.html";
		String htmlData = FileUtils.readFileToString(new File(fileName), "UTF-8");
		htmlData = htmlData.trim();
		Document doc = parser.parse(htmlData);
		System.err.println(DocumentUtil.toPrettyJsonString(doc));
	}

	public void testParse003() throws Exception {
		CaaParser parser = new CaaParser();
		String fileName = "src/test/resources/nlp4j.webcrawler.caa/out_00000027727.html";
		String htmlData = FileUtils.readFileToString(new File(fileName), "UTF-8");
		htmlData = htmlData.trim();
		Document doc = parser.parse(htmlData);
		System.err.println(DocumentUtil.toPrettyJsonString(doc));
	}

	public void testParse004() throws Exception {
		CaaParser parser = new CaaParser();
		String fileName = "src/test/resources/nlp4j.webcrawler.caa/out_00000001312.html";
		String htmlData = FileUtils.readFileToString(new File(fileName), "UTF-8");
		htmlData = htmlData.trim();
		Document doc = parser.parse(htmlData);
		System.err.println(DocumentUtil.toPrettyJsonString(doc));
	}

	public void testParse005() throws Exception {
		CaaParser parser = new CaaParser();
		String fileName = "src/test/resources/nlp4j.webcrawler.caa/out_00000009911.html";
		String htmlData = FileUtils.readFileToString(new File(fileName), "UTF-8");
		htmlData = htmlData.trim();
		Document doc = parser.parse(htmlData);
		System.err.println(DocumentUtil.toPrettyJsonString(doc));
	}

	public void testParse006() throws Exception {
		CaaParser parser = new CaaParser();
		String fileName = "src/test/resources/nlp4j.webcrawler.caa/out_00000001992.html";
		String htmlData = FileUtils.readFileToString(new File(fileName), "UTF-8");
		htmlData = htmlData.trim();
		Document doc = parser.parse(htmlData);
		System.err.println(DocumentUtil.toPrettyJsonString(doc));
	}

}
