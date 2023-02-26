package nlp4j.wiki.template;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiTemplateNormalizer {

	// ===和語の漢字表記===
	// {{ja-wagokanji|めだまやき}}

	/**
	 * テンプレート:ja-wagokanji
	 * https://ja.wiktionary.org/wiki/%E3%83%86%E3%83%B3%E3%83%97%E3%83%AC%E3%83%BC%E3%83%88:ja-wagokanji
	 */
	static private String REGEX_JA_WAGOKANJI = "\\{\\{ja-wagokanji\\|(.*?)\\}\\}";
	static private Pattern p_wagokanji = Pattern.compile(REGEX_JA_WAGOKANJI);

	/**
	 * テンプレート:ふりがな
	 * https://ja.wiktionary.org/wiki/%E3%83%86%E3%83%B3%E3%83%97%E3%83%AC%E3%83%BC%E3%83%88:%E3%81%B5%E3%82%8A%E3%81%8C%E3%81%AA
	 */
	static private String REGEX_FURIGANA = "\\{\\{ふりがな\\|(.*?)\\|.*?\\}\\}";
	static private Pattern p_furigana = Pattern.compile(REGEX_FURIGANA);

	/**
	 * テンプレート:おくりがな2
	 * https://ja.wiktionary.org/wiki/%E3%83%86%E3%83%B3%E3%83%97%E3%83%AC%E3%83%BC%E3%83%88:%E3%81%8A%E3%81%8F%E3%82%8A%E3%81%8C%E3%81%AA2
	 */
	static private String REGEX_OKURIGANA2 = "\\{\\{おくりがな2\\|(.*?)\\|(.*?)\\|(.*?)\\|.*?\\}\\}";
	static private Pattern p_okurigana2 = Pattern.compile(REGEX_OKURIGANA2);

	/**
	 * Template:読み仮名
	 * https://ja.wikipedia.org/wiki/Template:%E8%AA%AD%E3%81%BF%E4%BB%AE%E5%90%8D
	 */
	static private String REGEX_YOMIGANA = "\\{\\{読み仮名\\|(.*?)\\|.*?\\}\\}";
	static private Pattern p_yomigana = Pattern.compile(REGEX_YOMIGANA);

	/**
	 * Template:読み仮名 ruby不使用
	 * https://ja.wikipedia.org/wiki/Template:%E8%AA%AD%E3%81%BF%E4%BB%AE%E5%90%8D_ruby%E4%B8%8D%E4%BD%BF%E7%94%A8
	 */
	static private String REGEX_YOMIGANA_RUBY_FUSHIYOU = "\\{\\{読み仮名_ruby不使用\\|(.*?)\\|.*?\\}\\}";
	static private Pattern p_ruby_fushiyou = Pattern.compile(REGEX_YOMIGANA_RUBY_FUSHIYOU);

	public static String normalize(String text) {
		if (text == null) {
		} else {
			{ // 「ふりがな」テンプレート
				Matcher m = p_furigana.matcher(text);
				if (m.find()) {
					text = text.replaceAll(REGEX_FURIGANA, m.group(1));
				}

			}
			{ // 「おくりがな2」テンプレート
				Matcher m = p_okurigana2.matcher(text);
				if (m.find()) {
					text = text.replaceAll(REGEX_OKURIGANA2, m.group(1) + m.group(3));
				}

			}
			{ // 「ja-wagokanji」テンプレート
				Matcher m = p_wagokanji.matcher(text);
				if (m.find()) {
					text = text.replaceAll(REGEX_JA_WAGOKANJI, m.group(1));
				}
			}
			{ // Template:読み仮名
				Matcher m = p_yomigana.matcher(text);
				if (m.find()) {
					text = text.replaceAll(REGEX_YOMIGANA, m.group(1));
				}
			}
			{ // Template:読み仮名
				Matcher m = p_ruby_fushiyou.matcher(text);
				if (m.find()) {
					text = text.replaceAll(REGEX_YOMIGANA_RUBY_FUSHIYOU, m.group(1));
				}
			}
		}
		return text;
	}

}
