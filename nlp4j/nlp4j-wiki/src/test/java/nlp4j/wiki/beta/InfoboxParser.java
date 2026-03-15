package nlp4j.wiki.beta;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InfoboxParser {

	public static Map<String, String> extractFirstInfoboxAsPlainTextMap(String wiki) {
		String raw = extractFirstInfoboxRaw(wiki);
		if (raw == null) {
			return new LinkedHashMap<>();
		}
		return parseInfoboxTemplateToPlainTextMap(raw);
	}

	public static String removeFirstInfobox(String wiki) {
		String raw = extractFirstInfoboxRaw(wiki);
		if (raw == null) {
			return wiki;
		}
		return wiki.replace(raw, "");
	}

	public static String extractFirstInfoboxRaw(String wiki) {
		if (wiki == null) {
			return null;
		}

		int i = 0;
		while (i < wiki.length()) {
			int start = wiki.indexOf("{{Infobox", i);
			if (start < 0) {
				start = wiki.indexOf("{{infobox", i);
			}
			if (start < 0) {
				return null;
			}

			int end = ParserUtil.findMatchingDoubleBrace(wiki, start);
			if (end < 0) {
				return null;
			}

			return wiki.substring(start, end + 2);
		}
		return null;
	}

	public static Map<String, String> parseInfoboxTemplateToPlainTextMap(String rawTemplate) {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		if (rawTemplate == null || !rawTemplate.startsWith("{{")) {
			return map;
		}

		String body = rawTemplate.substring(2, rawTemplate.length() - 2);
		List<String> parts = ParserUtil.splitTopLevel(body, '|');
		if (parts.isEmpty()) {
			return map;
		}

		// parts[0] = Infobox xxx
		for (int i = 1; i < parts.size(); i++) {
			String part = parts.get(i);
			int eq = ParserUtil.indexOfTopLevel(part, '=');
			if (eq < 0) {
				continue;
			}

			String key = part.substring(0, eq).trim();
			String value = part.substring(eq + 1).trim();

			if (key.isEmpty()) {
				continue;
			}

			String plain = WikiCleaner.toPlainTextForInfoboxValue(value);
			map.put(key, plain);
		}

		return map;
	}
}