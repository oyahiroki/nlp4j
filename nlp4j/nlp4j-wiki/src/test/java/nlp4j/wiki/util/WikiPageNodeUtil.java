package nlp4j.wiki.util;

import nlp4j.wiki.WikiPageNode;

public class WikiPageNodeUtil {

	static public String toString(WikiPageNode pageNode) {
		StringBuilder sb = new StringBuilder();

		print(sb, pageNode, 0);

		return sb.toString();
	}

	private static void print(StringBuilder sb, WikiPageNode pageNode, int depth) {

		String indent = "";
		for (int n = 0; n < depth; n++) {
			indent += "\t";
		}

		sb.append(indent + "<header>" + pageNode.getHeader() + "</header>" + "\n");
		{
			String text = pageNode.getText();
			if (text.length() > 16) {
				text = text.substring(0, 16) + "...";
			}
			text = text.replace("\n", "\\n").replace("\r", "\\r");
			sb.append(indent + "<text>" + text + "</text>" + "\n");
		}

		if (pageNode.getChildren() != null) {
			for (WikiPageNode p : pageNode.getChildren()) {
				print(sb, p, depth + 1);
			}
		}
	}

}
