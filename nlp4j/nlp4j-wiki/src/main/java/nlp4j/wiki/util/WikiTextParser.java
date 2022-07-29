package nlp4j.wiki.util;

import java.util.Stack;

public class WikiTextParser {

	static public void parse(String text) {
		// {{テンプレート}}
		Stack<Character> stk1Template = new Stack<Character>();
		// [[リンク]]
		Stack<Character> stk2Link = new Stack<Character>();

		StringBuilder sb = new StringBuilder();

		int newLineCount = 0;

		for (int n = 0; n < text.length(); n++) {
			char c = text.charAt(n);
//			System.err.println(c);

			// (改行のチェック)
			if (c == '\r') {
				continue;
			} //
			else if (c == '\n') {
				newLineCount++;
				// ２行連続の改行
				if (newLineCount == 2) {
					System.err.println("<<<");
				}
				continue;
			} //
			else {
				newLineCount = 0;
			} // END OF (改行のチェック)

			if (c == '{') {
				stk1Template.push(c);
			} //
			else if (c == '}') {
				if (stk1Template.size() == 2 && sb.length() > 0) {
					System.err.println(sb.toString());
					sb = new StringBuilder();
				}
				stk1Template.pop();
			} //
			if (c == '[') {
				stk2Link.push(c);
			} //
			else if (c == ']') {
				if (stk2Link.size() == 2 && sb.length() > 0) {
					System.err.println(sb.toString());
					sb = new StringBuilder();
				}
				stk2Link.pop();
			} //

			else if (stk1Template.size() == 2) {
				sb.append(c);
			} //
			else if (stk2Link.size() == 2) {
				sb.append(c);
			} //
			else if (stk1Template.size() == 0 && stk2Link.size() == 0) {
//				System.err.println("OK " + c);
			}
		}
	}

}
