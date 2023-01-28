package nlp4j.wiki.util;

public class StringUtils {

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

}
