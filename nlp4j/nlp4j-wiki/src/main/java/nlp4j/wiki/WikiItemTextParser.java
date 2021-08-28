package nlp4j.wiki;

import java.util.ArrayList;

import nlp4j.node.Node;

/**
 * @author Hiroki Oya
 * @created_at 2021-07-26
 */
public class WikiItemTextParser {

	ArrayList<WikiPageNode> list = new ArrayList<>();
	WikiPageNode root = null;
	WikiPageNode ptr = null;

	int level = 0;

	public WikiPageNode getRoot() {
		return root;
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
			if (head.startsWith("======")) {
				return 5;
			} //
			else if (head.startsWith("=====")) {
				return 4;
			} //
			else if (head.startsWith("====")) {
				return 3;
			} //
			else if (head.startsWith("===")) {
				return 2;
			} //
			else if (head.startsWith("==")) {
				return 1;
			} //
			else {
				return 0;
			}
		}
	}

	public void parse(String wikiItemText) {

		// FOR EACH LINE
		for (String line : wikiItemText.split("\n")) {

			// is header
			// header
			// === {{noun}}: 犬・狗 ===
			// === {{noun}}: 戌 ===
			// === {{verb}}・往 ===
			if (line.startsWith("==")) {
				line = line.replace(" ", "");

//				int lvl = line.indexOf("{") - 1;
				int lvl = getLevel(line);

//				System.err.println("<h" + lvl + ">" + line);

				WikiPageNode wpd = new WikiPageNode();
				{ // normalize header
					String normailzedHeader = WikiUtils.normailzeHeader(line);
					wpd.setHeader(normailzedHeader);
				}

				{ // TODO Extract spell
					String[] ss = WikiUtils.extractSpells(line);
					if (ss != null && ss.length > 0) {
//						for (String s : ss) {
//							System.err.println("spell: " + s);
//						}
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

//			if (line.startsWith("=={")) {
//				System.err.println("<h1>" + line);
//				int lvl = 1;
//
//				WikiPageNode wpd = new WikiPageNode();
//				wpd.setHeader(line);
//				wpd.setLevel(lvl);
//
//				this.level = lvl;
//				continue;
//			} //
//			else if (line.startsWith("==={")) {
//				System.err.println("<h2>" + line);
//				int lvl = 2;
//
//				this.level = lvl;
//				continue;
//			} //
//			else if (line.startsWith("===={")) {
//				System.err.println("<h3>" + line);
//				int lvl = 3;
//
//				this.level = lvl;
//				continue;
//			} //
//			else if (line.startsWith("====={")) {
//				System.err.println("<h4>" + line);
//				int lvl = 4;
//
//				this.level = lvl;
//				continue;
//			} //
//			else if (line.startsWith("======{")) {
//				System.err.println("<h5>" + line);
//				int lvl = 5;
//
//				this.level = lvl;
//				continue;
//			} //
//			else if (line.equals("-----")) {
//				System.err.println("<hr>");
//				head = line;
//				int lvl = 0;
//
//				this.level = lvl;
//				continue;
//			} //
//			else {
//			ptr.addText(line);
//			}

//			Node<String> n1 = new Node<String>("a");
//			{
//				n1.addChildNode(new Node<String>("b"));
//				n1.addChildNode(new Node<String>("c"));
//			}

		} // END OF FOR EACH LINE

//		System.err.println("done----------------------");

//		for (WikiPageNode node : list) {
//			System.err.println("<node>");
//			System.err.println("<this>" + node.toString() + "</this>");
//			System.err.println("<hasparent>" + (node.getParent() != null) + "</hasparent>");
//			if ((node.getParent() != null)) {
//				System.err.println("<parent>" + node.getParent().toString() + "</parent>");
//				System.err.println("<parent_level>" + node.getParent().getLevel() + "</parent_level>");
//				System.err.println("<parent_header>" + node.getParent().getHeader() + "</parent_header>");
//			}
//			System.err.println("<level>" + node.getLevel() + "</level>");
//			System.err.println("<header>" + node.getHeader() + "</header>");
//			// System.err.println("<text>\n" + node.getText() + "\n</text>");
//			System.err.println("<text>length=" + node.getText().length() + "</text>");
//			if (node.getChildren() != null && node.getChildren().size() > 0) {
//				System.err.println("<children>");
//				for (WikiPageNode n : node.getChildren()) {
//					System.err.println("<child>" + n.toString() + "</child>");
//					System.err.println("<child_header>" + n.getHeader() + "</child_header>");
//				}
//				System.err.println("</children>");
//			}
//			System.err.println("</node>\n");
//		}

	}

}
