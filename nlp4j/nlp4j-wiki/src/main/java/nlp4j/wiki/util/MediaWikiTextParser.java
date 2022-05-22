package nlp4j.wiki.util;

import java.util.ArrayList;
import java.util.List;

public class MediaWikiTextParser {

	static public List<String> parse(String text, String s1, String s2) {

		List<String> ss = new ArrayList<>();

		int c1 = 0;

		int idx1 = -1;
		int idx2 = -1;

		for (int idx = 0; idx < text.length(); idx++) {

			String sub = (text.length() > idx + s1.length()) //
					? text.substring(idx, idx + s1.length())
					: text.substring(idx);

			if (sub.startsWith(s1)) {
//				System.err.println("OK1");
				if (c1 == 0) {
					idx1 = idx;
				}
				c1++;
//				System.err.println("c1=" + c1);
				idx += (s1.length() - 1);
			} //
			else if (sub.startsWith(s2)) {
				c1--;
//				System.err.println("c1=" + c1);
//				System.err.println("OK2");
				if (c1 == 0) {
					idx2 = idx;
//					System.err.println("OK3");

//					System.err.println("---");
//					System.err.println(text.substring(idx1, idx2 + s2.length()));
//					System.err.println("---");

					ss.add(text.substring(idx1, idx2 + s2.length()));
				}
				idx += (s2.length() - 1);
			}
		}

		return ss;

	}

}
