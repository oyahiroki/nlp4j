package nlp4j.wiki.util;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.example.TextConverter;
import org.sweble.wikitext.parser.parser.LinkTargetException;

import nlp4j.util.TextUtils;
import nlp4j.wiki.WikiItemTextParser;
import nlp4j.wiki.WikiItemTextParserInterface;
import nlp4j.wiki.WikiPageNode;

public class MediaWikiTextUtils {

	private static final String S2 = "\n|}";

	private static final String S = "\n{|";

	private static final char CHAR4 = ']';

	private static final char CHAR3 = '[';

	private static final char CHAR2 = '}';

	private static final char CHAR = '{';

	private static final String PREFIX = "{{";

	private static final String STR2 = "[[";

	private static final String S_PIPE = "|";

	private static final String STR = "]]";

	private static final char CH_PIPE = '|';

	private static final String NEWLINE = "\n";

	private static final String CATEGORY = "[[Category:";

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

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

		for (String line : wikiText.split(NEWLINE)) {
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
			sb.append(NEWLINE);
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
	 * Wikipedia記事の第一文をPlainTextで取得する
	 * 
	 * @param wikiText
	 * @param title
	 * @param lang
	 * @return
	 */
	static public String getRootNodeTextFirstSentence(String wikiText, String title, String lang) {
		String rootNodeText = MediaWikiTextUtils.getRootNodeText(wikiText);

		if (logger.isDebugEnabled()) {
			logger.debug("<rootNodeText>");
			logger.debug(rootNodeText);
			logger.debug("</rootNodeText>");
		}

		// IF(NOT_FOUND)
		if (rootNodeText == null) {
			return null;
		}

		if (rootNodeText.trim().startsWith(STR2)) {
			rootNodeText = MediaWikiTextUtils.removeLinkFirst(rootNodeText);
		}

		{
			rootNodeText = MediaWikiTextUtils.removeTemplateAll(rootNodeText);
		}
		{
//			rootNodeText = rootNodeText.replaceAll("\\{\\{.*?\\}\\}", "");
		}
		{
//			rootNodeText = rootNodeText.replaceAll("<ref .*?</ref>", "");

			rootNodeText = Jsoup.parse(rootNodeText).text();
		}

		{

//|-
//| colspan="2" style="padding: 0;"|
//
//
//
//『'''ゲート 自衛隊 彼の地にて、斯く戦えり'''』（ゲート じえいたい かのちにて かくたたかえり）は、[[柳内たくみ]]による[[日本]]の[[ファンタジー]][[小説]]。

			// 適切でないフォーマットへの対応
			if (rootNodeText.contains("colspan")) {
				int lastIndex = rootNodeText.lastIndexOf(S_PIPE);
				if (lastIndex != -1) {
					rootNodeText = rootNodeText.substring(lastIndex + 1).trim();
				}
			}

		}

		{
//			int idx = rootNodeText.lastIndexOf("}}\n\n");
//			if (idx != -1) {
//				rootNodeText = rootNodeText.substring(idx + 2).trim();
//			}
		}

		{
			rootNodeText = MediaWikiTextUtils.removeFirstTemplate(rootNodeText);
		}

		String text = MediaWikiTextUtils.toPlainText(title, rootNodeText);
		{
			int idx1 = text.indexOf(PREFIX);
			int idx2 = text.indexOf("}}");
			if (idx1 == -1 && idx2 > 0) {
				text = text.substring(idx2 + 2);
			}
		}

		String separator = ".";
		String separator_regex = "\\.";

		if (lang.equals("ja")) {
			separator = "。";
			separator_regex = "。";
		} //
		else if (lang.equals("zh")) {
			separator = "。";
			separator_regex = "。";
		} //

		if (title.contains(separator) == false || text.split(separator_regex).length == 1) {
			text = text.split(separator_regex)[0];
		} else {
			text = (text.split(separator_regex)[0] + separator + text.split(separator_regex)[1]);
		}

		text = text.replace("\u00a0", "");
		text = text.replaceAll("\\s+", " "); // 2回以上連続する空白を削除

		return text;
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

	/**
	 * wiki形式のテキストからカテゴリーのタグを取得する
	 * 
	 * @param wikiText
	 * @return
	 */
	public static List<String> parseCategoryTags(String wikiText) {
		List<String> tags = new ArrayList<>();

		if (wikiText != null) {
			for (String line : wikiText.split(NEWLINE)) {
				line = line.trim();

				if (line.startsWith(CATEGORY)) {
					if (line.contains(S_PIPE)) {
						int beginIndex = 11;
						int endIndex = line.indexOf(CH_PIPE);
						if (beginIndex < endIndex) {
							String v = line.substring(beginIndex, endIndex);
							{
								int idx = v.indexOf(STR);
								if (idx != -1) {
									v = v.substring(0, idx);
								}
							}
							tags.add(v);
						}
					} else {
						int beginIndex = 11;
						int endIndex = line.length() - 2;
						if (beginIndex < endIndex) {
							String v = line.substring(beginIndex, endIndex);
							{
								int idx = v.indexOf(STR);
								if (idx != -1) {
									v = v.substring(0, idx);
								}
							}
							tags.add(v);
						} //
						else {
							if (logger.isInfoEnabled()) {
								logger.info("Invalid_String: " + line);
							}
						}
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
		if (line.contains(S_PIPE)) {
			int idx = line.indexOf(S_PIPE);
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
			for (String line : wikiText.split(NEWLINE)) {
				if (line.startsWith(PREFIX) == true) {
					String tag = parseTag(line);

					if (tag != null) {
//					if (tag.contains("<!--")) {
						int idx1 = tag.indexOf("<!--");
						if (idx1 > 0) {
							tag = tag.substring(0, idx1);
						}
//					}

						{
							if (tags.contains(tag) == false) {
								tags.add(tag);
							}
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
			int idx1 = t.indexOf(STR2);
			int idx2 = t.indexOf(STR);
			if (idx1 != -1 && idx2 != -1) {
				t = t.substring(idx1 + 2, idx2);
			}
		}

		return t;
	}

	public static String removeFirstTemplate(String wikitext) {
		String wikitext_org = wikitext;

		wikitext = wikitext.trim();

		if (wikitext.startsWith(PREFIX) == false) {
			return wikitext_org;
		} else {

			int status = 0;
			for (int n = 0; n < wikitext.length(); n++) {
				char c = wikitext.charAt(n);
				if (c == CHAR) {
					status++;
				} else if (c == CHAR2) {
					status--;
				}
				if (status == 0) {
					if (wikitext.length() >= (n + 1)) {
						return wikitext.substring(n + 1).trim();
					} else {
						return "";
					}
				}
			}
		}
		return wikitext_org;

	}

	/**
	 * @param wikiText
	 * @return wikiText without Infovox
	 */
	static public String removeInfobox(String wikiText) {

		StringBuilder sbWikiText = new StringBuilder();
		List<String> infoBoxes = new ArrayList<String>();

		extractedInfobox(wikiText, sbWikiText, infoBoxes);

//		{
//			boolean debug = false;
//
//			if (debug == true) {
//				if (infoBoxes.size() > 0) {
//					for (String ib : infoBoxes) {
//						System.err.println("<infobox>");
//						System.err.println(ib.toString());
//						System.err.println("</infobox>");
//					}
//				}
//			}
//		}

		return sbWikiText.toString();
	}

	public static String removeLinkFirst(String wikitext) {
		wikitext = wikitext.trim();
		StringBuilder sb = new StringBuilder();
		{
			int status = 0;
			for (int n = 0; n < wikitext.length(); n++) {
				char c = wikitext.charAt(n);
				if (c == CHAR3) {
					status++;
				} else if (c == CHAR4) {
					status--;
					if (status == 0) {
						return sb.toString() + wikitext.substring(n + 1);
					}
				}
				if (status == 0) {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	private static final String OPEN = "{|";
	private static final String CLOSE = "|}";
	// DOTALLでもOK: Pattern.compile("\\{\\|.*?\\|\\}", Pattern.DOTALL)
	private static final Pattern TABLE = Pattern.compile("\\{\\|[\\s\\S]*?\\|\\}");

	/**
	 * @param wikitext
	 * @return
	 */
	public static String removeTable(String wikitext) {
		if (wikitext == null) {
			return null;
		}

//		if (wikitext.contains(S) && wikitext.contains(S2)) {
//			wikitext = wikitext.replace("\r", "").replace(NEWLINE, "【NLP4J_NL】")
//					// https://qiita.com/ymatsuta/items/cff10a2d8d0fa6b69485
//					.replaceAll("\\{\\|.*? \\|\\}", "") //
//					.replace("【NLP4J_NL】", NEWLINE) //
//			;
//		}

		{
			int open = wikitext.indexOf(OPEN);
			if (open < 0) {
				// CRが混ざる可能性があるならだけ除去
				return (wikitext.indexOf('\r') >= 0) ? wikitext.replace("\r", "") : wikitext;
			}
			// 終了記号がなければ何もしない（元コードと同等の保守的動作）
			if (wikitext.indexOf(CLOSE, open + OPEN.length()) < 0) {
				return (wikitext.indexOf('\r') >= 0) ? wikitext.replace("\r", "") : wikitext;
			}

			String noCR = (wikitext.indexOf('\r') >= 0) ? wikitext.replace("\r", "") : wikitext;
			wikitext = TABLE.matcher(noCR).replaceAll("");
		}

		return wikitext;
	}

	public static String removeTemplateAll(String wikitext) {
		wikitext = wikitext.trim();
		StringBuilder sb = new StringBuilder();
		{
			int status = 0;
			for (int n = 0; n < wikitext.length(); n++) {
				char c = wikitext.charAt(n);
				if (c == '{') {
					status++;
				} else if (c == '}') {
					status--;
					if (status == 0) {
						continue;
					}
				}
				if (status == 0) {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	public static String sweble(String wikiTitle, String wikiText) throws LinkTargetException, EngineException {
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
				for (String t : wikiText.split(NEWLINE)) {
					t = t.trim();
					if (t.startsWith("[[File:") && t.endsWith(STR)) {
						continue; // SKIP THIS LINE
					} //
					else if (t.startsWith("[[ファイル:") && t.endsWith(STR)) {
						continue; // SKIP THIS LINE
					} //
					else if (t.startsWith("[[画像:") && t.endsWith(STR)) {
						continue; // SKIP THIS LINE
					} //
					else {
						sb.append(t + NEWLINE);
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

			{ // NFKC
				wikiText = TextUtils.nfkc(wikiText);
			}
			{ // REMOVE （...）
				wikiText = StringUtils.removeBracketted(wikiText, "()"); // BRACKETS
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
							.replace(NEWLINE, "\\n") //
							.replaceAll("<gallery>.*?</gallery>", "") // ファイルへのリンクを除去 JA_ONLY
							.replace("\\n", NEWLINE)//
					;
				}
				if (wikiText.contains("<ref>")) {
					wikiText = wikiText //
							.replace(NEWLINE, "\\n")//
							.replaceAll("<ref>.*?</ref>", "") // ファイルへのリンクを除去 JA_ONLY
							.replace("\\n", NEWLINE)//
					;
				}
				if (wikiText.contains("<imagemap>")) {
					wikiText = wikiText //
							.replace(NEWLINE, "\\n")//
							.replaceAll("<imagemap>.*?</imagemap>", "") // ファイルへのリンクを除去 JA_ONLY
							.replace("\\n", NEWLINE)//
					;
				}
			}

			// TODO 大学テンプレートの処理

//			System.err.println(wikiText);
//			System.err.println("---");

			{ // リダイレクト対応
				if (WikipediaJaUtil.isRedirectPage(wikiText)) {
					wikiText = WikipediaJaUtil.getRedirectPageTitle(wikiText);
					return wikiText;
				}
			}

			// 「#REDIRECT [[サンドボックス]]」 → 「<WtRedirect />」 に変換される
			text = sweble(wikiTitle, wikiText);

//			System.err.println(wikiText);
//			System.err.println("---");

			{
				text = text //
//						.replace("\n\n", " ") // 連続する改行を半角空白にする
//						.replace("\n", "") // 改行を除去 // TODO 2023-08-15
//						.replaceAll("\\[\\[.*?\\]\\]", "") //
						.replaceAll("（.*?）", "") // カッコ注釈を外す
						.replaceAll("\\(.*?\\)", "") // カッコ注釈を外す
						.replaceAll("/\\*\\*.*?\\*\\*/", "") // コメントを外す
						.replace("**", "") // 強調記号を外す
						.replace("<WtRedirect />", "") // Swebleリダイレクトを外す
						.replace("<WtTable />", "") // Swebleテーブルを外す
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
