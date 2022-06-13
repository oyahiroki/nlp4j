package nlp4j.wiki.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	static final Pattern p = Pattern.compile("\\[\\[(.*?)\\]\\]");

	/**
	 * @param wikiText in Wikipedia Markdown format
	 * @return
	 */
	static public List<String> getWikiPageLinks(String wikiText) {
		List<String> ss = new ArrayList<>();

		Matcher m = p.matcher(wikiText);

		while (m.find()) {
			String g = m.group();
			if (g.length() > 4) {
				ss.add(g.substring(2, g.length() - 2));
			}
		}

		return ss;
	}

}
