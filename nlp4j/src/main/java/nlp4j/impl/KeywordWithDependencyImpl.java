package nlp4j.impl;

import java.util.ArrayList;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.util.XmlUtils;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class KeywordWithDependencyImpl extends DefaultKeyword implements KeywordWithDependency {

	private static final long serialVersionUID = 1L;

	String dependencyKey;

	ArrayList<KeywordWithDependency> children = new ArrayList<KeywordWithDependency>();
	KeywordWithDependency parent;

	int sequence = -1;

	@Override
	public void addChild(KeywordWithDependency keyword) {
		children.add(keyword);
	}

	@Override
	public ArrayList<KeywordWithDependency> getChildren() {
		return this.children;
	}

	public String getDependencyKey() {
		return dependencyKey;
	}

	@Override
	public int getDepth() {
		if (this.parent == null) {
			return 0;
		} else {
			return parent.getDepth() + 1;
		}
	}

	@Override
	public KeywordWithDependency getParent() {
		return this.parent;
	}

	@Override
	public KeywordWithDependency getRoot() {
		if (this.parent == null) {
			return this;
		} else {
			return this.parent.getRoot();
		}
	}

	public int getSequence() {
		return sequence;
	}

	@Override
	public boolean hasChild() {
		return (children != null && children.size() > 0);
	}

	@Override
	public boolean hasParent() {
		return (parent != null);
	}

	@Override
	public boolean isRoot() {
		return hasParent();
	}

	public void setDependencyKey(String dependencyKey) {
		this.dependencyKey = dependencyKey;
	}

	@Override
	public void setParent(KeywordWithDependency parent) {
		this.parent = parent;
		parent.addChild(this);
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toStringAsDependencyTree() {
		String indent = "\t";
		String bar = "-";
		StringBuffer sb = new StringBuffer();
		for (int n = 0; n < this.getDepth(); n++) {
			sb.append(indent);
		}
		sb.append(bar);
		sb.append(this.str);
		for (KeywordWithDependency c : children) {
			sb.append("\n");
			sb.append(c.toStringAsDependencyTree());
		}
		return sb.toString();
	}

	@Override
	public String toStringAsDependencyList() {
		StringBuffer sb = new StringBuffer();

		if (parent != null) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(this.getStr() + "..." + parent.getStr());

		} else {
//			do nothing
		}

		for (KeywordWithDependency c : children) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(c.toStringAsDependencyList());
		}
		return sb.toString();
	}

	@Override
	public String toStringAsXml() {
		return XmlUtils.prettyFormatXml(toStringAsXml(0));
	}

	public String toStringAsXml(int depth) {
		StringBuffer sb = new StringBuffer();
		sb.append("<w " //
				+ "str=\"" + this.str + "\" " //
				+ "lex=\"" + this.lex + "\" " //
				+ ">");
		for (KeywordWithDependency c : children) {
			sb.append(c.toStringAsXml(depth + 1));
		}
		sb.append("</w>");

		return sb.toString();
	}

	@Override
	public String toString() {
		return "KeywordWithDependencyImpl [" //
				+ "dependencyKey=" + dependencyKey + ", " //
				+ "children=" + (children != null && children.size() > 0) + ", " //
				+ "parent=" + (parent != null) + ", " //
				+ "sequence=" + sequence + ", " //
				+ "facet=" + facet + ", " //
				+ "lex=" + lex + ", " //
				+ "str=" + str + ", " //
				+ "reading=" + reading + ", " //
				+ "begin=" + begin + ", " //
				+ "end=" + end + "" + "]";
	}

}
