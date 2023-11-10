package nlp4j.wiki.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.example.TextConverter;
import org.sweble.wikitext.parser.parser.LinkTargetException;

import nlp4j.wiki.WikiItemTextParser;
import nlp4j.wiki.WikiItemTextParserInterface;
import nlp4j.wiki.WikiPageNode;

public class MediaWikiTextUtils {

	static private final WtEngineImpl engine;
	static private WikiConfig config = DefaultConfigEnWp.generate();
	static {
		engine = new WtEngineImpl(config);
	}

	static final Pattern p = Pattern.compile("\\[\\[(.*?)\\]\\]");

	private static void extractedInfobox(String wikiText, StringBuilder sb, List<String> infoBoxes) {
		StringBuilder sbInfobox = new StringBuilder();

		// Infobox は必ず行頭から始まる
		// Infobox は複数存在することがある

		boolean infobox = false;
		int level = 0;

		for (String line : wikiText.split("\n")) {
			// NOT Infobox
			if (infobox == false) {
				if (line.startsWith("{{Infobox")) {
					infobox = true;
					sbInfobox.append(line);
					level += 2;
				} //
				else {
					sb.append(line);
				}
			}
			// IN Infobox
			else {
				for (int n = 0; n < line.length(); n++) {
					char c = line.charAt(n);
					if (c == '{') { // 入れ子になっている
						level++;
					} //

					if (level == 0) {
						if (infobox == true) {
							infobox = false;
						}
						sb.append(c);
					} //
					else {
						sbInfobox.append(c);
					}

					if (c == '}') {
						level--;
						if (level == 0 && sbInfobox.length() > 0) {
							infoBoxes.add(sbInfobox.toString());
							sbInfobox = new StringBuilder();
						}
					} //
				}
			}
			sb.append("\n");
		} // END_OF_EACH(LINE)

		if (level == 0 && sbInfobox.length() > 0) {
			infoBoxes.add(sbInfobox.toString());
			sbInfobox = new StringBuilder();
		}
	}

