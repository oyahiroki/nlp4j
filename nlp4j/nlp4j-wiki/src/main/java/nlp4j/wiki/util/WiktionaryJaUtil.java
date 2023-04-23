package nlp4j.wiki.util;

import nlp4j.util.StringUtils;
import nlp4j.wiki.WikiPage;

public class WiktionaryJaUtil {

	static public String getRedirectPageTitle(String wikiText) {
		String t = wikiText.split("\n")[0];
		int idx1 = t.lastIndexOf("[[");
		int idx2 = t.lastIndexOf("]]");
		if (idx1 != -1 && (idx2 > idx1)) {
			String link = t.substring(idx1, idx2);
			return link;
		} else {
			return null;
		}
	}

	static public boolean isRedirectPage(String wikiText) {
		return wikiText != null && wikiText.startsWith("#転送");
	}

	static public String extractDefaultSort(WikiPage page) {

		String reading = null;

		if (page == null) {
			return null;
		}

		if (page.getTitle() == null) {
			return null;
		}

		String text = page.getText();
		String title = page.getTitle();

		if (text == null) {
			return null;
		}

		if (StringUtils.isJaKana(title)) {
			reading = page.getTitle();
			return reading;
		}

		if (text.contains("{{ja-DEFAULTSORT}}")) {
			reading = page.getTitle();
			return reading;
		}

		{ // ja-DEFAULTSORT
			// https://ja.wiktionary.org/wiki/%E3%83%86%E3%83%B3%E3%83%97%E3%83%AC%E3%83%BC%E3%83%88:ja-DEFAULTSORT
			String begin = "{{ja-DEFAULTSORT|";
			String end = "}}";
			reading = extract(text, begin, end);
		}
		if (reading == null) {
			String begin = "{{DEFAULTSORT|";
			String end = "}}";
			reading = extract(text, begin, end);
		}
		if (reading == null) {
			String begin = "{{DEFAULTSORT:";
			String end = "}}";
			reading = extract(text, begin, end);
		}
		if (reading == null) {
			String begin = "{{ja-noun|[[";
			String end = "]]}}";
			reading = extract(text, begin, end);
		}
		// [[category:{{ja}}_{{noun}}|て 手]]
		if (reading == null) {
			String begin = "[[category:{{ja}}_{{noun}}|";
			String end = "]]";
			reading = extract(text, begin, end);
		}
		if (reading == null) {
			String begin = "[[category:日本語_名詞|";
			String end = "]]";
			reading = extract(text, begin, end);
		}
		if (reading == null) {
			String begin = "[[category:{{ja}}|";
			String end = "]]";
			reading = extract(text, begin, end);
		}
		if (reading == null) {
			int idx = text.indexOf("[[Category:{{ja}}");
			if (idx != -1) {
				int idx2 = idx + "[[Category:{{ja}}".length();
				int idx3 = text.indexOf("]]", idx2);
				if (idx2 != -1 && idx3 != -1 && idx2 < idx3) {
					String yomi = text.substring(idx2 + 1, idx3).trim();
					if (yomi.length() > 0 && StringUtils.isJaKana(yomi)) {
						reading = yomi;
					}
					int len = yomi.split(" ").length;
					if (len > 0) {
						String y = yomi.split(" ")[len - 1];
						if (StringUtils.isJaKana(y)) {
							reading = y;
						}
					}
				}

			}
		}
		if (reading == null) {
			int idx = text.indexOf("[[category:{{ja}}");
			if (idx != -1) {
				int idx2 = idx + "[[category:{{ja}}".length();
				int idx3 = text.indexOf("]]", idx2);
				if (idx2 != -1 && idx3 != -1 && idx2 < idx3) {
					String yomi = text.substring(idx2 + 1, idx3).trim();
					if (yomi.length() > 0 && StringUtils.isJaKana(yomi)) {
						reading = yomi;
					}
					int len = yomi.split(" ").length;
					if (len > 0) {
						String y = yomi.split(" ")[len - 1];
						if (StringUtils.isJaKana(y)) {
							reading = y;
						}
					}
				}

			}
		}
		if (reading == null) {
			int idx = text.indexOf("[[Category:日本語");
			if (idx != -1) {
				int idx2 = idx + "[[Category:日本語".length();
				int idx3 = text.indexOf("]]", idx2);
				if (idx2 != -1 && idx3 != -1 && idx2 < idx3) {
					String yomi = text.substring(idx2 + 1, idx3).trim();
					if (yomi.length() > 0 && StringUtils.isJaKana(yomi)) {
						reading = yomi;
					}
					int len = yomi.split(" ").length;
					if (len > 0) {
						String y = yomi.split(" ")[len - 1];
						if (StringUtils.isJaKana(y)) {
							reading = y;
						}
					}
				}

			}
		}

		return reading;
	}

	private static String extract(String text, String begin, String end) {
		String reading = null;
		int idx0 = text.indexOf(begin);
		if (idx0 != -1) {
			int idx2 = idx0 + begin.length();
			int idx3 = text.indexOf(end, idx2);
			if (idx2 != -1 && idx3 != -1 && (idx2 < idx3)) {
				String yomi = text.substring(idx2, idx3).trim();
				String[] yy = yomi.split(" ");
				String y = null;
				for (int n = 0; n < yy.length; n++) {
					if (StringUtils.isJaKana(yy[n])) {
						y = yy[n];
					}
				}
				if (y != null) {
					reading = y;
				}
			}
		} //
		return reading;
	}

}
