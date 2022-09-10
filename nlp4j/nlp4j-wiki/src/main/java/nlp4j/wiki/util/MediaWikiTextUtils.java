package nlp4j.wiki.util;

import java.util.ArrayList;
import java.util.Collections;
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

import nlp4j.wiki.WikiItemTextParser;
import nlp4j.wiki.WikiPageNode;

public class MediaWikiTextUtils {

	static private final WtEngineImpl engine;
	static private WikiConfig config = DefaultConfigEnWp.generate();
	static {
		engine = new WtEngineImpl(config);
	}

	static public String getRootNodeText(String wikiText) {
		WikiItemTextParser parser = new WikiItemTextParser();
		parser.parse(wikiText);

		WikiPageNode rootNode = parser.getRoot();
//		System.err.println("<out>");

//		{
//			String s = rootNode.getText().length() > 16 ? rootNode.getText().substring(0, 16)
//					: rootNode.getText();
//			s = s.replace("\n", "").trim();
//			System.err.println(page.getTitle() + "\t" + s);
//		}

		String t = rootNode.getText();

		return t;
	}

	/**
	 * <pre>
	 * 1. Remove Templates. 
	 *     "{{otheruses}}" will be removed.
	 * 2. Compile section number. 
	 *     "== TITLE ==" will be "1. TITLE\n--------"
	 * </pre>
	 * 
	 * @param wikiTitle
	 * @param wikiText
	 * @return
	 */
	static public String toPlainText(String wikiTitle, String wikiText) {
		// Set-up a simple wiki configuration
		try {
			final int wrapCol = 1000;
			// Retrieve a page
			PageTitle pageTitle = PageTitle.make(config, wikiTitle);
			PageId pageId = new PageId(pageTitle, -1);
			// Instantiate a compiler for wiki pages
			// Compile the retrieved page
			EngProcessedPage cp = engine.postprocess(pageId, wikiText, null);
			TextConverter p = new TextConverter(config, wrapCol);
			String text = (String) p.go(cp.getPage());
			return text;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
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

	static public String processRedirect(String t) {

		if (t == null) {
			return t;
		}
		// けんか
		// #redirect [[喧嘩]]
		if (t.startsWith("#REDIRECT") || t.startsWith("#redirect")) {
			int idx1 = t.indexOf("[[");
			int idx2 = t.indexOf("]]");
			if (idx1 != -1 && idx2 != -1) {
				t = t.substring(idx1 + 2, idx2);
			}
		}

		return t;
	}

	public static List<String> parseTemplateTags(String t) {
		List<String> tags = new ArrayList<>();

		// FOR EACH LINE
		for (String line : t.split("\n")) {
			if (line.startsWith("{{") == true) {
				String tag = parseTag(line);
				if (tags.contains(tag) == false) {
					tags.add(tag);
				}
			}
		} // END OF ( FOR EACH LINE)

		Collections.sort(tags);

		return tags;
	}

	private static String parseTag(String line) {
		String tag;
		if (line.contains("|")) {
			int idx = line.indexOf("|");
			tag = line.substring(0, idx);
		} //
		else if (line.contains(":")) {
			int idx = line.indexOf(":");
			tag = line.substring(0, idx).trim();
		} //
		else {
			tag = line;
		}

		tag = tag.replace("{", "");
		tag = tag.replace("}", "");
		tag = tag.replace("[", "");
		tag = tag.replace("]", "");

		tag = tag.trim();

		if (tag.startsWith("Infobox ")) {
			tag = tag.replace("Infobox ", "");
		}

		tag = tag.toLowerCase();
		tag = tag.replace(" ", "_");

		return tag;
	}

}