	/**
	 * @param wikiText
	 * @return Root Node Wiki text
	 */
	static public String getRootNodeText(String wikiText) {
		if (wikiText == null) {
			return null;
		}
		WikiItemTextParserInterface parser = new WikiItemTextParser();
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
	 * @param wikiText in Wikipedia Markdown format
	 * @return
	 */
	static public List<String> getWikiPageLinks(String wikiText) {
		List<String> ss = new ArrayList<>();

		if (wikiText != null) {
			Matcher m = p.matcher(wikiText);
			while (m.find()) {
				String g = m.group();
				if (g.length() > 4) {
					String s = g.substring(2, g.length() - 2);
					ss.add(s);
				}
			}
		}

		return ss;
	}

	public static List<String> parseCategoryTags(String t) {
		List<String> tags = new ArrayList<>();

		if (t != null) {
			for (String line : t.split("\n")) {
				line = line.trim();

				if (line.startsWith("[[Category:")) {
					if (line.contains("|")) {
						String v = line.substring(11, line.indexOf('|'));
						{
							int idx = v.indexOf("]]");
							if (idx != -1) {
								v = v.substring(0, idx);
							}
						}
						tags.add(v);
					} else {
						String v = line.substring(11, line.length() - 2);
						{
							int idx = v.indexOf("]]");
							if (idx != -1) {
								v = v.substring(0, idx);
							}
						}
						tags.add(v);
					}
				}
			}
		}

		return tags;
	}

	private static String parseTag(String line) {
		if (line == null) {
			return null;
		}
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

//	private static String parseTextFromLink(String link) {
//		// https://ja.wikipedia.org/wiki/Help:%E3%83%AA%E3%83%B3%E3%82%AF
//
//		// パイプ付き
//		if (link.split("\\|").length == 2) {
//			return link.split("\\|")[1];
//		}
//		// 特別:転送
//		else if (link.startsWith("特別:転送")) {
//			return "";
//		}
//		//
//		else if (link.startsWith(":")) {
//			return "";
//		}
//		//
//		else if (link.startsWith("ファイル")) {
//			return "";
//		} else if (link.contains("#")) {
//			int idx = link.indexOf("#");
//			return link.substring(0, idx);
//		}
//		//
//		else {
//			return link;
//		}
//
//	}

	/**
	 * @param wikiText wiki形式のテキスト wiki format text
	 * @return
	 */
	public static List<String> parseTemplateTags(String wikiText) {
		List<String> tags = new ArrayList<>();

		if (wikiText != null) {
			// FOR EACH LINE
			for (String line : wikiText.split("\n")) {
				if (line.startsWith("{{") == true) {
					String tag = parseTag(line);

					if (tag.contains("<!--")) {
						int idx1 = tag.indexOf("<!--");
						if (idx1 > 0) {
							tag = tag.substring(0, idx1);
						}
					}

					if (tag != null) {
						if (tags.contains(tag) == false) {
							tags.add(tag);
						}
					}
				}
			} // END OF ( FOR EACH LINE)
		}

//		Collections.sort(tags);

		return tags;
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
	 * @param wikiText
	 * @return wikiText without Infovox
	 */
	static public String removeInfobox(String wikiText) {

		StringBuilder sbWikiText = new StringBuilder();
		List<String> infoBoxes = new ArrayList<String>();

		extractedInfobox(wikiText, sbWikiText, infoBoxes);

		boolean debug = false;

		if (debug == true) {
			if (infoBoxes.size() > 0) {
				for (String ib : infoBoxes) {
					System.err.println("<infobox>");
					System.err.println(ib.toString());
					System.err.println("</infobox>");
				}
			}
		}

		return sbWikiText.toString();
	}

	public static String removeTable(String wikitext) {
		if (wikitext == null) {
			return null;
		}

		if (wikitext.contains("\n{|") && wikitext.contains("\n|}")) {
			wikitext = wikitext.replace("\r", "").replace("\n", " ")
			// https://qiita.com/ymatsuta/items/cff10a2d8d0fa6b69485
//					.replaceAll("\\{\\| (.|\\n|\\r)*?\\|\\}", "");
					.replaceAll("\\{\\|.*? \\|\\}", "");
		}

		return wikitext;
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
		if (wikiText == null) {
			return null;
		}

		wikiText = WikiTemplateParser.removeInlineTemplate(wikiText);

		// Set-up a simple wiki configuration
		try {

			String text;

			{ // FOR EACH LINE
				StringBuilder sb = new StringBuilder();
				for (String t : wikiText.split("\n")) {
					t = t.trim();
					if (t.startsWith("[[File:") && t.endsWith("]]")) {
						continue; // SKIP THIS LINE
					} //
					else if (t.startsWith("[[ファイル:") && t.endsWith("]]")) {
						continue; // SKIP THIS LINE
					} //
					else if (t.startsWith("[[画像:") && t.endsWith("]]")) {
						continue; // SKIP THIS LINE
					} //
					else {
						sb.append(t + "\n");
					}
				}
				wikiText = sb.toString();
			}

//			System.err.println(wikiText);
//			System.err.println("---");

			{ // REMOVE TABLE
				wikiText = removeTable(wikiText);
			}

			{ // REMOVE {{Infobox}}
				wikiText = removeInfobox(wikiText);
			}

//			System.err.println(wikiText);
//			System.err.println("---");

			{ // REMOVE （...）
				wikiText = StringUtils.removeBracketted(wikiText, "（）"); // JA BRACKETS
			}

//			{
//				wikiText = wikiText //
//						.replaceAll("\\[\\[File.*?\\]\\]", "") // ファイルへのリンクを除去 JA_ONLY
//						.replaceAll("\\[\\[ファイル.*?\\]\\]", "") // ファイルへのリンクを除去 JA_ONLY
//						.trim();
//			}

			{ // DEBUG
				if (wikiText.contains("<gallery>")) {
					wikiText = wikiText //
							.replace("\n", "\\n") //
							.replaceAll("<gallery>.*?</gallery>", "") // ファイルへのリンクを除去 JA_ONLY
							.replace("\\n", "\n")//
					;
				}
				if (wikiText.contains("<ref>")) {
					wikiText = wikiText //
							.replace("\n", "\\n")//
							.replaceAll("<ref>.*?</ref>", "") // ファイルへのリンクを除去 JA_ONLY
							.replace("\\n", "\n")//
					;
				}
				if (wikiText.contains("<imagemap>")) {
					wikiText = wikiText //
							.replace("\n", "\\n")//
							.replaceAll("<imagemap>.*?</imagemap>", "") // ファイルへのリンクを除去 JA_ONLY
							.replace("\\n", "\n")//
					;
				}
			}

			// TODO 大学テンプレートの処理

//			System.err.println(wikiText);
//			System.err.println("---");

			text = sweble(wikiTitle, wikiText);

//			System.err.println(wikiText);
//			System.err.println("---");

			{
				text = text //
						.replace("\n\n", " ") // 連続する改行を半角空白にする
//						.replace("\n", "") // 改行を除去 // TODO 2023-08-15
//						.replaceAll("\\[\\[.*?\\]\\]", "") //
						.replaceAll("（.*?）", "") // カッコ注釈を外す
						.replaceAll("\\(.*?\\)", "") // カッコ注釈を外す
						.replaceAll("/\\*\\*.*?\\*\\*/", "") // コメント
						.replace("**", "") //
						.replace("<WtRedirect />", "") //
						.replace("<WtTable />", "") //
						.trim();
			}

//			System.err.println(wikiText);
//			System.err.println("---");
			return text;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	private static String sweble(String wikiTitle, String wikiText) throws LinkTargetException, EngineException {
		String text;
		final int wrapCol = 1000;
		// Retrieve a page
		PageTitle pageTitle = PageTitle.make(config, wikiTitle);
		PageId pageId = new PageId(pageTitle, -1);
		// Instantiate a compiler for wiki pages
		// Compile the retrieved page
		EngProcessedPage cp = engine.postprocess(pageId, wikiText, null);
		TextConverter p = new TextConverter(config, wrapCol);
		text = (String) p.go(cp.getPage());
		return text;
	}

// REMOVED 2022-01-27
//	public static String toPlainText2(String wikiText) {
//
//		StringBuilder sb = new StringBuilder();
//
//		Stack<Character> stack = new Stack<>();
//		int status = 0; // 0:default,1:filelink,2:リンク中のアンカー
//
//		for (int n = 0; n < wikiText.length(); n++) {
//			char c = wikiText.charAt(n);
//			String rest = wikiText.substring(n);
//
//			if (c == '{' || c == '[') {
//				stack.push(c);
////				System.err.println(toString(stack));
//			} //
//			else if (c == '}' || c == ']') {
//				if (stack.isEmpty() == false) {
//					stack.pop();
//				}
////				System.err.println(toString(stack));
//				if (stack.size() == 0) {
//					status = 0; // リセットされる
//				}
//			}
//			//
//			else {
//				if (stack.size() == 0) {
//					if (c == '\'') {
//
//					} else {
//						sb.append(c);
//					}
//				} //
//				else if (stack.size() == 2 && toString(stack).equals("[[")) {
//
//					if (status == 0) {
//						int idx = rest.indexOf("]]");
//						// 閉じている
//						if (idx != -1) {
//							String link = rest.substring(0, idx);
//							String text = parseTextFromLink(link);
//							sb.append(text);
//						}
//						// 閉じてない
//						else {
//							sb.append(rest);
//						}
//						status = 2;
//					}
//
////					// ファイルへのリンク
////					if (rest.startsWith("ファイル")) {
////						status = 1;
////					}
////					if (c == '#') { // リンク中のアンカー
////						status = 2;
////					}
////					if (status == 0) {
////						sb.append(c);
////					}
//				}
//			}
//		}
//
//		return sb.toString();
//	}

//	static private String toString(Stack stack) {
//		StringBuilder sb = new StringBuilder();
//		stack.forEach(c -> {
//			sb.append(c);
//		});
//		return sb.toString();
////	return(stack.toString().substring(1,stack.toString().length()-1));	
//	}

}
