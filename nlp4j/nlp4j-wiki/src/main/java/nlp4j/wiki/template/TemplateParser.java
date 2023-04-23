package nlp4j.wiki.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemplateParser {

	/**
	 * @param wikiTextLine Example: {{Redirect|&}}
	 * @param templateName
	 * @return
	 */
	static public List<String> parseTemplateLine(String wikiTextLine, String templateName) {
		List<String> params = null;

		if (wikiTextLine.startsWith("{{" + templateName) && wikiTextLine.endsWith("}}")) {
			// {{wikipedia}} 引数なし: 同名のWikipedia記事
			// {{wikipedia|記事名}}
			// {{wikipedia|記事名|表示名}}
			// TODO Wikipedia リンクの処理
//			System.err.println("wikipedia: " + line);

			String param = wikiTextLine.substring(templateName.length() + 2, wikiTextLine.length() - 2);

			// NO Params
			if (param.isEmpty()) {
				return new ArrayList<>();
			} else {
				if (param.startsWith("|")) {
					param = param.substring(1); // remove first |
					String[] pp = param.split("\\|");
					params = new ArrayList<>();
					params.addAll(Arrays.asList(pp));
					return params;
				}
			}

			System.err.println(param);

		} else {
			return null;
		}

		return params;
	}

}
