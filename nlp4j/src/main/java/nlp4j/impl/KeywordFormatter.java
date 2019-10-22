package nlp4j.impl;

import nlp4j.KeywordWithDependency;
import nlp4j.util.XmlUtils;

/**
 * キーワードのクラスを文字列としてフォーマットするクラスです。<br/>
 * Keyword formatter for printing as string data.
 * 
 * @author Hiroki Oya
 *
 */
public class KeywordFormatter {

	static public String toString1(KeywordWithDependency kwd) {

		StringBuffer sb = new StringBuffer();

		if (kwd.getParent() != null) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(kwd.getStr() + "..." + kwd.getParent().getStr());

		} else {
//		do nothing
		}

		for (KeywordWithDependency c : kwd.getChildren()) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(c.toStringAsDependencyList());
		}
		return sb.toString();

	}

	static public String toString2(KeywordWithDependency kwd) {
		String indent = "\t";
		String bar = "-";
		StringBuffer sb = new StringBuffer();
		for (int n = 0; n < kwd.getDepth(); n++) {
			sb.append(indent);
		}
		sb.append(bar);
		sb.append("sequence=" + kwd.getSequence() + ",lex=" + kwd.getLex() + ",str=" + kwd.getStr());
		for (KeywordWithDependency c : kwd.getChildren()) {
			sb.append("\n");
			sb.append(c.toStringAsDependencyTree());
		}
		return sb.toString();
	}

	static public String toStringAsXml(KeywordWithDependency kwd) {
		return XmlUtils.prettyFormatXml(toStringAsXml(kwd, 0));
	}

	static private String toStringAsXml(KeywordWithDependency kwd, int depth) {
		StringBuffer sb = new StringBuffer();

		if (kwd.getChildren() == null || kwd.getChildren().size() == 0) {
			sb.append("<w " //
					+ "str=\"" + kwd.getStr() + "\" " //
					+ "lex=\"" + kwd.getLex() + "\" " //
					+ "depth=\"" + depth + "\" " //
					+ "facet=\"" + kwd.getFacet() + "\" " //
					+ "/>");
		} else {
			sb.append("<w " //
					+ "str=\"" + kwd.getStr() + "\" " //
					+ "lex=\"" + kwd.getStr() + "\" " //
					+ "depth=\"" + depth + "\" " //
					+ "facet=\"" + kwd.getFacet() + "\" " //
					+ ">");
			for (KeywordWithDependency c : kwd.getChildren()) {
				sb.append(c.toStringAsXml(depth + 1));
			}
			sb.append("</w>");
		}

		return sb.toString();
	}

}
