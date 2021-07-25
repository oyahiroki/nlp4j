package nlp4j.wiki;

import java.util.ArrayList;

public class WikiPageNode {

	String header;

	StringBuilder text = new StringBuilder();

	private int level = -1;

	private WikiPageNode parent;

	ArrayList<WikiPageNode> children = new ArrayList<WikiPageNode>();

	public void addText(String s) {
		if (s == null) {
			return;
		}
		if (text.length() > 0) {
			text.append("\n");
		}
		text.append(s);
	}

	public String getHeader() {
		return header;
	}

	public String getText() {
		return text.toString();
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setLevel(int level) {
		this.level = level;

	}

	public WikiPageNode getParent() {
		return parent;
	}

	public void setParent(WikiPageNode parent) {
		this.parent = parent;
		if (this.parent != null) {
			this.parent.addChild(this);
		}
	}

	public ArrayList<WikiPageNode> getChildren() {
		return children;
	}

	public void addChild(WikiPageNode child) {
		this.children.add(child);
	}

	public int getLevel() {

		return this.level;
	}

	public WikiPageNode getParentByLevel(int lvl) {
		if (this.parent == null) {
			return null;
		} //
		else {
			if (this.parent.level < lvl) {
				return this.parent;
			} //
			else {
				return this.parent.getParentByLevel(lvl);
			}
		}
	}

}
