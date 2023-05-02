package nlp4j.wiki;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.wiki.template.TemplateParser;
import nlp4j.wiki.util.StringUtils;
import nlp4j.wiki.util.WikiUtils;

/**
 * <pre>
 * </pre>
 * 
 * created on 2021-07-26
 * 
 * @author Hiroki Oya
 */
public class WikipediaItemTextParser {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static private final String LV1 = "==";
	static private final String LV2 = "===";
	static private final String LV3 = "====";
	static private final String LV4 = "=====";
	static private final String LV5 = "======";

	private ArrayList<WikiPageNode> list = new ArrayList<>();
	private WikiPageNode root = null;
	private WikiPageNode ptr = null;

	private int level = 0;

	private StringBuilder sbInfobox = new StringBuilder();

	/**
	 * 
	 */
	public WikipediaItemTextParser() {
		super();
		WikiPageNode wpd = new WikiPageNode();
		wpd.setLevel(0);
		this.root = wpd;
		this.ptr = wpd;
	}

	public String getInfobox() {
		if (this.sbInfobox == null) {
			return null;
		} else {
			return this.sbInfobox.toString();
		}
	}

	/**
	 * 
	 * @param head like "=={{ja}}==","==={{noun}}==="
	 * @return
	 */
	private int getLevel(String head) {
		if (head.indexOf("{") != -1) {
			return head.indexOf("{") - 1;
		} //
		else {
			if (head.startsWith(LV5)) {
				return 5;
			} //
			else if (head.startsWith(LV4)) {
				return 4;
			} //
			else if (head.startsWith(LV3)) {
				return 3;
			} //
			else if (head.startsWith(LV2)) {
				return 2;
			} //
			else if (head.startsWith(LV1)) {
				return 1;
			} //
			else {
				return 0;
			}
		}
	}

	public WikiPageNode getRoot() {
		return root;
	}

	public List<WikiPageNode> getWikiPageNodesAsList() {
		return list;
	}

