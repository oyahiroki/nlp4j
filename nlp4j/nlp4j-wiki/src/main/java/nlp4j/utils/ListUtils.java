package nlp4j.utils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 2026-09-06
 */
public class ListUtils {
	public static boolean matchesAny(String regex, List<String> list) {
		if (regex == null || list == null) {
			return false;
		}

		Pattern pattern = Pattern.compile(regex);

		return list.stream().filter(Objects::nonNull).anyMatch(s -> pattern.matcher(s).matches());
	}
}
