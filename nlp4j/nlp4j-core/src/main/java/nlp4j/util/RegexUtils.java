package nlp4j.util;

/**
 * @author Hiroki Oya
 * @created_at 2021-08-06
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
	 * ^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠]*$
	 */
	static public String REGEX_JA_CHARS = "^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠]*$";
}