	/**
	 * Wikipage を解析する
	 * 
	 * @param wikiItemText Wiki形式の文字列
	 * @return
	 */
	public WikiPageNode parse(String wikiItemText, JsonObject page) {

		{ // normalize new line
			int n1 = wikiItemText.length();
			wikiItemText = wikiItemText.replace("\r\n", "\n");
			int n2 = wikiItemText.length();
			if (n1 != n2) {
				logger.info("New Line Code Normalized: " + (n1 - n2));
			}
		}

		{ // REMOVE XML COMMENT : XMLコメントを削除する
			// 複数行のXMLコメントは存在するか？(文法上は可能)
			wikiItemText = StringUtils.removeXmlComment(wikiItemText);
		}

		boolean isInfobox = false;

		boolean gotAbstract = false;

		List<String> redirectsFrom = new ArrayList<>();

		// FOR EACH LINE
		for (String line : wikiItemText.split("\n")) {

			{ // TRIM トリムする
				int n1 = line.length();
				line = line.trim();
				int n2 = line.length();
				if (n2 != n1) {
					logger.debug("Line trimmed: " + line);
				}
			}

			if (line.isEmpty()) {
//				logger.info("Empty Line");
				continue;
			}

			// リダイレクトされている場合
			// {{Redirect|&}}
			{
				List<String> params = TemplateParser.parseTemplateLine(line, "Redirect");
				if (params != null && params.size() == 1) {
//					System.err.println("Redirect from " + params.get(0));
					redirectsFrom.add(params.get(0));
					continue;
				}
			}

			// 何か他のテンプレート
			{
				if (line.startsWith("{{") && line.endsWith("}}")) {
					logger.debug("Other template");
					continue;
				}
			}

			// ファイルリンク
			{
				if (line.startsWith("[[File") && line.endsWith("]]")) {
					logger.debug("File");
					continue;
				}
			}

			{ // Infobox
				// Infobox 開始
				if (line.startsWith("{{Infobox")) {
					isInfobox = true;
					sbInfobox.append(line + "\n");
					continue;
				}
				if (isInfobox == true && line.equals("}}") == false) {
					sbInfobox.append(line + "\n");
					logger.debug("InfoBox: " + sbInfobox);
					continue;
				} //
				else if (isInfobox == true && line.equals("}}")) {
					isInfobox = false;
					sbInfobox.append(line + "\n");

					logger.debug("InfoBox: " + sbInfobox);

					continue;
				}
			}

//			// Wiktionary -> Wikipedia へのリンク
//			// https://ja.wiktionary.org/wiki/%E3%83%86%E3%83%B3%E3%83%97%E3%83%AC%E3%83%BC%E3%83%88:wikipedia
//			if (line.startsWith("{{wikipedia") && line.endsWith("}}")) {
//				// {{wikipedia}} 引数なし: 同名のWikipedia記事
//				// {{wikipedia|記事名}}
//				// {{wikipedia|記事名|表示名}}
//				// TODO Wikipedia リンクの処理
//				System.err.println("wikipedia: " + line);
//			}

			// Wikitionary では必ずLV1から始まるが、Wikipediaではそうではない

			if (gotAbstract == false) {
				String abstractText = line;
				logger.debug("Abstract Text??: " + abstractText);

				WikiPageNode wpd = new WikiPageNode();

				if (redirectsFrom.size() > 0) {
					JsonArray arr = new JsonArray();
					for (String r : redirectsFrom) {
						arr.add(r);
					}
					page.add("redirected", arr);
				}

				{ // header
					wpd.setHeader("abstract");
				}

				wpd.setLevel(0);
				list.add(wpd);

				this.ptr = wpd;
				this.level = 0;

				ptr.addText(line);

				gotAbstract = true;
				continue;
			}

			// is header
			// header
			// === {{noun}}: 犬・狗 ===
			// === {{noun}}: 戌 ===
			// === {{verb}}・往 ===
			if (line.startsWith(LV1)) {

				// 「豚」で以下のような例があった。(20210905)
				// ==={{noun}}=== <!--品詞 (名詞)-->
				// {{see|ぶた}}
				// [[Category:{{jpn}} {{noun}}|ふた ぶた]]
				// [[Category:{{jpn}} 豚|*]]
				// #ぶた。偶蹄目イノシシ科の動物で、家畜の一種。
				{
					int lastidx = line.lastIndexOf(LV1);
					if (lastidx != -1 && (line.length() > lastidx + 2)) {
						line = line.substring(0, lastidx + 2);
					}
				}

				line = line.replace(" ", "");

				int levelCurrent = getLevel(line);

				WikiPageNode wpd = new WikiPageNode();

				{ // normalize header
					String normailzedHeader = WikiUtils.normailzeHeader(line);
					wpd.setHeader(normailzedHeader);
				}

				{ // TODO Extract spell
					String[] ss = WikiUtils.extractSpells(line);
					if (ss != null && ss.length > 0) {
						wpd.setSpells(ss);
					}
				}

				wpd.setLevel(levelCurrent);
				list.add(wpd);

				if (this.level < levelCurrent) {
					wpd.setParent(this.ptr);
				} //
				else {
					WikiPageNode p = this.ptr.getParentByLevel(levelCurrent);
					wpd.setParent(p);
				}

				this.ptr = wpd;

				this.level = levelCurrent;

			} //
			else {
				ptr.addText(line);
			}
		} // END OF FOR EACH LINE

		return this.root;
	}

//	/**
//	 * Wikipage を解析する
//	 * 
//	 * @param page
//	 * @return
//	 */
//	public WikiPageNode parse(WikiPage page) {
//		if (page == null) {
//			return null;
//		} else {
//			return parse(page.getText());
//		}
//	}

	public String toWikiPageNodeTree() {
		StringBuilder sb = new StringBuilder();
		toWikiPageNodeTree(sb, this.root, 0);
		return sb.toString();
	}

	private void toWikiPageNodeTree(StringBuilder sb, WikiPageNode node, int depth) {
		for (int n = 0; n < depth; n++) {
			sb.append("\t");
		}
		if (depth != 0) {
			sb.append(node.getHeader() + "\n");
		}

		for (WikiPageNode c : node.getChildren()) {
			toWikiPageNodeTree(sb, c, depth + 1);
		}

	}

}
