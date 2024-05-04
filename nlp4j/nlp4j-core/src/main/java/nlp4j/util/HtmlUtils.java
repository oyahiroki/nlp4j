package nlp4j.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

/**
 * Created on: 2024-05-02
 * 
 * @since 1.3.7.13
 * 
 */
public class HtmlUtils {

	/**
	 * Remove HTML Comment with JSoup Engine
	 * 
	 * @param html
	 * @return HTML without comments
	 * @since 1.3.7.13
	 */
	public static String removeHtmlComments(String html) {
		Document document = Jsoup.parse(html);
		removeComments(document);
		return document.getElementById("nlp4j_removecomments").html();
	}

	private static void removeComments(Node node) {
		for (int i = 0; i < node.childNodes().size();) {
			Node child = node.childNode(i);
			if (child.nodeName().equals("#comment")) {
				child.remove();
			} else {
				removeComments(child);
				i++;
			}
		}
	}

}
