package nlp4j.wiki.util;

import java.util.Stack;

public class WikiTextParser {

	StringBuilder sbTemplate = new StringBuilder();

	int templateLevel = 0;

	public void parse(String text) {
		for (int n = 0; n < text.length(); n++) {
			char c = text.charAt(n);
			System.err.print(c);
			if (c == '{') {
				templateLevel++;
			} //
			else if (c == '}') {
				templateLevel--;

				if (templateLevel == 0) {
					System.err.println("テンプレート");
					System.err.println(sbTemplate.toString());
					sbTemplate = new StringBuilder();
				}
			} //

			if (templateLevel >= 2) {
				sbTemplate.append(c);
			}

		}
	}

	private boolean startsWith(String text, int n, String s) {
		if ((n + s.length()) > text.length()) {
			return false;
		} //
		else {
			return text.substring(n).startsWith(s);
		}
	}

}
