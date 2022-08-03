package nlp4j.wiki.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

public class MediaWikiTextUtilsTestCase extends TestCase {

	public void testToPlainText001() {
		String wikitext = "{{otheruses}}\r\n" //
				+ "{{Infobox Continent\r\n" //
				+ "|image = [[File:Europe (orthographic projection).svg|200px]]\r\n" //
				+ "|countries = 50\r\n" //
				+ "}}\r\n" //
				+ "\r\n" //
				+ "This is test. [TEST]\r\n" //
				+ "\r\n" //
				+ "== TITLE ==\r\n" //
				+ "\r\n" //
				+ "This is test2.\r\n" //
				+ "\r\n" //
				+ "";
		String plainText = MediaWikiTextUtils.toPlainText("test", wikitext);

		System.err.println(plainText);
	}

	public void testToPlainText002() throws IOException {
		// x-wiki 形式のテキストを用意する
		// Prepare x-wiki format text
		File file = new File("src/test/resources/hello.sweble/wiki-example.txt");
		String charsetName = "UTF-8";
		String wikitext = FileUtils.readFileToString(file, charsetName);
		String plainText = MediaWikiTextUtils.toPlainText("test", wikitext);

		System.err.println(plainText);
	}

	public void testGetWikiPageLinks() {
		
	}

}
