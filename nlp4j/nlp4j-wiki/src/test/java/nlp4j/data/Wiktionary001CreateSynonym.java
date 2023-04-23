package nlp4j.data;

import java.io.File;
import java.util.List;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiItemTextParser;
import nlp4j.wiki.WikiItemTextParserInterface;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;
import nlp4j.wiki.WikiPageNode;
import nlp4j.wiki.util.MediaWikiFileUtils;
import nlp4j.wiki.util.MediaWikiTextUtils;

public class Wiktionary001CreateSynonym {

	public static void main(String[] args) throws Exception {

		File dumpFile = MediaWikiFileUtils.getDumpFile( //
				"/usr/local/data/wiki/20220401", //
				"ja", //
				"wiktionary", //
				"20220401"); //

		System.err.println(dumpFile.getAbsolutePath());
		System.err.println(String.format("%,d", dumpFile.length()));

		WikiPageHandler wikiPageHander = new WikiPageHandler() {

			int count = 0;

			@Override
			public void read(WikiPage page) throws BreakException {

//				System.err.println(page);

				if (page != null && page.getTitle().contains(":") == false) {
					count++;

//					System.err.println(page.getText());

					String wikiText = page.getText();

					WikiItemTextParserInterface parser = new WikiItemTextParser();

					parser.parse(wikiText);

					WikiPageNode rootNode = parser.getRoot();

					List<WikiPageNode> jaNodes = rootNode.get("=={{ja}}==");

					for (WikiPageNode n : jaNodes) {
						{
							List<WikiPageNode> nodes = n.find("syn");
							if (nodes != null && nodes.size() > 0) {
								System.err.println("<syn>");
								String text0 = nodes.get(0).getText();
								System.err.println("<text>");
								System.err.println(text0);
								System.err.println("</text>");
//								System.err.println("---\n" + text0 + "\n---");
								// [[Category:{{ja}}]]
								System.err.println("<link>");
								System.err
										.println(page.getTitle() + " -> " + MediaWikiTextUtils.getWikiPageLinks(text0));
								System.err.println("</link>");
								System.err.println("</syn>");
							}
						}
						{
							List<WikiPageNode> nodes = n.find("rel");
							if (nodes != null && nodes.size() > 0) {
								System.err.println("<rel>");
								String text0 = nodes.get(0).getText();
								System.err.println("<text>");
								System.err.println(text0);
								System.err.println("</text>");
//								System.err.println("---\n" + text0 + "\n---");
								// [[Category:{{ja}}]]
								System.err.println("<link>");
								System.err
										.println(page.getTitle() + " -> " + MediaWikiTextUtils.getWikiPageLinks(text0));
								System.err.println("</link>");
								System.err.println("</rel>");
							}
						}

					}

					{
					}

//					if (count > 30000) {
//						throw new BreakException();
//					}
				}
			}
		};

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile);) {

			try {
				dumpReader.read(wikiPageHander);
			} catch (BreakException be) {
				System.err.println("OK");
			}

		}

	}
}
