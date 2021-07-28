package nlp4j.wiki;

import java.util.ArrayList;
import java.util.List;

public class WikiPageNode {

	String header;

	StringBuilder text = new StringBuilder();

	private int level = -1;

	private WikiPageNode parent;

	ArrayList<WikiPageNode> children = new ArrayList<WikiPageNode>();

	public void addChild(WikiPageNode child) {
		this.children.add(child);
	}

	public void addText(String s) {
		if (s == null) {
			return;
		}
		if (text.length() > 0) {
			text.append("\n");
		}
		text.append(s);
	}

	public List<WikiPageNode> getChildren() {
		return children;
	}

	public List<WikiPageNode> getChildrenByHeader(String s) {
		ArrayList<WikiPageNode> arr = new ArrayList<>();
		if (this.children == null) {
			return arr;
		} else {
			for (WikiPageNode n : this.children) {
				if (n.containsHeader(s)) {
					arr.add(n);
				}
			}
		}
		return arr;
	}

	public String getHeader() {
		return header;
	}

	public int getLevel() {

		return this.level;
	}

	public WikiPageNode getParent() {
		return parent;
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

	public String getText() {
		return text.toString();
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setLevel(int level) {
		this.level = level;

	}

	public void setParent(WikiPageNode parent) {
		this.parent = parent;
		if (this.parent != null) {
			this.parent.addChild(this);
		}
	}

	public boolean matchHeader(String pattern) {
		if (this.header == null) {
			return false;
		} //
		else {
			return this.header.matches(pattern);
		}
	}

	public boolean containsHeader(String s) {
		if (this.header == null) {
			return false;
		} //
		else {
			return this.header.contains(s);
		}
	}

	@Override
	public String toString() {

		int textLength = 0;
		if (this.text != null) {
			textLength = this.text.length();
		}
		int childSize = 0;
		if (children != null) {
			childSize = children.size();
		}

		return "WikiPageNode [" //
				+ "level=" + level + ", "//
				+ "header=" + header + ", "//
				+ "text=" + textLength + ", "//
				+ "parent=" + (parent != null) + ", "//
				+ "children=" + childSize //
				+ "]";
	}

}
