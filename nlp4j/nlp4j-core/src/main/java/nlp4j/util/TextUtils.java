package nlp4j.util;

import java.text.Normalizer;

/**
 * <pre>
 * String converter Utils
 * </pre>
 * 
 * created on 2022-09-09
 * 
 * @author Hiroki Oya
 */
public class TextUtils {

	/**
	 * Normalize a sequence of char values.
	 * 
	 * @param s
	 * @return
	 */
	static public String nfkc(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFKC);
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
