package example.p002readdumpwoindex;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiItemTextParser;
import nlp4j.wiki.WikiItemTextParserInterface;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;
import nlp4j.wiki.WikiPageNode;
import nlp4j.wiki.template.WikiTemplateNormalizer;
import nlp4j.wiki.util.MediaWikiFileUtils;
import nlp4j.wiki.util.MediaWikiTextUtils;

public class Wikipedia003ReadDumpWithoutIndexExampleJa_20220401b {

	public static void main(String[] args) throws Exception {

		// WIKI DUMP FILE
		File dumpFile = MediaWikiFileUtils.getDumpFile( //
				"/usr/local/data/wiki/20220501", // File Path
				"ja", // Language
				"wiki", // Type (wiki | wiktionary)
				"20220501"); // Version

		System.err.println("FILE: " + dumpFile.getAbsolutePath());
		System.err.println("FILE_SIZE: " + String.format("%,d", dumpFile.length()));

		File outFile = new File("R:/out_" + System.currentTimeMillis() + ".txt");

		boolean debug = false;

		// 自作のHandlerを指定する DEFINE Wiki Dump Handler
		WikiPageHandler wikiPageHander = new WikiPageHandler() {
			int count = 0;

			@Override
			public void read(WikiPage page) throws BreakException {
//				System.err.println(page);
				if (page != null && page.getTitle().contains(":") == true) {
					// SKIP
//					System.err.println("SKIP: " + page.getTitle());
					return;
				} //
				else {
//					System.err.println("TITLE: " + page.getTitle());
					WikiItemTextParserInterface parser = new WikiItemTextParser();
					parser.parse(page.getText());

					WikiPageNode rootNode = parser.getRoot();
//					System.err.println("<out>");

//					{
//						String s = rootNode.getText().length() > 16 ? rootNode.getText().substring(0, 16)
//								: rootNode.getText();
//						s = s.replace("\n", "").trim();
//						System.err.println(page.getTitle() + "\t" + s);
//					}

					String t = rootNode.getText();

					t = MediaWikiTextUtils.processRedirect(t);

					//
					t = MediaWikiTextUtils.toPlainText(page.getTitle(), t);
					t = t.replace("\n\n", " ") //
							.replace("\n", "") //
							.replaceAll("\\[\\[.*?\\]\\]", "") //
							.replaceAll("（.*?）", "") //
							.trim();
					t = t.replace("**", "");

					String data = page.getTitle() //
							+ "\t"//
							+ t;

					if (debug) {
						System.err.println("<out>");
						System.err.println( //
								data);
						System.err.println("</out>");
					}

					try {
						FileUtils.write(outFile, data + "\n", "UTF-8", true);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					count++;

					if (debug == true && count > 100) { // BREAK
						throw new BreakException();
					}

					if (count % 1000 == 0) {
						System.err.println(count);
					}
				}
			}
		};

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile)) {
			try {
				dumpReader.read(wikiPageHander);
			} catch (BreakException be) {
				System.err.println("FINISHED");
			}
		}

	}
}
