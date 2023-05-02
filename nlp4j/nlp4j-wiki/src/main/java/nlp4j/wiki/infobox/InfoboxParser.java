package nlp4j.wiki.infobox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * created_on: 2023-05-02
 * 
 * @author Hiroki Oya
 *
 */
public class InfoboxParser {

	static public Infobox parseInfoboxWikiText(String infoboxWikiText) {

		if (infoboxWikiText == null || infoboxWikiText.isEmpty()) {
			return null;
		}

		String infoboxName = null;
		Map<String, String> infoboxKeyvalue = new LinkedHashMap<>();

		int lineNumber = 0;

		String multilineKey = null;
		boolean is_multilineValue = false;
		StringBuilder multilineValue = new StringBuilder();

		int checkClose = 0;

		// FOR_EACH(LINE)
		for (String lineOfWikiTextInfobox : infoboxWikiText.split("\n")) {

			lineNumber++;

			lineOfWikiTextInfobox = lineOfWikiTextInfobox.trim();

			for (int n = 0; n < lineOfWikiTextInfobox.length(); n++) {
				char c = lineOfWikiTextInfobox.charAt(n);
				if (c == '{') {
					checkClose++;
				} else if (c == '}') {
					checkClose--;
				}
			}

			// 先頭行
			if (lineNumber == 1) {
				if (lineOfWikiTextInfobox.startsWith("{{Infobox") == false) {
					return null;
				}
				infoboxName = lineOfWikiTextInfobox.substring("{{Infobox".length()).trim();
			}
			// 先頭行以外
			else {
				// 「|」から始まる
				if (lineOfWikiTextInfobox.startsWith("|")) {
					if (is_multilineValue) {
						infoboxKeyvalue.put(multilineKey, multilineValue.toString());
						multilineKey = null;
						multilineValue = new StringBuilder();
						is_multilineValue = false;
					}
					String[] ss = lineOfWikiTextInfobox.substring(1).split("=");
					if (ss.length == 2) {
						ss[0] = ss[0].trim();
						ss[1] = ss[1].trim();
						if (ss[1].isEmpty() == false) {
							String key = ss[0];
							String value = ss[1];
							infoboxKeyvalue.put(key, value);
						} else {
							is_multilineValue = true;
							multilineKey = ss[0];
						}
					} //
					else if (ss.length == 1) {
						ss[0] = ss[0].trim();
						is_multilineValue = true;
						multilineKey = ss[0];
					}
				} //
				else {
					if (is_multilineValue == true) {
						multilineValue.append(lineOfWikiTextInfobox + "\n");
					}
					// END OF INFOBOX
					if (lineOfWikiTextInfobox.equals("}}") && checkClose == 0) {
//						System.err.println("Finished");
					}
				}

			}

		} // END_OF(FOR_EACH(LINE))

//		System.err.println("---");

		Infobox ib = new Infobox(infoboxName, infoboxKeyvalue);
		return ib;
	}

	static public List<Infobox> parse(String wikiText) {

		List<Infobox> infoboxList = new ArrayList<>();

		boolean isInfobox = false;
		StringBuilder sbInfobox = new StringBuilder();

		int countbrackets = 0;

		for (String line : wikiText.split("\n")) {
			line = line.trim();

			if (isInfobox == false) {
				if (line.startsWith("{{Infobox ")) {
					isInfobox = true;
					sbInfobox.append(line + "\n");
					countbrackets = 2;
				}
			}
			// ELSE(isInfobox==TRUE)
			else {
				for (int n = 0; n < line.length(); n++) {
					char c = line.charAt(n);
					if (c == '{') {
						countbrackets++;
					} //
					else if (c == '}') {
						countbrackets--;
					}
				}

				if (countbrackets > 0) {
					sbInfobox.append(line + "\n");
				}
				// brackets closed
				else if (countbrackets == 0) {
					String infoboxWikiText = sbInfobox.toString();
					Infobox infobox = parseInfoboxWikiText(infoboxWikiText);
					infoboxList.add(infobox);

					sbInfobox = new StringBuilder(); // initialize
					isInfobox = false; // initialize
				}
			}

		}
		return infoboxList;
	}

}
