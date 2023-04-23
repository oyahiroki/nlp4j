package nlp4j.wiki.template;

import java.util.List;

import junit.framework.TestCase;

public class TemplateParserTestCase extends TestCase {

	public void testParseTemplateLine001() {
		String line = "{{XXX}}";
		List<String> params = TemplateParser.parseTemplateLine(line, "XXX");
		System.err.println(params);
		assertNotNull(params);
		assertTrue(params.size() == 0);
	}

	public void testParseTemplateLine002() {
		String line = "{{Redirect|&}}";
		List<String> params = TemplateParser.parseTemplateLine(line, "Redirect");
		System.err.println(params);
		assertNotNull(params);
		assertTrue(params.size() == 1);
	}
	
	
	public void testParseTemplateLine003() {
		String line = "{{Otheruses|記号|競走馬|アンパサンド (競走馬)}}";
		List<String> params = TemplateParser.parseTemplateLine(line, "Otheruses");
		System.err.println(params);
		assertNotNull(params);
		assertTrue(params.size() == 3);
	}

}
