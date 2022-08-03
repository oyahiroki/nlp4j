package nlp4j.wiki.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import nlp4j.counter.Counter;

public class WikipediaTextParser {

	enum Status {
		TEXT, TEMPLATE, LINK, SECTION
	}

	List<String> templates = new ArrayList<String>();
	List<String> links = new ArrayList<String>();

	Status status = Status.TEXT;

	public void parseTopParagraph(String pageText) {

		Counter<Character> counter = new Counter<>();

		// {{テンプレート}}
		// [[リンク]]

		StringBuilder sb = new StringBuilder();
		// 行データ
		StringBuilder sbLine = new StringBuilder();

		boolean beginOfLine = true;

		for (int n = 0; n < pageText.length(); n++) {

			char c = pageText.charAt(n);

			if (c == '{') {
				counter.increment('{');
			} //
			else if (c == '}') {
				// close {}
				if (counter.getCount('{') == 1) {
					System.err.println("<text1>");
					System.err.println(sb.toString() + c);
					System.err.println("</text1>");
					sb = new StringBuilder();
				}
				counter.decrement('{');
			} //
			else if (c == '[') {
				counter.increment('[');
			} //
			else if (c == ']') {
				if (counter.getCount('[') == 1) {
//					System.err.println("<text1>");
//					System.err.println(sb.toString());
//					links.add(sb.toString());
//					System.err.println("</text1>");
//					sb = new StringBuilder();
				}
				counter.decrement('[');
			} //
			else if (c == '=') {
				if (beginOfLine == true) {
					System.err.println(sb.toString());
					System.err.println("<new_section>");
				}
			}

			beginOfLine = (c == '\n');

			sb.append(c);

		}
	}

}
