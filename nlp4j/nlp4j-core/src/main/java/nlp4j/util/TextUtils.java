package nlp4j.util;

import java.text.Normalizer;
import java.util.Stack;

/**
 * <pre>
 * String converter Utils
 * </pre>
 * 
 * created on 2022-09-09
 * 
 * @since 1.3.7.3
 * @author Hiroki Oya
 */
public class TextUtils {

	/**
	 * Get new instance of {@link TextUtils}
	 * 
	 * @param s
	 * @return
	 */
	static public TextUtils n(String s) {
		return new TextUtils(s);
	}

	/**
	 * Normalize a sequence of char values.
	 * 
	 * @param s
	 * @return
	 */
	static public String nfkc(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFKC);
	}

	/**
	 * <pre>
	 * created on: 2023-08-19
	 * updated on: 2025-01-28 1.3.7.16
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	static public String removeBrackets(String text) {
//		return s.replaceAll("\\(.*?\\)", "");

		StringBuilder sb = new StringBuilder();
		Stack<Character> stk = new Stack<Character>();
		for (int n = 0; n < text.length(); n++) {
			char c = text.charAt(n);
			if (c == '(') {
				stk.push(')');
				continue;
			} //
			else if (c == '{') {
				stk.push('}');
				continue;
			} //
//			else if (c == '[') {
//				stk.push(']');
//				continue;
//			} //
			else if (stk.empty() == false && c == stk.peek()) {
				stk.pop();
			} //
			else {
				if (stk.empty()) {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	private String s;

	/**
	 * @param s: string to be converted
	 */
	public TextUtils(String s) {
		this.s = s;
	}

	/**
	 * @return converted string
	 */
	public String get() {
		return s;
	}

	/**
	 * Normalize by NFKC
	 * 
	 * @return this
	 */
	public TextUtils nfkc() {
		this.s = nfkc(s);
		return this;
	}

	/**
	 * <pre>
	 * Input Example: "aaa(bbb)ccc" 
	 * Output Example: "aaaccc"
	 * </pre>
	 * 
	 * @return
	 */
	public TextUtils removeBrackets() {
//		this.s = this.s.replaceAll("\\(.*?\\)", "");

		this.s = removeBrackets(this.s);

		return this;
	}

	/**
	 * Remove '\r' and '\n'
	 * 
	 * @return this
	 */
	public TextUtils removeNewline() {
		this.s = s.replace("\r", "");
		this.s = s.replace("\n", "");
		return this;
	}

	/**
	 * Remove '\t'
	 * 
	 * @return this
	 */
	public TextUtils removeTab() {
		this.s = s.replace("\t", "");
		return this;
	}

	/**
	 * Trim
	 * 
	 * @return this
	 */
	public TextUtils trim() {
		this.s = s.trim();
		return this;
	}
}
