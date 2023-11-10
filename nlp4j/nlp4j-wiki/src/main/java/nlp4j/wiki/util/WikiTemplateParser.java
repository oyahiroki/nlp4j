package nlp4j.wiki.util;

import java.util.ArrayList;
import java.util.List;

public class WikiTemplateParser {

	static public String removeInlineTemplate(String wikiText) {

		List<String> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();

		char target1 = '{';
		char target2 = '}';

		int level = 0;

		for (int n = 0; n < wikiText.length(); n++) {
			char c = wikiText.charAt(n);
			int level_a = level;
//			System.err.println("level_1: " + level);
			if (c == target1) {
				level++;
			} //
			else if (c == target2) {
				level--;
			}
			int level_b = level;
//			System.err.println("level_2: " + level);

			if (level_a == 0 && level_b == 1) {
				if (sb.length() > 0) {
					list.add(sb.toString());
				}
				sb = new StringBuilder();
				sb.append(c);
			} //
			else if (level_a == 1 && level_b == 0) {
				sb.append(c);
				if (sb.length() > 0) {
					// 次の文字が制御文字以外=>本文中のテンプレートが用いられていた
					if ((n + 1) < wikiText.length() && Character.isISOControl(wikiText.charAt((n + 1))) == false) {
						String[] ss = sb.toString().split("\\|");
						if (ss.length > 1) {
							list.add(ss[1].replace("{", ""));
						} else {
							list.add(ss[0]);
						}
					} //
					else {
						list.add(sb.toString());
					}

				}
				sb = new StringBuilder();
			} else {
				sb.append(c);
			}
		}
		if (sb.length() > 0) {
			list.add(sb.toString());
			sb = new StringBuilder();
		}

//		for (String s : list) {
//			System.err.println("### " + s);
//		}

		String s = String.join("", list);

//		System.err.println(s);
//		System.err.println("---");

		return s;

	}

}
