package nlp4j.wiki;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.example.TextConverter;

import info.bliki.wiki.model.WikiModel;

public class WikiUtils {

	static private HashMap<String, String> map = new HashMap<String, String>();

	static {
		// key: before normailze, value: after normalized
		map.put("=={{jpn}}==", "=={{ja}}==");
		map.put("== {{jpn}} ==", "=={{ja}}==");
		map.put("== {{ja}} ==", "=={{ja}}==");
		map.put("==日本語==", "=={{ja}}==");
		map.put("== 日本語 ==", "=={{ja}}==");
		map.put("===名詞===", "==={{noun}}===");
	}

	static String splitter = "(:|・)";

	static public String[] extractSpells(String header) {

		// "=== {{verb}}・往 ===" -> "==={{verb}}・往==="
		header = header.replace(" ", "");

		// "==={{verb}}・往==="
		// ^idx1 ^idx2
		int idx1 = header.indexOf("}}");
		if (idx1 != -1) {
			int idx2 = header.indexOf("==", idx1);
			if (idx2 != -1 && (idx1 + 2) != idx2) {
				String spell = header.substring(idx1 + 2, idx2);

				ArrayList<String> list = new ArrayList<>();

				String[] ss = spell.split(splitter);
				for (String s : ss) {
					if (s.trim().isEmpty() == false) {
//						System.err.println(s);
						list.add(s);
					}
				}
				return list.toArray(new String[0]);
			} //
			else {
				return null;
			}
		} //
		else {
			return null;
		}

	}

	static public String normailzeHeader(String header) {

		// "=== {{verb}}・往 ===" -> "==={{verb}}・往==="
		header = header.replace(" ", "");

		if (header.contains("犬")) {
			System.err.println("debug");
		}

		{ // "==={{verb}}・往===" -> "==={{verb}}==="
			int idx2 = header.indexOf("}}");
			if (idx2 != -1) {
				int idx3 = header.indexOf("==", idx2);
				if (idx3 != -1) {
					header = header.substring(0, idx2 + 2) + header.substring(idx3);
				}
			}

		}

		if (map.containsKey(header)) {
			return map.get(header);
		}

		return header;
	}

	/**
	 * @param wikiText in Wiki Format
	 * @return HTML converted from Wiki Text
	 * @throws Exception on Error
	 */
	static public String toHtml(String wikiText) throws Exception {
		// info.bliki.wiki.model.WikiModel
		WikiModel model = new WikiModel("", "");
		model.setUp();
		String html;
		try {
			html = model.render(wikiText);
			return html;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	static public String toPlainText(String wikiPageTitle, String wikiText) {

		// Set-up a simple wiki configuration
		WikiConfig config = DefaultConfigEnWp.generate();

		try {
			final int wrapCol = 1000;

			// Retrieve a page
			PageTitle pageTitle = PageTitle.make(config, wikiPageTitle);

			PageId pageId = new PageId(pageTitle, -1);

			// Instantiate a compiler for wiki pages
			WtEngineImpl engine = new WtEngineImpl(config);

			// Compile the retrieved page
			EngProcessedPage cp = engine.postprocess(pageId, wikiPageTitle, null);

			TextConverter p = new TextConverter(config, wrapCol);
			String text = (String) p.go(cp.getPage());

			return text;

		} catch (Exception e) {
			return null;
		}
	}

}
