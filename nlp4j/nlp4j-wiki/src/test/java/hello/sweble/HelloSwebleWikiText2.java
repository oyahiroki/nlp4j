package hello.sweble;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.example.TextConverter;

public class HelloSwebleWikiText2 {

	public static void main(String[] args) throws Exception {

		// x-wiki 形式のテキストを用意する
		// Prepare x-wiki format text

		String wikitext = "{{otheruses}}\r\n" //
				+ "{{Infobox Continent\r\n" //
				+ "|image               = [[File:Europe (orthographic projection).svg|200px]]\r\n" //
				+ "|countries           = 50\r\n" //
				+ "}}\r\n" //
				+ "\r\n" //
				+ "This is test. [TEST]\r\n" //
				+ "\r\n" //
				+ "== TITLE ==\r\n" //
				+ "\r\n" //
				+ "This is test2.\r\n" //
				+ "\r\n" //
				+ "";

		// Set-up a simple wiki configuration
		WikiConfig config = DefaultConfigEnWp.generate();
		String fileTitle = "TEST";
		final int wrapCol = 1000;

		// Retrieve a page
		PageTitle pageTitle = PageTitle.make(config, fileTitle);

		PageId pageId = new PageId(pageTitle, -1);

		// Instantiate a compiler for wiki pages
		WtEngineImpl engine = new WtEngineImpl(config);

		// Compile the retrieved page
		EngProcessedPage cp = engine.postprocess(pageId, wikitext, null);

		TextConverter p = new TextConverter(config, wrapCol);
		String text = (String) p.go(cp.getPage());

		System.err.println(text);
	}

}
