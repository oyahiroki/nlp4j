package nlp4j.wiki.beta;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class JsonlWriter {
	public static void writePages(List<WikiPage> pages, Path outputPath) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
			for (WikiPage page : pages) {
				writer.write(toJsonLine(page));
				writer.newLine();
			}
		}
	}

	public static String toJsonLine(WikiPage page) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"title\":").append(toJsonString(page.title())).append(",");
		sb.append("\"plain_text\":").append(toJsonString(page.plainText())).append(",");
		sb.append("\"infobox\":").append(mapToJson(page.infobox()));
		sb.append("}");
		return sb.toString();
	}

	public static String mapToJson(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");

		boolean first = true;
		for (Map.Entry<String, String> e : map.entrySet()) {
			if (!first) {
				sb.append(",");
			}
			first = false;
			sb.append(toJsonString(e.getKey()));
			sb.append(":");
			sb.append(toJsonString(e.getValue()));
		}

		sb.append("}");
		return sb.toString();
	}

	public static String toJsonString(String s) {
		if (s == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();
		sb.append('"');
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\"' -> sb.append("\\\"");
			case '\\' -> sb.append("\\\\");
			case '\b' -> sb.append("\\b");
			case '\f' -> sb.append("\\f");
			case '\n' -> sb.append("\\n");
			case '\r' -> sb.append("\\r");
			case '\t' -> sb.append("\\t");
			default -> {
				if (c < 0x20) {
					sb.append(String.format("\\u%04x", (int) c));
				} else {
					sb.append(c);
				}
			}
			}
		}
		sb.append('"');
		return sb.toString();
	}
}
