package nlp4j.wiki;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <pre>
 * created_at 2021-07-26
 * </pre>
 * 
 * @author Hiroki Oya
 */
public class WikiItemTextParser {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private static final String LV1 = "==";
	private static final String LV2 = "===";
	private static final String LV3 = "====";
	private static final String LV4 = "=====";
	private static final String LV5 = "======";

	private ArrayList<WikiPageNode> list = new ArrayList<>();
	private WikiPageNode root = null;
	private WikiPageNode ptr = null;

	private int level = 0;

	/**
	 * 
	 */
	public WikiItemTextParser() {
		super();
		WikiPageNode wpd = new WikiPageNode();
		wpd.setLevel(0);
		this.root = wpd;
		this.ptr = wpd;
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

	public void parse(String wikiItemText) {

		{ // normalize new line
			int n1 = wikiItemText.length();
			wikiItemText = wikiItemText.replace("\r\n", "\n");
			int n2 = wikiItemText.length();
			if (n1 != n2) {
				logger.info("New Line Code Normalized: " + (n1 - n2));
			}
		}

		// FOR EACH LINE
		for (String line : wikiItemText.split("\n")) {

			{
				int n1 = line.length();
				line = line.trim();
				int n2 = line.length();
				if (n2 != n1) {
					logger.debug("Line trimmed: " + line);
				}
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

				int lvl = getLevel(line);

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

				wpd.setLevel(lvl);
				list.add(wpd);

				if (this.level < lvl) {
					wpd.setParent(this.ptr);
				} //
				else {
					WikiPageNode p = this.ptr.getParentByLevel(lvl);
					wpd.setParent(p);
				}

				this.ptr = wpd;

				this.level = lvl;

			} //
			else {
				ptr.addText(line);
			}
		} // END OF FOR EACH LINE

	}

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
