package nlp4j.wiki.beta;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class WikiPageProcessorFileTest {

	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			System.out.println("Usage:");
			System.out.println("java WikiPageProcessorFileTest <wiki-file>");
			return;
		}

		Path path = Path.of(args[0]);

		System.out.println("Reading file: " + path.toAbsolutePath());

		String wikiText = Files.readString(path, StandardCharsets.UTF_8);

		String title = extractTitleFromFilename(path);

		WikiPage page = WikiPageProcessor.processPage(title, wikiText);

		printResult(page);
	}

	private static String extractTitleFromFilename(Path path) {

		String name = path.getFileName().toString();

		int dot = name.lastIndexOf('.');
		if (dot > 0) {
			name = name.substring(0, dot);
		}

		return name;
	}

	private static void printResult(WikiPage page) {

		System.out.println("\n==============================");
		System.out.println("TITLE");
		System.out.println("==============================");
		System.out.println(page.title());

		System.out.println("\n==============================");
		System.out.println("PLAIN TEXT");
		System.out.println("==============================");
		System.out.println(page.plainText());

		System.out.println("\n==============================");
		System.out.println("INFOBOX");
		System.out.println("==============================");

		for (Map.Entry<String, String> e : page.infobox().entrySet()) {
			System.out.println(e.getKey() + " = " + e.getValue());
		}

		System.out.println("\n==============================");
		System.out.println("JSONL");
		System.out.println("==============================");

		String json = JsonlWriter.toJsonLine(page);
//		System.out.println(json);
	}
}