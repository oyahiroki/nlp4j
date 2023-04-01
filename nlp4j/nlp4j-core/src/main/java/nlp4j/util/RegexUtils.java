package nlp4j.util;

/**
 * created on 2021-08-06
 * 
 * @author Hiroki Oya
 */
public class RegexUtils {

	/**
	 * Regex for URL
	 */
	static public String REGEX_URL = "(https?|ftp)(:\\/\\/[-_.!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\$,%#]+)";

	/**
	 * Regext for Hash tag
	 */
	static public String REGEX_HASHTAG = "#[^#\\s]*";

	/**
	 * Regext for attention
	 */
	static public String REGEX_ATTENTION = "@[^#\\s]*";

	static public String REGEX_NOT_ALPHABET_SYMBOLS = "^[^a-zA-Z0-9!-/:-@\\[-`{-~]*$";

	/**
	 * <pre>
	 * 日本語の文字列
	 * TODO 検証
	 * </pre>
	 * 
	 * ^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠]*$
	 */
	static public String REGEX_JA_CHARS = "^[。、．，・\u0020\u3000ぁ-んァ-ヶｱ-ﾝﾞﾟー一-龠)]*$";
}
