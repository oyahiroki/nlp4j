package nlp4j.wiki.infobox;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.wiki.WikiPageNode;
import nlp4j.wiki.WikipediaItemTextParser;

/**
 * created on: 2023-05-02
 * 
 * @author Hiroki Oya
 *
 */
public class InfoboxParserTestCase extends TestCase {

	public void testParse001() throws Exception {

		String wikiText = FileUtils.readFileToString(new File("src/test/resources/nlp4j.wiki/jawiki-example.txt"),
				"UTF-8");

		List<Infobox> bb = InfoboxParser.parse(wikiText);
		System.err.println(bb.size());
		System.err.println(bb.get(0));

		assertEquals(1, bb.size());

	}

	public void testParse002() throws Exception {
		String wikiText = FileUtils.readFileToString(
				new File("src/test/resources/nlp4j.wiki/jawiki-example-multiple-infobox.txt"), "UTF-8");
		List<Infobox> bb = InfoboxParser.parse(wikiText);
		System.err.println(bb.size());

		System.err.println(bb.get(0));
		System.err.println(bb.get(1));

		assertEquals(2, bb.size());
	}

	public void testParse003() throws Exception {
		String wikiText = FileUtils.readFileToString(
				new File("src/test/resources/nlp4j.wiki/jawiki-example-without-infobox.txt"), "UTF-8");
		List<Infobox> bb = InfoboxParser.parse(wikiText);
		System.err.println(bb.size());

		assertEquals(0, bb.size());
	}

	public void testInfoboxWikiText001() throws Exception {

		String wikiText = FileUtils.readFileToString(new File("src/test/resources/nlp4j.wiki/jawiki-example.txt"),
				"UTF-8");
		WikipediaItemTextParser parser = new WikipediaItemTextParser();
		JsonObject pageInfo = new JsonObject();
//		System.err.println(pageInfo);
		{
			WikiPageNode node = parser.parse(wikiText, pageInfo);
//			System.err.println(pageInfo);
//			System.err.println(node.getHeader());
		}
		{
//			List<WikiPageNode> nodes = parser.getWikiPageNodesAsList();
//			for (WikiPageNode node : nodes) {
//				System.err.println(node.toString());
//			}
		}
		{
			String infobox = parser.getInfobox();
			Infobox ib = InfoboxParser.parseInfoboxWikiText(infobox);
		}

	}

	public void testInfoboxWikiText002() throws Exception {

		String wikiText = FileUtils.readFileToString(
				new File("src/test/resources/nlp4j.wiki/jawiki-example-without-infobox.txt"), "UTF-8");
		WikipediaItemTextParser parser = new WikipediaItemTextParser();
		JsonObject pageInfo = new JsonObject();
//		System.err.println(pageInfo);
		{
			WikiPageNode node = parser.parse(wikiText, pageInfo);
//			System.err.println(pageInfo);
//			System.err.println(node.getHeader());
		}
		{
//			List<WikiPageNode> nodes = parser.getWikiPageNodesAsList();
//			for (WikiPageNode node : nodes) {
//				System.err.println(node.toString());
//			}
		}
		{
			String infobox = parser.getInfobox();
			InfoboxParser infoboxParser = new InfoboxParser();

//			System.err.println(infobox);
			Infobox ib = infoboxParser.parseInfoboxWikiText(infobox);
			System.err.println(ib);
		}

	}

}
