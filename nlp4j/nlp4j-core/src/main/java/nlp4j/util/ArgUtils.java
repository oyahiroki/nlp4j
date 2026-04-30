package nlp4j.util;

import java.util.HashMap;
import java.util.Map;

public class ArgUtils {

	/**
	 * 引数を --key value 形式でパース
	 */
	static public Map<String, String> parseArgs(String[] args) {
		Map<String, String> map = new HashMap<>();

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (arg.startsWith("--")) {
				String key = arg.substring(2);

				if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
					map.put(key, args[i + 1]);
					i++;
				} else {
					// フラグ（値なし）
					map.put(key, "true");
				}
			} else if (arg.startsWith("-")) {
				// 短縮オプション対応（必要なら拡張）
				String key = arg.substring(1);
				if (i + 1 < args.length) {
					map.put(key, args[i + 1]);
					i++;
				}
			}
		}

		return map;
	}

}
