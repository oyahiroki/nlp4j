package nlp4j.wiki;

import java.util.ArrayList;
import java.util.List;

public class WikiPageNode {

	String header;

	StringBuilder text = new StringBuilder();

	private int level = -1;

	private WikiPageNode parent;

	ArrayList<WikiPageNode> children = new ArrayList<WikiPageNode>();

	private String[] spells;

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

	public boolean containsHeader(String s) {
		if (this.header == null) {
			return false;
		} //
		else {
			return this.header.contains(s);
		}
	}

	public List<WikiPageNode> get(String path) {

//		System.err.println("path=" + path);

		List<WikiPageNode> list = new ArrayList<>();

		String[] paths = path.split("/");

		int idx = 0;

		if (paths.length > 0) {
			for (WikiPageNode c : this.children) {
				if (c.getHeader() != null & c.getHeader().equals(paths[idx])) {
//					System.err.println("OK " + c.toString());
					if (paths.length > 1) {
						String path2 = join(paths, 1);
//						return c.get(path2);

						List<WikiPageNode> list2 = c.get(path2);

						if (list2 != null && list2.size() > 0) {
							list.addAll(list2);
						}

					} //
					else {
//						System.err.println("HI " + c.toString());
//						return c;
						list.add(c);
					}
				}
			}
		}

		return list;

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

	public String[] getSpells() {
		return spells;
	}

	public String getText() {
		return text.toString();
	}

	private String join(String[] paths, int idx) {
		StringBuilder sb = new StringBuilder();
		for (int n = idx; n < paths.length; n++) {
			if (sb.length() > 0) {
				sb.append("/");
			}
			sb.append(paths[n]);
		}
//		System.err.println(sb);
		return sb.toString();
	}

	public boolean matchHeader(String pattern) {
		if (this.header == null) {
			return false;
		} //
		else {
			return this.header.matches(pattern);
		}
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

	public void setSpells(String[] spells) {
		this.spells = spells;
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

	public String getTextDescendant() {
		StringBuilder sb = new StringBuilder();

		if (this.header != null) {
			sb.append(this.header + "\n");
		}
		if (this.text != null) {
			sb.append(this.text + "\n");
		}

		if (children != null) {
			for (WikiPageNode p : children) {
				sb.append(p.getTextDescendant());
			}
		}

		return sb.toString();
	}

	/**
	 * Find Descendant by header
	 * 
	 * @param s header name
	 * @return
	 */
	public List<WikiPageNode> find(String s) {

		List<WikiPageNode> list = new ArrayList<>();

		find(s, list);

		return list;
	}

	private void find(String string, List<WikiPageNode> list) {

		if (this.header != null && this.header.contains(string)) {
			list.add(this);
		}

		if (children != null) {
			for (WikiPageNode p : children) {
				p.find(string, list);
			}
		}

		return;

	}

}
