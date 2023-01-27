package nlp4j.wiki.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nlp4j.wiki.entity.WikiCategory;
import nlp4j.wiki.entity.WikiDefault;
import nlp4j.wiki.entity.WikiEntity;
import nlp4j.wiki.entity.WikiTemplate;

public class WikiTextParser {

	public List<WikiEntity> parse(String text) {

		Objects.nonNull(text);

		List<WikiEntity> entities = new ArrayList<>();
		int templateLevel = 0;

		StringBuilder sb = new StringBuilder();

		// FOR EACH CARACTER
		for (int n = 0; n < text.length(); n++) {
			char c = text.charAt(n);

			if (c == '{') {
				templateLevel++;
				if (templateLevel == 1) {
					if (sb.length() > 0) {
						WikiEntity e;
						if (sb.toString().startsWith("{{")) {
							e = new WikiTemplate(sb.toString());
						} else {
							e = new WikiDefault(sb.toString());
						}
						sb = new StringBuilder();
						entities.add(e);
					}
				}
				// always
				sb.append(c);
			} //
			else if (c == '}') {
				templateLevel--;

				if (templateLevel == 0) {
					if (sb.length() > 0) {
						// always
						sb.append(c);
						String s = sb.toString();
						extractCategories(entities, s);
					}
				} else {
					sb.append(c);
				}
			} //
			else {
				sb.append(c);
			}
		} // END OF (FOR EACH CARACTER)

		if (sb.length() > 0) {
			String s = sb.toString();
			extractCategories(entities, s);
		}

//		System.err.println(sb.toString());
//
//		for (WikiEntity e : entities) {
//			System.err.println("---");
//			System.err.println(e);
//		}

		return entities;

	}

	private Pattern pattern_category = Pattern.compile("\\[\\[Category.*?\\]\\]");

	private void extractCategories(List<WikiEntity> entities, String s) {

		if (s.contains("[[Category") == false) {
			return;
		}

		Matcher m = pattern_category.matcher(s);

		int idx = 0;

		int lastEnd = 0;

		while (m.find()) {

			int start = m.start();
			int end = m.end();
			lastEnd = end;

			if (start > idx) {
				String s2 = s.substring(idx, start);
				if (s2 != null && s2.trim().isEmpty() == false) {
					WikiEntity e = new WikiDefault(s2);
					entities.add(e);
				}
			}
			{
				String s1 = s.substring(start, end);
				WikiEntity e = new WikiCategory(s1);
				entities.add(e);
			}
//			System.err.println(m.group());
//			System.err.println(m.start());

			idx = end;
		}

		if (lastEnd < s.length()) {
			String s2 = s.substring(lastEnd);
			WikiEntity e = new WikiDefault(s2);
			entities.add(e);
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
