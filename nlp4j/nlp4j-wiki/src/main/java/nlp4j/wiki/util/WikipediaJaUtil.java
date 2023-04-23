package nlp4j.wiki.util;

public class WikipediaJaUtil {

	static public String getUrlByPageId(String id) {
		return "https://ja.wikipedia.org/wiki?curid=" + id;
	}

	static public String extractDefaultSort(String wikiText) {
		String defaultSort = null;

		int idx = wikiText.lastIndexOf("{{DEFAULTSORT:");

		if (idx != -1) {

			int idx2 = wikiText.indexOf("}}", idx);

			if (idx2 != -1) {
				defaultSort = wikiText.substring(idx + 14, idx2);
				return defaultSort;
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

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

	public static boolean isRedirectPage(String wikiText) {
		return wikiText != null && wikiText.startsWith("#REDIRECT ");
	}
}
