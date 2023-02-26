package nlp4j.wiki.util;

public class StringUtils {

	// Thank you
	// https://stackoverflow.com/questions/6804951/regex-to-remove-comments-in-xml-file-in-eclipse-java
	private static final String REGEX_XML_COMMENT = "(?s)" + "<!--" + ".*?" + "-->";

	/**
	 * @param s target string
	 * @param b pair of begin char and end char like "()"
	 * @return
	 */
	static public String removeBracketted(String s, String b) {
		int level = 0;
		StringBuilder sb = new StringBuilder();
		char c1 = b.charAt(0);
		char c2 = b.charAt(1);
		for (int n = 0; n < s.length(); n++) {
			char c = s.charAt(n);
			if (c == c1) {
				level++;
			} //
			if (level == 0) {
				sb.append(c);
			} //
			if (c == c2) {
				level--;
			} //
		}
		return sb.toString();

	}

	static public String remove(String s, String from, String to) {
		if (s.indexOf(from) == -1) {
			return s;
		}
		String regex = "(?s)" + from + ".*?" + to;
		return s.replaceAll(regex, "");
	}

	/**
	 * <pre>
	 * Remove XML Comment
	 * XMLコメントを削除する
	 * </pre>
	 * 
	 * created on: 2023-02-22
	 */
	static public String removeXmlComment(String xml) {
		return xml.replaceAll(REGEX_XML_COMMENT, "");
	}

}
