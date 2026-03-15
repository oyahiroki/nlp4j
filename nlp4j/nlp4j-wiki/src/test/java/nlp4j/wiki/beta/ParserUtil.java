package nlp4j.wiki.beta;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public  class ParserUtil {

	static boolean startsWith(String s, int pos, String prefix) {
		return s.regionMatches(pos, prefix, 0, prefix.length());
	}

	static boolean startsWithIgnoreCase(String s, int pos, String prefix) {
		return s.regionMatches(true, pos, prefix, 0, prefix.length());
	}

	static int indexOfIgnoreCase(String s, String target, int fromIndex) {
		int max = s.length() - target.length();
		for (int i = Math.max(0, fromIndex); i <= max; i++) {
			if (s.regionMatches(true, i, target, 0, target.length())) {
				return i;
			}
		}
		return -1;
	}

	static int findMatchingDoubleBrace(String s, int start) {
		if (!startsWith(s, start, "{{")) {
			return -1;
		}

		int depth = 0;
		int i = start;
		while (i < s.length() - 1) {
			if (startsWith(s, i, "{{")) {
				depth++;
				i += 2;
				continue;
			}
			if (startsWith(s, i, "}}")) {
				depth--;
				if (depth == 0) {
					return i;
				}
				i += 2;
				continue;
			}
			i++;
		}
		return -1;
	}

	static int findMatchingDoubleBracket(String s, int start) {
		if (!startsWith(s, start, "[[")) {
			return -1;
		}

		int depth = 0;
		int i = start;
		while (i < s.length() - 1) {
			if (startsWith(s, i, "[[")) {
				depth++;
				i += 2;
				continue;
			}
			if (startsWith(s, i, "]]")) {
				depth--;
				if (depth == 0) {
					return i;
				}
				i += 2;
				continue;
			}
			i++;
		}
		return -1;
	}

	static int findMatchingTableEnd(String s, int from) {
		return s.indexOf("|}", from);
	}

	static List<String> splitTopLevel(String s, char delimiter) {
		List<String> parts = new ArrayList<>();
		StringBuilder current = new StringBuilder();

		int braceDepth = 0; // {{ }}
		int bracketDepth = 0; // [[ ]]
		int parenDepth = 0; // ( )
		int tagDepth = 0; // < >

		int i = 0;
		while (i < s.length()) {
			if (i + 1 < s.length() && s.charAt(i) == '{' && s.charAt(i + 1) == '{') {
				braceDepth++;
				current.append("{{");
				i += 2;
				continue;
			}
			if (i + 1 < s.length() && s.charAt(i) == '}' && s.charAt(i + 1) == '}') {
				if (braceDepth > 0) {
					braceDepth--;
				}
				current.append("}}");
				i += 2;
				continue;
			}

			if (i + 1 < s.length() && s.charAt(i) == '[' && s.charAt(i + 1) == '[') {
				bracketDepth++;
				current.append("[[");
				i += 2;
				continue;
			}
			if (i + 1 < s.length() && s.charAt(i) == ']' && s.charAt(i + 1) == ']') {
				if (bracketDepth > 0) {
					bracketDepth--;
				}
				current.append("]]");
				i += 2;
				continue;
			}

			char c = s.charAt(i);
			if (c == '(') {
				parenDepth++;
			} else if (c == ')') {
				if (parenDepth > 0) {
					parenDepth--;
				}
			} else if (c == '<') {
				tagDepth++;
			} else if (c == '>') {
				if (tagDepth > 0) {
					tagDepth--;
				}
			}

			if (c == delimiter && braceDepth == 0 && bracketDepth == 0 && parenDepth == 0 && tagDepth == 0) {
				parts.add(current.toString());
				current.setLength(0);
			} else {
				current.append(c);
			}
			i++;
		}

		parts.add(current.toString());
		return parts;
	}

	static int indexOfTopLevel(String s, char target) {
		int braceDepth = 0;
		int bracketDepth = 0;
		int parenDepth = 0;
		int tagDepth = 0;

		int i = 0;
		while (i < s.length()) {
			if (i + 1 < s.length() && s.charAt(i) == '{' && s.charAt(i + 1) == '{') {
				braceDepth++;
				i += 2;
				continue;
			}
			if (i + 1 < s.length() && s.charAt(i) == '}' && s.charAt(i + 1) == '}') {
				if (braceDepth > 0) {
					braceDepth--;
				}
				i += 2;
				continue;
			}

			if (i + 1 < s.length() && s.charAt(i) == '[' && s.charAt(i + 1) == '[') {
				bracketDepth++;
				i += 2;
				continue;
			}
			if (i + 1 < s.length() && s.charAt(i) == ']' && s.charAt(i + 1) == ']') {
				if (bracketDepth > 0) {
					bracketDepth--;
				}
				i += 2;
				continue;
			}

			char c = s.charAt(i);
			if (c == '(') {
				parenDepth++;
			} else if (c == ')') {
				if (parenDepth > 0) {
					parenDepth--;
				}
			} else if (c == '<') {
				tagDepth++;
			} else if (c == '>') {
				if (tagDepth > 0) {
					tagDepth--;
				}
			}

			if (c == target && braceDepth == 0 && bracketDepth == 0 && parenDepth == 0 && tagDepth == 0) {
				return i;
			}

			i++;
		}

		return -1;
	}
}