package nlp4j.wiki;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.wiki.infobox.InfoboxParser;

public class WikipediaItemTextParserTestCase extends TestCase {

	public void testWikipediaItemTextParser() {
	}

	public void testGetRoot() {
	}

	public void testGetWikiPageNodesAsList() {
	}

	public void testParse001() throws Exception {
		String wikiText = FileUtils.readFileToString(new File("src/test/resources/nlp4j.wiki/jawiki-example.txt"),
				"UTF-8");
		WikipediaItemTextParser parser = new WikipediaItemTextParser();
		JsonObject pageInfo = new JsonObject();
		System.err.println(pageInfo);
		{
			WikiPageNode node = parser.parse(wikiText, pageInfo);
			System.err.println(pageInfo);
			System.err.println(node.getHeader());
		}
		{
			List<WikiPageNode> nodes = parser.getWikiPageNodesAsList();
			for (WikiPageNode node : nodes) {
				System.err.println(node.toString());
			}
		}
		{
			String infobox = parser.getInfobox();

			System.err.println(infobox);
			InfoboxParser.parseInfoboxWikiText(infobox);
		}

	}

	public void testToWikiPageNodeTree() {
	}

}
