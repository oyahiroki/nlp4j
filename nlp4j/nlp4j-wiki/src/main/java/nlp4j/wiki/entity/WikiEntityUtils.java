package nlp4j.wiki.entity;

public class WikiEntityUtils {

	static public WikiEntity get(String t) {
		if (t == null) {
			return null;
		} //
		t = t.trim();
		if (t.startsWith("{{")) {
			return (new WikiTemplate(t));
		} //
		else if (t.startsWith("[[")) {
			return (new WikiCategory(t));
		} //
		else {
			return (new WikiDefault(t));
		}

	}

}
