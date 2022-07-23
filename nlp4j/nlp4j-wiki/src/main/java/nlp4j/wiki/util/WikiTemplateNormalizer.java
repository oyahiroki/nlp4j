package nlp4j.wiki.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiTemplateNormalizer {

	// ===和語の漢字表記===
	// {{ja-wagokanji|めだまやき}}

	static private String REGEX_JA_WAGOKANJI = "\\{\\{ja-wagokanji\\|(.*?)\\}\\}";
	static private Pattern p_wagokanji = Pattern.compile(REGEX_JA_WAGOKANJI);

	static private String REGEX_FURIGANA = "\\{\\{ふりがな\\|(.*?)\\|.*?\\}\\}";
	static private Pattern p_furigana = Pattern.compile(REGEX_FURIGANA);

	static private String REGEX_OKURIGANA2 = "\\{\\{おくりがな2\\|(.*?)\\|(.*?)\\|(.*?)\\|.*?\\}\\}";
	static private Pattern p_okurigana2 = Pattern.compile(REGEX_OKURIGANA2);

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
		}
		return text;
	}

}
