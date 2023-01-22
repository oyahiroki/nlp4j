package nlp4j.wiki.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.TextUtils;
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

	/**
	 * @param wikiText wiki形式のテキスト wiki format text
	 * @return
	 */
	public static List<String> parseTemplateTags(String wikiText) {
		List<String> tags = new ArrayList<>();

		// FOR EACH LINE
		for (String line : wikiText.split("\n")) {
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

	public static List<String> parseCategoryTags(String t) {
		List<String> tags = new ArrayList<>();

		for (String line : t.split("\n")) {
			line = line.trim();

			if (line.startsWith("[[Category:")) {
				if (line.contains("|")) {
					String v = line.substring(11, line.indexOf('|'));
					tags.add(v);
				} else {
					String v = line.substring(11, line.length() - 2);
					tags.add(v);
				}
			}
		}

		return tags;
	}

	public static String toPlainText2(String wikiText) {

		StringBuilder sb = new StringBuilder();

		Stack<Character> stack = new Stack<>();
		int status = 0; // 0:default,1:filelink,2:リンク中のアンカー

		for (int n = 0; n < wikiText.length(); n++) {
			char c = wikiText.charAt(n);
			String rest = wikiText.substring(n);

			if (c == '{' || c == '[') {
				stack.push(c);
//				System.err.println(toString(stack));
			} //
			else if (c == '}' || c == ']') {
				if (stack.isEmpty() == false) {
					stack.pop();
				}
//				System.err.println(toString(stack));
				if (stack.size() == 0) {
					status = 0; // リセットされる
				}
			}
			//
			else {
				if (stack.size() == 0) {
					if (c == '\'') {

					} else {
						sb.append(c);
					}
				} //
				else if (stack.size() == 2 && toString(stack).equals("[[")) {

					if (status == 0) {
						int idx = rest.indexOf("]]");
						// 閉じている
						if (idx != -1) {
							String link = rest.substring(0, idx);
							String text = parseTextFromLink(link);
							sb.append(text);
						}
						// 閉じてない
						else {
							sb.append(rest);
						}
						status = 2;
					}

//					// ファイルへのリンク
//					if (rest.startsWith("ファイル")) {
//						status = 1;
//					}
//					if (c == '#') { // リンク中のアンカー
//						status = 2;
//					}
//					if (status == 0) {
//						sb.append(c);
//					}
				}
			}
		}

		return sb.toString();
	}

	private static String parseTextFromLink(String link) {
		// https://ja.wikipedia.org/wiki/Help:%E3%83%AA%E3%83%B3%E3%82%AF

		// パイプ付き
		if (link.split("\\|").length == 2) {
			return link.split("\\|")[1];
		}
		// 特別:転送
		else if (link.startsWith("特別:転送")) {
			return "";
		}
		//
		else if (link.startsWith(":")) {
			return "";
		}
		//
		else if (link.startsWith("ファイル")) {
			return "";
		} else if (link.contains("#")) {
			int idx = link.indexOf("#");
			return link.substring(0, idx);
		}
		//
		else {
			return link;
		}

	}

	static private String toString(Stack stack) {
		StringBuilder sb = new StringBuilder();
		stack.forEach(c -> {
			sb.append(c);
		});
		return sb.toString();
//	return(stack.toString().substring(1,stack.toString().length()-1));	
	}

}
