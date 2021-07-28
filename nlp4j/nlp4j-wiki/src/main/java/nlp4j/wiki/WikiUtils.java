package nlp4j.wiki;

import java.util.HashMap;

public class WikiUtils {

	static private HashMap<String, String> map = new HashMap<String, String>();

	static {
		map.put("=={{jpn}}==", "=={{ja}}==");
		map.put("== {{jpn}} ==", "=={{ja}}==");
		map.put("== {{ja}} ==", "=={{ja}}==");
		map.put("==日本語==", "=={{ja}}==");
		map.put("== 日本語 ==", "=={{ja}}==");
	}

	static public String normailzeHeader(String header) {

		if (map.containsKey(header)) {
			return map.get(header);
		}

		return header;
	}

}
