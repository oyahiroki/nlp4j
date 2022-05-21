package nlp4j.wiki.util;

import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.example.TextConverter;

public class MediaWikiTextUtils {

	static public String toPlainText(String wikiTitle, String wikiText) {
		// Set-up a simple wiki configuration
		WikiConfig config = DefaultConfigEnWp.generate();
		try {
			final int wrapCol = 1000;
			// Retrieve a page
			PageTitle pageTitle = PageTitle.make(config, wikiTitle);
			PageId pageId = new PageId(pageTitle, -1);
			// Instantiate a compiler for wiki pages
			WtEngineImpl engine = new WtEngineImpl(config);
			// Compile the retrieved page
			EngProcessedPage cp = engine.postprocess(pageId, wikiText, null);
			TextConverter p = new TextConverter(config, wrapCol);
			String text = (String) p.go(cp.getPage());
			return text;

		} catch (Exception e) {
			return null;
		}

	}

}
